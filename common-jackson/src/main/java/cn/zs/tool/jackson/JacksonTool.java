package cn.zs.tool.jackson;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Jackson 工具类
 *
 * @author sheng
 */
@SuppressWarnings("unchecked")
public interface JacksonTool {
    Logger log = LoggerFactory.getLogger(JacksonTool.class);

    /**
     * 获取实际操作json、xml的 {@link ObjectMapper}实例
     */
    ObjectMapper mapper();

    /**
     * 将 ObjectMapper 对应可读取字符串转化为 Java Bean
     *
     * @param target json 字符串
     * @param cls    Java Bean 的字节码对象
     * @param <T>    泛型
     * @return Java Bean
     */
    default <T> T readValue(String target, Class<T> cls) {
        return readValue(target, cls, e -> log.error("[{}] to bean [{}] 异常", target, cls, e));
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
    default <T> T readValue(String target, Class<T> cls, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().readValue(target, cls);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
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
    default <T> T readValue(String target, TypeReference<T> type) {
        return readValue(target, type, e -> log.error("[{}] to bean 异常", target, e));
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
    default <T> T readValue(String target, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().readValue(target, type);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 将 ObjectMapper 对应可读取字节数组反序列化为 JavaBean
     *
     * @param bytes {@link InputStream} json 文件输入流
     * @param type  {@link TypeReference } new TypeReference<T>{}
     */
    default <T> T readValue(byte[] bytes, TypeReference<T> type) {
        return readValue(bytes, type, e -> log.error("byte[] to bean 异常", e));
    }

    /**
     * 将 ObjectMapper 对应可读取字节数组反序列化为 JavaBean
     *
     * @param bytes        {@link InputStream} json 文件输入流
     * @param type         {@link TypeReference } new TypeReference<T>{}
     * @param errorHandler 异常处理
     */
    default <T> T readValue(byte[] bytes, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().readValue(bytes, type);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 将 ObjectMapper 对应可读取文件输入流反序列化为 JavaBean
     *
     * @param ins  {@link InputStream} json 文件输入流
     * @param type 类型
     */
    default <T> T readValue(InputStream ins, Class<T> type) {
        return readValue(ins, type, e -> log.error("文件输入流 to bean错误", e));
    }

    /**
     * 将 ObjectMapper 对应可读取文件输入流反序列化为 JavaBean
     *
     * @param ins          {@link InputStream} json 文件输入流
     * @param type         类型
     * @param errorHandler 异常处理
     */
    default <T> T readValue(InputStream ins, Class<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().readValue(ins, type);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 将 ObjectMapper 对应可读取文件输入流反序列化为 JavaBean
     *
     * @param ins  {@link InputStream} json 文件输入流
     * @param type {@link TypeReference } new TypeReference<T>{}
     */
    default <T> T readValue(InputStream ins, TypeReference<T> type) {
        return readValue(ins, type, e -> log.error("文件输入流 to bean错误", e));
    }

    /**
     * 将 ObjectMapper 对应可读取文件输入流反序列化为 JavaBean
     *
     * @param ins          {@link InputStream} json 文件输入流
     * @param type         {@link TypeReference } new TypeReference<T>{}
     * @param errorHandler 异常处理
     */
    default <T> T readValue(InputStream ins, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().readValue(ins, type);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 将 JsonNode 转化为 Java Bean
     *
     * @param target json
     * @param type   {@link TypeReference} new TypeReference<T>{}
     */
    default <T> T readValue(JsonNode target, TypeReference<T> type) {
        return readValue(target, type, e -> log.error("JsonNode to bean 异常", e));
    }

    /**
     * 将 JsonNode 转化为 Java Bean
     *
     * @param target       json
     * @param type         {@link TypeReference} new TypeReference<T>{}
     * @param errorHandler 异常处理
     */
    default <T> T readValue(JsonNode target, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().treeToValue(target, type);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 将 JsonNode 转化为 Java Bean
     *
     * @param target json
     * @param clazz  待转换的 Java Bean 类型
     */
    default <T> T readValue(JsonNode target, Class<T> clazz) {
        return readValue(target, clazz, e -> log.error("JsonNode to bean 异常", e));
    }

    /**
     * 将 JsonNode 转化为 Java Bean
     *
     * @param target       json
     * @param clazz        待转换的 Java Bean 类型
     * @param errorHandler 异常处理
     */
    default <T> T readValue(JsonNode target, Class<T> clazz, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().treeToValue(target, clazz);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 将 ObjectMapper 对应可读取字符串转化为 JsonNode 对象
     *
     * @param target json 字符串
     * @return {@link JsonNode}
     */
    default JsonNode readTree(String target) {
        return readTree(target, e -> log.error("[{}] to JsonNode 异常", target, e));
    }

    /**
     * 将 ObjectMapper 对应可读取字符串转化为 JsonNode 对象
     *
     * @param target       json 字符串
     * @param errorHandler 异常处理
     * @return {@link JsonNode}
     */
    default JsonNode readTree(String target, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().readTree(target);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 将 Java Bean 转化为 JsonNode
     *
     * @param obj Java Bean
     */
    default JsonNode toJsonNode(Object obj) {
        return toJsonNode(obj, e -> log.error("bean [{}] JsonNode异常", obj, e));
    }

    /**
     * 将 Java Bean 转化为 JsonNode
     *
     * @param obj          Java Bean
     * @param errorHandler 异常处理
     */
    default JsonNode toJsonNode(Object obj, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().valueToTree(obj);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 将 Java Bean obj 转化为 ObjectMapper 对应处理字符串
     *
     * @param obj Java Bean
     * @return Json 字符串
     */
    default String writeValueAsString(Object obj) {
        return writeValueAsString(obj, e -> log.error("bean [{}] to 字符串异常", obj, e));
    }

    /**
     * 将 Java Bean obj 转化为 ObjectMapper 对应处理字符串
     *
     * @param obj          Java Bean
     * @param errorHandler 异常处理
     * @return Json 字符串
     */
    default String writeValueAsString(Object obj, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().writeValueAsString(obj);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 类型转换
     *
     * @param obj   Java Bean
     * @param clazz 目标类型
     */
    default <T> T convertValue(Object obj, Class<T> clazz) {
        return convertValue(obj, clazz, e -> log.error("jackson转换类型异常", e));
    }

    /**
     * 类型转换
     *
     * @param obj          Java Bean
     * @param clazz        目标类型
     * @param errorHandler 异常处理
     */
    default <T> T convertValue(Object obj, Class<T> clazz, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().convertValue(obj, clazz);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }

    /**
     * 类型转换
     *
     * @param obj  Java Bean
     * @param type 目标类型
     */
    default <T> T convertValue(Object obj, TypeReference<T> type) {
        return convertValue(obj, type, e -> log.error("jackson转换类型异常", e));
    }

    /**
     * 类型转换
     *
     * @param obj          Java Bean
     * @param type         目标类型
     * @param errorHandler 异常处理
     */
    default <T> T convertValue(Object obj, TypeReference<T> type, ThrowingConsumer<Throwable, ? extends Throwable> errorHandler) {
        try {
            return mapper().convertValue(obj, type);
        } catch (Exception e) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errorHandler).accept(e);
        }
        return null;
    }
}
