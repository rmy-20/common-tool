package cn.zs.tool.core.fuction.throwing;

/**
 * 抛异常执行
 *
 * @author sheng
 */
@FunctionalInterface
public interface ThrowingRunnable<EX extends Throwable> {
    /**
     * 运行
     */
    void run() throws EX;
}
