package io.github.rmy20.tool.okhttp.decorator;

import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.http.core.decorator.UriBuilderDecorator;
import okhttp3.HttpUrl;

import java.util.Objects;

/**
 * OkHttp 参数构建器
 *
 * @author sheng
 */
public interface OkHttpUriBuilderDecorator<T extends OkHttpUriBuilderDecorator<T>> extends UriBuilderDecorator<T> {
    /**
     * 获取当前实例的HttpUrl.Builder
     */
    HttpUrl.Builder getUrlBuilder();

    /**
     * 添加参数
     *
     * @param name  参数名
     * @param value 参数值
     */
    @Override
    default T query(String name, Object value) {
        getUrlBuilder().addQueryParameter(name, Objects.toString(value, StringPool.EMPTY));
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
        getUrlBuilder().addEncodedQueryParameter(name, Objects.toString(value, StringPool.EMPTY));
        return self();
    }

    /**
     * 添加路径，会将 / 转换为 %2F
     *
     * @param path 路径，如 a
     */
    @Override
    default T path(String path) {
        getUrlBuilder().addPathSegment(path);
        return self();
    }

    /**
     * 添加路径
     *
     * @param path 路径，如 /a/b/c
     */
    @Override
    default T paths(String path) {
        getUrlBuilder().addPathSegments(path);
        return self();
    }

    /**
     * 添加路径并编码，会将 / 转换为 %2F
     *
     * @param path 路径，如 a
     */
    @Override
    default T pathEncoded(String path) {
        getUrlBuilder().addEncodedPathSegment(path);
        return self();
    }

    /**
     * 添加路径并编码
     *
     * @param path 路径，如 /a/b/c
     */
    @Override
    default T pathsEncoded(String path) {
        getUrlBuilder().addEncodedPathSegments(path);
        return self();
    }
}
