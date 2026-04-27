package io.github.rmy20.tool.okhttp.executor;

import io.github.rmy20.tool.http.core.execute.BaseExecutorBuilder;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.okhttp.response.OkHttpAsyncResponseFuture;
import okhttp3.Call;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * okhttp执行器构建器
 *
 * @author sheng
 */
public class OkHttpExecutorBuilder<R> extends BaseExecutorBuilder<R, OkHttpExecutor<R>, OkHttpAsyncExecutor<R>, OkHttpExecutorBuilder<R>> {
    /**
     * 请求对象
     */
    private final Call call;

    /**
     * 创建执行器构建器
     *
     * @param call         请求对象
     * @param resultHandle 结果处理器
     */
    public static <R> OkHttpExecutorBuilder<R> create(Call call, HttpResultHandle<R> resultHandle) {
        return new OkHttpExecutorBuilder<>(call, resultHandle);
    }

    public OkHttpExecutorBuilder(Call call, HttpResultHandle<R> resultHandle) {
        super(resultHandle);
        this.call = Objects.requireNonNull(call, "call must not be null");
    }

    @Override
    protected OkHttpExecutorBuilder<R> self() {
        return this;
    }

    @Override
    public OkHttpExecutor<R> execute() {
        return OkHttpExecutor.create(call, getResultHandle(), getErrHandler(), getOkPredicate(), isMustHandleResult());
    }

    @Override
    public CompletableFuture<OkHttpAsyncExecutor<R>> executeAsync() {
        return OkHttpAsyncResponseFuture.create(call, getResultHandle(), getErrHandler(), getOkPredicate(), isMustHandleResult());
    }
}
