package cn.zs.tool.http.core;

import cn.zs.tool.http.core.constant.HttpConstant;

import java.io.Closeable;
import java.io.InputStream;

/**
 * http 响应
 *
 * @author sheng
 */
public interface ClientHttpResponse extends Closeable {
    /**
     * 获取状态码
     */
    int getStatus();

    /**
     * 判断是否成功响应
     */
    default boolean isOk() {
        return HttpConstant.HTTP_OK_PREDICATE.test(getStatus());
    }

    /**
     * 获取响应信息
     */
    String getMessage();

    /**
     * 获取响应头
     */
    HttpHeaders getHeaders();

    /**
     * 获取响应体
     */
    InputStream getBody() throws Exception;
}
