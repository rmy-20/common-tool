package io.github.rmy20.tool.http.core.result;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.rmy20.tool.jackson.JsonTool;

/**
 * json 转换器
 *
 * @author sheng
 */
public class HttpJsonResultHandle<R> extends HttpJacksonResultHandle<R> {
    /**
     * 创建json转换器
     *
     * @param type 结果类型
     */
    public static <R> HttpJsonResultHandle<R> create(JsonTool jsonTool, Class<R> type) {
        return new HttpJsonResultHandle<>(jsonTool, type);
    }

    /**
     * 创建json转换器
     *
     * @param typeReference 结果类型
     */
    public static <R> HttpJsonResultHandle<R> create(JsonTool jsonTool, TypeReference<R> typeReference) {
        return new HttpJsonResultHandle<>(jsonTool, typeReference);
    }

    public HttpJsonResultHandle(JsonTool jsonTool, Class<R> resultType) {
        super(jsonTool, resultType);
    }

    public HttpJsonResultHandle(JsonTool jsonTool, TypeReference<R> typeReference) {
        super(jsonTool, typeReference);
    }
}
