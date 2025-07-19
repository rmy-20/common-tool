package cn.zs.tool.http.core.execute;

import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.constant.HttpConstant;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * http 异步执行
 *
 * @author sheng
 */
@Slf4j
public abstract class HttpAsyncExecuteProcessor<R> {
    /**
     * 默认异常处理器
     */
    protected static final Consumer<Throwable> HTTP_ERR_HANDLE = e -> log.error("http异步请求异常", e);

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
    protected final Consumer<Throwable> errHandler;

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

    /**
     * 创建异步请求对象
     *
     * @param msgConverter     http消息结果转换器
     * @param errHandler       异常处理器
     * @param okPredicate      状态码判断
     * @param mustHandleResult 是否必须处理结果
     */
    public HttpAsyncExecuteProcessor(HttpMsgConverter<R> msgConverter, Predicate<Integer> okPredicate,
                                     Consumer<Throwable> errHandler, boolean mustHandleResult) {
        this.msgConverter = Objects.requireNonNull(msgConverter, "msgConverter must not be null");
        this.okPredicate = Objects.nonNull(okPredicate) ? okPredicate : HttpConstant.HTTP_OK_PREDICATE;
        this.errHandler = Objects.nonNull(errHandler) ? errHandler : HTTP_ERR_HANDLE;
        this.mustHandleResult = mustHandleResult;
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
