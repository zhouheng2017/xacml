package samples;

import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.Indenter;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.RFC822NameAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.Subject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description: 构建PEP访问请求根据访问者的属性
 * @Date: 2018-05-23
 * @Time: 8:59
 */

public class IAMRequestBuilder {

    /**
     * 根据访问者的请求信息，来设置Subjects中的属性
     * @return
     * @throws URISyntaxException
     */
    public static Set setupSubjects() throws URISyntaxException {
        Set attributes = new HashSet();
        URI subjectId = new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");

        RFC822NameAttribute value = new RFC822NameAttribute("zhouheng2017@cnpc.com.cn");

        //构建subject
        attributes.add(new Attribute(subjectId, null, null, value));
        attributes.add(new Attribute(new URI("group"), "admin@cnpc.com.cn", null, new StringAttribute("developers")));

        HashSet subjects = new HashSet();
        subjects.add(new Subject(attributes));
        return subjects;
    }

    /**
     * 构建Resource中的属性值
     * @return
     * @throws URISyntaxException
     */
    public static Set setupResources() throws URISyntaxException {
        Set resource = new HashSet();

        AnyURIAttribute attributeValue = new AnyURIAttribute(new URI("http://server.example.com/"));

        resource.add(new Attribute(new URI(EvaluationCtx.RESOURCE_ID), null, null, attributeValue));

        return resource;
    }

    /**
     * 定义一个Action的提交属性
     * @return
     * @throws URISyntaxException
     */
    public static Set setupAction() throws URISyntaxException {
        Set action = new HashSet();

        URI actionId = new URI("urn:oasis:names:tc:xacml:1.0:action-id");

        action.add(new Attribute(actionId, null, null, new StringAttribute("commit")));

        return action;
    }

    public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
        String path = "E:\\maven\\workspace\\java2\\xacml\\src\\main\\java\\samples/request.xml";
        OutputStream outputStream = new FileOutputStream(new File(path));
        RequestCtx requestCtx = new RequestCtx(setupSubjects(), setupResources(), setupAction(), new HashSet());

        requestCtx.encode(System.out, new Indenter());
        requestCtx.encode(outputStream, new Indenter());

    }


}
