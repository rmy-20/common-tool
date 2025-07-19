package cn.zs.tool.http.core.converter;

import cn.zs.tool.jackson.XmlTool;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.Objects;

/**
 * xml 消息转换器
 *
 * @author sheng
 */
public class XmlHttpMsgConverter<R> implements HttpMsgConverter<R> {
    /**
     * 结果类型
     */
    private final Class<R> resultType;

    /**
     * 类型引用
     */
    private final TypeReference<R> typeReference;

    /**
     * 创建json转换器
     *
     * @param type 结果类型
     */
    public static <R> XmlHttpMsgConverter<R> create(Class<R> type) {
        return new XmlHttpMsgConverter<>(type);
    }

    /**
     * 创建{@link XmlHttpMsgConverter}
     */
    public static <R> XmlHttpMsgConverter<R> create(TypeReference<R> typeReference) {
        return new XmlHttpMsgConverter<>(typeReference);
    }

    public XmlHttpMsgConverter(Class<R> resultType) {
        this.resultType = Objects.requireNonNull(resultType, "resultType must not be null");
        this.typeReference = null;
    }

    public XmlHttpMsgConverter(TypeReference<R> typeReference) {
        this.typeReference = Objects.requireNonNull(typeReference, "typeReference must not be null");
        this.resultType = null;
    }

    @Override
    public R apply(InputStream inputStream) throws Throwable {
        return XmlTool.XML_TOOL.readValue(inputStream, typeReference);
    }
}
