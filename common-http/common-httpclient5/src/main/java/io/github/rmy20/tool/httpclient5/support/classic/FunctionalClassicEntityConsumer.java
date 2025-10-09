package io.github.rmy20.tool.httpclient5.support.classic;

import io.github.rmy20.tool.core.constant.CommonConstant;
import io.github.rmy20.tool.core.function.throwing.ThrowingBiFunc;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.nio.support.classic.AbstractClassicEntityConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * 函数式实现#{@link AbstractClassicEntityConsumer}
 *
 * @author sheng
 */
public class FunctionalClassicEntityConsumer<R> extends AbstractClassicEntityConsumer<R> {
    /**
     * 函数式实现消费器
     */
    private final ThrowingBiFunc<ContentType, InputStream, R, ? extends Throwable> func;

    /**
     * 线程池
     */
    private final ExecutorService executorService;

    /**
     * 是否关闭线程池
     */
    private final boolean shutdownExecutor;

    public static <R> FunctionalClassicEntityConsumer<R> create(ThrowingBiFunc<ContentType, InputStream, R, Throwable> func) {
        return new FunctionalClassicEntityConsumer<>(func);
    }

    public FunctionalClassicEntityConsumer(ThrowingBiFunc<ContentType, InputStream, R, Throwable> func) {
        this(func, 4096, CommonConstant.EXECUTOR_SERVICE, false);
    }

    public FunctionalClassicEntityConsumer(ThrowingBiFunc<ContentType, InputStream, R, Throwable> func, int initialBufferSize,
                                           ExecutorService executorService, boolean shutdownExecutor) {
        super(initialBufferSize, executorService);
        this.func = Objects.requireNonNull(func, "func must not be null");
        this.executorService = executorService;
        this.shutdownExecutor = shutdownExecutor;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected R consumeData(ContentType contentType, InputStream inputStream) throws IOException {
        return ((ThrowingBiFunc<ContentType, InputStream, R, RuntimeException>) func).apply(contentType, inputStream);
    }

    @Override
    public void releaseResources() {
        super.releaseResources();
        if (shutdownExecutor && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
