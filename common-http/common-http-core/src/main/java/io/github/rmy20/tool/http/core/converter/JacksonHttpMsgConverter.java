package io.github.rmy20.tool.http.core.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.rmy20.tool.jackson.JacksonTool;

import java.io.InputStream;
import java.util.Objects;

/**
 * Jackson 转换器
 *
 * @author sheng
 */
public class JacksonHttpMsgConverter<R> implements HttpMsgConverter<R> {
    /**
     * 结果类型
     */
    protected final Class<R> resultType;

    /**
     * 结果泛型类型
     */
    protected final TypeReference<R> typeReference;

    /**
     * JacksonTool
     */
    protected final JacksonTool jacksonTool;

    /**
     * 创建json转换器
     *
     * @param type 结果类型
     */
    public static <R> JacksonHttpMsgConverter<R> create(JacksonTool jacksonTool, Class<R> type) {
        return new JacksonHttpMsgConverter<>(jacksonTool, type);
    }

    /**
     * 创建json转换器
     *
     * @param typeReference 结果类型
     */
    public static <R> JacksonHttpMsgConverter<R> create(JacksonTool jacksonTool, TypeReference<R> typeReference) {
        return new JacksonHttpMsgConverter<>(jacksonTool, typeReference);
    }

    public JacksonHttpMsgConverter(JacksonTool jacksonTool, Class<R> resultType) {
        this.jacksonTool = Objects.requireNonNull(jacksonTool, "jacksonTool must not be null");
        this.resultType = Objects.requireNonNull(resultType, "resultType must not be null");
        this.typeReference = null;
    }

    public JacksonHttpMsgConverter(JacksonTool jacksonTool, TypeReference<R> typeReference) {
        this.jacksonTool = Objects.requireNonNull(jacksonTool, "jacksonTool must not be null");
        this.typeReference = Objects.requireNonNull(typeReference, "typeReference must not be null");
        this.resultType = null;
    }

    @Override
    public R apply(InputStream inputStream) throws Throwable {
        return Objects.nonNull(typeReference) ? jacksonTool.readValue(inputStream, typeReference)
                : jacksonTool.readValue(inputStream, resultType);
    }
}
