package cn.zs.tool.http.core.converter;

import cn.zs.tool.jackson.JsonTool;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.Objects;

/**
 * json 转换器
 *
 * @author sheng
 */
public class JsonHttpMsgConverter<R> implements HttpMsgConverter<R> {
    /**
     * 结果类型
     */
    private final Class<R> resultType;

    /**
     * 结果泛型类型
     */
    private final TypeReference<R> typeReference;

    /**
     * 创建json转换器
     *
     * @param type 结果类型
     */
    public static <R> JsonHttpMsgConverter<R> create(Class<R> type) {
        return new JsonHttpMsgConverter<>(type);
    }

    /**
     * 创建json转换器
     *
     * @param typeReference 结果类型
     */
    public static <R> JsonHttpMsgConverter<R> create(TypeReference<R> typeReference) {
        return new JsonHttpMsgConverter<>(typeReference);
    }

    public JsonHttpMsgConverter(Class<R> resultType) {
        this.resultType = Objects.requireNonNull(resultType, "resultType must not be null");
        this.typeReference = null;
    }

    public JsonHttpMsgConverter(TypeReference<R> typeReference) {
        this.resultType = null;
        this.typeReference = Objects.requireNonNull(typeReference, "typeReference must not be null");
    }

    @Override
    public R apply(InputStream inputStream) throws Throwable {
        return Objects.nonNull(typeReference) ? JsonTool.JSON_TOOL.readValue(inputStream, typeReference)
                : JsonTool.JSON_TOOL.readValue(inputStream, resultType);
    }
}
