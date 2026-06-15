package io.github.rmy20.tool.core.function;

/**
 * 链式调用
 *
 * @author sheng
 */
public interface Chainable<T extends Chainable<T>> {
    /**
     * 返回自身
     */
    T self();
}
