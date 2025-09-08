package cn.zs.tool.httpclient5.request;

import cn.zs.tool.http.core.request.BaseHttpRequest;
import cn.zs.tool.httpclient5.constant.HttpRequestMethodEnum;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * httpclient5请求
 *
 * @author sheng
 */
public class HttpClient5Request extends HttpClient5BaseRequest<HttpClient5Request> implements BaseHttpRequest<HttpClient5Request> {
    public HttpClient5Request(String url, HttpRequestMethodEnum method) {
        super(url, method);
    }

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static HttpClient5Request create(String url, HttpRequestMethodEnum method) {
        return new HttpClient5Request(url, method);
    }

    // region 请求体

    /**
     * 设置请求体
     */
    @Override
    public HttpClient5Request body(String body, Charset charset) {
        return body(new StringEntity(body, charset));
    }

    /**
     * 设置请求体
     */
    public HttpClient5Request body(byte[] body, ContentType contentType) {
        return body(body, contentType, getDefaultCharset());
    }

    /**
     * 设置请求体
     */
    public HttpClient5Request body(byte[] body, ContentType contentType, Charset charset) {
        return body(new ByteArrayEntity(body, contentType, Objects.nonNull(charset) ? charset.name() : null));
    }

    /**
     * 设置请求体
     */
    public HttpClient5Request body(File file, ContentType contentType) {
        return body(file, contentType, getDefaultCharset());
    }

    /**
     * 设置请求体
     */
    public HttpClient5Request body(File file, ContentType contentType, Charset charset) {
        return body(new FileEntity(file, contentType, Objects.nonNull(charset) ? charset.name() : null));
    }

    /**
     * 设置请求体
     */
    public HttpClient5Request body(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        return this;
    }

    // endregion

    @Override
    public HttpClient5Request self() {
        return this;
    }
}
