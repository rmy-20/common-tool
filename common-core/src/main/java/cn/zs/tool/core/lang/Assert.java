package cn.zs.tool.core.lang;

import cn.zs.tool.core.text.StringUtil;

import java.util.function.Supplier;

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
     * @param <EX>          异常类型
     */
    public static <EX extends RuntimeException> void isTrue(boolean condition, final Supplier<? extends EX> throwSupplier) {
        if (!condition) {
            throw throwSupplier.get();
        }
    }
}
