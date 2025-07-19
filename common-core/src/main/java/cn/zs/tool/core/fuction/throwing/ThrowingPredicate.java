package cn.zs.tool.core.fuction.throwing;

/**
 * 抛异常的断言类
 *
 * @author sheng
 */
@FunctionalInterface
public interface ThrowingPredicate<T, EX extends Throwable> {
    /**
     * 根据参数判断 true false
     */
    boolean test(T t) throws EX;
}
