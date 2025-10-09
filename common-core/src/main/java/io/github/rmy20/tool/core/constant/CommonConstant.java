package io.github.rmy20.tool.core.constant;

import io.github.rmy20.tool.core.util.RandomUtil;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
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
     * 线程池
     */
    ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1, TimeUnit.MINUTES,
            new SynchronousQueue<>(),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("common-thread-pool-" + RandomUtil.generateUuidSimple());
                return thread;
            });
}
