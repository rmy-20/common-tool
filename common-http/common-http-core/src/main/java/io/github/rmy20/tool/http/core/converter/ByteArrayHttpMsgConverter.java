package io.github.rmy20.tool.http.core.converter;

import io.github.rmy20.tool.core.io.IOUtil;

import java.io.InputStream;

/**
 * byte[]转换器
 *
 * @author sheng
 */
public class ByteArrayHttpMsgConverter implements HttpMsgConverter<byte[]> {
    /**
     * 默认实例
     */
    public static final ByteArrayHttpMsgConverter INSTANCE = create();

    /**
     * 创建 {@link ByteArrayHttpMsgConverter}
     */
    public static ByteArrayHttpMsgConverter create() {
        return new ByteArrayHttpMsgConverter();
    }

    @Override
    public byte[] apply(InputStream inputStream) throws Throwable {
        return IOUtil.readAllBytes(inputStream);
    }
}
