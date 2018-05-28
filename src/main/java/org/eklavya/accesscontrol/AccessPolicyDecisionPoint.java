/*
 * Created on Sep 16, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eklavya.accesscontrol;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import com.sun.xacml.*;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.PolicyFinderModule;
import com.sun.xacml.finder.PolicyFinderResult;
import com.sun.xacml.finder.impl.FilePolicyModule;
import sample.src.SamplePolicyFinderModule;

/**
 * @author Administrator
 * <p>
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AccessPolicyDecisionPoint {

    /**
     * Evaluates the given request and returns the Response that the PDP
     * will hand back to the PEP.
     *
     *
     * @return the result of the evaluation
     * @throws Exception      if there is a problem accessing the file
     * @throws ParsingException if the Request is invalid
     */
    public static void main(String[] args) throws Exception {
       /* if (args.length < 2) {
            System.out.println("Usage: <request> <AccessPolicy> [policies]");
            System.exit(1);
        }
        // Step 1: Get the request and policy file from the command line
        String requestFile = null;
*/
        String[] policyFiles = { "E:\\maven\\workspace\\java2\\xacml\\src\\main\\java\\samples\\obligation2.xml",
                "E:/maven/workspace/java2/xacml/src/main/java/samples/generated2.xml",
        "E:/maven/workspace/java2/xacml/src/main/java/samples/selector2.xml",
        "E:/maven/workspace/java2/xacml/src/main/java/samples/sensitive2.xml",
        "E:/maven/workspace/java2/xacml/src/main/java/samples/time-range2.xml"};


        AccessPolicy accessPolicy = new AccessPolicy();
        Policy policy = accessPolicy.getPolicies();

        FilePolicyModule filePolicyModule = new FilePolicyModule();
        for (int i = 0; i < policyFiles.length; i++) {
            filePolicyModule.addPolicy(policyFiles[i]);
        }


        // Step 2: Setup the PolicyFinder that this PDP will use
        //
        SamplePolicyFinderModule samplePolicyFinderModule = new SamplePolicyFinderModule();
        MyPolicyFinderModule myPolicyFinderModule = new MyPolicyFinderModule();
        myPolicyFinderModule.addPolicy();

        PolicyFinder policyFinder = new PolicyFinder();
        Set policyModules = new HashSet();
        policyModules.add(filePolicyModule);
//        policyModules.add(policy);
//        policyModules.add(samplePolicyFinderModule);
//        policyModules.add(myPolicyFinderModule);
        policyFinder.setModules(policyModules);
 /*       EvaluationCtx evaluationCtx = new
        policyFinder.findPolicy(EvaluationCtx)*/





        // create the PDP
        PDP pdp = new PDP(new PDPConfig(null, policyFinder, null));

        RequestCtx request =
                RequestCtx.getInstance(new FileInputStream("E:\\maven\\workspace\\java2\\xacml\\src\\main\\java\\samples\\sensitive2.xml"));

        // evaluate the request
        ResponseCtx response = pdp.evaluate(request);

        // for this sample program, we'll just print out the response
        response.encode(System.out, new Indenter());
    }

}
