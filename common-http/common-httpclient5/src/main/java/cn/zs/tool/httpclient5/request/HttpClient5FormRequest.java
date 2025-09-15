package cn.zs.tool.httpclient5.request;

import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.MediaType;
import cn.zs.tool.http.core.request.BaseFormRequest;
import cn.zs.tool.httpclient5.constant.HttpRequestMethodEnum;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * httpclient5 form请求
 *
 * @author sheng
 */
public class HttpClient5FormRequest extends HttpClient5BaseRequest<HttpClient5FormRequest>
        implements BaseFormRequest<HttpClient5FormRequest> {
    /**
     * 表单参数
     */
    private final List<NameValuePair> formParams = new ArrayList<>();

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static HttpClient5FormRequest create(String url, HttpRequestMethodEnum method) {
        return new HttpClient5FormRequest(url, method);
    }

    public HttpClient5FormRequest(String url, HttpRequestMethodEnum method) {
        super(url, method);
    }

    @Override
    public HttpClient5FormRequest addText(String name, Object value) {
        return addTextEncoded(name, value);
    }

    /**
     * 添加表单参数
     *
     * @param name  key
     * @param value value
     */
    @Override
    public HttpClient5FormRequest addTextEncoded(String name, Object value) {
        formParams.add(new BasicNameValuePair(name, Objects.toString(value, StringPool.EMPTY)));
        return this;
    }

    /**
     * 添加表单参数
     *
     * @param pair key-value
     */
    public HttpClient5FormRequest addTextEncoded(NameValuePair pair) {
        if (Objects.nonNull(pair)) {
            formParams.add(pair);
        }
        return this;
    }

    @Override
    protected void executeBefore() {
        super.executeBefore();
        this.httpEntity = new UrlEncodedFormEntity(formParams, getDefaultCharset());
        if (StringUtil.isAllBlank(getHeaders().getContentType(), httpEntity.getContentType())) {
            getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
    }

    @Override
    public HttpClient5FormRequest self() {
        return this;
    }
}
