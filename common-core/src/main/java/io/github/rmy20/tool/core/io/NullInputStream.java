package io.github.rmy20.tool.core.io;

import io.github.rmy20.tool.core.lang.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * 空流
 *
 * @author sheng
 */
public class NullInputStream extends InputStream {
    private volatile boolean closed;

    /**
     * 创建空流
     */
    public static NullInputStream create() {
        return new NullInputStream();
    }

    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }

    @Override
    public int available() throws IOException {
        ensureOpen();
        return 0;
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Assert.nonNull(b, "读取空流数组不能为空");
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException("无效数组访问范围");
        }
        if (len == 0) {
            return 0;
        }
        ensureOpen();
        return -1;
    }

    @Override
    public long skip(long n) throws IOException {
        ensureOpen();
        return 0L;
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }
}
