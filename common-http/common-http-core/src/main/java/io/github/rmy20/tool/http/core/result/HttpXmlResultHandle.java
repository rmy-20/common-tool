package io.github.rmy20.tool.http.core.result;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.rmy20.tool.jackson.XmlTool;

/**
 * xml 结果处理器
 *
 * @author sheng
 */
public class HttpXmlResultHandle<R> extends HttpJacksonResultHandle<R> {
    /**
     * 创建json转换器
     *
     * @param type 结果类型
     */
    public static <R> HttpXmlResultHandle<R> create(XmlTool xmlTool, Class<R> type) {
        return new HttpXmlResultHandle<>(xmlTool, type);
    }

    /**
     * 创建{@link HttpXmlResultHandle}
     */
    public static <R> HttpXmlResultHandle<R> create(XmlTool xmlTool, TypeReference<R> typeReference) {
        return new HttpXmlResultHandle<>(xmlTool, typeReference);
    }

    public HttpXmlResultHandle(XmlTool xmlTool, Class<R> resultType) {
        super(xmlTool, resultType);
    }

    public HttpXmlResultHandle(XmlTool xmlTool, TypeReference<R> typeReference) {
        super(xmlTool, typeReference);
    }
}
