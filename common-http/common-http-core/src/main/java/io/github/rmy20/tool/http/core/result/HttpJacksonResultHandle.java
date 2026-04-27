package io.github.rmy20.tool.http.core.result;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.rmy20.tool.jackson.JacksonTool;

import java.io.InputStream;
import java.util.Objects;

/**
 * Jackson 转换器
 *
 * @author sheng
 */
public class HttpJacksonResultHandle<R> implements HttpResultHandle<R> {
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
    public static <R> HttpJacksonResultHandle<R> create(JacksonTool jacksonTool, Class<R> type) {
        return new HttpJacksonResultHandle<>(jacksonTool, type);
    }

    /**
     * 创建json转换器
     *
     * @param typeReference 结果类型
     */
    public static <R> HttpJacksonResultHandle<R> create(JacksonTool jacksonTool, TypeReference<R> typeReference) {
        return new HttpJacksonResultHandle<>(jacksonTool, typeReference);
    }

    public HttpJacksonResultHandle(JacksonTool jacksonTool, Class<R> resultType) {
        this.jacksonTool = Objects.requireNonNull(jacksonTool, "jacksonTool must not be null");
        this.resultType = Objects.requireNonNull(resultType, "resultType must not be null");
        this.typeReference = null;
    }

    public HttpJacksonResultHandle(JacksonTool jacksonTool, TypeReference<R> typeReference) {
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
