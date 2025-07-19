package cn.zs.tool.okhttp.request;

import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.decorator.HttpHeaderDecorator;
import cn.zs.tool.http.core.exception.HttpException;
import cn.zs.tool.okhttp.constant.OkHttpConstant;
import cn.zs.tool.okhttp.decorator.OkHttpExecuteDecorator;
import cn.zs.tool.okhttp.decorator.OkHttpUriBuilderDecorator;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Objects;

/**
 * okhttp 请求基类
 *
 * @author sheng
 */
public abstract class OkHttpBaseRequest<T extends OkHttpBaseRequest<T>>
        implements OkHttpUriBuilderDecorator<T>, HttpHeaderDecorator<T>, OkHttpExecuteDecorator {

    /**
     * OkHttpClient
     */
    protected OkHttpClient httpClient;

    /**
     * url
     */
    private final HttpUrl.Builder urlBuilder;

    /**
     * 请求方法
     */
    private final HttpMethodEnum method;

    /**
     * 请求头
     */
    private final HttpHeaders headers;

    public OkHttpBaseRequest(String url, HttpMethodEnum method) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (Objects.isNull(httpUrl)) {
            throw new HttpException(String.format("okhttp解析url[%s]-[%s]失败", url, method.getMethod()));
        }
        this.urlBuilder = httpUrl.newBuilder();
        this.method = method;
        this.headers = HttpHeaders.create();
    }

    /**
     * 设置 HttpClient
     */
    public T httpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return self();
    }

    @Override
    public abstract T self();

    @Override
    public OkHttpClient getHttpClient() {
        return Objects.nonNull(httpClient) ? httpClient : OkHttpConstant.HTTP_CLIENT;
    }

    @Override
    public HttpUrl.Builder getUrlBuilder() {
        return urlBuilder;
    }

    @Override
    public HttpMethodEnum getMethod() {
        return method;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
