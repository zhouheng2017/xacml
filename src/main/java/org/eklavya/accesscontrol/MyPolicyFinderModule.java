package org.eklavya.accesscontrol;

import com.sun.xacml.*;
import com.sun.xacml.ctx.Status;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.PolicyFinderModule;
import com.sun.xacml.finder.PolicyFinderResult;
import org.w3c.dom.Node;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-05-25
 * @Time: 13:55
 */
public class MyPolicyFinderModule extends PolicyFinderModule {
    /**
     * For this example we'll define a core namespace that this module
     * knows how to handle...for your code, you may have specific references
     * you handle or you may handle all references.
     */
    public static final String REFERENCE_NAMESPACE = "urn:my:custom:space";

    // the finder that is using this module
    private PolicyFinder finder;

    /**
     * Basic constructor where you can initialize private module data.
     */
    public MyPolicyFinderModule() {
        // any initialization you need to do, for example a table that
        // caches previously loaded policies
    }

    /**
     * Always return true, since indeed this class supports references.
     *
     * @return true
     */
    @Override
    public boolean isIdReferenceSupported() {
        return true;
    }

    /**
     * Called when the <code>PolicyFinder</code> initializes. This lets your
     * code keep track of the finder, which is especially useful if you have
     * to create policy sets.
     *
     * @param finder the <code>PolicyFinder</code> that's using this module
     */
    @Override
    public void init(PolicyFinder finder) {
        // a second initializer that lets you keep track of the finder that
        // is using this class...this information is needed when you try
        // to instantiate a policy set, but may be useful for other tasks

        this.finder = finder;
    }

    /**
     * A partially implemented version of the method that is invoked to find
     * a policy reference. In this case, we first check that the URI is
     * one we know how to handle, then we fetch the policy, and if needed,
     * we parse the XML and create a new AbstractPolicy handling errors
     * correctly. Obviously the steps involved in fetching the policy are
     * left out here.
     *
     * @param idReference an identifier specifying some policy
     * @param type type of reference (policy or policySet) as identified by
     *             the fields in <code>PolicyReference</code>
     *
     * @return the result of looking for the referenced policy
     */
    @Override
    public PolicyFinderResult findPolicy(URI idReference, int type) {
        // if you know that you only support certain references, then you
        // should go ahead and check if you handle the given URI, and
        // if you don't you're done
        if (! idReference.toString().startsWith(REFERENCE_NAMESPACE))
            return new PolicyFinderResult();

        // if you are doing any kind of caching, you should check now for
        // a cached version of the policy

        // Fetch the policy from whatever source you're using...this might
        // actually get an AbstractPolicy object, or it might return an
        // XML document (the more likely case). Note that if this returns
        // more than one policy then this is either an error case or this
        // module is responsible for creating a new PolicySet that combines
        // the referenced policies (based on the module's design). In the
        // second case you instantiate a new PolicySet with the policies
        // you've retrieved, though care needs to be taken with how you
        // specify the Target (this is a little easier when dynamically
        // generating PolicySets based on Requests).

        // if we fetched an AbstractPolicy object, then that gets returned
        // (if the type parameter matches the retrieved type) and we're done

        // if we're still here then the retrieval mechanism found an XML
        // document...you should replace this line to reference the retrieved
        // XML instance
        Node root = null;

        // this will be the policy that was referenced
        AbstractPolicy policy;

        try {
            if (type == PolicyReference.POLICY_REFERENCE) {
                // a policy is being referenced
                policy = Policy.getInstance(root);
            } else {
                // a policy set is being referenced
                policy = PolicySet.getInstance(root, finder);
            }
        } catch (ParsingException pe) {
            // some error occured while loading the policy, so we need to
            // return the error for the PolicyFinder to handle the problem
            ArrayList code = new ArrayList();
            String message = "error parsing referenced policy \"" +
                    idReference.toString() + "\": " + pe.getMessage();

            code.add(Status.STATUS_SYNTAX_ERROR);

            return new PolicyFinderResult(new Status(code, message));
        }

        // if we got here then we loaded a new policy...we may want to cache
        // it or otherwise track it

        // now return the policy
        return new PolicyFinderResult(policy);
    }

    public Policy getPolicySet() throws URISyntaxException, UnknownIdentifierException {
        AccessPolicy accessPolicy = new AccessPolicy();
        Policy policies = accessPolicy.getPolicies();

        return policies;
    }

    public void addPolicy() throws URISyntaxException, UnknownIdentifierException {
        HashSet set = new HashSet();
        set.add(getPolicySet());
    }

}
