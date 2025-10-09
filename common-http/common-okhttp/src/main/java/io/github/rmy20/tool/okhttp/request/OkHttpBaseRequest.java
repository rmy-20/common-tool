package io.github.rmy20.tool.okhttp.request;

import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.converter.ByteArrayHttpMsgConverter;
import io.github.rmy20.tool.http.core.converter.FileHttpMsgConverter;
import io.github.rmy20.tool.http.core.converter.HttpMsgConverter;
import io.github.rmy20.tool.http.core.converter.JsonHttpMsgConverter;
import io.github.rmy20.tool.http.core.converter.OutputStreamHttpMsgConverter;
import io.github.rmy20.tool.http.core.converter.StringHttpMsgConverter;
import io.github.rmy20.tool.http.core.converter.XmlHttpMsgConverter;
import io.github.rmy20.tool.http.core.exception.HttpException;
import io.github.rmy20.tool.http.core.request.BaseRequest;
import io.github.rmy20.tool.okhttp.constant.OkHttpConstant;
import io.github.rmy20.tool.okhttp.decorator.OkHttpUriBuilderDecorator;
import io.github.rmy20.tool.okhttp.executor.OkHttpExecutorBuilder;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * okhttp 请求基类
 *
 * @author sheng
 */
public abstract class OkHttpBaseRequest<T extends OkHttpBaseRequest<T>>
        implements OkHttpUriBuilderDecorator<T>, BaseRequest<T> {

    /**
     * OkHttpClient
     */
    protected OkHttpClient httpClient;

    /**
     * url
     */
    private final HttpUrl.Builder urlBuilder;

    /**
     * 请求方法
     */
    private final HttpMethodEnum method;

    /**
     * 请求头
     */
    private final HttpHeaders headers;

    /**
     * 默认编码字符集，默认 UTF-8
     */
    protected Charset defaultCharset = StandardCharsets.UTF_8;

    public OkHttpBaseRequest(String url, HttpMethodEnum method) {
        this.method = Objects.requireNonNull(method, "Http method must not be null");
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (Objects.isNull(httpUrl)) {
            throw new HttpException(String.format("okhttp解析url[%s]-[%s]失败", url, method.getMethod()));
        }
        this.urlBuilder = httpUrl.newBuilder();
        this.headers = HttpHeaders.create();
    }

    /**
     * 设置 HttpClient
     */
    public T httpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return self();
    }

    /**
     * 设置默认编码字符集
     */
    public T defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return self();
    }

    /**
     * 获取默认编码字符集
     */
    @Override
    public Charset getDefaultCharset() {
        return Objects.nonNull(defaultCharset) ? defaultCharset : BaseRequest.super.getDefaultCharset();
    }

    @Override
    public abstract T self();

    @Override
    public HttpUrl.Builder getUrlBuilder() {
        return urlBuilder;
    }

    /**
     * 获取请求体
     */
    protected abstract RequestBody getRequestBody();

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    /**
     * 获取 OkHttpClient
     */
    public OkHttpClient getHttpClient() {
        return Objects.nonNull(httpClient) ? httpClient : OkHttpConstant.HTTP_CLIENT;
    }

    // region 请求

    /**
     * 获取处理 UTF_8 {@link String}结果的请求执行器
     */
    @Override
    public OkHttpExecutorBuilder<String> stringExecutor() {
        return executor(StringHttpMsgConverter.UTF_8_INSTANCE);
    }

    /**
     * 获取处理{@link String}结果的请求执行器
     *
     * @param charset 结果编码
     */
    @Override
    public OkHttpExecutorBuilder<String> stringExecutor(Charset charset) {
        return executor(StringHttpMsgConverter.create(charset));
    }

    /**
     * 获取处理 json 结果的请求执行器
     *
     * @param msgConverter {@link JsonHttpMsgConverter}
     */
    @Override
    public <R> OkHttpExecutorBuilder<R> jsonExecutor(JsonHttpMsgConverter<R> msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取处理 xml 结果的请求执行器
     *
     * @param msgConverter {@link XmlHttpMsgConverter}
     */
    @Override
    public <R> OkHttpExecutorBuilder<R> xmlExecutor(XmlHttpMsgConverter<R> msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取获取 byte[] 结果的请求执行器
     */
    @Override
    public OkHttpExecutorBuilder<byte[]> bytesExecutor() {
        return executor(ByteArrayHttpMsgConverter.INSTANCE);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param targetFile 目标文件
     * @return true 为成功
     */
    @Override
    public OkHttpExecutorBuilder<Boolean> downloadExecutor(File targetFile) {
        return executor(FileHttpMsgConverter.create(targetFile));
    }

    /**
     * 获取下载文件请求执行器
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    @Override
    public OkHttpExecutorBuilder<Boolean> downloadExecutor(FileHttpMsgConverter msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取下载文件结果的请求执行器
     *
     * @param outputStream 输出流
     * @return true 为成功
     */
    @Override
    public OkHttpExecutorBuilder<Boolean> downloadExecutor(OutputStream outputStream) {
        return executor(OutputStreamHttpMsgConverter.create(outputStream));
    }

    /**
     * 获取下载文件结果的请求执行器
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    @Override
    public OkHttpExecutorBuilder<Boolean> downloadExecutor(OutputStreamHttpMsgConverter msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取请求执行器
     *
     * @param msgConverter 结果处理器
     */
    @Override
    public <R> OkHttpExecutorBuilder<R> executor(HttpMsgConverter<R> msgConverter) {
        return OkHttpExecutorBuilder.create(call(), msgConverter);
    }

    // endregion

    /**
     * 请求前执行
     */
    protected void executeBefore() {
    }

    /**
     * 获取{@link Call}
     */
    protected Call call() {
        executeBefore();
        Request.Builder requestBuilder = new Request.Builder()
                .url(getUrlBuilder().build());
        getHeaders().forEach((name, valueList) -> valueList.forEach(value -> requestBuilder.addHeader(name, value)));
        RequestBody requestBody = getRequestBody();
        if (Objects.isNull(requestBody) && method.isNeedBody()) {
            requestBody = RequestBody.EMPTY;
        }
        return getHttpClient().newCall(requestBuilder.method(method.getMethod(), requestBody).build());
    }
}
