package cn.zs.tool.jackson;

import cn.zs.tool.core.date.DateConstants;
import cn.zs.tool.core.date.DateTool;
import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.util.Locale;
import java.util.Objects;

/**
 * xml 工具类
 *
 * @author sheng
 */
public class XmlTool implements JacksonTool {
    /**
     * 默认的 xml 工具
     * <ol>
     *     <li>忽略无法识别的字段</li>
     *     <li>枚举输出成字符串</li>
     *     <li>时间格式为{@link DateTool#yyyy_MM_dd_HH_mm_ss}</li>
     *     <li>默认时区</li>
     *     <li>java8 时间模块</li>
     * </ol>
     */
    public static final XmlTool XML_TOOL = XmlTool.create(XmlMapper.builder()
            // 忽略无法识别的字段
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // 枚举输出成字符串
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            // 时间格式
            .defaultDateFormat(DateTool.yyyy_MM_dd_HH_mm_ss.getSimpleFormatter())
            // java 8 时间模块
            .addModule(SimpleModuleConfig.JAVA_TIME_MODULE)
            .defaultLocale(Locale.CHINA)
            .defaultTimeZone(DateConstants.GMT_8_TIME_ZONE)
            .findAndAddModules()
            .build());

    /**
     * xml 工具
     * <ol>
     *     <li>忽略无法识别的字段</li>
     *     <li>枚举输出成字符串</li>
     *     <li>时间格式为{@link DateTool#yyyy_MM_dd_HH_mm_ss}</li>
     *     <li>默认时区</li>
     *     <li>java8 时间模块</li>
     *     <li>序列化时加上头信息：<?xml version='1.0' encoding='UTF-8'?></li>
     * </ol>
     */
    public static final XmlTool DECLARATION_XML_TOOL = XmlTool.create(XmlMapper.builder()
            // 忽略无法识别的字段
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // 枚举输出成字符串
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            // 序列化时加上头信息：<?xml version='1.0' encoding='UTF-8'?>
            .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
            // 时间格式
            .defaultDateFormat(DateTool.yyyy_MM_dd_HH_mm_ss.getSimpleFormatter())
            // java 8 时间模块
            .addModule(SimpleModuleConfig.JAVA_TIME_MODULE)
            .defaultLocale(Locale.CHINA)
            .defaultTimeZone(DateConstants.GMT_8_TIME_ZONE)
            .findAndAddModules()
            .build());

    /**
     * xml 处理器
     */
    private final ObjectMapper mapper;

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

    @Override
    public ObjectMapper mapper() {
        return mapper;
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
    public String toXml(Object bean, ThrowingConsumer<Throwable, Throwable> errorHandler) {
        return writeValueAsString(bean, errorHandler);
    }
}
