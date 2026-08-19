package io.github.rmy20.tool.http.jdkclient.publisher;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * 需求管理
 *
 * @author sheng
 */
public class DemandManager {
    /**
     * 生产者阻塞最长时间
     */
    private static final long PRODUCER_WAIT_TIMEOUT_NS = TimeUnit.SECONDS.toNanos(30L);

    /**
     * 生产者唤醒或无需阻塞标记
     */
    private static final Object READY = new Object();

    /**
     * 下游订阅者状态
     */
    private enum StateEnum {
        RUNNING, CANCELLED, TERMINATED,
        ;
    }

    /**
     * 下游订阅者状态
     */
    private final AtomicReference<StateEnum> state = new AtomicReference<>(StateEnum.RUNNING);

    /**
     * 阻塞/唤醒生产者
     */
    private final AtomicReference<Object> parkedProducer = new AtomicReference<>();

    /**
     * 下游累计需求
     */
    private final AtomicLong requested = new AtomicLong(0L);

    /**
     * 异常终止时的错误
     */
    @Getter
    @Setter
    private volatile Throwable error;

    /**
     * 添加需求
     */
    public void addDemand(long demand) {
        StateEnum state = this.state.get();
        if (state != StateEnum.RUNNING) {
            return;
        }
        while (true) {
            long current = requested.get();
            if (current == Long.MAX_VALUE) {
                return;
            }
            long next = current + demand;
            if (next < 0) {
                next = Long.MAX_VALUE;
            }
            if (requested.compareAndSet(current, next)) {
                return;
            }
        }
    }

    /**
     * 获取累计请求数
     */
    public long getRequested() {
        return requested.get();
    }

    /**
     * 扣减需求额度，返回剩余的需求额度，终止返回-1
     */
    public long tryConsume(long n) {
        while (true) {
            if (state.get() != StateEnum.RUNNING) {
                return -1L;
            }
            long demand = requested.get();
            if (demand == Long.MAX_VALUE) {
                return demand;
            }
            long next = demand - n;
            if (next < 0L) {
                next = 0L;
            }
            if (requested.compareAndSet(demand, next)) {
                return next;
            }
        }
    }

    /**
     * 设置取消信号
     */
    public boolean tryCancel() {
        return state.compareAndSet(StateEnum.RUNNING, StateEnum.CANCELLED);
    }

    /**
     * 设置终止信号
     */
    public boolean tryTerminate() {
        return state.compareAndSet(StateEnum.RUNNING, StateEnum.TERMINATED);
    }

    /**
     * 是否已设置取消信号
     */
    public boolean isCancelled() {
        return state.get() == StateEnum.CANCELLED;
    }

    /**
     * 是否已设置终止信号
     */
    public boolean isTerminated() {
        return state.get() == StateEnum.TERMINATED;
    }

    /**
     * 是否已设置终止信号
     */
    public boolean isRunning() {
        return state.get() == StateEnum.RUNNING;
    }

    /**
     * 生产者阻塞
     */
    public void awaitProducer() throws IOException {
        Thread currentThread = Thread.currentThread();
        // 阻塞截止时间
        long deadline = System.nanoTime() + PRODUCER_WAIT_TIMEOUT_NS;
        while (true) {
            if (Thread.interrupted()) {
                currentThread.interrupt();
                throw new IOException("Producer thread interrupted");
            }
            Object mark = parkedProducer.get();
            // 下游已唤醒信号，允许继续生产，不阻塞
            if (mark == READY) {
                break;
            }
            if (mark != null && mark != currentThread) {
                throw new IllegalStateException("Only single producer thread supported");
            }
            // 剩余时间
            long remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                throw new IOException("Timeout waiting for consumer demand (30s)");
            }
            // 当前线程 CAS 抢占阻塞
            if (parkedProducer.compareAndSet(null, currentThread)) {
                try {
                    LockSupport.parkNanos(remaining);
                } finally {
                    parkedProducer.compareAndSet(currentThread, null);
                }
            }
        }
        // 去除消费者预支的唤醒标记，当队列再次到达高水位时阻塞生产者
        parkedProducer.lazySet(null);
    }

    /**
     * 唤醒生产者
     */
    public void resumeProducer() {
        // 为 READY 时，表明已有线程唤醒
        if (parkedProducer.get() == READY) {
            return;
        }
        // 防止其他线程插队
        Object mark = parkedProducer.getAndSet(READY);
        if (mark instanceof Thread thread) {
            LockSupport.unpark(thread);
        }
    }
}
