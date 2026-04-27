package io.github.rmy20.tool.urlconnection.executor;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.body.Body;
import io.github.rmy20.tool.http.core.execute.BaseExecutor;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.urlconnection.response.UrlConnectionResponse;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * url connection http请求处理
 *
 * @author sheng
 */
public class UrlConnectionExecutor<R> extends BaseExecutor<R> {
    /**
     * URLConnection
     */
    private final HttpURLConnection connection;

    /**
     * 请求体
     */
    private final Body body;

    /**
     * 响应
     */
    private UrlConnectionResponse urlConnectionResponse;

    /**
     * 是否已执行
     */
    private final AtomicBoolean isExecute = new AtomicBoolean(false);

    /**
     * 创建#{@link UrlConnectionExecutor}
     *
     * @param resultHandle     结果处理器
     * @param okPredicate      响应码判断
     * @param errHandler       错误处理
     * @param mustHandleResult 是否处理结果
     * @param connection       #{@link HttpURLConnection}
     * @param body             请求体
     */
    public static <R> UrlConnectionExecutor<R> create(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                                      ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                                      HttpURLConnection connection, Body body) {
        return new UrlConnectionExecutor<>(resultHandle, okPredicate, errHandler, mustHandleResult, connection, body);
    }

    public UrlConnectionExecutor(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                 ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                 HttpURLConnection connection, Body body) {
        super(resultHandle, okPredicate, errHandler, mustHandleResult);
        this.connection = Objects.requireNonNull(connection, "connection must not be null");
        this.body = body;
        execute();
    }

    protected void execute() {
        if (isExecute.compareAndSet(false, true)) {
            try {
                connection.connect();
                if (connection.getDoOutput() && Objects.nonNull(body)) {
                    try (OutputStream outputStream = connection.getOutputStream();) {
                        body.writeTo(outputStream);
                    }
                } else {
                    connection.getResponseCode();
                }
                try (UrlConnectionResponse urlConnectionResponse = UrlConnectionResponse.create(connection);) {
                    this.urlConnectionResponse = urlConnectionResponse;
                    if (isOk() || (mustHandleResult && Objects.nonNull(urlConnectionResponse.getBody()))) {
                        result = resultHandle.apply(urlConnectionResponse.getBody());
                    }
                }
            } catch (Throwable e) {
                setStatusMsg(e.getMessage(), "urlConnection execute error");
                errorHandler(e);
            }
        }
    }

    @Override
    public int getStatus() {
        return Objects.nonNull(urlConnectionResponse) ? urlConnectionResponse.getStatus() : -1;
    }

    @Override
    public String getMessage() {
        return StringUtil.isNotBlank(statusMsg) ? statusMsg : urlConnectionResponse.getMessage();
    }

    @Override
    public HttpHeaders getHeaders() {
        return Objects.nonNull(urlConnectionResponse) ? urlConnectionResponse.getHeaders() : HttpHeaders.EMPTY_HEADERS;
    }
}
