package cn.zs.tool.httpclient5.executor;

import cn.zs.tool.core.fuction.throwing.ThrowingFunc;
import cn.zs.tool.core.fuction.throwing.ThrowingSupplier;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.execute.ExecutorBaseBuilder;
import cn.zs.tool.httpclient5.constant.HttpClient5Constant;
import cn.zs.tool.httpclient5.response.HttpClient5AsyncResponseFuture;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.nio.AsyncPushConsumer;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.HandlerFactory;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * httpclient5执行器构建器
 *
 * @author sheng
 */
public class HttpClient5ExecutorBuilder<R> extends ExecutorBaseBuilder<R,
        HttpClient5Executor<R>, HttpClient5AsyncExecutor<R>, HttpClient5ExecutorBuilder<R>> {
    /**
     * 请求信息
     */
    private final HttpUriRequestBase request;

    /**
     * 同步请求 #{@link HttpClient}
     */
    protected HttpClient httpClient;

    /**
     * 异步请求 #{@link HttpAsyncClient}
     */
    protected HttpAsyncClient httpAsyncClient;

    /**
     * 请求上下文
     */
    private HttpContext httpContext;

    /**
     * #{@link AsyncRequestProducer} 提供者
     */
    private ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc;

    /**
     * #{@link AsyncResponseConsumer} 提供者
     */
    private ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier;

    /**
     * #{@link AsyncPushConsumer} 提供者
     */
    private ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier;

    /**
     * 创建执行器构建器
     *
     * @param request      请求
     * @param msgConverter 消息转换器
     */
    public static <R> HttpClient5ExecutorBuilder<R> create(HttpUriRequestBase request,
                                                           HttpMsgConverter<R> msgConverter) {
        return new HttpClient5ExecutorBuilder<>(request, msgConverter);
    }

    public HttpClient5ExecutorBuilder(HttpUriRequestBase request, HttpMsgConverter<R> msgConverter) {
        super(msgConverter);
        this.request = Objects.requireNonNull(request, "request must not be null");
    }

    /**
     * 设置 HttpClient
     */
    public HttpClient5ExecutorBuilder<R> httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return self();
    }

    /**
     * 获取当前实例所用 #{@link HttpClient}
     */
    public HttpClient getHttpClient() {
        return Objects.nonNull(httpClient) ? httpClient : HttpClient5Constant.HTTP_CLIENT;
    }

    /**
     * 设置 HttpAsyncClient
     */
    public HttpClient5ExecutorBuilder<R> httpAsyncClient(HttpAsyncClient httpAsyncClient) {
        this.httpAsyncClient = httpAsyncClient;
        return self();
    }

    /**
     * 获取当前实例所用 #{@link HttpAsyncClient}
     */
    public HttpAsyncClient getHttpAsyncClient() {
        return Objects.nonNull(httpAsyncClient) ? httpAsyncClient : HttpClient5Constant.HTTP_ASYNC_CLIENT;
    }

    /**
     * 请求上下文
     */
    public HttpClient5ExecutorBuilder<R> httpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
        return self();
    }

    /**
     * 获取当前实例所用请求上下文
     */
    public HttpContext getHttpContext() {
        return Objects.nonNull(httpContext) ? httpContext : HttpClientContext.create();
    }

    /**
     * 设置 #{@link AsyncRequestProducer} 提供者
     */
    public HttpClient5ExecutorBuilder<R> asyncRequestProducerFunc(ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc) {
        this.asyncRequestProducerFunc = asyncRequestProducerFunc;
        return self();
    }

    /**
     * 设置 #{@link AsyncResponseConsumer} 提供者
     */
    public HttpClient5ExecutorBuilder<R> asyncResponseConsumerSupplier(ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier) {
        this.asyncResponseConsumerSupplier = asyncResponseConsumerSupplier;
        return self();
    }

    /**
     * 设置 #{@link AsyncPushConsumer} 提供者
     */
    public HttpClient5ExecutorBuilder<R> pushHandlerFactorySupplier(ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier) {
        this.pushHandlerFactorySupplier = pushHandlerFactorySupplier;
        return self();
    }

    @Override
    protected HttpClient5ExecutorBuilder<R> self() {
        return this;
    }

    @Override
    public HttpClient5Executor<R> execute() {
        return HttpClient5Executor.create(getHttpClient(), request, getHttpContext(),
                getMsgConverter(), getOkPredicate(), getErrHandler(), isMustHandleResult());
    }

    @Override
    public CompletableFuture<HttpClient5AsyncExecutor<R>> executeAsync() {
        return HttpClient5AsyncResponseFuture.create(getMsgConverter(), getOkPredicate(), getErrHandler(), isMustHandleResult(),
                getHttpAsyncClient(), request, getHttpContext(), asyncRequestProducerFunc, asyncResponseConsumerSupplier, pushHandlerFactorySupplier);
    }
}
