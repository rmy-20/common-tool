package io.github.rmy20.tool.http.core.decorator;

import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.http.core.uri.RfcUri;

import java.util.Objects;

/**
 * Rfc 3986 URI 参数构建
 *
 * @author sheng
 */
public interface RfcUriBuilderDecorator<T extends RfcUriBuilderDecorator<T>> extends UriBuilderDecorator<T> {
    /**
     * 获取当前实例的URI构建器
     */
    RfcUri.Builder getUriBuilder();

    /**
     * 添加参数
     *
     * @param name  参数名
     * @param value 参数值
     */
    @Override
    default T query(String name, Object value) {
        getUriBuilder().query(name, Objects.toString(value, StringPool.EMPTY));
        return self();
    }

    /**
     * 添加参数并编码
     *
     * @param name  参数名
     * @param value 参数值
     */
    @Override
    default T queryEncoded(String name, Object value) {
        getUriBuilder().queryEncoded(name, Objects.toString(value, StringPool.EMPTY));
        return self();
    }

    /**
     * 添加路径，会将 / 转换为 %2F
     *
     * @param path 路径，如 a
     */
    @Override
    default T path(String path) {
        getUriBuilder().pathSegment(path);
        return self();
    }

    /**
     * 添加路径
     *
     * @param path 路径，如 /a/b/c
     */
    @Override
    default T paths(String path) {
        getUriBuilder().pathSegments(path);
        return self();
    }

    /**
     * 添加路径并编码，会将 / 转换为 %2F
     *
     * @param path 路径，如 a
     */
    @Override
    default T pathEncoded(String path) {
        getUriBuilder().pathSegmentEncoded(path);
        return self();
    }

    /**
     * 添加路径并编码
     *
     * @param path 路径，如 /a/b/c
     */
    @Override
    default T pathsEncoded(String path) {
        getUriBuilder().pathSegmentsEncoded(path);
        return self();
    }

    @Override
    default T fragment(String fragment) {
        getUriBuilder().fragment(fragment);
        return self();
    }
}
