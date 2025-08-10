package cn.zs.tool.http.core.converter;

import cn.zs.tool.jackson.JsonTool;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * json 转换器
 *
 * @author sheng
 */
public class JsonHttpMsgConverter<R> extends JacksonHttpMsgConverter<R> {
    /**
     * 创建json转换器
     *
     * @param type 结果类型
     */
    public static <R> JsonHttpMsgConverter<R> create(JsonTool jsonTool, Class<R> type) {
        return new JsonHttpMsgConverter<>(jsonTool, type);
    }

    /**
     * 创建json转换器
     *
     * @param typeReference 结果类型
     */
    public static <R> JsonHttpMsgConverter<R> create(JsonTool jsonTool, TypeReference<R> typeReference) {
        return new JsonHttpMsgConverter<>(jsonTool, typeReference);
    }

    public JsonHttpMsgConverter(JsonTool jsonTool, Class<R> resultType) {
        super(jsonTool, resultType);
    }

    public JsonHttpMsgConverter(JsonTool jsonTool, TypeReference<R> typeReference) {
        super(jsonTool, typeReference);
    }
}
