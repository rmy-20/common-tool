package io.github.rmy20.tool.jackson.v3;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

/**
 * xml 工具类
 *
 * @param mapper xml 处理器
 * @author sheng
 */
public record XmlTool(ObjectMapper mapper) implements JacksonTool {

    /**
     * 创建 XmlTool
     *
     * @param mapper {@link ObjectMapper}
     */
    public static XmlTool create(ObjectMapper mapper) {
        return new XmlTool(mapper);
    }

    public XmlTool(ObjectMapper mapper) {
        this.mapper = Objects.requireNonNull(mapper, "mapper require not null");
    }

    /**
     * 将 Java Bean 转化为 XML
     *
     * @param bean {@link Object}
     * @return XML 字符串
     */
    public String toXml(Object bean) {
        return writeValueAsString(bean, e -> log.error("bean [{}] to xml 异常", bean, e));
    }

    /**
     * 将 Java Bean 转化为 XML
     *
     * @param bean         {@link Object}
     * @param errorHandler 异常处理
     * @return XML 字符串
     */
    public String toXml(Object bean, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return writeValueAsString(bean, errorHandler);
    }
}
