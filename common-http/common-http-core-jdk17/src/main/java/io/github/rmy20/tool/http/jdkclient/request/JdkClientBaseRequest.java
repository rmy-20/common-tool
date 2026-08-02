package io.github.rmy20.tool.http.jdkclient.request;

import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.body.Body;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.decorator.RfcUriBuilderDecorator;
import io.github.rmy20.tool.http.core.exception.HttpException;
import io.github.rmy20.tool.http.core.execute.BaseExecutor;
import io.github.rmy20.tool.http.core.execute.BaseExecutorBuilder;
import io.github.rmy20.tool.http.core.request.BaseRequest;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.http.core.uri.RfcUri;

import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * JDK httpclient客户端基础请求
 *
 * @author sheng
 */
public abstract class JdkClientBaseRequest<T extends JdkClientBaseRequest<T>> implements RfcUriBuilderDecorator<T>, BaseRequest<T> {
    /**
     * uri
     */
    private final RfcUri.Builder uriBuilder;

    /**
     * 请求方法
     */
    private final HttpMethodEnum method;

    /**
     * 请求体
     */
    protected Body body;

    /**
     * 请求头
     */
    protected HttpHeaders headers;

    /**
     * 异步执行线程池
     */
    protected ExecutorService executorService;

    /**
     * 默认编码字符集，默认 UTF-8
     */
    protected Charset defaultCharset = StandardCharsets.UTF_8;

    public JdkClientBaseRequest(String url, HttpMethodEnum method) {
        this.method = Objects.requireNonNull(method, "Http method must not be null");
        RfcUri.Builder uriBuilder = RfcUri.fromUri(url);
        if (Objects.isNull(uriBuilder)) {
            throw new HttpException(String.format("RfcUri解析url[%s]-[%s]失败", url, method.getMethod()));
        }
        this.uriBuilder = uriBuilder;
        this.headers = HttpHeaders.create();
    }

    @Override
    public Charset getDefaultCharset() {
        return Objects.nonNull(defaultCharset) ? defaultCharset : BaseRequest.super.getDefaultCharset();
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    @Override
    public RfcUri.Builder getUriBuilder() {
        return uriBuilder;
    }

    @Override
    public <R> BaseExecutorBuilder<R, ? extends BaseExecutor<R>, ? extends BaseExecutor<R>, ?> executor(HttpResultHandle<R> resultHandle) {
        return null;
    }

    protected HttpRequest createRequest() {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uriBuilder.build().uri());
        // 请求头
        getHeaders().forEach((name, valueList) -> valueList.forEach(value -> builder.header(name, value)));
        if (Objects.nonNull(this.body)) {
            builder.method(method.getMethod(), null);
        }
        return builder.build();
    }
}
