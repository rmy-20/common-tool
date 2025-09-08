package cn.zs.tool.httpclient5.executor;

import cn.zs.tool.core.fuction.throwing.ThrowingFunc;
import cn.zs.tool.core.fuction.throwing.ThrowingSupplier;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.execute.BaseExecutorBuilder;
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
public class HttpClient5ExecutorBuilder<R> extends BaseExecutorBuilder<R,
        HttpClient5Executor<R>, HttpClient5AsyncExecutor<R>, HttpClient5ExecutorBuilder<R>> {
    /**
     * 请求信息
     */
    private final HttpUriRequestBase request;

    /**
     * 同步请求 #{@link HttpClient}
     */
    private final HttpClient httpClient;

    /**
     * 异步请求 #{@link HttpAsyncClient}
     */
    private final HttpAsyncClient httpAsyncClient;

    /**
     * 请求上下文
     */
    private final HttpContext httpContext;

    /**
     * #{@link AsyncRequestProducer} 提供者
     */
    private final ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc;

    /**
     * #{@link AsyncResponseConsumer} 提供者
     */
    private final ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier;

    /**
     * #{@link AsyncPushConsumer} 提供者
     */
    private final ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier;

    /**
     * 创建执行器构建器
     *
     * @param request                       请求
     * @param msgConverter                  消息转换器
     * @param httpClient                    同步请求 #{@link HttpClient}
     * @param httpAsyncClient               异步请求 #{@link HttpAsyncClient}
     * @param httpContext                   请求上下文
     * @param asyncRequestProducerFunc      #{@link AsyncRequestProducer} 提供者
     * @param asyncResponseConsumerSupplier #{@link AsyncResponseConsumer} 提供者
     * @param pushHandlerFactorySupplier    #{@link AsyncPushConsumer} 提供者
     */
    public static <R> HttpClient5ExecutorBuilder<R> create(HttpUriRequestBase request, HttpMsgConverter<R> msgConverter,
                                                           HttpClient httpClient, HttpAsyncClient httpAsyncClient, HttpContext httpContext,
                                                           ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc,
                                                           ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier,
                                                           ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier) {
        return new HttpClient5ExecutorBuilder<>(request, msgConverter, httpClient, httpAsyncClient, httpContext,
                asyncRequestProducerFunc, asyncResponseConsumerSupplier, pushHandlerFactorySupplier);
    }

    public HttpClient5ExecutorBuilder(HttpUriRequestBase request, HttpMsgConverter<R> msgConverter,
                                      HttpClient httpClient, HttpAsyncClient httpAsyncClient, HttpContext httpContext,
                                      ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc,
                                      ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier,
                                      ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier) {
        super(msgConverter);
        this.request = Objects.requireNonNull(request, "request must not be null");
        this.httpClient = httpClient;
        this.httpAsyncClient = httpAsyncClient;
        this.httpContext = httpContext;
        this.asyncRequestProducerFunc = asyncRequestProducerFunc;
        this.asyncResponseConsumerSupplier = asyncResponseConsumerSupplier;
        this.pushHandlerFactorySupplier = pushHandlerFactorySupplier;
    }

    /**
     * 获取当前实例所用 #{@link HttpClient}
     */
    public HttpClient getHttpClient() {
        return Objects.nonNull(httpClient) ? httpClient : HttpClient5Constant.HTTP_CLIENT;
    }

    /**
     * 获取当前实例所用 #{@link HttpAsyncClient}
     */
    public HttpAsyncClient getHttpAsyncClient() {
        return Objects.nonNull(httpAsyncClient) ? httpAsyncClient : HttpClient5Constant.HTTP_ASYNC_CLIENT;
    }

    /**
     * 获取当前实例所用请求上下文
     */
    public HttpContext getHttpContext() {
        return Objects.nonNull(httpContext) ? httpContext : HttpClientContext.create();
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
