package cn.zs.tool.core.fuction.throwing;

/**
 * 带异常消费者
 *
 * @author sheng
 */
@FunctionalInterface
public interface ThrowingConsumer<T, EX extends Throwable> {
    /**
     * 消费输入的参数
     */
    void accept(T t) throws EX;
}
