package io.github.rmy20.tool.okhttp.response;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.okhttp.executor.OkHttpAsyncExecutor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * okhttp回调
 *
 * @author sheng
 */
@Slf4j
public class OkHttpAsyncResponseFuture<R> extends CompletableFuture<OkHttpAsyncExecutor<R>> implements Callback {
    /**
     * 结果处理器
     */
    private final HttpResultHandle<R> resultHandle;

    /**
     * 错误处理器
     */
    private final ThrowingConsumer<Throwable, Throwable> errHandler;

    /**
     * 成功判断
     */
    private final Predicate<Integer> okPredicate;

    /**
     * 是否必须处理结果
     */
    private final Boolean mustHandleResult;

    /**
     * 创建{@link OkHttpAsyncResponseFuture}
     *
     * @param call         {@link Call}
     * @param resultHandle 结果处理器
     * @param errHandler   错误处理器
     * @param okPredicate  成功判断
     */
    public static <R> OkHttpAsyncResponseFuture<R> create(Call call, HttpResultHandle<R> resultHandle,
                                                          ThrowingConsumer<Throwable, Throwable> errHandler,
                                                          Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return new OkHttpAsyncResponseFuture<>(call, resultHandle, errHandler, okPredicate, mustHandleResult);
    }

    public OkHttpAsyncResponseFuture(Call call, HttpResultHandle<R> resultHandle, ThrowingConsumer<Throwable, Throwable> errHandler,
                                     Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        this.resultHandle = Objects.requireNonNull(resultHandle, "resultHandle must not be null");
        this.errHandler = Objects.requireNonNull(errHandler, "errHandler must not be null");
        this.okPredicate = Objects.requireNonNull(okPredicate, "okPredicate must not be null");
        this.mustHandleResult = mustHandleResult;
        enqueue(call);
    }

    /**
     * 异步请求
     */
    private void enqueue(Call call) {
        try {
            Objects.requireNonNull(call, "call must not be null").enqueue(this);
        } catch (Throwable e) {
            log.error("执行okhttp异步请求异常", e);
            completeExceptionally(e);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        completeExceptionally(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            complete(OkHttpAsyncExecutor.create(call, response, resultHandle, errHandler, okPredicate, mustHandleResult));
        } catch (Throwable e) {
            log.error("构建okhttp异步响应处理器异常", e);
            completeExceptionally(e);
        }
    }
}
