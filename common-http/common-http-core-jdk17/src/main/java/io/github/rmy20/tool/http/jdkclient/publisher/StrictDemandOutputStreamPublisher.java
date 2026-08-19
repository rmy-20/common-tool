package io.github.rmy20.tool.http.jdkclient.publisher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;

/**
 * 按需将 #{@link java.io.OutputStream} 适配为 #{@link java.util.concurrent.Flow.Publisher}
 *
 * @author sheng
 */
public class StrictDemandOutputStreamPublisher implements Flow.Publisher<ByteBuffer> {
    /**
     * 默认缓冲区大小
     */
    private static final int CHUNK_SIZE = 8192;

    /**
     * 实际待发布数据
     */
    private final OutputStreamProducer outputStreamProducer;

    /**
     * 缓冲区大小
     */
    private final int chunkSize;

    /**
     * 生产数据线程池
     */
    private final Executor executor;

    /**
     * 创建#{@link StrictDemandOutputStreamPublisher}
     */
    public static StrictDemandOutputStreamPublisher create(Executor executor, OutputStreamProducer outputStreamProducer) {
        return new StrictDemandOutputStreamPublisher(executor, outputStreamProducer);
    }

    /**
     * 创建#{@link StrictDemandOutputStreamPublisher}
     */
    public static StrictDemandOutputStreamPublisher create(Executor executor, OutputStreamProducer outputStreamProducer, int chunkSize) {
        return new StrictDemandOutputStreamPublisher(executor, outputStreamProducer, chunkSize);
    }

    public StrictDemandOutputStreamPublisher(Executor executor, OutputStreamProducer outputStreamProducer) {
        this(executor, outputStreamProducer, CHUNK_SIZE);
    }

    public StrictDemandOutputStreamPublisher(Executor executor, OutputStreamProducer outputStreamProducer, int chunkSize) {
        this.executor = Objects.requireNonNull(executor, "executor must not be null");
        this.outputStreamProducer = Objects.requireNonNull(outputStreamProducer, "outputStreamProducer must not be null");
        if (chunkSize < 1024) {
            throw new IllegalArgumentException("chunkSize must be greater than 1024");
        }
        this.chunkSize = chunkSize;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
        Objects.requireNonNull(subscriber, "Subscriber must not be null");
        StrictDemandOutputStreamSubscription outputStreamSubscription
                = new StrictDemandOutputStreamSubscription(subscriber, outputStreamProducer, chunkSize);
        subscriber.onSubscribe(outputStreamSubscription);
        executor.execute(outputStreamSubscription::invokeProducer);
    }

    /**
     * 订阅
     */
    private static class StrictDemandOutputStreamSubscription extends AbstractChunkedOutputStream implements Flow.Subscription {
        /**
         * 实际流数据消费者
         */
        private final Flow.Subscriber<? super ByteBuffer> subscriber;

        /**
         * 实际待发布数据
         */
        private final OutputStreamProducer outputStreamProducer;

        /**
         * 需求管理
         */
        private final DemandManager demandManager = new DemandManager();

        /**
         * 生产者已发送但未扣减的数量
         */
        private long produced;

        /**
         * 防止重复发送终端信号
         */
        private volatile boolean terminalSent;

        private StrictDemandOutputStreamSubscription(Flow.Subscriber<? super ByteBuffer> subscriber, OutputStreamProducer outputStreamProducer,
                                                     int chunkSize) {
            super(chunkSize);
            this.subscriber = subscriber;
            this.outputStreamProducer = outputStreamProducer;
        }

        private void invokeProducer() {
            try {
                outputStreamProducer.write(this);
                close();
                if (demandManager.tryTerminate() && !terminalSent) {
                    terminalSent = true;
                    subscriber.onComplete();
                }
            } catch (Throwable e) {
                if (demandManager.tryTerminate() && !terminalSent) {
                    terminalSent = true;
                    subscriber.onError(e);
                }
            }
        }

        @Override
        protected void ensureOpen() throws IOException {
            if (demandManager.isCancelled()) {
                throw new IOException("Subscription cancelled");
            }
            if (demandManager.isTerminated()) {
                throw new IOException("Subscription terminated");
            }
            if (closed) {
                throw new IOException("Subscription is closed");
            }
        }

        @Override
        protected void flushBuffer() throws IOException {
            if (position < 1) {
                return;
            }
            awaitDemand();
            byte[] data = new byte[position];
            System.arraycopy(buffer, 0, data, 0, position);
            position = 0;
            subscriber.onNext(ByteBuffer.wrap(data));
            produced++;
        }

        /**
         * 阻塞等待下游需求
         */
        private void awaitDemand() throws IOException {
            while (true) {
                if (demandManager.isCancelled()) {
                    throw new IOException("Subscription cancelled");
                }
                if (demandManager.isTerminated()) {
                    Throwable err = demandManager.getError();
                    if (Objects.nonNull(err)) {
                        throw new IOException("Subscription terminated", err);
                    }
                    throw new IOException("Subscription terminated");
                }
                long demand = demandManager.getRequested();
                if (demand == Long.MAX_VALUE) {
                    produced = 0L;
                    return;
                }
                // 当前生产者已发送的还没有达到需求数
                if (produced < demand) {
                    return;
                }
                // 扣减需求
                demand = demandManager.tryConsume(produced);
                produced = 0L;
                // ＜ 0 则已停止，下次循环抛出异常
                if (demand > 0L) {
                    return;
                } else if (demand == 0L) {
                    demandManager.awaitProducer();
                }
            }
        }

        @Override
        public void request(long n) {
            if (n < 1) {
                demandManager.setError(new IllegalArgumentException("request demand must be positive, but was " + n));
                demandManager.tryTerminate();
            } else {
                demandManager.addDemand(n);
            }
            demandManager.resumeProducer();
        }

        @Override
        public void cancel() {
            if (demandManager.tryCancel()) {
                demandManager.resumeProducer();
            }
        }
    }
}
