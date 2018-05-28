package samples;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-05-24
 * @Time: 10:24
 */

import com.sun.xacml.Indenter;
import com.sun.xacml.PDP;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.impl.CurrentEnvModule;
import com.sun.xacml.finder.impl.FilePolicyModule;
import com.sun.xacml.finder.impl.SelectorModule;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


class TestPDP {
    static public void main(String[] args) throws Exception {
        PDP pdp = null;   //存放策略结果
        //首先从文件系统中得到相应的策略文件
        FilePolicyModule filePolicy = new FilePolicyModule();
//        filePolicy.addPolicy("./TestPolicy.xml");  //加入策略文件TestPolicy. xml

//        filePolicy.addPolicy("generated.xml");
        filePolicy.addPolicy("obligation.xml");
//        filePolicy.addPolicy("selector.xml");
//        filePolicy.addPolicy("time-range.xml");

        //通过文件，建立策略寻找点—PolicyFinder
        PolicyFinder policyF = new PolicyFinder();
        Set policyModules = new HashSet();
        policyModules.add(filePolicy);
        policyF.setModules(policyModules);

        //建立属性查找点—AttributeFinder
        CurrentEnvModule envAttr = new CurrentEnvModule();
        SelectorModule selectorAttr = new SelectorModule();
        AttributeFinder attrFinder = new AttributeFinder();
        List attributeModules = new ArrayList();
        attributeModules.add(envAttr);
        attributeModules.add(selectorAttr);
        attrFinder.setModules(attributeModules);

        //最后建立我们的PDP，即策略库信息
        pdp = new PDP(new PDPConfig(attrFinder, policyF, null));

        //下面接收请求信息，该请求信息存放在文件TestRequest.xml中
//        RequestCtx request=RequestCtx.getInstance(new FileInputStream ("./TestRequest.xml"));
        RequestCtx request = RequestCtx.getInstance(new FileInputStream("sensitive.xml"));
        ResponseCtx response = pdp.evaluate(request);

        response.encode(System.out, new Indenter()); //输出结果

    }
}