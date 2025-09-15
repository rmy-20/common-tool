package cn.zs.tool.http.core.body.multipart;

import cn.zs.tool.core.io.IOUtil;
import cn.zs.tool.http.core.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 文件 form data
 *
 * @author sheng
 */
public class FileMultipart extends BaseMultipart<FileMultipart> {
    /**
     * 文件
     */
    private final File file;

    public FileMultipart(String name, File file) {
        this(name, file.getName(), file);
    }

    public FileMultipart(String name, File file, MediaType contentType) {
        this(name, file.getName(), file, contentType);
    }

    public FileMultipart(String name, String fileName, File file) {
        this(name, fileName, file, MediaType.APPLICATION_OCTET_STREAM);
    }

    public FileMultipart(String name, String fileName, File file, MediaType contentType) {
        super(name, contentType);
        this.file = file;
        this.fileName = fileName;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        IOUtil.copy(file, outputStream);
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public boolean repeatable() {
        return true;
    }

    @Override
    protected FileMultipart self() {
        return this;
    }
}
