package cn.zs.tool.jackson;

import cn.zs.tool.core.date.DateConstants;
import cn.zs.tool.core.date.DateTool;
import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.Locale;
import java.util.Objects;

/**
 * Json 工具类
 *
 * @author sheng
 */
public class JsonTool implements JacksonTool {
    /**
     * Json 工具
     * <ol>
     *     <li>忽略无法识别的字段</li>
     *     <li>枚举输出成字符串</li>
     *     <li>时间格式为{@link DateTool#yyyy_MM_dd_HH_mm_ss}</li>
     *     <li>默认时区</li>
     *     <li>java8 时间模块</li>
     *     <li>大数格式化</li>
     * </ol>
     */
    public static final JsonTool JSON_TOOL = JsonTool.create(JsonMapper.builder()
            // 忽略无法识别的字段
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // 枚举输出成字符串
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            // 时间格式
            .defaultDateFormat(DateTool.yyyy_MM_dd_HH_mm_ss.getSimpleFormatter())
            .defaultTimeZone(DateConstants.GMT_8_TIME_ZONE)
            // java 8 时间模块
            .addModule(SimpleModuleConfig.JAVA_TIME_MODULE)
            .defaultLocale(Locale.CHINA)
            // 大数格式化
            .addModule(SimpleModuleConfig.BIG_NUM_MODULE)
            .findAndAddModules()
            .build());

    /**
     * 忽略值为null字段的 Json 工具
     * <ol>
     *     <li>忽略无法识别的字段</li>
     *     <li>枚举输出成字符串</li>
     *     <li>时间格式为{@link DateTool#yyyy_MM_dd_HH_mm_ss}</li>
     *     <li>默认时区</li>
     *     <li>java8 时间模块</li>
     *     <li>大数格式化</li>
     *     <li>序列化时忽略值为null的字段</li>
     * </ol>
     */
    public static final JsonTool IGNORE_NULL_JSON_TOOL = JsonTool.create(JsonMapper.builder()
            // 忽略无法识别的字段
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // 枚举输出成字符串
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            // 时间格式
            .defaultDateFormat(DateTool.yyyy_MM_dd_HH_mm_ss.getSimpleFormatter())
            .defaultTimeZone(DateConstants.GMT_8_TIME_ZONE)
            // java 8 时间模块
            .addModule(SimpleModuleConfig.JAVA_TIME_MODULE)
            .defaultLocale(Locale.CHINA)
            // 大数格式化
            .addModule(SimpleModuleConfig.BIG_NUM_MODULE)
            .findAndAddModules()
            .build()
            // 忽略值为null的字段
            .setSerializationInclusion(JsonInclude.Include.NON_NULL));

    /**
     * 未知字段抛出异常且忽略值为null字段的 Json 工具
     * <ol>
     *     <li>忽略无法识别的字段</li>
     *     <li>枚举输出成字符串</li>
     *     <li>时间格式为{@link DateTool#yyyy_MM_dd_HH_mm_ss}</li>
     *     <li>默认时区</li>
     *     <li>java8 时间模块</li>
     *     <li>大数格式化</li>
     *     <li>未知字段抛出异常</li>
     *     <li>序列化时忽略值为null的字段</li>
     * </ol>
     */
    public static final JsonTool FAIL_UNKNOWN_JSON_TOOL = JsonTool.create(JsonMapper.builder()
            // 忽略无法识别的字段
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            // 反序列化时未知字段抛出异常
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // 枚举输出成字符串
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            // 时间格式
            .defaultDateFormat(DateTool.yyyy_MM_dd_HH_mm_ss.getSimpleFormatter())
            .defaultTimeZone(DateConstants.GMT_8_TIME_ZONE)
            // java 8 时间模块
            .addModule(SimpleModuleConfig.JAVA_TIME_MODULE)
            .defaultLocale(Locale.CHINA)
            // 大数格式化
            .addModule(SimpleModuleConfig.BIG_NUM_MODULE)
            .findAndAddModules()
            .build()
            // 忽略值为null的字段
            .setSerializationInclusion(JsonInclude.Include.NON_NULL));

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
    public String toJson(Object o, ThrowingConsumer<Throwable, Throwable> errorHandler) {
        return writeValueAsString(o, errorHandler);
    }
}
