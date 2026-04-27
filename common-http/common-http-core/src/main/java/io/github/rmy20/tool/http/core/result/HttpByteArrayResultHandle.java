package io.github.rmy20.tool.http.core.result;

import io.github.rmy20.tool.core.io.IOUtil;

import java.io.InputStream;

/**
 * byte[]转换器
 *
 * @author sheng
 */
public class HttpByteArrayResultHandle implements HttpResultHandle<byte[]> {
    /**
     * 默认实例
     */
    public static final HttpByteArrayResultHandle INSTANCE = create();

    /**
     * 创建 {@link HttpByteArrayResultHandle}
     */
    public static HttpByteArrayResultHandle create() {
        return new HttpByteArrayResultHandle();
    }

    @Override
    public byte[] apply(InputStream inputStream) throws Throwable {
        return IOUtil.readAllBytes(inputStream);
    }
}
