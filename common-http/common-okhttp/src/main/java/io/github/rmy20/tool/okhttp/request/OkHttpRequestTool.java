package io.github.rmy20.tool.okhttp.request;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseRequestTool;
import okhttp3.OkHttpClient;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * okhttp 请求工具类
 *
 * @author sheng
 */
public class OkHttpRequestTool implements BaseRequestTool {
    /**
     * OkHttpClient
     */
    private OkHttpClient httpClient;

    /**
     * 默认编码字符集，默认 UTF-8
     */
    private Charset defaultCharset;

    /**
     * 创建 #{@link OkHttpRequestTool}
     */
    public static OkHttpRequestTool create() {
        return new OkHttpRequestTool();
    }

    @Override
    public OkHttpRequest get(String url) {
        return addConfig(OkHttpRequest.create(url, HttpMethodEnum.GET));
    }

    @Override
    public OkHttpRequest get(URI url) {
        return addConfig(OkHttpRequest.create(url.toString(), HttpMethodEnum.GET));
    }

    @Override
    public OkHttpRequest post(String url) {
        return addConfig(OkHttpRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public OkHttpRequest post(URI url) {
        return addConfig(OkHttpRequest.create(url.toString(), HttpMethodEnum.POST));
    }

    @Override
    public OkHttpRequest put(String url) {
        return addConfig(OkHttpRequest.create(url, HttpMethodEnum.PUT));
    }

    @Override
    public OkHttpRequest put(URI url) {
        return addConfig(OkHttpRequest.create(url.toString(), HttpMethodEnum.PUT));
    }

    @Override
    public OkHttpRequest delete(String url) {
        return addConfig(OkHttpRequest.create(url, HttpMethodEnum.DELETE));
    }

    @Override
    public OkHttpRequest delete(URI url) {
        return addConfig(OkHttpRequest.create(url.toString(), HttpMethodEnum.DELETE));
    }

    @Override
    public OkHttpRequest request(String url, HttpMethodEnum method) {
        return addConfig(OkHttpRequest.create(url, method));
    }

    @Override
    public OkHttpRequest request(URI url, HttpMethodEnum method) {
        return addConfig(OkHttpRequest.create(url.toString(), method));
    }

    @Override
    public OkHttpFormRequest form(String url) {
        return addConfig(OkHttpFormRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public OkHttpFormRequest form(String url, HttpMethodEnum method) {
        return addConfig(OkHttpFormRequest.create(url, method));
    }

    @Override
    public OkHttpMultipartRequest multipart(String url) {
        return addConfig(OkHttpMultipartRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public OkHttpMultipartRequest multipart(String url, HttpMethodEnum method) {
        return addConfig(OkHttpMultipartRequest.create(url, method));
    }

    private <T extends OkHttpBaseRequest<T>> T addConfig(T t) {
        return t.httpClient(httpClient).defaultCharset(defaultCharset);
    }

    /**
     * 设置 HttpClient
     */
    public OkHttpRequestTool httpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * 设置默认编码字符集
     */
    public OkHttpRequestTool defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }
}
