package io.github.rmy20.tool.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import io.github.rmy20.tool.core.date.DateConstants;
import io.github.rmy20.tool.core.date.DateTool;
import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;

import java.io.InputStream;
import java.util.Locale;

/**
 * json 常量
 *
 * @author sheng
 */
public class XmlUtil {
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

    private static XmlTool defaultTool = XML_TOOL;

    /**
     * 将 ObjectMapper 对应可读取字符串转化为 Java Bean
     *
     * @param target json 字符串
     * @param cls    Java Bean 的字节码对象
     * @param <T>    泛型
     * @return Java Bean
     */
    public static <T> T readValue(String target, Class<T> cls) {
        return defaultTool().readValue(target, cls);
    }

    /**
     * 将 ObjectMapper 对应可读取字符串转化为 Java Bean
     *
     * @param target       json 字符串
     * @param cls          Java Bean 的字节码对象
     * @param errorHandler 异常处理
     * @param <T>          泛型
     * @return Java Bean
     */
    public static <T> T readValue(String target, Class<T> cls, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readValue(target, cls, errorHandler);
    }

    /**
     * <p>将 ObjectMapper 对应可读取字符串转化为 T 对象</p><br>
     * 栗子：
     * <ol>
     *     <li>Set<String> set = readValue("[\"1\"]", new TypeReference<Set<String>>() {});</li>
     *     <li>List<String> list = readValue("[\"1\"]", new TypeReference<List<String>>() {});</li>
     *     <li>Map<String, Object> map = readValue("{\"1\": \"a\"}", new TypeReference<Map<String, Object>>() {});</li>
     * </ol>
     *
     * @param target json
     * @param type   {@link TypeReference} new TypeReference<T>{}
     * @param <T>    泛型，转化的对象类型
     * @return T
     */
    public static <T> T readValue(String target, TypeReference<T> type) {
        return defaultTool().readValue(target, type);
    }

    /**
     * <p>将 ObjectMapper 对应可读取字符串转化为 T 对象</p><br>
     * 栗子：
     * <ol>
     *     <li>Set<String> set = readValue("[\"1\"]", new TypeReference<Set<String>>() {});</li>
     *     <li>List<String> list = readValue("[\"1\"]", new TypeReference<List<String>>() {});</li>
     *     <li>Map<String, Object> map = readValue("{\"1\": \"a\"}", new TypeReference<Map<String, Object>>() {});</li>
     * </ol>
     *
     * @param target       json
     * @param type         {@link TypeReference} new TypeReference<T>{}
     * @param errorHandler 异常处理
     * @param <T>          泛型，转化的对象类型
     * @return T
     */
    public static <T> T readValue(String target, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readValue(target, type, errorHandler);
    }

    /**
     * 将 ObjectMapper 对应可读取字节数组转化为 Java Bean
     *
     * @param target json 字节数组
     * @param cls    Java Bean 的字节码对象
     * @param <T>    泛型
     * @return Java Bean
     */
    public static <T> T readValue(byte[] target, Class<T> cls) {
        return defaultTool().readValue(target, cls);
    }

    /**
     * 将 ObjectMapper 对应可读取字节数组转化为 Java Bean
     *
     * @param target       json 字节数组
     * @param cls          Java Bean 的字节码对象
     * @param errorHandler 异常处理
     * @param <T>          泛型
     * @return Java Bean
     */
    public static <T> T readValue(byte[] target, Class<T> cls, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readValue(target, cls, errorHandler);
    }

    /**
     * 将 ObjectMapper 对应可读取字节数组反序列化为 JavaBean
     *
     * @param bytes {@link InputStream} json 文件输入流
     * @param type  {@link TypeReference } new TypeReference<T>{}
     */
    public static <T> T readValue(byte[] bytes, TypeReference<T> type) {
        return defaultTool().readValue(bytes, type);
    }

    /**
     * 将 ObjectMapper 对应可读取字节数组反序列化为 JavaBean
     *
     * @param bytes        {@link InputStream} json 文件输入流
     * @param type         {@link TypeReference } new TypeReference<T>{}
     * @param errorHandler 异常处理
     */
    public static <T> T readValue(byte[] bytes, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readValue(bytes, type, errorHandler);
    }

    /**
     * 将 ObjectMapper 对应可读取文件输入流反序列化为 JavaBean
     *
     * @param ins  {@link InputStream} json 文件输入流
     * @param type 类型
     */
    public static <T> T readValue(InputStream ins, Class<T> type) {
        return defaultTool().readValue(ins, type);
    }

    /**
     * 将 ObjectMapper 对应可读取文件输入流反序列化为 JavaBean
     *
     * @param ins          {@link InputStream} json 文件输入流
     * @param type         类型
     * @param errorHandler 异常处理
     */
    public static <T> T readValue(InputStream ins, Class<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readValue(ins, type, errorHandler);
    }

    /**
     * 将 ObjectMapper 对应可读取文件输入流反序列化为 JavaBean
     *
     * @param ins  {@link InputStream} json 文件输入流
     * @param type {@link TypeReference } new TypeReference<T>{}
     */
    public static <T> T readValue(InputStream ins, TypeReference<T> type) {
        return defaultTool().readValue(ins, type);
    }

    /**
     * 将 ObjectMapper 对应可读取文件输入流反序列化为 JavaBean
     *
     * @param ins          {@link InputStream} json 文件输入流
     * @param type         {@link TypeReference } new TypeReference<T>{}
     * @param errorHandler 异常处理
     */
    public static <T> T readValue(InputStream ins, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readValue(ins, type, errorHandler);
    }

    /**
     * 将 JsonNode 转化为 Java Bean
     *
     * @param target json
     * @param clazz  待转换的 Java Bean 类型
     */
    public static <T> T readValue(JsonNode target, Class<T> clazz) {
        return defaultTool().readValue(target, clazz);
    }

    /**
     * 将 JsonNode 转化为 Java Bean
     *
     * @param target       json
     * @param clazz        待转换的 Java Bean 类型
     * @param errorHandler 异常处理
     */
    public static <T> T readValue(JsonNode target, Class<T> clazz, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readValue(target, clazz, errorHandler);
    }

    /**
     * 将 JsonNode 转化为 Java Bean
     *
     * @param target json
     * @param type   {@link TypeReference} new TypeReference<T>{}
     */
    public static <T> T readValue(JsonNode target, TypeReference<T> type) {
        return defaultTool().readValue(target, type);
    }

    /**
     * 将 JsonNode 转化为 Java Bean
     *
     * @param target       json
     * @param type         {@link TypeReference} new TypeReference<T>{}
     * @param errorHandler 异常处理
     */
    public static <T> T readValue(JsonNode target, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readValue(target, type, errorHandler);
    }

    /**
     * 将 ObjectMapper 对应可读取字符串转化为 JsonNode 对象
     *
     * @param target json 字符串
     * @return {@link JsonNode}
     */
    public static JsonNode readTree(String target) {
        return defaultTool().readTree(target);
    }

    /**
     * 将 ObjectMapper 对应可读取字符串转化为 JsonNode 对象
     *
     * @param target       json 字符串
     * @param errorHandler 异常处理
     * @return {@link JsonNode}
     */
    public static JsonNode readTree(String target, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().readTree(target, errorHandler);
    }

    /**
     * 将 Java Bean 转化为 JsonNode
     *
     * @param obj Java Bean
     */
    public static JsonNode toJsonNode(Object obj) {
        return defaultTool().toJsonNode(obj);
    }

    /**
     * 将 Java Bean 转化为 JsonNode
     *
     * @param obj          Java Bean
     * @param errorHandler 异常处理
     */
    public static JsonNode toJsonNode(Object obj, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().toJsonNode(obj, errorHandler);
    }

    /**
     * 将 Java Bean obj 转化为 ObjectMapper 对应处理字符串
     *
     * @param obj Java Bean
     * @return Json 字符串
     */
    public static String toXml(Object obj) {
        return defaultTool().toXml(obj);
    }

    /**
     * 将 Java Bean obj 转化为 ObjectMapper 对应处理字符串
     *
     * @param obj          Java Bean
     * @param errorHandler 异常处理
     * @return Json 字符串
     */
    public static String writeValueAsString(Object obj, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().toXml(obj, errorHandler);
    }

    /**
     * 类型转换
     *
     * @param obj   Java Bean
     * @param clazz 目标类型
     */
    public static <T> T convertValue(Object obj, Class<T> clazz) {
        return defaultTool().convertValue(obj, clazz);
    }

    /**
     * 类型转换
     *
     * @param obj          Java Bean
     * @param clazz        目标类型
     * @param errorHandler 异常处理
     */
    public static <T> T convertValue(Object obj, Class<T> clazz, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().convertValue(obj, clazz, errorHandler);
    }

    /**
     * 类型转换
     *
     * @param obj  Java Bean
     * @param type 目标类型
     */
    public static <T> T convertValue(Object obj, TypeReference<T> type) {
        return defaultTool().convertValue(obj, type);
    }

    /**
     * 类型转换
     *
     * @param obj          Java Bean
     * @param type         目标类型
     * @param errorHandler 异常处理
     */
    public static <T> T convertValue(Object obj, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        return defaultTool().convertValue(obj, type, errorHandler);
    }

    private static XmlTool defaultTool() {
        return defaultTool;
    }

    public static void defaultTool(XmlTool defaultTool) {
        XmlUtil.defaultTool = defaultTool;
    }
}
