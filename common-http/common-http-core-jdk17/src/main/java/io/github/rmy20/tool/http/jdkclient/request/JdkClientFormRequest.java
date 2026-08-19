package io.github.rmy20.tool.http.jdkclient.request;

import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.body.UrlEncodedFormBody;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseFormRequest;

import java.net.http.HttpRequest;
import java.util.Objects;

/**
 * jdk httpclient 表单请求
 *
 * @author sheng
 */
public class JdkClientFormRequest extends JdkClientBaseRequest<JdkClientFormRequest> implements BaseFormRequest<JdkClientFormRequest> {
    /**
     * 表单数据
     */
    private final UrlEncodedFormBody formBody;

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static JdkClientFormRequest create(String url, HttpMethodEnum method) {
        return new JdkClientFormRequest(url, method);
    }

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static JdkClientFormRequest create(String url, HttpMethodEnum method, UrlEncodedFormBody formBody) {
        return new JdkClientFormRequest(url, method, formBody);
    }

    public JdkClientFormRequest(String url, HttpMethodEnum method) {
        this(url, method, UrlEncodedFormBody.create());
    }

    public JdkClientFormRequest(String url, HttpMethodEnum method, UrlEncodedFormBody formBody) {
        super(url, method);
        this.formBody = Objects.requireNonNull(formBody, "formBody can not be null");
    }

    @Override
    public JdkClientFormRequest addText(String name, Object value) {
        formBody.addText(name, value);
        return this;
    }

    @Override
    public JdkClientFormRequest addTextEncoded(String name, Object value) {
        formBody.addTextEncoded(name, value);
        return this;
    }

    @Override
    public JdkClientFormRequest self() {
        return this;
    }

    @Override
    protected void executeBefore() {
        if (StringUtil.isBlank(headers.getContentType())) {
            headers.setContentType(formBody.getContentType());
        }
        this.bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(formBody.getContent());
    }
}
