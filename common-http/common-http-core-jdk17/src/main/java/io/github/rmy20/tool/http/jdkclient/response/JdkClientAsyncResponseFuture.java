package io.github.rmy20.tool.http.jdkclient.response;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.io.IOUtil;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.http.jdkclient.executor.JdkClientAsyncExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * jdk httpclient 异步回调
 *
 * @author sheng
 */
@Slf4j
public class JdkClientAsyncResponseFuture<R> extends CompletableFuture<JdkClientAsyncExecutor<R>> {
    /**
     * 创建#{@link JdkClientAsyncResponseFuture}
     */
    public static <R> JdkClientAsyncResponseFuture<R> create(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                                             ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                                             HttpClient httpClient, HttpRequest httpRequest) {
        return new JdkClientAsyncResponseFuture<>(resultHandle, okPredicate, errHandler, mustHandleResult, httpClient, httpRequest);
    }

    public JdkClientAsyncResponseFuture(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                        ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                        HttpClient httpClient, HttpRequest httpRequest) {
        Objects.requireNonNull(resultHandle, "resultHandle must not be null");
        Objects.requireNonNull(okPredicate, "okPredicate must not be null");
        Objects.requireNonNull(errHandler, "errHandler must not be null");
        Objects.requireNonNull(httpClient, "httpClient must not be null");
        Objects.requireNonNull(httpRequest, "httpRequest must not be null");
        executeAsync(resultHandle, okPredicate, errHandler, mustHandleResult, httpClient, httpRequest);
    }

    private void executeAsync(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                              ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                              HttpClient httpClient, HttpRequest httpRequest) {
        try {
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofInputStream())
                    .whenComplete((response, e) -> {
                        if (Objects.nonNull(e) || Objects.isNull(response)) {
                            completeExceptionally(e);
                        } else {
                            try {
                                complete(JdkClientAsyncExecutor.create(resultHandle, okPredicate, errHandler, mustHandleResult, response));
                            } catch (Throwable throwable) {
                                IOUtil.closeQuietly(response.body());
                                completeExceptionally(throwable);
                            }
                        }
                    });
        } catch (Throwable e) {
            log.error("httpclient执行异步请求异常", e);
            completeExceptionally(e);
        }
    }
}
