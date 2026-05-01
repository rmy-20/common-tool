package io.github.rmy20.tool.httpclient4.executor;

import io.github.rmy20.tool.core.constant.CommonConstant;
import io.github.rmy20.tool.http.core.execute.BaseExecutorBuilder;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.httpclient4.constant.HttpClient4Constant;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * httpclient4执行器构建器
 *
 * @author sheng
 */
public class HttpClient4ExecutorBuilder<R> extends BaseExecutorBuilder<R,
        HttpClient4Executor<R>, HttpClient4Executor<R>, HttpClient4ExecutorBuilder<R>> {
    /**
     * 同步请求{@link HttpClient}
     */
    private final HttpClient httpClient;

    /**
     * 请求信息
     */
    private final HttpRequestBase requestBase;

    /**
     * 请求上下文
     */
    private final HttpContext httpContext;

    /**
     * 异步执行的线程池
     */
    private final ExecutorService executorService;

    /**
     * 请求
     */
    public static <R> HttpClient4ExecutorBuilder<R> create(HttpResultHandle<R> resultHandle, HttpClient httpClient, HttpRequestBase requestBase,
                                                           HttpContext httpContext, ExecutorService executorService) {
        return new HttpClient4ExecutorBuilder<>(resultHandle, httpClient, requestBase, httpContext, executorService);
    }

    public HttpClient4ExecutorBuilder(HttpResultHandle<R> resultHandle, HttpClient httpClient, HttpRequestBase requestBase,
                                      HttpContext httpContext, ExecutorService executorService) {
        super(resultHandle);
        this.requestBase = Objects.requireNonNull(requestBase, "requestBase must not be null");
        this.httpClient = httpClient;
        this.httpContext = httpContext;
        this.executorService = executorService;
    }

    /**
     * 获取当前实例发起请求所用的{@link HttpClient}
     */
    public HttpClient getHttpClient() {
        return Objects.nonNull(httpClient) ? httpClient : HttpClient4Constant.HTTP_CLIENT;
    }

    /**
     * 获取当前实例请求上下文
     */
    public HttpContext getHttpContext() {
        return Objects.nonNull(httpContext) ? httpContext : HttpClientContext.create();
    }

    /**
     * 异步执行所用线程池
     */
    public ExecutorService getExecutorService() {
        return Objects.nonNull(executorService) ? executorService : CommonConstant.EXECUTOR_SERVICE;
    }

    @Override
    protected HttpClient4ExecutorBuilder<R> self() {
        return this;
    }

    @Override
    public HttpClient4Executor<R> execute() {
        return HttpClient4Executor.create(getHttpClient(), requestBase, getHttpContext(),
                getResultHandle(), getOkPredicate(), getErrHandler(), isMustHandleResult());
    }

    @Override
    public CompletableFuture<HttpClient4Executor<R>> executeAsync() {
        return CompletableFuture.supplyAsync(this::execute, getExecutorService());
    }
}
