package cn.zs.tool.http.core.body;

import cn.zs.tool.core.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 输入流请求体
 *
 * @author sheng
 */
public class InputStreamBody implements Body {
    /**
     * 输入流
     */
    private final InputStream inputStream;

    public InputStreamBody(InputStream inputStream) {
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
