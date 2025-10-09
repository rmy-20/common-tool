package io.github.rmy20.tool.http.core.body.multipart;

import io.github.rmy20.tool.http.core.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 字节数组 form data
 *
 * @author sheng
 */
public class ByteArrayMultipart extends BaseMultipart<ByteArrayMultipart> {
    /**
     * 字节数组
     */
    private final byte[] content;

    public ByteArrayMultipart(String name, byte[] content) {
        this(name, null, content);
    }

    public ByteArrayMultipart(String name, String fileName, byte[] content) {
        this(name, fileName, content, MediaType.APPLICATION_OCTET_STREAM);
    }

    public ByteArrayMultipart(String name, String fileName, byte[] content, MediaType contentType) {
        super(name, contentType);
        this.content = Objects.requireNonNull(content, "content can not be null");
        this.fileName = fileName;
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
    protected ByteArrayMultipart self() {
        return this;
    }
}
