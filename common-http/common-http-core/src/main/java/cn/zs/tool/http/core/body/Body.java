package cn.zs.tool.http.core.body;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 请求体
 *
 * @author sheng
 */
@FunctionalInterface
public interface Body extends Closeable {
    /**
     * 空请求体
     */
    Body NULL_BODY = new Body() {
        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
        }

        @Override
        public long contentLength() {
            return 0;
        }
    };

    /**
     * 将当前请求体内容写到输出流中
     *
     * @param outputStream 输出流
     */
    void writeTo(OutputStream outputStream) throws IOException;

    /**
     * 当前请求体内容是否可重复读
     */
    default boolean repeatable() {
        return false;
    }

    /**
     * 当前请求体内容长度
     */
    default long contentLength() {
        return -1;
    }

    @Override
    default void close() throws IOException {
    }
}
