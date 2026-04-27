package io.github.rmy20.tool.httpclient5.request;

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
import io.github.rmy20.tool.httpclient5.executor.HttpClient5ExecutorBuilder;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.concurrent.Cancellable;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ProtocolVersion;
import org.apache.hc.core5.http.io.entity.NullEntity;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * httpclient5 请求基类
 *
 * @author sheng
 */
public abstract class HttpClient5BaseRequest<T extends HttpClient5BaseRequest<T>>
        implements RfcUriBuilderDecorator<T>, BaseRequest<T> {
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

    /**
     * 同步请求 #{@link HttpClient}
     */
    private HttpClient httpClient;

    // /**
    //  * 异步请求 #{@link HttpAsyncClient}
    //  */
    // private HttpAsyncClient httpAsyncClient;

    /**
     * 请求上下文
     */
    private HttpContext httpContext;

    // /**
    //  * #{@link AsyncRequestProducer} 提供者
    //  */
    // private ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc;
    //
    // /**
    //  * #{@link AsyncResponseConsumer} 提供者
    //  */
    // private ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable> asyncResponseConsumerSupplier;
    //
    // /**
    //  * #{@link AsyncPushConsumer} 提供者
    //  */
    // private ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier;

    /**
     * 异步执行线程池
     */
    protected ExecutorService executorService;

    public HttpClient5BaseRequest(String url, HttpMethodEnum method) {
        this.method = Objects.requireNonNull(method, "Http method must not be null");
        RfcUri rfcUri = RfcUri.parse(url);
        if (Objects.isNull(rfcUri)) {
            throw new HttpException(String.format("RfcUri解析url[%s]-[%s]失败", url, method.getMethod()));
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

    /**
     * 设置 HttpClient
     */
    public T httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return self();
    }

    // /**
    //  * 设置 HttpAsyncClient
    //  */
    // public T httpAsyncClient(HttpAsyncClient httpAsyncClient) {
    //     this.httpAsyncClient = httpAsyncClient;
    //     return self();
    // }

    /**
     * 请求上下文
     */
    public T httpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
        return self();
    }

    // /**
    //  * 设置 #{@link AsyncRequestProducer} 提供者
    //  */
    // public T asyncRequestProducerFunc(ThrowingFunc<HttpUriRequestBase, AsyncRequestProducer, Throwable> asyncRequestProducerFunc) {
    //     this.asyncRequestProducerFunc = asyncRequestProducerFunc;
    //     return self();
    // }

    // /**
    //  * 设置 #{@link AsyncResponseConsumer} 提供者
    //  */
    // public T asyncResponseConsumerSupplier(ThrowingSupplier<AsyncResponseConsumer<BasicClassicHttpResponse>, Throwable>
    // asyncResponseConsumerSupplier) {
    //     this.asyncResponseConsumerSupplier = asyncResponseConsumerSupplier;
    //     return self();
    // }

    // /**
    //  * 设置 #{@link AsyncPushConsumer} 提供者
    //  */
    // public T pushHandlerFactorySupplier(ThrowingSupplier<HandlerFactory<AsyncPushConsumer>, Throwable> pushHandlerFactorySupplier) {
    //     this.pushHandlerFactorySupplier = pushHandlerFactorySupplier;
    //     return self();
    // }

    /**
     * 设置线程池
     */
    public T executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return self();
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
    protected HttpEntity getRequestBody() {
        return this.httpEntity;
    }

    // region 请求

    /**
     * 获取处理 UTF_8 {@link String}结果的请求执行器
     */
    @Override
    public HttpClient5ExecutorBuilder<String> stringExecutor() {
        return executor(HttpStringResultHandle.UTF_8_INSTANCE);
    }

    /**
     * 获取处理{@link String}结果的请求执行器
     *
     * @param charset 结果编码
     */
    @Override
    public HttpClient5ExecutorBuilder<String> stringExecutor(Charset charset) {
        return executor(HttpStringResultHandle.create(charset));
    }

    /**
     * 获取处理 json 结果的请求执行器
     *
     * @param resultHandle {@link HttpJsonResultHandle}
     */
    @Override
    public <R> HttpClient5ExecutorBuilder<R> jsonExecutor(HttpJsonResultHandle<R> resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取处理 xml 结果的请求执行器
     *
     * @param resultHandle {@link HttpXmlResultHandle}
     */
    @Override
    public <R> HttpClient5ExecutorBuilder<R> xmlExecutor(HttpXmlResultHandle<R> resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取处理 byte[] 结果的请求执行器
     */
    @Override
    public HttpClient5ExecutorBuilder<byte[]> bytesExecutor() {
        return executor(HttpByteArrayResultHandle.INSTANCE);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param targetFile 目标文件
     * @return 下载文件大小
     */
    @Override
    public HttpClient5ExecutorBuilder<Long> downloadExecutor(File targetFile) {
        return executor(HttpFileResultHandle.create(targetFile));
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param resultHandle 结果处理器
     * @return 下载文件大小
     */
    @Override
    public HttpClient5ExecutorBuilder<Long> downloadExecutor(HttpFileResultHandle resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param outputStream 输出流
     * @return 下载文件大小
     */
    @Override
    public HttpClient5ExecutorBuilder<Long> downloadExecutor(OutputStream outputStream) {
        return executor(HttpOutputStreamResultHandle.create(outputStream));
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param resultHandle 结果处理器
     * @return 下载文件大小
     */
    @Override
    public HttpClient5ExecutorBuilder<Long> downloadExecutor(HttpOutputStreamResultHandle resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取请求执行器
     *
     * @param resultHandle 结果处理器
     */
    @Override
    public <R> HttpClient5ExecutorBuilder<R> executor(HttpResultHandle<R> resultHandle) {
        return HttpClient5ExecutorBuilder.create(createRequest(), resultHandle, httpClient, httpContext, executorService
                /*, httpAsyncClient, asyncRequestProducerFunc, asyncResponseConsumerSupplier, pushHandlerFactorySupplier*/);
    }

    // endregion

    /**
     * 请求前执行
     */
    protected void executeBefore() {
    }

    protected HttpUriRequestBase createRequest() {
        executeBefore();
        HttpUriRequestBase requestBase = new HttpUriRequestBase(method.getMethod(), uriBuilder.build().uri());
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
        if (Objects.isNull(requestBody) && method.isNeedBody()) {
            requestBody = NullEntity.INSTANCE;
        }
        requestBase.setEntity(requestBody);
        return requestBase;
    }
}
