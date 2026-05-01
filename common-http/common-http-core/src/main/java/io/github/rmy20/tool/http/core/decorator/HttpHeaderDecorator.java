package io.github.rmy20.tool.http.core.decorator;

import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.http.core.MediaType;

import java.util.List;
import java.util.Objects;

/**
 * http header 装饰器
 *
 * @author sheng
 */
public interface HttpHeaderDecorator<T extends HttpHeaderDecorator<T>> extends ContentTypeDecorator<T> {
    /**
     * 添加请求头
     *
     * @param name  header
     * @param value value
     */
    default T addHeader(String name, Object value) {
        getHeaders().add(name, Objects.toString(value, StringPool.EMPTY));
        return self();
    }

    /**
     * 添加请求头
     *
     * @param condition 是否添加
     * @param name      header
     * @param value     value
     */
    default T addHeader(boolean condition, String name, Object value) {
        return condition ? addHeader(name, value) : self();
    }

    /**
     * 覆盖添加请求头
     *
     * @param name  header
     * @param value value
     */
    default T setHeader(String name, Object value) {
        getHeaders().set(name, Objects.toString(value, StringPool.EMPTY));
        return self();
    }

    /**
     * 覆盖添加请求头
     *
     * @param condition 是否添加
     * @param name      header
     * @param value     value
     */
    default T setHeader(boolean condition, String name, Object value) {
        return condition ? setHeader(name, value) : self();
    }

    /**
     * 设置Content-Length
     */
    default T setContentLength(long contentLength) {
        getHeaders().setContentLength(contentLength);
        return self();
    }

    /**
     * 设置Content-Length
     *
     * @param condition 是否设置
     */
    default T setContentLength(boolean condition, long contentLength) {
        return condition ? setContentLength(contentLength) : self();
    }

    /**
     * 设置Accept
     */
    default T setAccept(String... value) {
        getHeaders().setAccept(value);
        return self();
    }

    /**
     * 设置Accept
     *
     * @param condition 是否设置
     */
    default T setAccept(boolean condition, String... value) {
        return condition ? setAccept(value) : self();
    }

    /**
     * 设置Accept
     */
    default T setAccept(List<String> value) {
        getHeaders().setAccept(value);
        return self();
    }

    /**
     * 设置Accept
     *
     * @param condition 是否设置
     */
    default T setAccept(boolean condition, List<String> value) {
        return condition ? setAccept(value) : self();
    }

    /**
     * 设置Accept
     */
    default T setAccept(MediaType... value) {
        getHeaders().setAccept(value);
        return self();
    }

    /**
     * 设置Accept
     *
     * @param condition 是否设置
     */
    default T setAccept(boolean condition, MediaType... value) {
        return condition ? setAccept(value) : self();
    }

    /**
     * 移除请求头
     */
    default T removeHeader(String name) {
        getHeaders().remove(name);
        return self();
    }

    /**
     * 移除请求头
     *
     * @param condition 是否移除
     */
    default T removeHeader(boolean condition, String name) {
        return condition ? removeHeader(name) : self();
    }
}
