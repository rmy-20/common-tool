package io.github.rmy20.tool.httpclient4.request;

import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseFormRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * httpclient4 form 请求
 *
 * @author sheng
 */
public class HttpClient4FormRequest extends HttpClient4BaseRequest<HttpClient4FormRequest>
        implements BaseFormRequest<HttpClient4FormRequest> {
    /**
     * 表单参数
     */
    private final List<NameValuePair> formParamList = new ArrayList<>();

    /**
     * 创建
     */
    public static HttpClient4FormRequest create(String url, HttpMethodEnum method) {
        return new HttpClient4FormRequest(url, method);
    }

    protected HttpClient4FormRequest(String url, HttpMethodEnum method) {
        super(url, method);
    }

    @Override
    public HttpClient4FormRequest addText(String name, Object value) {
        return addTextEncoded(name, value);
    }

    @Override
    public HttpClient4FormRequest addTextEncoded(String name, Object value) {
        formParamList.add(new BasicNameValuePair(name, Objects.toString(value, StringPool.EMPTY)));
        return self();
    }

    /**
     * 添加表单参数
     *
     * @param pair key-value
     */
    public HttpClient4FormRequest addTextEncoded(NameValuePair pair) {
        if (Objects.nonNull(pair)) {
            formParamList.add(pair);
        }
        return self();
    }

    @Override
    protected void executeBefore() {
        super.executeBefore();
        this.httpEntity = new UrlEncodedFormEntity(formParamList, getDefaultCharset());
        if (StringUtil.isBlank(getHeaders().getContentType()) && Objects.isNull(this.httpEntity.getContentType())) {
            getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
    }

    @Override
    public HttpClient4FormRequest self() {
        return this;
    }
}
