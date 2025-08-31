package cn.zs.tool.httpclient5.response;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import cn.zs.tool.core.fuction.throwing.ThrowingFunc;
import cn.zs.tool.core.fuction.throwing.ThrowingSupplier;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.httpclient5.executor.HttpClient5AsyncExecutor;
import cn.zs.tool.httpclient5.support.classic.FunctionalClassicEntityConsumer;
import cn.zs.tool.httpclient5.support.classic.HttpEntityClassicEntityProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.nio.AsyncPushConsumer;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.HandlerFactory;
import org.apache.hc.core5.http.nio.support.AbstractAsyncResponseConsumer;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * 异步响应结果
 *
 * @author sheng
 */
@Slf4j
public class HttpClient5AsyncResponseFuture<R> extends CompletableFuture<HttpClient5AsyncExecutor<R>> implements FutureCallback<BasicClassicHttpResponse> {
    /**
     * 响应处理器
     */
    protected final HttpMsgConverter<R> msgConverter;

    /**
     * 成功判断
     */
    protected final Predicate<Integer> okPredicate;

    /**
     * 错误处理器
     */
    protected final ThrowingConsumer<Throwable, Throwable> errHandler;

    /**
     * true 为必须处理结果
     */
    protected final boolean mustHandleResult;

    /**
     * 同步请求 #{@link HttpClient}
     */
    private final HttpAsyncClient httpClient;

    /**
     * 请求信息
     */
    private final HttpUriRequestBase request;

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
     * 创建异步响应结果
     *
     * @param msgConverter                  消息转换器
     * @param okPredicate                   响应成功判断
     * @param errHandler                    错误处理器
     * @param mustHandleResult              true 为必须处理结果
     * @param httpClient                    httpClient
     * @param request                       请求信息
     * @param httpContext                   请求上下文
     * @param asyncRequestProducerFunc      #{@link AsyncRequestProducer} 提供者
     * @param asyncResponseConsumerSupplier #{@link AsyncResponseConsumer} 提供者
     * @param pushHandlerFactorySupplier    #{@link AsyncPushConsumer} 提供者
     */
    public static <R> HttpClient5AsyncResponseFuture<R> create(HttpMsgConverter<R> msgConverter, Predicate<Integer> okPredicate,
                                                               ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                                               HttpAsyncClient httpClient, HttpUriRequestBase request, HttpContext httpContext,
                                                               ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc,
                                                               ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier,
                                                               ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier) {
        return new HttpClient5AsyncResponseFuture<>(msgConverter, okPredicate, errHandler, mustHandleResult,
                httpClient, request, httpContext, asyncRequestProducerFunc, asyncResponseConsumerSupplier, pushHandlerFactorySupplier);
    }

    public HttpClient5AsyncResponseFuture(HttpMsgConverter<R> msgConverter, Predicate<Integer> okPredicate,
                                          ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                          HttpAsyncClient httpClient, HttpUriRequestBase request, HttpContext httpContext,
                                          ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc,
                                          ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier,
                                          ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier) {
        this.msgConverter = Objects.requireNonNull(msgConverter, "msgConverter must not be null");
        this.errHandler = Objects.requireNonNull(errHandler, "errHandler must not be null");
        this.okPredicate = Objects.requireNonNull(okPredicate, "okPredicate must not be null");
        this.mustHandleResult = mustHandleResult;
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
        this.request = Objects.requireNonNull(request, "request must not be null");
        this.httpContext = httpContext;
        this.asyncRequestProducerFunc = asyncRequestProducerFunc;
        this.asyncResponseConsumerSupplier = asyncResponseConsumerSupplier;
        this.pushHandlerFactorySupplier = pushHandlerFactorySupplier;
        executeAsync();
    }

    protected void executeAsync() {
        try {
            httpClient.execute(getAsyncRequestProducer(), getAsyncResponseConsumer(), getPushHandlerFactory(), httpContext, this);
        } catch (Throwable e) {
            log.error("执行okhttp异步请求异常", e);
            completeExceptionally(e);
        }
    }

    /**
     * 获取#{@link AsyncRequestProducer}
     */
    public AsyncRequestProducer getAsyncRequestProducer() throws Throwable {
        if (Objects.nonNull(asyncRequestProducerFunc)) {
            return asyncRequestProducerFunc.apply(request);
        }
        HttpEntity entity = request.getEntity();
        return new BasicRequestProducer(request, Objects.nonNull(entity) ? HttpEntityClassicEntityProducer.create(entity) : null);
    }

    public AsyncResponseConsumer<BasicClassicHttpResponse> getAsyncResponseConsumer() throws Throwable {
        if (Objects.nonNull(asyncResponseConsumerSupplier)) {
            return asyncResponseConsumerSupplier.get();
        }
        return new AbstractAsyncResponseConsumer<BasicClassicHttpResponse, InputStream>(
                FunctionalClassicEntityConsumer.create((contentType, inputStream) -> inputStream)) {
            @Override
            public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {
            }

            @Override
            protected BasicClassicHttpResponse buildResult(HttpResponse response, InputStream entity, ContentType contentType) {
                BasicClassicHttpResponse classicHttpResponse = new BasicClassicHttpResponse(response.getCode(),
                        response.getReasonPhrase());
                classicHttpResponse.setEntity(new InputStreamEntity(entity, contentType));
                return classicHttpResponse;
            }
        };
    }

    public HandlerFactory<AsyncPushConsumer> getPushHandlerFactory() throws Throwable {
        return Objects.nonNull(pushHandlerFactorySupplier) ? pushHandlerFactorySupplier.get() : null;
    }

    @Override
    public void completed(BasicClassicHttpResponse basicHttpResponse) {
        try {
            complete(HttpClient5AsyncExecutor.create(basicHttpResponse, msgConverter, okPredicate, errHandler, mustHandleResult));
        } catch (Throwable e) {
            log.error("构建http client异步响应处理器异常", e);
            completeExceptionally(e);
        }
    }

    @Override
    public void failed(Exception e) {
        completeExceptionally(e);
    }

    @Override
    public void cancelled() {
        completeExceptionally(new HttpException("async http cancelled"));
    }
}
