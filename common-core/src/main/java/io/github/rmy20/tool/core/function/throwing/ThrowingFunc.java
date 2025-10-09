package io.github.rmy20.tool.core.function.throwing;

/**
 * 带异常的函数式接口
 *
 * @author sheng
 */
@FunctionalInterface
public interface ThrowingFunc<T, R, EX extends Throwable> {
    /**
     * 处理参数 t 并返回结果 R
     */
    R apply(T t) throws EX;
}
