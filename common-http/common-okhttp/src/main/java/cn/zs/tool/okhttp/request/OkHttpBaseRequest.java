package cn.zs.tool.okhttp.request;

import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.converter.ByteArrayHttpMsgConverter;
import cn.zs.tool.http.core.converter.FileHttpMsgConverter;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.converter.JsonHttpMsgConverter;
import cn.zs.tool.http.core.converter.OutputStreamHttpMsgConverter;
import cn.zs.tool.http.core.converter.StringHttpMsgConverter;
import cn.zs.tool.http.core.converter.XmlHttpMsgConverter;
import cn.zs.tool.http.core.decorator.HttpHeaderDecorator;
import cn.zs.tool.http.core.exception.HttpException;
import cn.zs.tool.okhttp.constant.OkHttpConstant;
import cn.zs.tool.okhttp.decorator.OkHttpUriBuilderDecorator;
import cn.zs.tool.okhttp.executor.OkHttpExecutorBuilder;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * okhttp 请求基类
 *
 * @author sheng
 */
public abstract class OkHttpBaseRequest<T extends OkHttpBaseRequest<T>>
        implements OkHttpUriBuilderDecorator<T>, HttpHeaderDecorator<T> {

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
    public OkHttpExecutorBuilder<String> stringExecutor() {
        return executor(StringHttpMsgConverter.UTF_8_INSTANCE);
    }

    /**
     * 获取处理{@link String}结果的请求执行器
     *
     * @param charset 结果编码
     */
    public OkHttpExecutorBuilder<String> stringExecutor(Charset charset) {
        return executor(StringHttpMsgConverter.create(charset));
    }

    /**
     * 获取处理 json 结果的请求执行器
     *
     * @param msgConverter {@link JsonHttpMsgConverter}
     */
    public <R> OkHttpExecutorBuilder<R> jsonExecutor(JsonHttpMsgConverter<R> msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取处理 xml 结果的请求执行器
     *
     * @param msgConverter {@link XmlHttpMsgConverter}
     */
    public <R> OkHttpExecutorBuilder<R> xmlExecutor(XmlHttpMsgConverter<R> msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取获取 byte[] 结果的请求执行器
     */
    public OkHttpExecutorBuilder<byte[]> bytesExecutor() {
        return executor(ByteArrayHttpMsgConverter.INSTANCE);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param targetFile 目标文件
     * @return true 为成功
     */
    public OkHttpExecutorBuilder<Boolean> downloadExecutor(File targetFile) {
        return executor(FileHttpMsgConverter.create(targetFile));
    }

    /**
     * 获取下载文件请求执行器
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    public OkHttpExecutorBuilder<Boolean> downloadExecutor(FileHttpMsgConverter msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取下载文件结果的请求执行器
     *
     * @param outputStream 输出流
     * @return true 为成功
     */
    public OkHttpExecutorBuilder<Boolean> downloadExecutor(OutputStream outputStream) {
        return executor(OutputStreamHttpMsgConverter.create(outputStream));
    }

    /**
     * 获取下载文件结果的请求执行器
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    public OkHttpExecutorBuilder<Boolean> downloadExecutor(OutputStreamHttpMsgConverter msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取请求执行器
     *
     * @param msgConverter 结果处理器
     */
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
