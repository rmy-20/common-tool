package cn.zs.tool.core.lang;

import cn.zs.tool.core.fuction.throwing.ThrowingRunnable;
import cn.zs.tool.core.fuction.throwing.ThrowingSupplier;
import cn.zs.tool.core.text.StringUtil;

/**
 * 断言类
 *
 * @author sheng
 */
public final class Assert {
    /**
     * 断言
     *
     * @param condition 条件 == false 则抛出异常
     * @param msg       错误信息
     */
    public static void isTrue(boolean condition, String msg) {
        if (!condition) {
            throw new AssertException(msg);
        }
    }

    /**
     * 断言
     *
     * @param obj 当对象为null则抛出异常
     * @param msg 错误信息
     */
    public static void nonNull(Object obj, String msg) {
        if (obj == null) {
            throw new AssertException(msg);
        }
    }

    public static void notBlank(String str, String msg) {
        if (StringUtil.isBlank(str)) {
            throw new AssertException(msg);
        }
    }

    /**
     * 断言
     *
     * @param condition     条件
     * @param throwSupplier 异常生产者
     */
    public static void isTrue(boolean condition, final ThrowingSupplier<Throwable, ? extends Throwable> throwSupplier) {
        isTrue(() -> condition, () -> {
            throw throwSupplier.get();
        });
    }

    /**
     * 断言
     *
     * @param condition 条件
     * @param errRun    不符合条件时执行的方法
     */
    @SuppressWarnings("unchecked")
    public static void isTrue(ThrowingSupplier<Boolean, ? extends Throwable> condition,
                              ThrowingRunnable<? extends Throwable> errRun) {
        if (!((ThrowingSupplier<Boolean, RuntimeException>) condition).get()) {
            ((ThrowingRunnable<RuntimeException>) errRun).run();
        }
    }
}
