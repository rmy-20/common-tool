package io.github.rmy20.tool.http.core.uri;

/**
 * 判断传入字符是否合法
 *
 * @author sheng
 */
@FunctionalInterface
public interface AllowedPredicate {
    /**
     * 判断传入字符是否合法
     */
    boolean isAllowed(int c);
}
