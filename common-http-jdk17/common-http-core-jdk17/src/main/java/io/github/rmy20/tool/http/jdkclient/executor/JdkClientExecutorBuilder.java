package io.github.rmy20.tool.http.jdkclient.executor;

import io.github.rmy20.tool.http.jdkclient.constant.JdkClientConstant;
import io.github.rmy20.tool.http.core.execute.BaseExecutorBuilder;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.http.jdkclient.response.JdkClientAsyncResponseFuture;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * http客户端执行器构建器
 *
 * @author sheng
 */
public class JdkClientExecutorBuilder<R> extends BaseExecutorBuilder<R, JdkClientExecutor<R>,
        JdkClientAsyncExecutor<R>, JdkClientExecutorBuilder<R>> {
    /**
     * http客户端
     */
    private final HttpClient httpClient;

    /**
     * http请求参数
     */
    private final HttpRequest httpRequest;

    /**
     * 创建#{@link JdkClientExecutorBuilder}
     */
    public static <R> JdkClientExecutorBuilder<R> create(HttpResultHandle<R> resultHandle, HttpClient httpClient,
                                                         HttpRequest httpRequest) {
        return new JdkClientExecutorBuilder<>(resultHandle, httpClient, httpRequest);
    }

    public JdkClientExecutorBuilder(HttpResultHandle<R> resultHandle, HttpClient httpClient, HttpRequest httpRequest) {
        super(resultHandle);
        this.httpRequest = Objects.requireNonNull(httpRequest, "httpRequest must not be null");
        this.httpClient = Objects.nonNull(httpClient) ? httpClient : JdkClientConstant.HTTP_CLIENT;
    }

    @Override
    public JdkClientExecutor<R> execute() {
        return JdkClientExecutor.create(getResultHandle(), getOkPredicate(),
                getErrHandler(), isMustHandleResult(), httpClient, httpRequest);
    }

    @Override
    public CompletableFuture<JdkClientAsyncExecutor<R>> executeAsync() {
        return JdkClientAsyncResponseFuture.create(getResultHandle(), getOkPredicate(),
                getErrHandler(), isMustHandleResult(), httpClient, httpRequest);
    }

    @Override
    public JdkClientExecutorBuilder<R> self() {
        return this;
    }
}
