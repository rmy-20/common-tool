package io.github.rmy20.tool.http.jdkclient.executor;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.execute.BaseExecutor;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.http.jdkclient.response.JdkClientResponse;
import io.github.rmy20.tool.http.urlconnection.executor.UrlConnectionExecutor;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * http客户端执行器
 *
 * @author sheng
 */
public class JdkClientExecutor<R> extends BaseExecutor<R> {
    /**
     * http客户端
     */
    private final HttpClient httpClient;

    /**
     * http请求参数
     */
    private final HttpRequest httpRequest;

    /**
     * 响应
     */
    private JdkClientResponse clientResponse;

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
     * @param httpClient       #{@link HttpClient}
     * @param httpRequest      请求参数
     */
    public static <R> JdkClientExecutor<R> create(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                                  ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                                  HttpClient httpClient, HttpRequest httpRequest) {
        return new JdkClientExecutor<>(resultHandle, okPredicate, errHandler, mustHandleResult, httpClient, httpRequest);
    }

    protected JdkClientExecutor(HttpResultHandle<R> resultHandle, Predicate<Integer> okPredicate,
                                ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult,
                                HttpClient httpClient, HttpRequest httpRequest) {
        super(resultHandle, okPredicate, errHandler, mustHandleResult);
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
        this.httpRequest = Objects.requireNonNull(httpRequest, "httpRequest must not be null");
        this.execute();
    }

    protected void execute() {
        if (isExecute.compareAndSet(false, true)) {
            try {
                try (JdkClientResponse clientResponse = JdkClientResponse.create(httpClient.send(httpRequest,
                        HttpResponse.BodyHandlers.ofInputStream()));) {
                    this.clientResponse = clientResponse;
                    if (isOk() || (mustHandleResult && Objects.nonNull(clientResponse.getBody()))) {
                        result = resultHandle.apply(clientResponse.getBody());
                    }
                }
            } catch (Throwable e) {
                setStatusMsg(e.getMessage(), "http execute error");
                errorHandler(e);
            }
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
