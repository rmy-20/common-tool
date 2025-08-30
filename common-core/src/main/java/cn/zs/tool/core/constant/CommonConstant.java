package cn.zs.tool.core.constant;

import cn.zs.tool.core.util.RandomUtil;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
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
    ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 1, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(2000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("common-thread-pool-" + RandomUtil.generateUuidSimple());
            return thread;
        }
    });
}
