package cn.zs.tool.http.core.execute;

import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.constant.HttpConstant;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * http 同步执行
 *
 * @author sheng
 */
@Slf4j
public abstract class HttpExecuteProcessor<R, T extends HttpExecuteProcessor<R, T>> {
    /**
     * 默认异常处理器
     */
    protected static final Consumer<Throwable> HTTP_ERR_HANDLE = e -> log.error("http请求异常", e);

    /**
     * 响应结果处理器
     */
    protected final HttpMsgConverter<R> msgConverter;

    /**
     * 成功判断
     */
    protected Predicate<Integer> okPredicate;

    /**
     * 错误处理器
     */
    protected Consumer<Throwable> errHandler;

    /**
     * 响应状态信息
     */
    protected String statusMsg;

    /**
     * true 为必须处理结果
     */
    protected boolean mustHandleResult;

    /**
     * 结果
     */
    protected R result;

    protected HttpExecuteProcessor(HttpMsgConverter<R> msgConverter) {
        this.msgConverter = Objects.requireNonNull(msgConverter, "msgConverter must not be null");
    }

    /**
     * 返回当前实例
     */
    protected abstract T self();

    /**
     * 获取结果前执行
     */
    protected void execute() {
    }

    /**
     * 获取结果
     */
    public R get() {
        execute();
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
        return getOkPredicate().test(getStatus());
    }

    /**
     * 获取状态描述信息
     */
    public abstract String getMessage();

    /**
     * 获取请求头
     */
    public abstract HttpHeaders getHeaders();

    /**
     * 获取成功判断器
     */
    public Predicate<Integer> getOkPredicate() {
        return Objects.nonNull(okPredicate) ? okPredicate : HttpConstant.HTTP_OK_PREDICATE;
    }

    /**
     * 获取错误处理器
     */
    public Consumer<Throwable> getErrHandler() {
        return Objects.nonNull(errHandler) ? errHandler : HTTP_ERR_HANDLE;
    }

    /**
     * 错误处理器
     */
    public T errHandler(Consumer<Throwable> errHandler) {
        this.errHandler = errHandler;
        return self();
    }

    /**
     * 成功判断器
     */
    public T okPredicate(Predicate<Integer> okPredicate) {
        this.okPredicate = okPredicate;
        return self();
    }

    /**
     * 是否必须处理结果
     */
    public T mustHandleResult(boolean mustHandleResult) {
        this.mustHandleResult = mustHandleResult;
        return self();
    }
}
