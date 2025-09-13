package cn.zs.tool.http.core.body.multipart;

import cn.zs.tool.http.core.MediaType;
import cn.zs.tool.http.core.constant.MediaTypeEnum;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 字符串 form data
 *
 * @author sheng
 */
public class StringMultipart extends BaseMultipart<StringMultipart> {
    /**
     * 字符串内容
     */
    private final byte[] content;

    public StringMultipart(String name, String text) {
        this(name, text, StandardCharsets.UTF_8);
    }

    public StringMultipart(String name, String text, Charset charset) {
        this(name, text, charset, MediaTypeEnum.TEXT_PLAIN.getMediaType());
    }

    public StringMultipart(String name, String text, Charset charset, MediaType contentType) {
        super(name, contentType);
        if (Objects.isNull(charset)) {
            charset = StandardCharsets.UTF_8;
        }
        this.content = Objects.requireNonNull(text, "text can not be null").getBytes(charset);
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

    @Override
    protected StringMultipart self() {
        return this;
    }
}
