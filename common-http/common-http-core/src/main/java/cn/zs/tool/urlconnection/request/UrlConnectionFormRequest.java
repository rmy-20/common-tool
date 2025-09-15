package cn.zs.tool.urlconnection.request;

import cn.zs.tool.http.core.body.UrlEncodedFormBody;
import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.request.BaseFormRequest;

import java.util.Objects;

/**
 * form请求
 *
 * @author sheng
 */
public class UrlConnectionFormRequest extends UrlConnectionBaseRequest<UrlConnectionFormRequest>
        implements BaseFormRequest<UrlConnectionFormRequest> {
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
    public static UrlConnectionFormRequest create(String url, HttpMethodEnum method) {
        return new UrlConnectionFormRequest(url, method);
    }

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static UrlConnectionFormRequest create(String url, HttpMethodEnum method, UrlEncodedFormBody formBody) {
        return new UrlConnectionFormRequest(url, method, formBody);
    }

    public UrlConnectionFormRequest(String url, HttpMethodEnum method) {
        this(url, method, UrlEncodedFormBody.create());
    }

    public UrlConnectionFormRequest(String url, HttpMethodEnum method, UrlEncodedFormBody formBody) {
        super(url, method);
        this.formBody = Objects.requireNonNull(formBody, "formBody can not be null");
        super.body = this.formBody;
    }

    @Override
    public UrlConnectionFormRequest addText(String name, Object value) {
        formBody.addText(name, value);
        return this;
    }

    @Override
    public UrlConnectionFormRequest addTextEncoded(String name, Object value) {
        formBody.addTextEncoded(name, value);
        return this;
    }

    @Override
    public UrlConnectionFormRequest self() {
        return this;
    }
}
