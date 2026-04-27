package io.github.rmy20.tool.urlconnection.executor;

import io.github.rmy20.tool.core.constant.CommonConstant;
import io.github.rmy20.tool.http.core.body.Body;
import io.github.rmy20.tool.http.core.execute.BaseExecutorBuilder;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;

import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * URLConnection 执行器构建器
 *
 * @author sheng
 */
public class UrlConnectionExecutorBuilder<R>
        extends BaseExecutorBuilder<R, UrlConnectionExecutor<R>, UrlConnectionExecutor<R>, UrlConnectionExecutorBuilder<R>> {
    /**
     * URLConnection
     */
    private final HttpURLConnection connection;

    /**
     * 请求体
     */
    private final Body body;

    /**
     * 异步执行的线程池
     */
    private final ExecutorService executorService;

    /**
     * 创建URLConnection执行器构建器
     *
     * @param resultHandle 结果处理器
     * @param connection   #{@link HttpURLConnection}
     */
    public static <R> UrlConnectionExecutorBuilder<R> create(HttpResultHandle<R> resultHandle, HttpURLConnection connection,
                                                             Body body, ExecutorService executorService) {
        return new UrlConnectionExecutorBuilder<>(resultHandle, connection, body, executorService);
    }

    public UrlConnectionExecutorBuilder(HttpResultHandle<R> resultHandle, HttpURLConnection connection,
                                        Body body, ExecutorService executorService) {
        super(resultHandle);
        this.connection = Objects.requireNonNull(connection, "connection must not be null");
        this.body = body;
        this.executorService = executorService;
    }

    /**
     * 获取异步执行的线程池
     */
    public ExecutorService getExecutorService() {
        return Objects.nonNull(executorService) ? executorService : CommonConstant.EXECUTOR_SERVICE;
    }

    @Override
    protected UrlConnectionExecutorBuilder<R> self() {
        return this;
    }

    @Override
    public UrlConnectionExecutor<R> execute() {
        return UrlConnectionExecutor.create(getResultHandle(), getOkPredicate(), getErrHandler(), isMustHandleResult(), connection, body);
    }

    @Override
    public CompletableFuture<UrlConnectionExecutor<R>> executeAsync() {
        return CompletableFuture.supplyAsync(this::execute, getExecutorService());
    }
}
