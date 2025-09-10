package cn.zs.tool.http.core.body;

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
public class StringBody implements Body {
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

    public StringBody(String content) {
        this(content, StandardCharsets.UTF_8);
    }

    public StringBody(String content, Charset charset) {
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
