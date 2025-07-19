package cn.zs.tool.okhttp.decorator;

import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.converter.ByteArrayHttpMsgConverter;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.converter.JsonHttpMsgConverter;
import cn.zs.tool.http.core.converter.StringHttpMsgConverter;
import cn.zs.tool.http.core.converter.XmlHttpMsgConverter;
import cn.zs.tool.okhttp.executor.OkHttpAsyncExecutor;
import cn.zs.tool.okhttp.executor.OkHttpExecutor;
import cn.zs.tool.okhttp.response.OkHttpAsyncResponseFuture;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * okhttp 执行装饰器
 *
 * @author sheng
 */
public interface OkHttpExecuteDecorator {
    /**
     * 获取{@link OkHttpClient}
     */
    OkHttpClient getHttpClient();

    /**
     * 获取请求方法
     */
    HttpMethodEnum getMethod();

    /**
     * 获取请求头
     */
    HttpHeaders getHeaders();

    /**
     * 获取请求地址
     */
    HttpUrl.Builder getUrlBuilder();

    /**
     * 获取请求体
     */
    RequestBody getRequestBody();

    // region 同步请求

    /**
     * 同步执行处理 UTF_8 {@link String}结果
     */
    default OkHttpExecutor<String> executeForString() {
        return execute(StringHttpMsgConverter.UTF_8_INSTANCE);
    }

    /**
     * 同步执行处理{@link String}结果
     *
     * @param charset 结果编码
     */
    default OkHttpExecutor<String> executeForString(Charset charset) {
        return execute(StringHttpMsgConverter.create(charset));
    }

    /**
     * 同步执行处理 json 结果
     */
    default <R> OkHttpExecutor<R> executeForJson(Class<R> resultType) {
        return executeForJson(JsonHttpMsgConverter.create(resultType));
    }

    /**
     * 同步执行处理 json 结果
     *
     * @param msgConverter {@link JsonHttpMsgConverter}
     */
    default <R> OkHttpExecutor<R> executeForJson(JsonHttpMsgConverter<R> msgConverter) {
        return execute(msgConverter);
    }

    /**
     * 同步执行处理 xml 结果
     */
    default <R> OkHttpExecutor<R> executeForXml(Class<R> resultType) {
        return executeForXml(XmlHttpMsgConverter.create(resultType));
    }

    /**
     * 同步执行处理 xml 结果
     *
     * @param msgConverter {@link JsonHttpMsgConverter}
     */
    default <R> OkHttpExecutor<R> executeForXml(XmlHttpMsgConverter<R> msgConverter) {
        return execute(msgConverter);
    }

    /**
     * 同步执行获取 byte[] 结果执行器
     */
    default OkHttpExecutor<byte[]> executeForBytes() {
        return execute(ByteArrayHttpMsgConverter.INSTANCE);
    }

    /**
     * 同步执行
     *
     * @param msgConverter 结果处理器
     */
    default <R> OkHttpExecutor<R> execute(HttpMsgConverter<R> msgConverter) {
        return OkHttpExecutor.create(call(), msgConverter);
    }
    // endregion

    // region http 异步执行

    /**
     * 异步执行获取{@link String}类型结果包装器
     */
    default CompletableFuture<OkHttpAsyncExecutor<String>> executeAsyncForString() {
        return executeAsyncForString(StringHttpMsgConverter.UTF_8_INSTANCE);
    }

    /**
     * 异步执行获取{@link String}类型结果包装器
     */
    default CompletableFuture<OkHttpAsyncExecutor<String>> executeAsyncForString(Charset charset) {
        return executeAsyncForString(StringHttpMsgConverter.create(charset));
    }

    /**
     * 异步执行获取{@link String}类型结果包装器
     */
    default CompletableFuture<OkHttpAsyncExecutor<String>> executeAsyncForString(StringHttpMsgConverter msgConverter) {
        return executeAsyncForString(msgConverter, null, null, false);
    }

    /**
     * 异步执行获取{@link String}类型结果包装器
     */
    default CompletableFuture<OkHttpAsyncExecutor<String>> executeAsyncForString(HttpMsgConverter<String> msgConverter,
                                                                                 Consumer<Throwable> errHandler, Predicate<Integer> okPredicate,
                                                                                 Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步执行获取 json 反序列化类型结果包装器
     */
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForJson(Class<R> type) {
        return executeAsyncForJson(JsonHttpMsgConverter.create(type));
    }

    /**
     * 异步执行获取 json 反序列化类型结果包装器
     */
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForJson(JsonHttpMsgConverter<R> msgConverter) {
        return executeAsyncForJson(msgConverter, null, null, false);
    }

    /**
     * 异步执行获取 json 反序列化类型结果包装器
     */
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForJson(JsonHttpMsgConverter<R> msgConverter, Consumer<Throwable> errHandler,
                                                                              Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步执行获取 xml 反序列化类型结果包装器
     */
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForXml(Class<R> type) {
        return executeAsyncForXml(XmlHttpMsgConverter.create(type));
    }

    /**
     * 异步执行获取 xml 反序列化类型结果包装器
     */
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForXml(XmlHttpMsgConverter<R> msgConverter) {
        return executeAsyncForXml(msgConverter, null, null, false);
    }

    /**
     * 异步执行获取 xml 反序列化类型结果包装器
     */
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsyncForXml(XmlHttpMsgConverter<R> msgConverter, Consumer<Throwable> errHandler,
                                                                             Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步执行获取字节数组结果包装器
     */
    default CompletableFuture<OkHttpAsyncExecutor<byte[]>> executeAsyncForBytes() {
        return executeAsyncForBytes(ByteArrayHttpMsgConverter.INSTANCE);
    }

    /**
     * 异步执行获取字节数组结果包装器
     */
    default CompletableFuture<OkHttpAsyncExecutor<byte[]>> executeAsyncForBytes(HttpMsgConverter<byte[]> msgConverter) {
        return executeAsyncForBytes(msgConverter, null, null, false);
    }

    /**
     * 异步执行获取字节数组结果包装器
     */
    default CompletableFuture<OkHttpAsyncExecutor<byte[]>> executeAsyncForBytes(HttpMsgConverter<byte[]> msgConverter, Consumer<Throwable> errHandler,
                                                                                Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return executeAsync(msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    /**
     * 异步执行
     *
     * @param msgConverter 结果处理器
     */
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsync(HttpMsgConverter<R> msgConverter) {
        return executeAsync(msgConverter, false);
    }

    /**
     * 异步执行
     *
     * @param msgConverter     结果处理器
     * @param mustHandleResult 是否必须处理结果，默认为false
     */
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsync(HttpMsgConverter<R> msgConverter,
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
    default <R> CompletableFuture<OkHttpAsyncExecutor<R>> executeAsync(HttpMsgConverter<R> msgConverter, Consumer<Throwable> errHandler,
                                                                       Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return OkHttpAsyncResponseFuture.create(this::call, msgConverter, errHandler, okPredicate, mustHandleResult);
    }
    // endregion

    /**
     * 请求前执行
     */
    default void executeBefore() {
    }

    /**
     * 获取{@link Call}
     */
    default Call call() {
        executeBefore();
        Request.Builder requestBuilder = new Request.Builder()
                .url(getUrlBuilder().build());
        getHeaders().forEach((name, valueList) -> valueList.forEach(value -> requestBuilder.addHeader(name, value)));
        HttpMethodEnum method = getMethod();
        RequestBody requestBody = getRequestBody();
        if (Objects.isNull(requestBody) && method.isNeedBody()) {
            requestBody = RequestBody.EMPTY;
        }
        return getHttpClient().newCall(requestBuilder.method(method.getMethod(), requestBody).build());
    }
}
