package io.github.rmy20.tool.http.jdkclient.publisher;

import java.io.OutputStream;

/**
 * #{@link java.io.OutputStream}写入数据
 *
 * @author sheng
 */
@FunctionalInterface
public interface OutputStreamProducer {
    /**
     * 写入数据到 #{@link java.io.OutputStream}
     */
    void write(OutputStream outputStream) throws Throwable;
}
