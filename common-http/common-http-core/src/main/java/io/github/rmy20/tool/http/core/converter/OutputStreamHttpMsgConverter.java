package io.github.rmy20.tool.http.core.converter;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.io.IOUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 输出流转换器，true 为成功
 *
 * @author sheng
 */
public class OutputStreamHttpMsgConverter implements HttpMsgConverter<Boolean> {
    /**
     * 输出流
     */
    private final OutputStream outputStream;

    /**
     * 缓冲区
     */
    private final byte[] buffer;

    /**
     * 错误处理
     */
    private final ThrowingConsumer<Throwable, Throwable> errHandler;

    /**
     * 创建文件转换器
     *
     * @param outputStream 输出流
     */
    public static OutputStreamHttpMsgConverter create(OutputStream outputStream) {
        return new OutputStreamHttpMsgConverter(outputStream);
    }

    /**
     * 创建文件转换器
     *
     * @param outputStream 输出流
     * @param buffer       缓冲区
     * @param errHandler   错误处理
     */
    public static OutputStreamHttpMsgConverter create(OutputStream outputStream, byte[] buffer, ThrowingConsumer<Throwable, Throwable> errHandler) {
        return new OutputStreamHttpMsgConverter(outputStream, buffer, errHandler);
    }

    public OutputStreamHttpMsgConverter(OutputStream outputStream) {
        this(outputStream, null, null);
    }

    public OutputStreamHttpMsgConverter(OutputStream outputStream, byte[] buffer, ThrowingConsumer<Throwable, Throwable> errHandler) {
        this.outputStream = Objects.requireNonNull(outputStream, "targetFile must not be null");
        this.buffer = Objects.nonNull(buffer) ? buffer : new byte[2048];
        this.errHandler = Objects.nonNull(errHandler) ? errHandler : ex -> {
            throw ex;
        };
    }

    @Override
    public Boolean apply(InputStream inputStream) throws Throwable {
        return IOUtil.copy(inputStream, outputStream, buffer, errHandler) >= 0;
    }
}
