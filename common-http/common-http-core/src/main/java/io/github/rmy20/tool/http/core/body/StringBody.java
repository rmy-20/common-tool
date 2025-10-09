package io.github.rmy20.tool.http.core.body;

import io.github.rmy20.tool.http.core.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 字符串请求体
 *
 * @author sheng
 */
public class StringBody extends Body {
    /**
     * 字符串内容
     */
    private final byte[] content;

    /**
     * 创建#{@link StringBody}
     *
     * @param content 字符串
     */
    public static StringBody create(String content) {
        return new StringBody(content);
    }

    /**
     * 创建#{@link StringBody}
     *
     * @param content 字符串
     * @param charset 字符集
     */
    public static StringBody create(String content, Charset charset) {
        return new StringBody(content, charset);
    }

    /**
     * 创建#{@link StringBody}
     *
     * @param content     字符串
     * @param contentType 内容类型
     */
    public static StringBody create(String content, MediaType contentType) {
        return new StringBody(content, contentType);
    }

    /**
     * 创建#{@link StringBody}
     *
     * @param content     字符串
     * @param contentType 内容类型
     */
    public static StringBody create(String content, Charset charset, MediaType contentType) {
        return new StringBody(content, charset, contentType);
    }

    public StringBody(String content) {
        this(content, MediaType.TEXT_PLAIN);
    }

    public StringBody(String content, Charset charset) {
        this(content, charset, MediaType.TEXT_PLAIN);
    }

    public StringBody(String content, MediaType contentType) {
        this(content, StandardCharsets.UTF_8, contentType);
    }

    public StringBody(String content, Charset charset, MediaType contentType) {
        super(contentType);
        if (Objects.isNull(charset)) {
            charset = StandardCharsets.UTF_8;
        }
        this.content = Objects.requireNonNull(content, "content must not be null").getBytes(charset);
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(content);
        outputStream.flush();
    }

    @Override
    public long contentLength() {
        return content.length;
    }

    @Override
    public boolean repeatable() {
        return true;
    }
}
