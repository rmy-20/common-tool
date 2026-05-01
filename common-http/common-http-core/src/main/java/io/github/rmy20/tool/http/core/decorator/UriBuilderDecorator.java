package io.github.rmy20.tool.http.core.decorator;

/**
 * URI 参数构建
 *
 * @author sheng
 */
public interface UriBuilderDecorator<T extends UriBuilderDecorator<T>> {
    /**
     * 获取当前实例
     */
    T self();

    /**
     * 添加参数
     *
     * @param name  参数名
     * @param value 参数值
     */
    T query(String name, Object value);

    /**
     * 添加参数
     *
     * @param condition 是否添加
     * @param name      参数名
     * @param value     参数值
     */
    default T query(boolean condition, String name, Object value) {
        return condition ? query(name, value) : self();
    }

    /**
     * 添加参数并编码
     *
     * @param name  参数名
     * @param value 参数值
     */
    T queryEncoded(String name, Object value);

    /**
     * 添加参数并编码
     *
     * @param condition 是否添加
     * @param name      参数名
     * @param value     参数值
     */
    default T queryEncoded(boolean condition, String name, Object value) {
        return condition ? queryEncoded(name, value) : self();
    }

    /**
     * 添加路径，会将 / 转换为 %2F
     *
     * @param path 路径，如 a
     */
    T path(String path);

    /**
     * 添加路径，会将 / 转换为 %2F
     *
     * @param condition 是否添加
     * @param path      路径，如 a
     */
    default T path(boolean condition, String path) {
        return condition ? path(path) : self();
    }

    /**
     * 添加路径
     *
     * @param path 路径，如 /a/b/c
     */
    T paths(String path);

    /**
     * 添加路径
     *
     * @param condition 是否添加
     * @param path      路径，如 /a/b/c
     */
    default T paths(boolean condition, String path) {
        return condition ? paths(path) : self();
    }

    /**
     * 添加路径并编码，会将 / 转换为 %2F
     *
     * @param path 路径，如 a
     */
    T pathEncoded(String path);

    /**
     * 添加路径并编码，会将 / 转换为 %2F
     *
     * @param condition 是否添加
     * @param path      路径，如 a
     */
    default T pathEncoded(boolean condition, String path) {
        return condition ? pathEncoded(path) : self();
    }

    /**
     * 添加路径并编码
     *
     * @param path 路径，如 /a/b/c
     */
    T pathsEncoded(String path);

    /**
     * 添加路径并编码
     *
     * @param condition 是否添加
     * @param path      路径，如 /a/b/c
     */
    default T pathsEncoded(boolean condition, String path) {
        return condition ? pathsEncoded(path) : self();
    }

    /**
     * 设置片段标识符
     */
    T fragment(String fragment);

    /**
     * 设置片段标识符
     *
     * @param condition 是否添加
     */
    default T fragment(boolean condition, String fragment) {
        return condition ? fragment(fragment) : self();
    }
}
