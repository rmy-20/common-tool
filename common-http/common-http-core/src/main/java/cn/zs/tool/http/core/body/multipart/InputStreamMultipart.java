package cn.zs.tool.http.core.body.multipart;

import cn.zs.tool.core.io.IOUtil;
import cn.zs.tool.http.core.MediaType;
import cn.zs.tool.http.core.constant.MediaTypeEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 文件流 form data
 *
 * @author sheng
 */
public class InputStreamMultipart extends BaseMultipart<InputStreamMultipart> {
    /**
     * 输入流
     */
    private final InputStream inputStream;

    public InputStreamMultipart(String name, InputStream inputStream) {
        this(name, null, inputStream);
    }

    public InputStreamMultipart(String name, String fileName, InputStream inputStream) {
        this(name, fileName, inputStream, MediaTypeEnum.APPLICATION_OCTET_STREAM.getMediaType());
    }

    public InputStreamMultipart(String name, String fileName, InputStream inputStream, MediaType contentType) {
        super(name, contentType);
        this.inputStream = Objects.requireNonNull(inputStream, "inputStream must not be null");
        this.fileName = fileName;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        IOUtil.copy(inputStream, outputStream);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    protected InputStreamMultipart self() {
        return this;
    }
}
