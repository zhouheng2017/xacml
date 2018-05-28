package samples;

import com.sun.xacml.*;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeDesignator;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.combine.CombiningAlgFactory;
import com.sun.xacml.combine.OrderedPermitOverridesRuleAlg;
import com.sun.xacml.combine.RuleCombiningAlgorithm;
import com.sun.xacml.cond.Apply;
import com.sun.xacml.cond.Function;
import com.sun.xacml.cond.FunctionFactory;
import com.sun.xacml.cond.FunctionTypeException;
import com.sun.xacml.ctx.Result;
import jdk.management.resource.ResourceType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-05-23
 * @Time: 11:21
 */
public class IAMPolicyBuilder {

    /**
     * 创建TargetMatch实例
     * @param type
     * @param functionId
     * @param designator
     * @param value
     * @return
     */
    public static TargetMatch createTargetMatch(int type, String functionId,
                                                AttributeDesignator designator,
                                                AttributeValue value) {
        try {
            FunctionFactory factory = FunctionFactory.getTargetInstance();

            Function function = factory.createFunction(functionId);
            return new TargetMatch(type, function, designator, value);
        } catch (Exception e) {
            return null;

        }

    }

    /**
     * 创建target
     * @return
     * @throws URISyntaxException
     */
    public static Target createPolicyTarget() throws URISyntaxException {

        List subjects = new ArrayList();
        List resources = new ArrayList();

        List subject = new ArrayList();

        //创建function id subjectMatchId
        String subjectMatchId = "urn:oasis:names:tc:xacml:1.0:function:rfc822Name-match";

        URI subjectDesignatorType = new URI("urn:oasis:names:tc:xacml:1.0:data-type:rfc922Name");

        URI subjectDesignatorId = new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");

        AttributeDesignator subjectDesignator = new AttributeDesignator(
                AttributeDesignator.SUBJECT_TARGET, subjectDesignatorType, subjectDesignatorId, false);

        StringAttribute subjectValue = new StringAttribute("cnpc.com.cn");

        subject.add(createTargetMatch(TargetMatch.SUBJECT, subjectMatchId, subjectDesignator, subjectValue));


        /**
         * 创建resource
         */
        List resource = new ArrayList();

        String resourceMatchId = "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal";

        URI resourceDesignatorType = new URI("http://www.32.org/2001/XMLSchema#anyURI");

        URI resourceDesignatorId = new URI("urn:oasis:names:tc:xacml:1.0:resource:resource-id");

        AttributeDesignator resourceDesignator = new AttributeDesignator(AttributeDesignator.RESOURCE_TARGET, resourceDesignatorType, resourceDesignatorId, false);

        AnyURIAttribute resourceValue = new AnyURIAttribute(new URI("http://server.example.com/"));

        resource.add(createTargetMatch(TargetMatch.RESOURCE, resourceMatchId, resourceDesignator, resourceValue));


        subjects.add(subject);
        resources.add(resource);

        //创建Target
        return new Target(subjects, resources, null);
    }

    public static Target createRuleTarget() throws URISyntaxException {
        List action = new ArrayList();

        List actions = new ArrayList();

        String actionMatchId = "urn:oasis:names:tc:xacml:1.0:function:string-equal";

        URI actionDesignatorType = new URI("http://www.w3.org/2001/XMLSchema#string");

        URI actionDesignatorId = new URI("urn:oasis:names:tc:xacml:1.0:action-id");

        AttributeDesignator actionDesignator = new AttributeDesignator(AttributeDesignator.ACTION_TARGET, actionDesignatorType, actionDesignatorId, false);

        StringAttribute actionValue = new StringAttribute("commit");

        action.add(createTargetMatch(TargetMatch.ACTION, actionMatchId, actionDesignator, actionValue));

        actions.add(action);
        return new Target(null, null, actions);
    }

    public static Apply createRuleCondition() throws URISyntaxException{

        List conditionArgs = new ArrayList();
        FunctionFactory factory = FunctionFactory.getConditionInstance();
        Function conditionFunction = null;

        try {
            conditionFunction = factory.createFunction("urn:oasis:names:tc:xacml:1.0:function:string-equal");
        } catch (Exception e) {
            return null;
        }

        List applyArgs = new ArrayList();

        factory = FunctionFactory.getGeneralInstance();
        Function ApplyFunction = null;

        try {
            ApplyFunction = factory.createFunction("urn:oasis:names:tc:xacml:1.0:function:string-one-and-only");
        } catch (Exception e) {
            return null;
        }

        URI designatorType =
                new URI("http://www.w3.org/2001/XMLSchema#string");
        URI designatorId =
                new URI("group");
        URI designatorIssuer =
                new URI("admin@cnpc.com.cn");
        AttributeDesignator designator =
                new AttributeDesignator(AttributeDesignator.SUBJECT_TARGET,
                        designatorType, designatorId, false,
                        designatorIssuer);
        applyArgs.add(designator);

        Apply apply = new Apply(ApplyFunction, applyArgs, false);

        conditionArgs.add(apply);

        StringAttribute value = new StringAttribute("developers");
        conditionArgs.add(value);

        return new Apply(conditionFunction, conditionArgs, true);
    }

    public static Rule createRule() throws URISyntaxException {
        URI ruleId = new URI("CommitRule");

        int effect = Result.DECISION_PERMIT;

        Target target = createRuleTarget();

        Apply condition = createRuleCondition();

        return new Rule(ruleId, effect, null, target, condition);
    }

    public static void main(String[] args) throws URISyntaxException, UnknownIdentifierException {

        /**
         * 创建策略ID
         */
        URI policyId = new URI("GeneratedPolicy");

        /**
         * 创建规则组合算法
         */
        URI combiningAlgId = new URI(OrderedPermitOverridesRuleAlg.algId);
        CombiningAlgFactory factory = CombiningAlgFactory.getInstance();
        RuleCombiningAlgorithm combiningAlg = (RuleCombiningAlgorithm) factory.createAlgorithm(combiningAlgId);

        //创建规则的描述
        String description =
                "This policy applies to any accounts at users.example.com " +
                        "accessing server.example.com. The one Rule applies to the " +
                        "specific action of doing a CVS commit, but other Rules could " +
                        "be defined that handled other actions. In this case, only " +
                        "certain groups of people are allowed to commit. There is a " +
                        "final fall-through rule that always returns Deny.";

        //创建策略目标
        Target policyTarget = createPolicyTarget();

        //创建默认的Rule
        Rule defaultRule = new Rule(new URI("FinalRule"), Result.DECISION_DENY, null, null, null);

        Rule commitRule = createRule();
        List runList = new ArrayList();
        runList.add(commitRule);
        runList.add(defaultRule);
        Policy policy = new Policy(policyId, combiningAlg, description, policyTarget, runList);
    }


}
