package io.github.rmy20.tool.http.jdkclient.executor;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.execute.BaseExecutor;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.http.jdkclient.response.JdkClientResponse;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * http客户端异步执行器
 *
 * @author sheng
 */
public class JdkClientAsyncExecutor<R> extends BaseExecutor<R> {
    /**
     * 响应
     */
    private JdkClientResponse clientResponse;

    /**
     * 创建#{@link JdkClientAsyncExecutor}
     */
    public static <R> JdkClientAsyncExecutor<R> create(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                                       ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                                       HttpResponse<InputStream> response) {
        return new JdkClientAsyncExecutor<>(resultHandle, okPredicate, errHandler, mustHandleResult, response);
    }

    protected JdkClientAsyncExecutor(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                     ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                     HttpResponse<InputStream> response) {
        super(resultHandle, okPredicate, errHandler, mustHandleResult);
        handleResult(Objects.requireNonNull(response, "httpResponse must not be null"));
    }

    private void handleResult(HttpResponse<InputStream> response) {
        try (JdkClientResponse clientResponse = JdkClientResponse.create(response)) {
            this.clientResponse = clientResponse;
            if (isOk() || (mustHandleResult && Objects.nonNull(clientResponse.getBody()))) {
                result = resultHandle.apply(clientResponse.getBody());
            }
        } catch (Throwable e) {
            setStatusMsg(e.getMessage(), "jdk httpclient async handle result error");
            errorHandler(e);
        }
    }

    @Override
    public int getStatus() {
        return Objects.nonNull(clientResponse) ? clientResponse.getStatus() : -1;
    }

    @Override
    public String getMessage() {
        return StringUtil.isNotBlank(statusMsg) ? statusMsg : (Objects.nonNull(clientResponse) ? clientResponse.getMessage() : "error");
    }

    @Override
    public HttpHeaders getHeaders() {
        return Objects.nonNull(clientResponse) ? clientResponse.getHeaders() : HttpHeaders.EMPTY_HEADERS;
    }
}
