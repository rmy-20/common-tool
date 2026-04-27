package io.github.rmy20.tool.http.core.execute;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * http 执行
 *
 * @author sheng
 */
@Slf4j
public abstract class BaseExecutor<R> {
    /**
     * 响应处理器
     */
    protected final HttpResultHandle<R> resultHandle;

    /**
     * 成功判断
     */
    protected final Predicate<Integer> okPredicate;

    /**
     * 错误处理器
     */
    protected final ThrowingConsumer<Throwable, ? extends Throwable> errHandler;

    /**
     * true 为必须处理结果
     */
    protected final boolean mustHandleResult;

    /**
     * 响应状态信息
     */
    protected String statusMsg;

    /**
     * 结果
     */
    protected R result;

    protected BaseExecutor(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                           ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult) {
        this.resultHandle = Objects.requireNonNull(resultHandle, "resultHandle must not be null");
        this.okPredicate = Objects.requireNonNull(okPredicate, "okPredicate must not be null");
        this.errHandler = Objects.requireNonNull(errHandler, "errHandler must not be null");
        this.mustHandleResult = mustHandleResult;
    }

    /**
     * 异常处理
     *
     * @param ex 异常
     */
    @SuppressWarnings("unchecked")
    protected void errorHandler(Throwable ex) {
        ((ThrowingConsumer<Throwable, RuntimeException>) errHandler).accept(ex);
    }

    /**
     * 响应状态信息
     */
    protected void setStatusMsg(String statusMsg, String defaultMsg) {
        this.statusMsg = StringUtil.isNotBlank(statusMsg) ? statusMsg : defaultMsg;
    }

    /**
     * 获取结果
     */
    public R get() {
        return result;
    }

    /**
     * 获取状态码
     */
    public abstract int getStatus();

    /**
     * 判断是否成功响应
     */
    public boolean isOk() {
        return okPredicate.test(getStatus());
    }

    /**
     * 获取状态描述信息
     */
    public abstract String getMessage();

    /**
     * 获取请求头
     */
    public abstract HttpHeaders getHeaders();
}
