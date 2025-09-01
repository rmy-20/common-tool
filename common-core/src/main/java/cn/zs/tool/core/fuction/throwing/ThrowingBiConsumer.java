package cn.zs.tool.core.fuction.throwing;

/**
 * 带异常消费者
 *
 * @author sheng
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U, EX extends Throwable> {
    /**
     * 消费输入的参数
     */
    void accept(T t, U u) throws EX;
}
