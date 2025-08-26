package cn.zs.tool.okhttp.executor;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.execute.HttpExecuteProcessor;
import cn.zs.tool.okhttp.response.OkHttpResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * okhttp http请求处理
 *
 * @author sheng
 */
@Slf4j
public class OkHttpExecutor<R> extends HttpExecuteProcessor<R> {
    /**
     * okhttp调用
     */
    private final Call call;

    /**
     * okhttp响应对象
     */
    private OkHttpResponse okHttpResponse;

    /**
     * 创建okhttp交换机
     *
     * @param call             okhttp调用
     * @param msgConverter     结果处理器
     * @param errHandler       异常处理器
     * @param okPredicate      ok响应码判断
     * @param mustHandleResult 是否强制处理结果
     */
    public static <R> OkHttpExecutor<R> create(Call call, HttpMsgConverter<R> msgConverter, ThrowingConsumer<Throwable, Throwable> errHandler,
                                               Predicate<Integer> okPredicate, boolean mustHandleResult) {
        return new OkHttpExecutor<>(call, msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    public OkHttpExecutor(Call call, HttpMsgConverter<R> msgConverter, ThrowingConsumer<Throwable, Throwable> errHandler,
                          Predicate<Integer> okPredicate, boolean mustHandleResult) {
        super(msgConverter, okPredicate, errHandler, mustHandleResult);
        this.call = Objects.requireNonNull(call, "okhttp call must not be null");
        execute();
    }

    protected void execute() {
        if (Objects.isNull(okHttpResponse) && !call.isExecuted()) {
            try (OkHttpResponse clientResponse = OkHttpResponse.create(call.execute())) {
                this.okHttpResponse = clientResponse;
                if (isOk() || (mustHandleResult && Objects.nonNull(clientResponse.getBody()))) {
                    result = msgConverter.apply(clientResponse.getBody());
                }
            } catch (Throwable e) {
                statusMsg = e.getMessage();
                errorHandler(e);
            }
        }
    }

    @Override
    public int getStatus() {
        return Objects.nonNull(okHttpResponse) ? okHttpResponse.getStatus() : -1;
    }

    /**
     * 获取状态码
     */
    @Override
    public String getMessage() {
        return StringUtil.isNotBlank(statusMsg) ? statusMsg : okHttpResponse.getMessage();
    }

    /**
     * 获取请求头
     */
    @Override
    public HttpHeaders getHeaders() {
        return Objects.nonNull(okHttpResponse) ? okHttpResponse.getHeaders() : HttpHeaders.EMPTY_HEADERS;
    }
}
