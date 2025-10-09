package io.github.rmy20.tool.core.function.throwing;

/**
 * 带异常的函数式接口
 *
 * @author sheng
 */
public interface ThrowingSupplier<T, EX extends Throwable> {
    /**
     * 获取结果
     */
    T get() throws EX;
}
