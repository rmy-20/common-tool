package io.github.rmy20.tool.httpclient4;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseRequestTool;
import io.github.rmy20.tool.httpclient4.request.HttpClient4BaseRequest;
import io.github.rmy20.tool.httpclient4.request.HttpClient4FormRequest;
import io.github.rmy20.tool.httpclient4.request.HttpClient4MultipartRequest;
import io.github.rmy20.tool.httpclient4.request.HttpClient4Request;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.protocol.HttpContext;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

/**
 * httpclient4 请求工具类
 *
 * @author sheng
 */
public class HttpClient4RequestTool implements BaseRequestTool {
    /**
     * 请求配置
     */
    private RequestConfig requestConfig;

    /**
     * 取消请求控制
     */
    private Cancellable cancellable;

    /**
     * 请求协议版本
     */
    private ProtocolVersion protocolVersion;

    /**
     * 默认编码字符集，默认 UTF-8
     */
    protected Charset defaultCharset;

    /**
     * 同步请求 #{@link HttpClient}
     */
    private HttpClient httpClient;

    /**
     * 请求上下文
     */
    private HttpContext httpContext;

    /**
     * 异步线程池
     */
    private ExecutorService executorService;

    /**
     * 创建#{@link HttpClient4RequestTool}
     */
    public static HttpClient4RequestTool create() {
        return new HttpClient4RequestTool();
    }

    @Override
    public HttpClient4Request get(String url) {
        return addConfig(HttpClient4Request.create(url, HttpMethodEnum.GET));
    }

    @Override
    public HttpClient4Request get(URI url) {
        return addConfig(HttpClient4Request.create(url.toString(), HttpMethodEnum.GET));
    }

    @Override
    public HttpClient4Request post(String url) {
        return addConfig(HttpClient4Request.create(url, HttpMethodEnum.POST));
    }

    @Override
    public HttpClient4Request post(URI url) {
        return addConfig(HttpClient4Request.create(url.toString(), HttpMethodEnum.POST));
    }

    @Override
    public HttpClient4Request put(String url) {
        return addConfig(HttpClient4Request.create(url, HttpMethodEnum.PUT));
    }

    @Override
    public HttpClient4Request put(URI url) {
        return addConfig(HttpClient4Request.create(url.toString(), HttpMethodEnum.PUT));
    }

    @Override
    public HttpClient4Request delete(String url) {
        return addConfig(HttpClient4Request.create(url, HttpMethodEnum.DELETE));
    }

    @Override
    public HttpClient4Request delete(URI url) {
        return addConfig(HttpClient4Request.create(url.toString(), HttpMethodEnum.DELETE));
    }

    @Override
    public HttpClient4Request request(String url, HttpMethodEnum method) {
        return addConfig(HttpClient4Request.create(url, method));
    }

    @Override
    public HttpClient4Request request(URI url, HttpMethodEnum method) {
        return addConfig(HttpClient4Request.create(url.toString(), method));
    }

    @Override
    public HttpClient4FormRequest form(String url) {
        return addConfig(HttpClient4FormRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public HttpClient4FormRequest form(String url, HttpMethodEnum method) {
        return addConfig(HttpClient4FormRequest.create(url, method));
    }

    @Override
    public HttpClient4MultipartRequest multipart(String url) {
        return addConfig(HttpClient4MultipartRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public HttpClient4MultipartRequest multipart(String url, HttpMethodEnum method) {
        return addConfig(HttpClient4MultipartRequest.create(url, method));
    }

    private <T extends HttpClient4BaseRequest<T>> T addConfig(T t) {
        return t.requestConfig(requestConfig)
                .cancellable(cancellable)
                .protocolVersion(protocolVersion)
                .defaultCharset(defaultCharset)
                .httpClient(httpClient)
                .httpContext(httpContext)
                .executorService(executorService);
    }

    /**
     * 设置请求配置
     */
    public HttpClient4RequestTool requestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return this;
    }

    /**
     * 设置取消请求控制
     */
    public HttpClient4RequestTool cancellable(Cancellable cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    /**
     * 设置请求协议版本
     */
    public HttpClient4RequestTool protocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    /**
     * 设置默认编码字符集
     */
    public HttpClient4RequestTool defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }

    /**
     * 设置 HttpClient
     */
    public HttpClient4RequestTool httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * 请求上下文
     */
    public HttpClient4RequestTool httpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
        return this;
    }

    /**
     * 设置线程池
     */
    public HttpClient4RequestTool executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }
}
