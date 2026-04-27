package io.github.rmy20.tool.httpclient5;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseRequestTool;
import io.github.rmy20.tool.httpclient5.request.HttpClient5BaseRequest;
import io.github.rmy20.tool.httpclient5.request.HttpClient5FormRequest;
import io.github.rmy20.tool.httpclient5.request.HttpClient5MultipartRequest;
import io.github.rmy20.tool.httpclient5.request.HttpClient5Request;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.concurrent.Cancellable;
import org.apache.hc.core5.http.ProtocolVersion;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

/**
 * httpclient5 请求工具类
 *
 * @author sheng
 */
public class HttpClient5RequestTool implements BaseRequestTool {
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

    // /**
    //  * 异步请求 #{@link HttpAsyncClient}
    //  */
    // private HttpAsyncClient httpAsyncClient;

    /**
     * 请求上下文
     */
    private HttpContext httpContext;

    // /**
    //  * #{@link AsyncRequestProducer} 提供者
    //  */
    // private ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc;
    //
    // /**
    //  * #{@link AsyncResponseConsumer} 提供者
    //  */
    // private ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier;
    //
    // /**
    //  * #{@link AsyncPushConsumer} 提供者
    //  */
    // private ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier;

    /**
     * 异步线程池
     */
    private ExecutorService executorService;

    /**
     * 创建#{@link HttpClient5RequestTool}
     */
    public static HttpClient5RequestTool create() {
        return new HttpClient5RequestTool();
    }

    @Override
    public HttpClient5Request get(String url) {
        return addConfig(HttpClient5Request.create(url, HttpMethodEnum.GET));
    }

    @Override
    public HttpClient5Request get(URI url) {
        return addConfig(HttpClient5Request.create(url.toString(), HttpMethodEnum.GET));
    }

    @Override
    public HttpClient5Request post(String url) {
        return addConfig(HttpClient5Request.create(url, HttpMethodEnum.POST));
    }

    @Override
    public HttpClient5Request post(URI url) {
        return addConfig(HttpClient5Request.create(url.toString(), HttpMethodEnum.POST));
    }

    @Override
    public HttpClient5Request put(String url) {
        return addConfig(HttpClient5Request.create(url, HttpMethodEnum.PUT));
    }

    @Override
    public HttpClient5Request put(URI url) {
        return addConfig(HttpClient5Request.create(url.toString(), HttpMethodEnum.PUT));
    }

    @Override
    public HttpClient5Request delete(String url) {
        return addConfig(HttpClient5Request.create(url, HttpMethodEnum.DELETE));
    }

    @Override
    public HttpClient5Request delete(URI url) {
        return addConfig(HttpClient5Request.create(url.toString(), HttpMethodEnum.DELETE));
    }

    @Override
    public HttpClient5Request request(String url, HttpMethodEnum method) {
        return addConfig(HttpClient5Request.create(url, method));
    }

    @Override
    public HttpClient5Request request(URI url, HttpMethodEnum method) {
        return addConfig(HttpClient5Request.create(url.toString(), method));
    }

    @Override
    public HttpClient5FormRequest form(String url) {
        return addConfig(HttpClient5FormRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public HttpClient5FormRequest form(String url, HttpMethodEnum method) {
        return addConfig(HttpClient5FormRequest.create(url, method));
    }

    @Override
    public HttpClient5MultipartRequest multipart(String url) {
        return addConfig(HttpClient5MultipartRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public HttpClient5MultipartRequest multipart(String url, HttpMethodEnum method) {
        return addConfig(HttpClient5MultipartRequest.create(url, method));
    }

    private <T extends HttpClient5BaseRequest<T>> T addConfig(T t) {
        return t.requestConfig(requestConfig)
                .cancellable(cancellable)
                .protocolVersion(protocolVersion)
                .defaultCharset(defaultCharset)
                .httpClient(httpClient)
                // .httpAsyncClient(httpAsyncClient)
                .httpContext(httpContext)
                // .asyncRequestProducerFunc(asyncRequestProducerFunc)
                // .asyncResponseConsumerSupplier(asyncResponseConsumerSupplier)
                // .pushHandlerFactorySupplier(pushHandlerFactorySupplier)
                .executorService(executorService);
    }

    /**
     * 设置请求配置
     */
    public HttpClient5RequestTool requestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return this;
    }

    /**
     * 设置取消请求控制
     */
    public HttpClient5RequestTool cancellable(Cancellable cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    /**
     * 设置请求协议版本
     */
    public HttpClient5RequestTool protocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    /**
     * 设置默认编码字符集
     */
    public HttpClient5RequestTool defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }

    /**
     * 设置 HttpClient
     */
    public HttpClient5RequestTool httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    // /**
    //  * 设置 HttpAsyncClient
    //  */
    // public HttpClient5RequestTool httpAsyncClient(HttpAsyncClient httpAsyncClient) {
    //     this.httpAsyncClient = httpAsyncClient;
    //     return this;
    // }

    /**
     * 请求上下文
     */
    public HttpClient5RequestTool httpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
        return this;
    }

    // /**
    //  * 设置 #{@link AsyncRequestProducer} 提供者
    //  */
    // public HttpClient5RequestTool asyncRequestProducerFunc(ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc) {
    //     this.asyncRequestProducerFunc = asyncRequestProducerFunc;
    //     return this;
    // }

    // /**
    //  * 设置 #{@link AsyncResponseConsumer} 提供者
    //  */
    // public HttpClient5RequestTool asyncResponseConsumerSupplier(ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier) {
    //     this.asyncResponseConsumerSupplier = asyncResponseConsumerSupplier;
    //     return this;
    // }

    // /**
    //  * 设置 #{@link AsyncPushConsumer} 提供者
    //  */
    // public HttpClient5RequestTool pushHandlerFactorySupplier(ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable>
    // pushHandlerFactorySupplier) {
    //     this.pushHandlerFactorySupplier = pushHandlerFactorySupplier;
    //     return this;
    // }

    /**
     * 设置线程池
     */
    public HttpClient5RequestTool executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }
}
