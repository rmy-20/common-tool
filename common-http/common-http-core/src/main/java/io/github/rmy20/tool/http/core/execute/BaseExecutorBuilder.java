package io.github.rmy20.tool.http.core.execute;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.http.core.constant.HttpConstant;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * 执行器构建者
 *
 * @author sheng
 */
@Slf4j
public abstract class BaseExecutorBuilder<R, SyncExecutor extends BaseExecutor<R>,
        AsyncExecutor extends BaseExecutor<R>, T extends BaseExecutorBuilder<R, SyncExecutor, AsyncExecutor, T>> {
    /**
     * 错误处理器
     */
    private static final ThrowingConsumer<Throwable, Throwable> ERR_HANDLE = throwable -> log.error("http请求异常", throwable);

    /**
     * 响应结果处理器
     */
    @Getter
    protected final HttpResultHandle<R> resultHandle;

    /**
     * 成功判断
     */
    protected Predicate<Integer> okPredicate;

    /**
     * 错误处理器
     */
    protected ThrowingConsumer<Throwable, Throwable> errHandler;

    /**
     * true 为必须处理结果
     */
    @Getter
    protected boolean mustHandleResult = false;

    public BaseExecutorBuilder(HttpResultHandle<R> resultHandle) {
        this.resultHandle = Objects.requireNonNull(resultHandle, "resultHandle must not be null");
    }

    /**
     * 返回当前实例
     */
    protected abstract T self();

    /**
     * 设置成功判断
     */
    public T okPredicate(Predicate<Integer> okPredicate) {
        this.okPredicate = okPredicate;
        return self();
    }

    /**
     * 获取成功判断器
     */
    public Predicate<Integer> getOkPredicate() {
        return Objects.nonNull(okPredicate) ? okPredicate : HttpConstant.HTTP_OK_PREDICATE;
    }

    /**
     * 错误处理器
     */
    public T errHandler(ThrowingConsumer<Throwable, Throwable> errHandler) {
        this.errHandler = errHandler;
        return self();
    }

    /**
     * 获取错误处理器
     */
    public ThrowingConsumer<Throwable, Throwable> getErrHandler() {
        return Objects.nonNull(errHandler) ? errHandler : ERR_HANDLE;
    }

    /**
     * 必须处理结果
     */
    public T mustHandleResult() {
        this.mustHandleResult = true;
        return self();
    }

    /**
     * 同步执行
     */
    public abstract SyncExecutor execute();

    /**
     * 异步执行
     */
    public abstract CompletableFuture<AsyncExecutor> executeAsync();
}
