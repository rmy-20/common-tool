package io.github.rmy20.tool.http.core.body;

import io.github.rmy20.tool.core.io.IOUtil;
import io.github.rmy20.tool.http.core.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 输入流请求体
 *
 * @author sheng
 */
public class InputStreamBody extends Body {
    /**
     * 输入流
     */
    private final InputStream inputStream;

    /**
     * 创建#{@link InputStreamBody}
     */
    public static InputStreamBody create(InputStream inputStream) {
        return new InputStreamBody(inputStream);
    }

    /**
     * 创建#{@link InputStreamBody}
     */
    public static InputStreamBody create(InputStream inputStream, MediaType contentType) {
        return new InputStreamBody(inputStream, contentType);
    }

    public InputStreamBody(InputStream inputStream) {
        this(inputStream, MediaType.APPLICATION_OCTET_STREAM);
    }

    public InputStreamBody(InputStream inputStream, MediaType contentType) {
        super(contentType);
        this.inputStream = Objects.requireNonNull(inputStream, "inputStream must not be null");
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        IOUtil.copy(inputStream, outputStream);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
