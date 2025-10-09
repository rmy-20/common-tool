package io.github.rmy20.tool.http.core.converter;

import io.github.rmy20.tool.core.io.IOUtil;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 字符串转换器
 *
 * @author sheng
 */
public class StringHttpMsgConverter implements HttpMsgConverter<String> {
    /**
     * UTF-8
     */
    public static final StringHttpMsgConverter UTF_8_INSTANCE = create(StandardCharsets.UTF_8);

    /**
     * 字符编码
     */
    private final Charset charset;

    /**
     * 创建{@link StringHttpMsgConverter}
     *
     * @param charset 字符编码
     */
    public static StringHttpMsgConverter create(Charset charset) {
        return new StringHttpMsgConverter(charset);
    }

    public StringHttpMsgConverter(Charset charset) {
        this.charset = Objects.requireNonNull(charset, "charset must not be null");
    }

    @Override
    public String apply(InputStream inputStream) throws Throwable {
        return new String(IOUtil.readAllBytes(inputStream), charset);
    }
}
