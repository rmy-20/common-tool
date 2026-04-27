package io.github.rmy20.tool.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;

import java.util.Objects;

/**
 * Json 工具类
 *
 * @author sheng
 */
public class JsonTool implements JacksonTool {

    /**
     * json 处理器
     */
    private final ObjectMapper mapper;

    /**
     * 创建 JsonTool
     *
     * @param mapper {@link ObjectMapper}
     */
    public static JsonTool create(ObjectMapper mapper) {
        return new JsonTool(mapper);
    }

    public JsonTool(ObjectMapper mapper) {
        this.mapper = Objects.requireNonNull(mapper, "mapper require not null");
    }

    @Override
    public ObjectMapper mapper() {
        return mapper;
    }

    /**
     * 将 Java Bean o 转化为 字符串
     *
     * @param o Java Bean
     * @return Json 字符串
     */
    public String toJson(Object o) {
        return writeValueAsString(o, e -> log.error("bean [{}] to json 异常", o, e));
    }

    /**
     * 将 Java Bean o 转化为 字符串
     *
     * @param o            Java Bean
     * @param errorHandler 异常处理
     * @return Json 字符串
     */
    public String toJson(Object o, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return writeValueAsString(o, errorHandler);
    }
}
