package cn.zs.tool.okhttp.request;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
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
import cn.zs.tool.okhttp.executor.OkHttpAsyncExecutor;
import cn.zs.tool.okhttp.executor.OkHttpExecutor;
import cn.zs.tool.okhttp.response.OkHttpAsyncResponseFuture;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

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
        this.method = Objects.requireNonNull(method,  "Http method must not be null");
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

    // region 同步请求

    /**
     * 同步执行处理 UTF_8 {@link String}结果
     */
    public OkHttpExecutor<String> executeForString() {
        return execute(StringHttpMsgConverter.UTF_8_INSTANCE);
    }

    /**
     * 同步执行处理{@link String}结果
     *
     * @param charset 结果编码
     */
    public OkHttpExecutor<String> executeForString(Charset charset) {
        return execute(StringHttpMsgConverter.create(charset));
    }

    /**
     * 同步执行处理 json 结果
     *
     * @param msgConverter {@link JsonHttpMsgConverter}
     */
    public <R> OkHttpExecutor<R> executeForJson(JsonHttpMsgConverter<R> msgConverter) {
        return execute(msgConverter);
    }

    /**
     * 同步执行处理 xml 结果
     *
     * @param msgConverter {@link XmlHttpMsgConverter}
     */
    public <R> OkHttpExecutor<R> executeForXml(XmlHttpMsgConverter<R> msgConverter) {
        return execute(msgConverter);
    }

    /**
     * 同步执行获取 byte[] 结果执行器
     */
    public OkHttpExecutor<byte[]> executeForBytes() {
        return execute(ByteArrayHttpMsgConverter.INSTANCE);
    }

    /**
     * 同步执行下载文件
     *
     * @param targetFile 目标文件
     * @return true 为成功
     */
    public OkHttpExecutor<Boolean> download(File targetFile) {
        return execute(FileHttpMsgConverter.create(targetFile));
    }

    /**
     * 同步执行下载文件
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    public OkHttpExecutor<Boolean> download(FileHttpMsgConverter msgConverter) {
        return execute(msgConverter);
    }

    /**
     * 同步执行下载文件
     *
     * @param outputStream 输出流
     * @return true 为成功
     */
    public OkHttpExecutor<Boolean> download(OutputStream outputStream) {
        return execute(OutputStreamHttpMsgConverter.create(outputStream));
    }

    /**
     * 同步执行下载文件
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    public OkHttpExecutor<Boolean> download(OutputStreamHttpMsgConverter msgConverter) {
        return execute(msgConverter);
    }

    /**
     * 同步执行
     *
     * @param msgConverter 结果处理器
     */
    public <R> OkHttpExecutor<R> execute(HttpMsgConverter<R> msgConverter) {
        return OkHttpExecutor.create(call(), msgConverter);
    }

    // endregion

    // region http 异步执行

    /**
     * 异步执行获取{@link String}类型结果包装器
     */
    public CompletableFuture<OkHttpAsyncExecutor<String>> executeAsyncForString() {
        return executeAsyncForString(StringHttpMsgConverter.UTF_8_INSTANCE);
    }

    /**
     * 异步执行获取{@link String}类型结果包装器
     */
    public CompletableFuture<OkHttpAsyncExecutor<String>> executeAsyncForString(Charset charset) {
        return executeAsyncForString(StringHttpMsgConverter.create(charset));
    }

    /**
     * 异步执行获取{@link String}类型结果包装器
     */
    public CompletableFuture<OkHttpAsyncExecutor<String>> executeAsyncForString(StringHttpMsgConverter msgConverter) {
        return executeAsyncForString(msgConverter, null, null, false);
    }

    /**
     * 异步执行获取{@link String}类型结果包装器
     */
    public CompletableFuture<OkHttpAsyncExecutor<String>> executeAsyncForString(HttpMsgConverter<String> msgConverter,
                                                                                ThrowingConsumer<Throwable, Throwable> errHandler,
                                                                                Predicate<Integer> okPredicate,
                                                                                Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步执行获取 json 反序列化类型结果包装器
     */
    public <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForJson(JsonHttpMsgConverter<R> msgConverter) {
        return executeAsyncForJson(msgConverter, null, null, false);
    }

    /**
     * 异步执行获取 json 反序列化类型结果包装器
     */
    public <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForJson(JsonHttpMsgConverter<R> msgConverter,
                                                                             ThrowingConsumer<Throwable, Throwable> errHandler,
                                                                             Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步执行获取 xml 反序列化类型结果包装器
     */
    public <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForXml(XmlHttpMsgConverter<R> msgConverter) {
        return executeAsyncForXml(msgConverter, null, null, false);
    }

    /**
     * 异步执行获取 xml 反序列化类型结果包装器
     */
    public <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForXml(XmlHttpMsgConverter<R> msgConverter,
                                                                            ThrowingConsumer<Throwable, Throwable> errHandler,
                                                                            Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步执行获取字节数组结果包装器
     */
    public CompletableFuture<OkHttpAsyncExecutor<byte[]>> executeAsyncForBytes() {
        return executeAsyncForBytes(ByteArrayHttpMsgConverter.INSTANCE);
    }

    /**
     * 异步执行获取字节数组结果包装器
     */
    public CompletableFuture<OkHttpAsyncExecutor<byte[]>> executeAsyncForBytes(HttpMsgConverter<byte[]> msgConverter) {
        return executeAsyncForBytes(msgConverter, null, null, false);
    }

    /**
     * 异步执行获取字节数组结果包装器
     */
    public CompletableFuture<OkHttpAsyncExecutor<byte[]>> executeAsyncForBytes(HttpMsgConverter<byte[]> msgConverter,
                                                                               ThrowingConsumer<Throwable, Throwable> errHandler,
                                                                               Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步下载
     *
     * @param outputStream 输出流
     * @return true 为成功
     */
    public CompletableFuture<OkHttpAsyncExecutor<Boolean>> asyncDownload(OutputStream outputStream) {
        return asyncDownload(OutputStreamHttpMsgConverter.create(outputStream));
    }

    /**
     * 异步下载
     *
     * @param msgConverter 输出流处理器
     * @return true 为成功
     */
    public CompletableFuture<OkHttpAsyncExecutor<Boolean>> asyncDownload(OutputStreamHttpMsgConverter msgConverter) {
        return asyncDownload(msgConverter, null, null, false);
    }

    /**
     * 异步下载
     *
     * @param msgConverter     输出流处理器
     * @param okPredicate      ok状态码断言，为空默认使用 status -> status >= 200 && status < 300
     * @param errHandler       异常处理器
     * @param mustHandleResult 是否必须处理结果，默认为false
     * @return true 为成功
     */
    public CompletableFuture<OkHttpAsyncExecutor<Boolean>> asyncDownload(OutputStreamHttpMsgConverter msgConverter,
                                                                         ThrowingConsumer<Throwable, Throwable> errHandler,
                                                                         Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步下载
     *
     * @param targetFile 目标文件
     * @return true 为成功
     */
    public CompletableFuture<OkHttpAsyncExecutor<Boolean>> asyncDownload(File targetFile) {
        return asyncDownload(FileHttpMsgConverter.create(targetFile));
    }

    /**
     * 异步下载
     *
     * @param msgConverter 文件下载处理器
     * @return true 为成功
     */
    public CompletableFuture<OkHttpAsyncExecutor<Boolean>> asyncDownload(FileHttpMsgConverter msgConverter) {
        return asyncDownload(msgConverter, null, null, false);
    }

    /**
     * 异步下载
     *
     * @param msgConverter     文件下载处理器
     * @param okPredicate      ok状态码断言，为空默认使用 status -> status >= 200 && status < 300
     * @param errHandler       异常处理器
     * @param mustHandleResult 是否必须处理结果，默认为false
     * @return true 为成功
     */
    public CompletableFuture<OkHttpAsyncExecutor<Boolean>> asyncDownload(FileHttpMsgConverter msgConverter,
                                                                         ThrowingConsumer<Throwable, Throwable> errHandler,
                                                                         Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步执行
     *
     * @param msgConverter 结果处理器
     */
    public <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsync(HttpMsgConverter<R> msgConverter) {
        return executeAsync(msgConverter, false);
    }

    /**
     * 异步执行
     *
     * @param msgConverter     结果处理器
     * @param mustHandleResult 是否必须处理结果，默认为false
     */
    public <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsync(HttpMsgConverter<R> msgConverter,
                                                                      Boolean mustHandleResult) {
        return executeAsync(msgConverter, null, null, mustHandleResult);
    }

    /**
     * 异步执行
     *
     * @param msgConverter     结果处理器
     * @param okPredicate      ok状态码断言，为空默认使用 status -> status >= 200 && status < 300
     * @param errHandler       异常处理器
     * @param mustHandleResult 是否必须处理结果，默认为false
     */
    public <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsync(HttpMsgConverter<R> msgConverter,
                                                                      ThrowingConsumer<Throwable, Throwable> errHandler,
                                                                      Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return OkHttpAsyncResponseFuture.create(this::call, msgConverter, errHandler, okPredicate, mustHandleResult);
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
