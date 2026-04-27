package io.github.rmy20.tool.httpclient5.executor;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.execute.BaseExecutor;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.httpclient5.response.HttpClient5Response;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * httpclient5 http 异步请求处理
 *
 * @author sheng
 */
public class HttpClient5AsyncExecutor<R> extends BaseExecutor<R> {
    /**
     * 响应
     */
    private HttpClient5Response httpClient5Response;

    /**
     * 创建httpclient5异步请求处理
     *
     * @param basicHttpResponse httpclient 响应
     * @param resultHandle      结果处理器
     * @param okPredicate       响应码判断
     * @param errHandler        异常处理
     * @param mustHandleResult  是否必须处理结果
     */
    public static <R> HttpClient5AsyncExecutor<R> create(BasicClassicHttpResponse basicHttpResponse, HttpResultHandle<R> resultHandle,
                                                         Predicate<Integer> okPredicate, ThrowingConsumer<Throwable, Throwable> errHandler,
                                                         boolean mustHandleResult) {
        return new HttpClient5AsyncExecutor<>(basicHttpResponse, resultHandle, okPredicate, errHandler, mustHandleResult);
    }

    protected HttpClient5AsyncExecutor(BasicClassicHttpResponse basicHttpResponse, HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                       ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult) {
        super(resultHandle, okPredicate, errHandler, mustHandleResult);
        handleResult(Objects.requireNonNull(basicHttpResponse, "basicHttpResponse can not be null"));
    }

    protected void handleResult(BasicClassicHttpResponse basicHttpResponse) {
        if (Objects.isNull(httpClient5Response)) {
            try (HttpClient5Response client5Response = HttpClient5Response.create(basicHttpResponse)) {
                this.httpClient5Response = client5Response;
                if (isOk() || (mustHandleResult && Objects.nonNull(client5Response.getBody()))) {
                    result = resultHandle.apply(httpClient5Response.getBody());
                }
            } catch (Throwable e) {
                setStatusMsg(e.getMessage(), "httpclient async handle result error");
                errorHandler(e);
            }
        }
    }

    @Override
    public int getStatus() {
        return Objects.nonNull(httpClient5Response) ? httpClient5Response.getStatus() : -1;
    }

    /**
     * 获取状态码
     */
    @Override
    public String getMessage() {
        return StringUtil.isNotBlank(statusMsg) ? statusMsg : httpClient5Response.getMessage();
    }

    /**
     * 获取请求头
     */
    @Override
    public HttpHeaders getHeaders() {
        return Objects.nonNull(httpClient5Response) ? httpClient5Response.getHeaders() : HttpHeaders.EMPTY_HEADERS;
    }
}
