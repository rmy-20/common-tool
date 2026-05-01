package io.github.rmy20.tool.httpclient4.executor;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.execute.BaseExecutor;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.httpclient4.response.HttpClient4Response;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * httpclient4 http请求执行器
 *
 * @author sheng
 */
public class HttpClient4Executor<R> extends BaseExecutor<R> {
    /**
     * 同步请求{@link HttpClient}
     */
    private final HttpClient httpClient;

    /**
     * 请求信息
     */
    private final HttpRequestBase requestBase;

    /**
     * 请求上下文
     */
    private final HttpContext httpContext;

    /**
     * 响应
     */
    private HttpClient4Response httpClient4Response;

    /**
     * 是否已执行
     */
    private final AtomicBoolean isExecute = new AtomicBoolean(false);

    /**
     * 创建{@link HttpClient4Executor}
     */
    public static <R> HttpClient4Executor<R> create(HttpClient httpClient, HttpRequestBase requestBase, HttpContext httpContext,
                                                    HttpResultHandle<R> resultHandle,
                                                    Predicate<Integer> okPredicate, ThrowingConsumer<Throwable, Throwable> errHandler,
                                                    boolean mustHandleResult) {
        return new HttpClient4Executor<>(httpClient, requestBase, httpContext, resultHandle, okPredicate, errHandler, mustHandleResult);
    }

    public HttpClient4Executor(HttpClient httpClient, HttpRequestBase requestBase, HttpContext httpContext, HttpResultHandle<R> resultHandle,
                               Predicate<Integer> okPredicate, ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult) {
        super(resultHandle, okPredicate, errHandler, mustHandleResult);
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
        this.requestBase = Objects.requireNonNull(requestBase, "requestBase must not be null");
        this.httpContext = httpContext;
        execute();
    }

    private void execute() {
        if (isExecute.compareAndSet(false, true)) {
            try {
                httpClient.execute(requestBase, response -> {
                    try (HttpClient4Response httpClient4Response = HttpClient4Response.create(response);) {
                        this.httpClient4Response = httpClient4Response;
                        if (httpClient4Response.isOk() || (mustHandleResult && Objects.nonNull(httpClient4Response.getBody()))) {
                            result = resultHandle.apply(httpClient4Response.getBody());
                        }
                    } catch (Throwable ex) {
                        setStatusMsg(ex.getMessage(), "httpclient handle result error");
                        errorHandler(ex);
                    }
                    return result;
                }, httpContext);
            } catch (Throwable ex) {
                setStatusMsg(ex.getMessage(), "httpclient execute error");
                errorHandler(ex);
            }
        }
    }

    @Override
    public int getStatus() {
        return Objects.nonNull(httpClient4Response) ? httpClient4Response.getStatus() : -1;
    }

    @Override
    public String getMessage() {
        return StringUtil.isNotBlank(statusMsg) ? statusMsg : httpClient4Response.getMessage();
    }

    @Override
    public HttpHeaders getHeaders() {
        return Objects.nonNull(httpClient4Response) ? httpClient4Response.getHeaders() : HttpHeaders.EMPTY_HEADERS;
    }
}
