package io.github.rmy20.tool.http.jdkclient.publisher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 将 #{@link java.io.OutputStream} 适配为 #{@link java.util.concurrent.Flow.Publisher}
 * </br>
 * 有界队列缓冲+高低水位阻塞生产者混合模型，默认队列达到80阻塞 OutputStream 写入线程；低水位30唤醒写入线程；使用#{@link java.util.concurrent.LinkedBlockingQueue}作为有界队列
 *
 * @author sheng
 */
public class BoundedQueueOutputStreamPublisher implements Flow.Publisher<ByteBuffer> {
    /**
     * 默认高水位
     */
    private static final int HIGH_WATER_MARK = 80;

    /**
     * 默认低水位
     */
    private static final int LOW_WATER_MARK = 30;

    /**
     * 默认内部缓冲区大小
     */
    private static final int CHUNK_SIZE = 8192;

    /**
     * 实际待发布数据
     */
    private final OutputStreamProducer outputStreamProducer;

    /**
     * 内部缓冲区大小
     */
    private final int chunkSize;

    /**
     * 低水位
     */
    private final int lowWaterMark;

    /**
     * 高水位
     */
    private final int highWaterMark;

    /**
     * 生产者线程池
     */
    private final Executor executor;

    /**
     * 创建#{@link BoundedQueueOutputStreamPublisher}
     */
    public static BoundedQueueOutputStreamPublisher create(Executor executor, OutputStreamProducer outputStreamProducer) {
        return new BoundedQueueOutputStreamPublisher(executor, outputStreamProducer);
    }

    /**
     * 创建#{@link BoundedQueueOutputStreamPublisher}
     */
    public static BoundedQueueOutputStreamPublisher create(Executor executor, OutputStreamProducer outputStreamProducer,
                                                           int chunkSize, int lowWaterMark, int highWaterMark) {
        return new BoundedQueueOutputStreamPublisher(executor, outputStreamProducer, chunkSize, lowWaterMark, highWaterMark);
    }

    public BoundedQueueOutputStreamPublisher(Executor executor, OutputStreamProducer outputStreamProducer) {
        this(executor, outputStreamProducer, CHUNK_SIZE, LOW_WATER_MARK, HIGH_WATER_MARK);
    }

    public BoundedQueueOutputStreamPublisher(Executor executor, OutputStreamProducer outputStreamProducer, int chunkSize, int lowWaterMark,
                                             int highWaterMark) {
        this.executor = Objects.requireNonNull(executor, "executor must not be null");
        this.outputStreamProducer = Objects.requireNonNull(outputStreamProducer, "outputStreamProducer must not be null");
        if (chunkSize < 1024) {
            throw new IllegalArgumentException("chunkSize must be greater than 1024");
        }
        if (lowWaterMark < 5) {
            throw new IllegalArgumentException("lowWaterMark must be greater than 5");
        }
        if (highWaterMark < (lowWaterMark << 1)) {
            throw new IllegalArgumentException("highWaterMark must be greater than 2 * lowWater");
        }
        this.chunkSize = chunkSize;
        this.lowWaterMark = lowWaterMark;
        this.highWaterMark = highWaterMark;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
        Objects.requireNonNull(subscriber, "subscriber must not be null");
        BoundedQueueOutputStreamSubscription subscription
                = new BoundedQueueOutputStreamSubscription(subscriber, outputStreamProducer, chunkSize, lowWaterMark, highWaterMark);
        subscriber.onSubscribe(subscription);
        executor.execute(subscription::invokeProducer);
    }

    /**
     * 订阅
     */
    private static class BoundedQueueOutputStreamSubscription extends AbstractChunkedOutputStream implements Flow.Subscription {
        /**
         * 消费线程串行
         */
        private static final AtomicIntegerFieldUpdater<BoundedQueueOutputStreamSubscription> WIP
                = AtomicIntegerFieldUpdater.newUpdater(BoundedQueueOutputStreamSubscription.class, "wip");

        /**
         * 消费线程串行标记
         */
        private volatile int wip = 0;

        /**
         * 实际流数据消费者
         */
        private final Flow.Subscriber<? super ByteBuffer> subscriber;

        /**
         * 实际待发布数据
         */
        private final OutputStreamProducer outputStreamProducer;

        /**
         * 缓冲队列
         */
        private final LinkedBlockingQueue<ByteBuffer> queue;

        /**
         * 低水位，队列低于该值唤醒生产者
         */
        private final int lowWaterMark;

        /**
         * 高水位，队列高于该值阻塞生产者
         */
        private final int highWaterMark;

        /**
         * 需求管理
         */
        private final DemandManager demandManager = new DemandManager();

        /**
         * 防止重复发送终端信号
         */
        private volatile boolean terminalSent;

        private BoundedQueueOutputStreamSubscription(Flow.Subscriber<? super ByteBuffer> subscriber, OutputStreamProducer outputStreamProducer,
                                                     int chunkSize, int lowWaterMark, int highWaterMark) {
            super(chunkSize);
            this.subscriber = subscriber;
            this.outputStreamProducer = outputStreamProducer;
            this.lowWaterMark = lowWaterMark;
            this.highWaterMark = highWaterMark;
            this.queue = new LinkedBlockingQueue<>(highWaterMark + 10);
        }

        private void invokeProducer() {
            try {
                outputStreamProducer.write(this);
            } catch (Throwable e) {
                demandManager.tryTerminate();
                demandManager.setError(e);
            } finally {
                try {
                    close();
                } catch (IOException ignore) {
                }
                drain();
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

        /**
         * 将缓冲区入队
         */
        @Override
        protected void flushBuffer() throws IOException {
            if (position == 0) {
                return;
            }
            byte[] data = new byte[position];
            System.arraycopy(buffer, 0, data, 0, position);
            position = 0;
            offerOrAwait(ByteBuffer.wrap(data));
            // 通知消费者清空队列
            drain();
        }

        @Override
        protected void onClose() throws IOException {
            // 清空数据
            drain();
        }

        /**
         * 将数据块入队列，如队列长度超过高水位则先阻塞
         */
        private void offerOrAwait(ByteBuffer chunk) throws IOException {
            // 检查队列长度
            while (queue.size() >= highWaterMark) {
                ensureOpen();
                demandManager.awaitProducer();
            }
            if (!queue.offer(chunk)) {
                throw new IOException("Queue capacity exhausted");
            }
        }

        private void clearQueue() {
            queue.clear();
            demandManager.resumeProducer();
        }

        /**
         * 将队列的数据发送
         */
        private void drain() {
            // 不为 0 则表明已有线程在跑
            if (WIP.getAndIncrement(this) != 0) {
                return;
            }
            int missed = 1;
            while (true) {
                try {
                    // 消费者取消，清空队列并退出
                    if (demandManager.isCancelled()) {
                        clearQueue();
                        return;
                    }
                    // 错误终止
                    Throwable error = demandManager.getError();
                    if (demandManager.isTerminated()) {
                        if (!terminalSent && Objects.nonNull(error)) {
                            terminalSent = true;
                            subscriber.onError(error);
                        }
                        clearQueue();
                        return;
                    }
                    // 正常结束
                    if (closed && Objects.isNull(error) && queue.isEmpty()) {
                        if (demandManager.tryTerminate() && !terminalSent) {
                            terminalSent = true;
                            subscriber.onComplete();
                        }
                        return;
                    }

                    long demand = demandManager.getRequested();
                    int emitted = 0;
                    while (demand > 0 && !queue.isEmpty()) {
                        ByteBuffer chunk = queue.poll();
                        if (Objects.isNull(chunk)) {
                            break;
                        }
                        // 低水位唤醒生产者
                        if (queue.size() < lowWaterMark) {
                            demandManager.resumeProducer();
                        }
                        subscriber.onNext(chunk);
                        emitted++;
                        demand--;
                    }
                    if (emitted > 0) {
                        long remaining = demandManager.tryConsume(emitted);
                        if (remaining < 0L) {
                            clearQueue();
                        }
                    }
                } catch (Throwable e) {
                    demandManager.tryTerminate();
                    demandManager.setError(e);
                    clearQueue();
                    if (!terminalSent) {
                        terminalSent = true;
                        subscriber.onError(e);
                    }
                    return;
                } finally {
                    missed = WIP.decrementAndGet(this);
                }
                if (missed < 1) {
                    return;
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
            drain();
        }

        @Override
        public void cancel() {
            if (demandManager.tryCancel()) {
                demandManager.resumeProducer();
                drain();
            }
        }
    }
}
