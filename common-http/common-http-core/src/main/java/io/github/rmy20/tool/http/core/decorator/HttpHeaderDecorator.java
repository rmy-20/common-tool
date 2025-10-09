package io.github.rmy20.tool.http.core.decorator;

import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.MediaType;

import java.util.List;
import java.util.Objects;

/**
 * http header 装饰器
 *
 * @author sheng
 */
public interface HttpHeaderDecorator<T extends HttpHeaderDecorator<T>> {
    /**
     * 获取当前实例
     */
    T self();

    /**
     * 获取请求头
     */
    HttpHeaders getHeaders();

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
     * 设置Content-Type
     */
    default T setContentType(String value) {
        getHeaders().setContentType(value);
        return self();
    }

    /**
     * 设置Content-Type
     */
    default T setContentType(MediaType value) {
        getHeaders().setContentType(value);
        return self();
    }

    /**
     * 设置Content-Length
     */
    default T setContentType(long contentLength) {
        getHeaders().setContentLength(contentLength);
        return self();
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
     */
    default T setAccept(List<String> value) {
        getHeaders().setAccept(value);
        return self();
    }

    /**
     * 设置Accept
     */
    default T setAccept(MediaType... value) {
        getHeaders().setAccept(value);
        return self();
    }

    /**
     * 移除请求头
     */
    default T removeHeader(String name) {
        getHeaders().remove(name);
        return self();
    }
}
