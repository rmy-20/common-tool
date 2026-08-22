package io.github.rmy20.tool.http.jdkclient.publisher;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;

/**
 * 将 #{@link java.io.OutputStream} 适配为 #{@link java.util.concurrent.Flow.Publisher}
 *
 * @author sheng
 */
public abstract class AbstractProducerPublisher<T> implements Flow.Publisher<T> {
    /**
     * 默认内部缓冲区大小
     */
    protected static final int CHUNK_SIZE = 8192;

    /**
     * 实际待发布数据
     */
    protected final OutputStreamProducer outputStreamProducer;

    /**
     * 生产数据线程池
     */
    protected final Executor executor;

    /**
     * 内部缓冲区大小
     */
    protected final int chunkSize;

    protected AbstractProducerPublisher(Executor executor, OutputStreamProducer outputStreamProducer, int chunkSize) {
        this.executor = Objects.requireNonNull(executor, "executor must not be null");
        this.outputStreamProducer = Objects.requireNonNull(outputStreamProducer, "outputStreamProducer must not be null");
        if (chunkSize < 1024) {
            throw new IllegalArgumentException("chunkSize must be greater than 1024");
        }
        this.chunkSize = chunkSize;
    }
}
