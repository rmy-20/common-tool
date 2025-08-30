package cn.zs.tool.httpclient5.request;

import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.converter.ByteArrayHttpMsgConverter;
import cn.zs.tool.http.core.converter.FileHttpMsgConverter;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.converter.JsonHttpMsgConverter;
import cn.zs.tool.http.core.converter.OutputStreamHttpMsgConverter;
import cn.zs.tool.http.core.converter.StringHttpMsgConverter;
import cn.zs.tool.http.core.converter.XmlHttpMsgConverter;
import cn.zs.tool.http.core.decorator.HttpHeaderDecorator;
import cn.zs.tool.http.core.decorator.RfcUriBuilderDecorator;
import cn.zs.tool.http.core.exception.HttpException;
import cn.zs.tool.http.core.uri.RfcUri;
import cn.zs.tool.httpclient5.constant.HttpRequestMethodEnum;
import cn.zs.tool.httpclient5.executor.HttpClient5ExecutorBuilder;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.concurrent.Cancellable;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ProtocolVersion;
import org.apache.hc.core5.http.io.entity.NullEntity;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * httpclient5 请求基类
 *
 * @author sheng
 */
public abstract class HttpClient5BaseRequest<T extends HttpClient5BaseRequest<T>>
        implements RfcUriBuilderDecorator<T>, HttpHeaderDecorator<T> {
    /**
     * uri
     */
    private final RfcUri.Builder uriBuilder;

    /**
     * 请求方法
     */
    private final HttpRequestMethodEnum method;

    /**
     * 请求体
     */
    protected HttpEntity httpEntity;

    /**
     * 请求头
     */
    private final HttpHeaders headers;

    /**
     * 请求配置
     */
    private RequestConfig requestConfig;

    /**
     * 取消请求控制
     */
    private Cancellable cancellable;

    /**
     * 请求协议版本
     */
    private ProtocolVersion protocolVersion;

    /**
     * 默认编码字符集，默认 UTF-8
     */
    protected Charset defaultCharset = StandardCharsets.UTF_8;

    public HttpClient5BaseRequest(String url, HttpRequestMethodEnum method) {
        this.method = Objects.requireNonNull(method, "Http method must not be null");
        RfcUri rfcUri = RfcUri.parse(url);
        if (Objects.isNull(rfcUri)) {
            throw new HttpException(String.format("RfcUri解析url[%s]-[%s]失败", url, method.getMethod().getMethod()));
        }
        this.uriBuilder = rfcUri.newBuilder();
        this.headers = HttpHeaders.create();
    }

    /**
     * 设置请求配置
     */
    public T requestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return self();
    }

    /**
     * 设置取消请求控制
     */
    public T cancellable(Cancellable cancellable) {
        this.cancellable = cancellable;
        return self();
    }

    /**
     * 设置请求协议版本
     */
    public T protocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
        return self();
    }

    /**
     * 设置默认编码字符集
     */
    public HttpClient5BaseRequest<T> defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return self();
    }

    /**
     * 获取默认编码字符集
     */
    public Charset getDefaultCharset() {
        return Objects.nonNull(defaultCharset) ? defaultCharset : StandardCharsets.UTF_8;
    }

    @Override
    public abstract T self();

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
    protected HttpEntity getRequestBody() {
        return this.httpEntity;
    }

    // region 请求

    /**
     * 获取处理 UTF_8 {@link String}结果的请求执行器
     */
    public HttpClient5ExecutorBuilder<String> stringExecutor() {
        return executor(StringHttpMsgConverter.UTF_8_INSTANCE);
    }

    /**
     * 获取处理{@link String}结果的请求执行器
     *
     * @param charset 结果编码
     */
    public HttpClient5ExecutorBuilder<String> stringExecutor(Charset charset) {
        return executor(StringHttpMsgConverter.create(charset));
    }

    /**
     * 获取处理 json 结果的请求执行器
     *
     * @param msgConverter {@link JsonHttpMsgConverter}
     */
    public <R> HttpClient5ExecutorBuilder<R> jsonExecutor(JsonHttpMsgConverter<R> msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取处理 xml 结果的请求执行器
     *
     * @param msgConverter {@link XmlHttpMsgConverter}
     */
    public <R> HttpClient5ExecutorBuilder<R> xmlExecutor(XmlHttpMsgConverter<R> msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取处理 byte[] 结果的请求执行器
     */
    public HttpClient5ExecutorBuilder<byte[]> bytesExecutor() {
        return executor(ByteArrayHttpMsgConverter.INSTANCE);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param targetFile 目标文件
     * @return true 为成功
     */
    public HttpClient5ExecutorBuilder<Boolean> downloadExecutor(File targetFile) {
        return executor(FileHttpMsgConverter.create(targetFile));
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    public HttpClient5ExecutorBuilder<Boolean> downloadExecutor(FileHttpMsgConverter msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param outputStream 输出流
     * @return true 为成功
     */
    public HttpClient5ExecutorBuilder<Boolean> downloadExecutor(OutputStream outputStream) {
        return executor(OutputStreamHttpMsgConverter.create(outputStream));
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    public HttpClient5ExecutorBuilder<Boolean> downloadExecutor(OutputStreamHttpMsgConverter msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取请求执行器
     *
     * @param msgConverter 结果处理器
     */
    public <R> HttpClient5ExecutorBuilder<R> executor(HttpMsgConverter<R> msgConverter) {
        return HttpClient5ExecutorBuilder.create(createRequest(), msgConverter);
    }

    // endregion

    /**
     * 请求前执行
     */
    protected void executeBefore() {
    }

    protected HttpUriRequestBase createRequest() {
        executeBefore();
        HttpUriRequestBase requestBase = method.create(uriBuilder.build().getUri());
        if (Objects.nonNull(requestConfig)) {
            requestBase.setConfig(requestConfig);
        }
        if (Objects.nonNull(cancellable)) {
            requestBase.setDependency(cancellable);
        }
        if (Objects.nonNull(protocolVersion)) {
            requestBase.setVersion(protocolVersion);
        }
        // 请求头
        getHeaders().forEach((name, valueList) -> valueList.forEach(value -> requestBase.addHeader(name, value)));
        // 请求体
        HttpEntity requestBody = getRequestBody();
        if (Objects.isNull(requestBody) && method.getMethod().isNeedBody()) {
            requestBody = NullEntity.INSTANCE;
        }
        requestBase.setEntity(requestBody);
        return requestBase;
    }
}
