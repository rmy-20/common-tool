package io.github.rmy20.tool.urlconnection.request;

import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.body.Body;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.result.HttpByteArrayResultHandle;
import io.github.rmy20.tool.http.core.result.HttpFileResultHandle;
import io.github.rmy20.tool.http.core.result.HttpJsonResultHandle;
import io.github.rmy20.tool.http.core.result.HttpOutputStreamResultHandle;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.http.core.result.HttpStringResultHandle;
import io.github.rmy20.tool.http.core.decorator.RfcUriBuilderDecorator;
import io.github.rmy20.tool.http.core.exception.HttpException;
import io.github.rmy20.tool.http.core.request.BaseRequest;
import io.github.rmy20.tool.http.core.result.HttpXmlResultHandle;
import io.github.rmy20.tool.http.core.uri.RfcUri;
import io.github.rmy20.tool.urlconnection.executor.UrlConnectionExecutorBuilder;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * url connection 请求基类
 *
 * @author sheng
 */
public abstract class UrlConnectionBaseRequest<T extends UrlConnectionBaseRequest<T>> implements RfcUriBuilderDecorator<T>, BaseRequest<T> {
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
    protected final HttpHeaders headers;

    /**
     * 代理
     */
    protected Proxy proxy;

    /**
     * 连接超时时间，单位秒
     */
    private int connectTimeout = -1;

    /**
     * 读超时，单位秒
     */
    private int readTimeout = -1;

    /**
     * 块模式分块长度
     */
    private int chunkSize = 4096;

    /**
     * 异步执行线程池
     */
    protected ExecutorService executorService;

    /**
     * 默认编码字符集，默认 UTF-8
     */
    protected Charset defaultCharset = StandardCharsets.UTF_8;

    public UrlConnectionBaseRequest(String url, HttpMethodEnum method) {
        this.method = Objects.requireNonNull(method, "Http method must not be null");
        RfcUri rfcUri = RfcUri.parse(url);
        if (Objects.isNull(rfcUri)) {
            throw new HttpException(String.format("RfcUri解析url[%s]-[%s]失败", url, method.getMethod()));
        }
        this.uriBuilder = rfcUri.newBuilder();
        this.headers = HttpHeaders.create();
    }

    /**
     * 获取默认编码字符集
     */
    @Override
    public Charset getDefaultCharset() {
        return Objects.nonNull(defaultCharset) ? defaultCharset : BaseRequest.super.getDefaultCharset();
    }

    @Override
    public RfcUri.Builder getUriBuilder() {
        return uriBuilder;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    /**
     * 获取请求体
     */
    protected Body getRequestBody() {
        return this.body;
    }

    /**
     * 设置代理
     */
    public T proxy(Proxy proxy) {
        this.proxy = proxy;
        return self();
    }

    /**
     * 设置连接超时时间，单位秒
     */
    public T connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return self();
    }

    /**
     * 设置读超时时间，单位秒
     */
    public T readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return self();
    }

    /**
     * 设置块大小
     */
    public T chunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        return self();
    }

    /**
     * 设置线程池
     */
    public T executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return self();
    }

    /**
     * 设置默认编码字符集
     */
    public T defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return self();
    }

    // region 请求

    /**
     * 获取处理 UTF_8 {@link String}结果的请求执行器
     */
    @Override
    public UrlConnectionExecutorBuilder<String> stringExecutor() {
        return executor(HttpStringResultHandle.UTF_8_INSTANCE);
    }

    /**
     * 获取处理{@link String}结果的请求执行器
     *
     * @param charset 结果编码
     */
    @Override
    public UrlConnectionExecutorBuilder<String> stringExecutor(Charset charset) {
        return executor(HttpStringResultHandle.create(charset));
    }

    /**
     * 获取处理 json 结果的请求执行器
     *
     * @param resultHandle {@link HttpJsonResultHandle}
     */
    @Override
    public <R> UrlConnectionExecutorBuilder<R> jsonExecutor(HttpJsonResultHandle<R> resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取处理 xml 结果的请求执行器
     *
     * @param resultHandle {@link HttpXmlResultHandle}
     */
    @Override
    public <R> UrlConnectionExecutorBuilder<R> xmlExecutor(HttpXmlResultHandle<R> resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取处理 byte[] 结果的请求执行器
     */
    @Override
    public UrlConnectionExecutorBuilder<byte[]> bytesExecutor() {
        return executor(HttpByteArrayResultHandle.INSTANCE);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param targetFile 目标文件
     * @return 下载文件大小
     */
    @Override
    public UrlConnectionExecutorBuilder<Long> downloadExecutor(File targetFile) {
        return executor(HttpFileResultHandle.create(targetFile));
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param resultHandle 结果处理器
     * @return 下载文件大小
     */
    @Override
    public UrlConnectionExecutorBuilder<Long> downloadExecutor(HttpFileResultHandle resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param outputStream 输出流
     * @return 下载文件大小
     */
    @Override
    public UrlConnectionExecutorBuilder<Long> downloadExecutor(OutputStream outputStream) {
        return executor(HttpOutputStreamResultHandle.create(outputStream));
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param resultHandle 结果处理器
     * @return 下载文件大小
     */
    @Override
    public UrlConnectionExecutorBuilder<Long> downloadExecutor(HttpOutputStreamResultHandle resultHandle) {
        return executor(resultHandle);
    }

    @Override
    public <R> UrlConnectionExecutorBuilder<R> executor(HttpResultHandle<R> resultHandle) {
        return UrlConnectionExecutorBuilder.create(resultHandle, openConnection(), getRequestBody(), executorService);
    }

    // endregion

    /**
     * 请求前执行
     */
    protected void executeBefore() {
        if (StringUtil.isBlank(headers.getContentType()) && Objects.nonNull(body)) {
            headers.setContentType(body.getContentType());
        }
    }

    protected HttpURLConnection openConnection() {
        try {
            executeBefore();
            URL url = uriBuilder.build().url();
            URLConnection urlConnection = Objects.nonNull(proxy) ? url.openConnection(proxy) : url.openConnection();
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            if (connectTimeout >= 0) {
                connection.setConnectTimeout(connectTimeout * 1000);
            }
            if (readTimeout >= 0) {
                connection.setReadTimeout(readTimeout * 1000);
            }
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(HttpMethodEnum.GET == method);
            connection.setRequestMethod(method.getMethod());
            // 请求头
            getHeaders().forEach((name, valueList) -> valueList.forEach(value -> connection.addRequestProperty(name, value)));
            // 请求体
            if (method.isNeedBody()) {
                connection.setDoOutput(true);
                if (Objects.isNull(body)) {
                    body = Body.NULL_BODY;
                }
                long contentLength = body.contentLength();
                if (contentLength >= 0) {
                    connection.setFixedLengthStreamingMode(contentLength);
                } else {
                    connection.setChunkedStreamingMode(chunkSize);
                }
            }
            return connection;
        } catch (Exception e) {
            throw new HttpException("Open URLConnection error", e);
        }
    }
}
