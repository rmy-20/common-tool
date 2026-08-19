package io.github.rmy20.tool.http.jdkclient.publisher;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 缓冲流
 *
 * @author sheng
 */
public abstract class AbstractChunkedOutputStream extends OutputStream {
    /**
     * 缓冲区大小
     */
    protected final int chunkSize;

    /**
     * 缓冲区
     */
    protected final byte[] buffer;

    /**
     * 缓冲区写入位置
     */
    protected int position;

    /**
     * 标记流关闭
     */
    protected volatile boolean closed;

    public AbstractChunkedOutputStream(int chunkSize) {
        if (chunkSize < 1024) {
            throw new IllegalArgumentException("chunkSize must be greater than 1024");
        }
        this.chunkSize = chunkSize;
        this.buffer = new byte[chunkSize];
    }

    @Override
    public void write(int b) throws IOException {
        ensureOpen();
        if (position >= chunkSize) {
            flushBuffer();
        }
        buffer[position++] = (byte) b;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        ensureOpen();
        int remaining = len;
        int actualOff = off;
        while (remaining > 0) {
            int chunkSpace = chunkSize - position;
            if (chunkSpace < 1) {
                flushBuffer();
                chunkSpace = chunkSize;
            }
            int copyLen = Math.min(chunkSpace, remaining);
            System.arraycopy(b, actualOff, buffer, position, copyLen);
            position += copyLen;
            actualOff += copyLen;
            remaining -= copyLen;
        }
    }

    @Override
    public void flush() throws IOException {
        ensureOpen();
        flushBuffer();
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        try {
            flushBuffer();
        } finally {
            closed = true;
            onClose();
        }
    }

    /**
     * 检查流是否可写，若不可写则抛异常
     */
    protected abstract void ensureOpen() throws IOException;

    /**
     * 将缓冲区写出
     */
    protected abstract void flushBuffer() throws IOException;

    /**
     * 关闭流
     */
    protected void onClose() throws IOException {
    }
}
