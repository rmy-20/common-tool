package cn.zs.tool.http.core.converter;

import cn.zs.tool.jackson.XmlTool;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * xml 消息转换器
 *
 * @author sheng
 */
public class XmlHttpMsgConverter<R> extends JacksonHttpMsgConverter<R> {
    /**
     * 创建json转换器
     *
     * @param type 结果类型
     */
    public static <R> XmlHttpMsgConverter<R> create(XmlTool xmlTool, Class<R> type) {
        return new XmlHttpMsgConverter<>(xmlTool, type);
    }

    /**
     * 创建{@link XmlHttpMsgConverter}
     */
    public static <R> XmlHttpMsgConverter<R> create(XmlTool xmlTool, TypeReference<R> typeReference) {
        return new XmlHttpMsgConverter<>(xmlTool, typeReference);
    }

    public XmlHttpMsgConverter(XmlTool xmlTool, Class<R> resultType) {
        super(xmlTool, resultType);
    }

    public XmlHttpMsgConverter(XmlTool xmlTool, TypeReference<R> typeReference) {
        super(xmlTool, typeReference);
    }
}
