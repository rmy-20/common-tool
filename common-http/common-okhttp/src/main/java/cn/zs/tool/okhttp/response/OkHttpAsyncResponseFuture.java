package cn.zs.tool.okhttp.response;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.okhttp.executor.OkHttpAsyncExecutor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * okhttp回调
 *
 * @author sheng
 */
@Slf4j
public class OkHttpAsyncResponseFuture<R> extends CompletableFuture<OkHttpAsyncExecutor<R>> implements Callback {
    /**
     * 结果处理器
     */
    private final HttpMsgConverter<R> msgConverter;

    /**
     * 错误处理器
     */
    private final ThrowingConsumer<Throwable, Throwable> errHandler;

    /**
     * 成功判断
     */
    private final Predicate<Integer> okPredicate;

    /**
     * 是否必须处理结果
     */
    private final Boolean mustHandleResult;

    /**
     * 创建{@link OkHttpAsyncResponseFuture}
     *
     * @param callSupplier {@link Call}生产者
     * @param msgConverter 结果处理器
     * @param errHandler   错误处理器
     * @param okPredicate  成功判断
     */
    public static <R> OkHttpAsyncResponseFuture<R> create(Supplier<Call> callSupplier, HttpMsgConverter<R> msgConverter,
                                                          ThrowingConsumer<Throwable, Throwable> errHandler,
                                                          Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        return new OkHttpAsyncResponseFuture<>(callSupplier, msgConverter, errHandler, okPredicate, mustHandleResult);
    }

    public OkHttpAsyncResponseFuture(Supplier<Call> callSupplier, HttpMsgConverter<R> msgConverter, ThrowingConsumer<Throwable, Throwable> errHandler,
                                     Predicate<Integer> okPredicate, Boolean mustHandleResult) {
        this.msgConverter = Objects.requireNonNull(msgConverter, "msgConverter must not be null");
        this.errHandler = errHandler;
        this.okPredicate = okPredicate;
        this.mustHandleResult = mustHandleResult;
        enqueue(callSupplier);
    }

    /**
     * 异步请求
     */
    private void enqueue(Supplier<Call> callSupplier) {
        try {
            callSupplier.get().enqueue(this);
        } catch (Throwable e) {
            log.error("执行okhttp异步请求异常", e);
            completeExceptionally(e);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        completeExceptionally(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            complete(OkHttpAsyncExecutor.create(call, response, msgConverter, errHandler, okPredicate, mustHandleResult));
        } catch (Throwable e) {
            log.error("构建okhttp异步响应处理器异常", e);
            completeExceptionally(e);
        }
    }
}
