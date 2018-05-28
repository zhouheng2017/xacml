/*
package com.create;

import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.xacml.Indenter;
import com.sun.xacml.Policy;
import com.sun.xacml.Target;
import com.sun.xacml.TargetMatch;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeDesignator;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.combine.CombiningAlgFactory;
import com.sun.xacml.combine.OrderedPermitOverridesRuleAlg;
import com.sun.xacml.combine.RuleCombiningAlgorithm;
import com.sun.xacml.cond.Function;
import com.sun.xacml.cond.FunctionFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static sample.src.SamplePolicyBuilder.createPolicyTarget;

*/
/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-05-18
 * @Time: 9:48
 *//*

public class TestClass {

    */
/**
     * 创建policy
     *//*

    public void createPolicy() throws URI.MalformedURIException, URISyntaxException {

//        create policy identifier and policy description
        URI policyId = new URI("ProjectPlanAccessPolicy");
        String description = "This AccessPolicy appies to any account at secf.com";
        URI combiningAlgId = new URI(OrderedPermitOverridesRuleAlg.algId);
        CombiningAlgFactory factory = CombiningAlgFactory.getInstance();
        RuleCombiningAlgorithm combiningAlg =
                (RuleCombiningAlgorithm) (factory.createAlgorithm(combiningAlgId));
// Create the target for the policy
        Target policyTarget = createPolicyTarget();
// Create the rules for the policy
        List ruleList = createRules();
// Create the policy
        Policy policy =
                new Policy(policyId, combiningAlg, description, policyTarget, ruleList);
// Display the policy on the std out
        policy.encode(System.out, new Indenter());

    }

    private List createRules() {
        return new ArrayList();
    }


    public static Target createPolicyTarget() throws URISyntaxException, URI.MalformedURIException {
        List subjects = new ArrayList();
        List resources = new ArrayList();
        // Attributes of Subject type
        // Multiple subject attributes can be specified. In this
        // case only one is being defined.
        List subject = new ArrayList();
        URI subjectDesignatorType =
                new URI("urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name");
        URI subjectDesignatorId =
                new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
        // Match function for the subject-id attribute
        String subjectMatchId =
                "urn:oasis:names:tc:xacml:1.0:function:rfc822Name-match";
        AttributeDesignator subjectDesignator =
                new AttributeDesignator(
                        AttributeDesignator.SUBJECT_TARGET,
                        subjectDesignatorType,
                        subjectDesignatorId,
                        false);
        StringAttribute subjectValue = new
                StringAttribute("secf.com");
        // get the factory that handles Target functions
        FunctionFactory factory =
                FunctionFactory.getTargetInstance();
        // get an instance of the right function for matching
        // subject attributes
        Function subjectFunction =
                factory.createFunction(subjectMatchId);
        TargetMatch subjectMatch = new TargetMatch(
                TargetMatch.SUBJECT,
                subjectFunction,
                subjectDesignator,
                subjectValue);
        subject.add(subjectMatch);
        // Attributes of resource type
        // Multiple resource attributes can be specified. In this
        // case only one is being defined.
        List resource = new ArrayList();
        URI resourceDesignatorType =
                new URI("http://www.w3.org/2001/XMLSchema#anyURI");
        URI resourceDesignatorId =
                new URI("urn:oasis:names:tc:xacml:1.0:resource:resource-id");
        // Match function for the resource-id attribute
        String resourceMatchId =
                "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal";
        AttributeDesignator resourceDesignator =
                new AttributeDesignator(
                        AttributeDesignator.RESOURCE_TARGET,
                        resourceDesignatorType,
                        resourceDesignatorId,
                        false);
        AnyURIAttribute resourceValue =
                new AnyURIAttribute(
                        new URI("file:///D:/Documents/Administrator/Desktop/ProjectPlan.html"));
        // Get an instance of the right function for matching
        // resource attribute
        Function resourceFunction =
                factory.createFunction(resourceMatchId);
        TargetMatch resourceMatch = new TargetMatch(
                TargetMatch.RESOURCE,
                resourceFunction,
                resourceDesignator,
                resourceValue);
        resource.add(resourceMatch);
        // Put the subject and resource sections into their lists
        subjects.add(subject);
        resources.add(resource);
        // Create and return the new target. No action type
        // attributes have been specified in the target
        return new Target(subjects, resources, null);
    }
}
*/
