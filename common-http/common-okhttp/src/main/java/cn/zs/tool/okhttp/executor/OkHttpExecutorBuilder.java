package cn.zs.tool.okhttp.executor;

import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.execute.ExecutorBaseBuilder;
import cn.zs.tool.okhttp.response.OkHttpAsyncResponseFuture;
import okhttp3.Call;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * okhttp执行器构建器
 *
 * @author sheng
 */
public class OkHttpExecutorBuilder<R> extends ExecutorBaseBuilder<R, OkHttpExecutor<R>, OkHttpAsyncExecutor<R>, OkHttpExecutorBuilder<R>> {
    /**
     * 请求对象
     */
    private final Call call;

    /**
     * 创建执行器构建器
     *
     * @param call         请求对象
     * @param msgConverter 消息转换器
     */
    public static <R> OkHttpExecutorBuilder<R> create(Call call, HttpMsgConverter<R> msgConverter) {
        return new OkHttpExecutorBuilder<>(call, msgConverter);
    }

    public OkHttpExecutorBuilder(Call call, HttpMsgConverter<R> msgConverter) {
        super(msgConverter);
        this.call = Objects.requireNonNull(call, "call must not be null");
    }

    @Override
    protected OkHttpExecutorBuilder<R> self() {
        return this;
    }

    @Override
    public OkHttpExecutor<R> execute() {
        return OkHttpExecutor.create(call, getMsgConverter(), getErrHandler(), getOkPredicate(), isMustHandleResult());
    }

    @Override
    public CompletableFuture<OkHttpAsyncExecutor<R>> executeAsync() {
        return OkHttpAsyncResponseFuture.create(call, getMsgConverter(), getErrHandler(), getOkPredicate(), isMustHandleResult());
    }
}
