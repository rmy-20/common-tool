package io.github.rmy20.tool.http.jdkclient.request;

import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.decorator.RfcUriBuilderDecorator;
import io.github.rmy20.tool.http.core.exception.HttpException;
import io.github.rmy20.tool.http.core.request.BaseRequest;
import io.github.rmy20.tool.http.core.result.HttpByteArrayResultHandle;
import io.github.rmy20.tool.http.core.result.HttpFileResultHandle;
import io.github.rmy20.tool.http.core.result.HttpJsonResultHandle;
import io.github.rmy20.tool.http.core.result.HttpOutputStreamResultHandle;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.http.core.result.HttpStringResultHandle;
import io.github.rmy20.tool.http.core.result.HttpXmlResultHandle;
import io.github.rmy20.tool.http.core.uri.RfcUri;
import io.github.rmy20.tool.http.jdkclient.executor.JdkClientExecutorBuilder;

import java.io.File;
import java.io.OutputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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
    protected HttpRequest.BodyPublisher bodyPublisher;

    /**
     * 请求头
     */
    protected HttpHeaders headers;

    /**
     * http客户端
     */
    private HttpClient httpClient;

    /**
     * 协议版本
     */
    private HttpClient.Version version;

    /**
     * 超时时间
     */
    private Duration timeout;

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

    /**
     * 设置#{@link HttpClient}
     */
    public T httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return self();
    }

    /**
     * 协议版本
     */
    public T version(HttpClient.Version version) {
        this.version = version;
        return self();
    }

    /**
     * 超时时间
     */
    public T timeout(Duration timeout) {
        this.timeout = timeout;
        return self();
    }

    /**
     * 超时时间
     */
    public T timeout(long timeout, ChronoUnit unit) {
        this.timeout = Duration.of(timeout, unit);
        return self();
    }

    /**
     * 设置默认编码字符集
     */
    public T defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return self();
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

    // region 请求

    /**
     * 获取处理 UTF_8 {@link String}结果的请求执行器
     */
    @Override
    public JdkClientExecutorBuilder<String> stringExecutor() {
        return executor(HttpStringResultHandle.UTF_8_INSTANCE);
    }

    /**
     * 获取处理{@link String}结果的请求执行器
     *
     * @param charset 结果编码
     */
    @Override
    public JdkClientExecutorBuilder<String> stringExecutor(Charset charset) {
        return executor(HttpStringResultHandle.create(charset));
    }

    /**
     * 获取处理 json 结果的请求执行器
     *
     * @param resultHandle {@link HttpJsonResultHandle}
     */
    @Override
    public <R> JdkClientExecutorBuilder<R> jsonExecutor(HttpJsonResultHandle<R> resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取处理 xml 结果的请求执行器
     *
     * @param resultHandle {@link HttpXmlResultHandle}
     */
    @Override
    public <R> JdkClientExecutorBuilder<R> xmlExecutor(HttpXmlResultHandle<R> resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取处理 byte[] 结果的请求执行器
     */
    @Override
    public JdkClientExecutorBuilder<byte[]> bytesExecutor() {
        return executor(HttpByteArrayResultHandle.INSTANCE);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param targetFile 目标文件
     * @return 下载文件大小
     */
    @Override
    public JdkClientExecutorBuilder<Long> downloadExecutor(File targetFile) {
        return executor(HttpFileResultHandle.create(targetFile));
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param resultHandle 结果处理器
     * @return 下载文件大小
     */
    @Override
    public JdkClientExecutorBuilder<Long> downloadExecutor(HttpFileResultHandle resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param outputStream 输出流
     * @return 下载文件大小
     */
    @Override
    public JdkClientExecutorBuilder<Long> downloadExecutor(OutputStream outputStream) {
        return executor(HttpOutputStreamResultHandle.create(outputStream));
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param resultHandle 结果处理器
     * @return 下载文件大小
     */
    @Override
    public JdkClientExecutorBuilder<Long> downloadExecutor(HttpOutputStreamResultHandle resultHandle) {
        return executor(resultHandle);
    }

    @Override
    public <R> JdkClientExecutorBuilder<R> executor(HttpResultHandle<R> resultHandle) {
        return JdkClientExecutorBuilder.create(resultHandle, httpClient, createRequest());
    }

    // endregion

    /**
     * 请求前执行
     */
    protected void executeBefore() {
    }

    protected HttpRequest createRequest() {
        executeBefore();
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uriBuilder.build().uri());
        // 协议
        if (Objects.nonNull(version)) {
            builder.version(version);
        }
        // 超时
        if (Objects.nonNull(timeout)) {
            builder.timeout(timeout);
        }
        // 请求头
        getHeaders().forEach((name, valueList) -> valueList.forEach(value -> builder.header(name, value)));
        // 请求体
        if (Objects.nonNull(bodyPublisher)) {
            builder.method(method.getMethod(), bodyPublisher);
        } else {
            switch (method) {
                case GET -> builder.GET();
                case DELETE -> builder.DELETE();
                default -> builder.method(method.getMethod(), HttpRequest.BodyPublishers.noBody());
            }
        }
        return builder.build();
    }
}
