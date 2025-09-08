package cn.zs.tool.http.core.request;

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
}
