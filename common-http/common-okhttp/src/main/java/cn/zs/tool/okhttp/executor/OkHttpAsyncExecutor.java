package cn.zs.tool.okhttp.executor;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.execute.BaseExecutor;
import cn.zs.tool.okhttp.response.OkHttpResponse;
import okhttp3.Call;
import okhttp3.Response;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * okhttp 异步处理
 *
 * @author sheng
 */
public class OkHttpAsyncExecutor<R> extends BaseExecutor<R> {
    /**
     * okhttp响应对象
     */
    private OkHttpResponse okHttpResponse;

    /**
     * 创建异步请求对象
     *
     * @param call             {@link Call}
     * @param response         {@link Response okhttp 响应结果}
     * @param msgConverter     http消息结果转换器
     * @param errHandler       异常处理器
     * @param okPredicate      状态码判断
     * @param mustHandleResult 是否必须处理结果
     */
    public static <R> OkHttpAsyncExecutor<R> create(Call call, Response response, HttpMsgConverter<R> msgConverter,
                                                    ThrowingConsumer<Throwable, Throwable> errHandler, Predicate<Integer> okPredicate,
                                                    Boolean mustHandleResult) {
        return new OkHttpAsyncExecutor<>(call, response, msgConverter, errHandler, okPredicate, Boolean.TRUE.equals(mustHandleResult));
    }

    public OkHttpAsyncExecutor(Call call, Response response, HttpMsgConverter<R> msgConverter,
                               ThrowingConsumer<Throwable, Throwable> errHandler, Predicate<Integer> okPredicate, boolean mustHandleResult) {
        super(msgConverter, okPredicate, errHandler, mustHandleResult);
        Objects.requireNonNull(call, "call must not be null");
        Objects.requireNonNull(response, "response must not be null");
        handleResult(response);
    }

    private void handleResult(Response response) {
        try (OkHttpResponse httpResponse = OkHttpResponse.create(response)) {
            this.okHttpResponse = httpResponse;
            if (httpResponse.isOk() || (mustHandleResult && Objects.nonNull(httpResponse.getBody()))) {
                this.result = msgConverter.apply(httpResponse.getBody());
            }
        } catch (Throwable e) {
            statusMsg = e.getMessage();
            errorHandler(e);
        }
    }

    @Override
    public int getStatus() {
        return Objects.nonNull(okHttpResponse) ? okHttpResponse.getStatus() : -1;
    }

    @Override
    public String getMessage() {
        return StringUtil.isNotBlank(statusMsg) ? statusMsg : okHttpResponse.getMessage();
    }

    @Override
    public HttpHeaders getHeaders() {
        return Objects.nonNull(okHttpResponse) ? okHttpResponse.getHeaders() : HttpHeaders.EMPTY_HEADERS;
    }
}
