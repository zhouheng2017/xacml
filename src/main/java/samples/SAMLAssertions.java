package samples;/*
 * Created on Aug 5, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import com.verisign.saml.SOAPAssertionProviderFactory;
import com.verisign.saml.assertions.Attribute;
import com.verisign.saml.assertions.AttributeDesignator;
import com.verisign.saml.assertions.AttributeStatement;
import com.verisign.saml.assertions.Authenticity;
import com.verisign.saml.assertions.Identifiers;
import com.verisign.saml.assertions.NameIdentifier;
import com.verisign.saml.assertions.Subject;
import com.verisign.saml.assertions.SubjectConfirmation;
import com.verisign.saml.authorities.AttributeStatementProvider;
import com.verisign.saml.protocol.AttributeQuery;
import com.verisign.xmlsig.RSASigningKey;
import com.verisign.xmlsig.RSAVerifyingKey;

/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SAMLAssertions {

	// SAML service provider
	static final String SERVICE_URL =
		"https://pilotpts.verisign.com/vspts/SamlResponder";
	// Email of the subject that uniquely identifies it
	static final String SUBJECT_EMAIL = "jdoe@tpms.verisign.com";
	// Subject password to authenticate the user
	static final String SUBJECT_PASSWORD = "password";
	// The keystore to be used for getting the private key and the public key
//	static final String KEYSTORE_FILE = "saml_sample_keystore";
	static final String KEYSTORE_FILE = "E:\\maven\\workspace\\java2\\xacml\\src\\main\\java\\samples\\saml_sample_keystore";
	// Keystore password
	static final String STORE_PASS = "changeit";

	static final String SIGNER_ALIAS = "pilot_saml_signer";
	//	VeriSign attribute naming constants
	static final String V_NAME_QUALIFIER = "verisign.com/ams";
	static final String V_ATTR_NAMESPACE =
		"verisign.com/ams/namespace/common";
	static final String V_ATTR_EMAIL = "//verisign.com/core/attr/email";
	static final String V_ATTR_FIRSTNAME = "//verisign.com/core/attr/first_name";

	public static void main(String arg[]) {
		//	Create the subject info
		// Specify name of the subject to be queried
		NameIdentifier nameId =
			new NameIdentifier(SUBJECT_EMAIL, V_NAME_QUALIFIER, "");
		//Specify the method to be used for authenticating the user
		String[] confMethods = { Identifiers.AUTHN_METHOD_PASSWORD };
		// Use the subject's nameidentifier and the specifed authentication
		// method with the required parameters to create subject
		SubjectConfirmation sconf =
			new SubjectConfirmation(confMethods, SUBJECT_PASSWORD);
		Subject subject = new Subject(nameId, sconf);

		// Specifies the attribute and the namespace in which to
		// interpret the attribute.
		/*
		AttributeDesignator[] reqAttrs =
			{ new AttributeDesignator(V_ATTR_NAMESPACE, V_ATTR_EMAIL),
				new AttributeDesignator(V_ATTR_NAMESPACE, V_ATTR_FIRSTNAME)};
				*/

		AttributeDesignator[] reqAttrs = null;

		// Create the attribute query object with the subject and the
		// attributes whose value you are interested in.
		// I have specifed no attributes as the current implemetation returns
		// all attribute values irrespective of what you want
		AttributeQuery query = new AttributeQuery(subject, reqAttrs);
		// Create the attribute statement provider from the key store and
		// using the service url on which the sample SAML service is running
		AttributeStatementProvider provider = null;
		try {
			provider =
				getAttributeStatementProvider();
		} catch (Exception e) {
			// XXX Auto-generated catch block
			e.printStackTrace();
		}

		// Execute the query on the attribute statement provider
		// Get the attribute statment object.
		// Attribute stement object contains the values of all the
		// attributes that the provider sent as the reult of the
		// query.
		// Iterate thru' all the attribuites in the statement to
		// get the values

		AttributeStatement[] statements = null;
		try {
			statements = provider.processAttributeQuery(query);
		} catch (Exception e1) {
			// XXX Auto-generated catch block
			e1.printStackTrace();
		}
		if (statements.length == 0) {
			System.out.println("No statements returned");
		}
		for (int i = 0; i < statements.length; i += 1) {

			AttributeStatement statement = statements[i];
			Authenticity authenticity = statement.getAuthenticity();
			System.out.println(
				"Statement "
					+ (i + 1)
					+ " authenticity: "
					+ authenticity.isAuthentic());


				// Print attribute statement
				System.out.println("--- Attribute Statement ---");
				System.out.println(
					"  Subject: "
						+ statement.getSubject().getNameIdentifier().getValue());

				// Print attribute values
				Attribute[] attrs = statement.getAttributes();
				for (int k = 0; k < attrs.length; k += 1) {
					Attribute attr = attrs[k];
					System.out.println(
						"Namespace: " + attr.getAttributeNamespace());
					System.out.println("     Name: " + attr.getAttributeName());
					Object[] values = attr.getAttributeValues();
					for (int m = 0; m < values.length; m += 1) {
						System.out.println(
							"    Value: " + values[m].toString());
					}
				}
		}
	}

	private static AttributeStatementProvider getAttributeStatementProvider()
		throws Exception {
		// Read the keystore and get the signing cert/key.
		InputStream fileInput = new FileInputStream(KEYSTORE_FILE);
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(fileInput, STORE_PASS.toCharArray());
		PrivateKey key =
			(PrivateKey) keystore.getKey(
				SIGNER_ALIAS,
				STORE_PASS.toCharArray());
		if (key == null) {
			throw new Exception(
				SIGNER_ALIAS + " key not found in keystore " + KEYSTORE_FILE);
		}
		Certificate[] certArray = keystore.getCertificateChain(SIGNER_ALIAS);
		if (certArray == null) {
			throw new Exception(
				SIGNER_ALIAS + " cert not found in keystore " + KEYSTORE_FILE);
		}
		X509Certificate[] certs = new X509Certificate[certArray.length];
		System.arraycopy(certArray, 0, certs, 0, certs.length);

		// Create SOAP assertion provider factory with signing information
		SOAPAssertionProviderFactory factory =
			new SOAPAssertionProviderFactory(new URL(SERVICE_URL));
		factory.setSigningKey(new RSASigningKey(key));
		factory.setVerifyingKey(new RSAVerifyingKey(certs));

		return factory.newAttributeStatementProvider();
	}

}
