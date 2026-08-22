package io.github.rmy20.tool.http.core.body;

import io.github.rmy20.tool.http.core.MediaType;
import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 请求体
 *
 * @author sheng
 */
public abstract class Body implements Closeable {
    /**
     * 空请求体
     */
    public static final Body NULL_BODY = new Body(MediaType.APPLICATION_OCTET_STREAM) {
        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
        }

        @Override
        public long contentLength() {
            return 0;
        }
    };

    /**
     * 请求内容类型
     */
    @Getter
    protected final MediaType contentType;

    public Body(MediaType contentType) {
        this.contentType = Objects.requireNonNull(contentType, "contentType must not be null");
    }

    /**
     * 将当前请求体内容写到输出流中
     *
     * @param outputStream 输出流
     */
    public abstract void writeTo(OutputStream outputStream) throws IOException;

    /**
     * 当前请求体内容是否可重复读
     */
    public boolean repeatable() {
        return false;
    }

    /**
     * 当前请求体内容长度
     */
    public long contentLength() {
        return -1;
    }

    @Override
    public void close() throws IOException {
    }
}
