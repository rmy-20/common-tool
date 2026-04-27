package io.github.rmy20.tool.http.core.result;

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
public class HttpStringResultHandle implements HttpResultHandle<String> {
    /**
     * UTF-8
     */
    public static final HttpStringResultHandle UTF_8_INSTANCE = create(StandardCharsets.UTF_8);

    /**
     * 字符编码
     */
    private final Charset charset;

    /**
     * 创建{@link HttpStringResultHandle}
     *
     * @param charset 字符编码
     */
    public static HttpStringResultHandle create(Charset charset) {
        return new HttpStringResultHandle(charset);
    }

    public HttpStringResultHandle(Charset charset) {
        this.charset = Objects.requireNonNull(charset, "charset must not be null");
    }

    @Override
    public String apply(InputStream inputStream) throws Throwable {
        return new String(IOUtil.readAllBytes(inputStream), charset);
    }
}
