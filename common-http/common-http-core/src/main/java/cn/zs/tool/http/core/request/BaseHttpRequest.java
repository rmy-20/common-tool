package cn.zs.tool.http.core.request;

import cn.zs.tool.http.core.MediaType;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 普通请求
 *
 * @author sheng
 */
public interface BaseHttpRequest<T extends BaseHttpRequest<T>> extends BaseRequest<T> {
    /**
     * 设置请求体，默认UTF-8编码
     */
    default T body(String body) {
        return body(body, getDefaultCharset());
    }

    /**
     * 设置请求体
     */
    T body(String body, Charset charset);

    /**
     * 设置请求体
     *
     * @param body      请求体
     * @param mediaType 请求体类型
     */
    default T body(byte[] body, MediaType mediaType) {
        return body(body, mediaType, getDefaultCharset());
    }

    /**
     * 设置请求体
     *
     * @param body      请求体
     * @param mediaType 请求体类型
     * @param charset   请求体编码
     */
    T body(byte[] body, MediaType mediaType, Charset charset);

    /**
     * 设置请求体
     *
     * @param body      请求体
     * @param mediaType 请求体类型
     */
    default T body(File body, MediaType mediaType) {
        return body(body, mediaType, getDefaultCharset());
    }

    /**
     * 设置请求体
     *
     * @param body      请求体
     * @param mediaType 请求体类型
     * @param charset   请求体编码
     */
    T body(File body, MediaType mediaType, Charset charset);
}
