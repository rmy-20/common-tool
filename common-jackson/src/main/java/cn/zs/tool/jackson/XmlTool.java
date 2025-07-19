package cn.zs.tool.jackson;

import cn.zs.tool.core.date.DateConstants;
import cn.zs.tool.core.date.DateTool;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

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
     * 错误处理器
     */
    private final Consumer<Throwable> errorHandler;

    /**
     * 创建 XmlTool
     *
     * @param mapper {@link ObjectMapper}
     */
    public static XmlTool create(ObjectMapper mapper) {
        return new XmlTool(mapper, null);
    }

    /**
     * 创建 XmlTool
     *
     * @param mapper       {@link ObjectMapper}
     * @param errorHandler 错误处理器
     */
    public static XmlTool create(ObjectMapper mapper, Consumer<Throwable> errorHandler) {
        return new XmlTool(mapper, errorHandler);
    }

    public XmlTool(ObjectMapper mapper, Consumer<Throwable> errorHandler) {
        this.mapper = Objects.requireNonNull(mapper, "ObjectMapper require not null");
        this.errorHandler = Objects.nonNull(errorHandler) ? errorHandler : ERROR_HANDLER;
    }

    @Override
    public ObjectMapper mapper() {
        return mapper;
    }

    @Override
    public Consumer<Throwable> errorHandler() {
        return errorHandler;
    }

    /**
     * 将 Java Bean 转化为 XML
     *
     * @param bean {@link Object}
     * @return XML 字符串
     */
    public String toXml(Object bean) {
        return writeValueAsString(bean);
    }
}
