package cn.zs.tool.http.core.body;

import cn.zs.tool.core.io.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 文件请求体
 *
 * @author sheng
 */
public class FileBody implements Body {
    /**
     * 文件
     */
    private final File file;

    /**
     * 创建 #{@link FileBody}
     */
    public static FileBody create(File file) {
        return new FileBody(file);
    }

    public FileBody(File file) {
        this.file = Objects.requireNonNull(file, "file must not be null");
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
}
