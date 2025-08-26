package cn.zs.tool.httpclient5.executor;

import cn.zs.tool.core.fuction.throwing.ThrowingSupplier;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.execute.ExecutorBaseBuilder;
import cn.zs.tool.httpclient5.response.HttpClient5Response;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * httpclient5执行器构建器
 *
 * @author sheng
 */
public class HttpClient5ExecutorBuilder<R> extends ExecutorBaseBuilder<R,
        HttpClient5Executor<R>, HttpClient5Executor<R>, HttpClient5ExecutorBuilder<R>> {
    /**
     * 响应提供者
     */
    private final ThrowingSupplier<HttpClient5Response, Throwable> responseSupplier;

    /**
     * 创建执行器构建器
     *
     * @param responseSupplier 响应提供者
     * @param msgConverter     消息转换器
     */
    public static <R> HttpClient5ExecutorBuilder<R> create(ThrowingSupplier<HttpClient5Response, Throwable> responseSupplier,
                                                           HttpMsgConverter<R> msgConverter) {
        return new HttpClient5ExecutorBuilder<>(responseSupplier, msgConverter);
    }

    public HttpClient5ExecutorBuilder(ThrowingSupplier<HttpClient5Response, Throwable> responseSupplier, HttpMsgConverter<R> msgConverter) {
        super(msgConverter);
        this.responseSupplier = Objects.requireNonNull(responseSupplier, "responseSupplier must not be null");
    }

    @Override
    protected HttpClient5ExecutorBuilder<R> self() {
        return this;
    }

    @Override
    public HttpClient5Executor<R> execute() {
        return HttpClient5Executor.create(responseSupplier, getMsgConverter(), getOkPredicate(), getErrHandler(), isMustHandleResult());
    }

    @Override
    public CompletableFuture<HttpClient5Executor<R>> executeAsync() {
        return null;
    }
}
