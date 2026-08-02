package io.github.rmy20.tool.core.constant;

import io.github.rmy20.tool.core.util.RandomUtil;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 常量
 *
 * @author sheng
 */
public interface CommonConstant {
    /**
     * 100
     */
    BigDecimal HUNDRED = new BigDecimal("100");

    /**
     * 0.01
     */
    BigDecimal ONE_CENT = new BigDecimal("0.01");

    /**
     * 当任务数队列已满1000，且执行线程超过最大线程数100时，执行策略为调用者运行任务
     */
    ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(6, 100, 1, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1000),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("common-thread-pool-" + RandomUtil.generateSequenceUlid());
                return thread;
            }, new ThreadPoolExecutor.CallerRunsPolicy());
}
