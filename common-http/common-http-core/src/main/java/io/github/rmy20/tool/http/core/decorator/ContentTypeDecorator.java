package io.github.rmy20.tool.http.core.decorator;

import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.MediaType;

/**
 * content-type
 *
 * @author sheng
 */
public interface ContentTypeDecorator<T extends ContentTypeDecorator<T>> {
    /**
     * 获取当前实例
     */
    T self();

    /**
     * 获取请求头
     */
    HttpHeaders getHeaders();

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
     * 设置Content-Type为 application/json
     */
    default T jsonContentType() {
        return setContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * 设置Content-Type为 application/json;charset=UTF-8
     */
    default T jsonUtf8ContentType() {
        return setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * 设置Content-Type为 application/xml
     */
    default T xmlContentType() {
        return setContentType(MediaType.APPLICATION_XML);
    }

    /*
     * 设置Content-Type为 application/octet-stream
     */
    default T octetStreamContentType() {
        return setContentType(MediaType.APPLICATION_OCTET_STREAM);
    }
}
