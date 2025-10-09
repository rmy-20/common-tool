package io.github.rmy20.tool.http.core.converter;

import io.github.rmy20.tool.core.function.throwing.ThrowingConsumer;
import io.github.rmy20.tool.core.io.IOUtil;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * 文件转换器，true 为成功
 *
 * @author sheng
 */
public class FileHttpMsgConverter implements HttpMsgConverter<Boolean> {
    /**
     * 目标文件
     */
    private final File targetFile;

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
     * @param targetFile 目标文件
     */
    public static FileHttpMsgConverter create(File targetFile) {
        return new FileHttpMsgConverter(targetFile);
    }

    /**
     * 创建文件转换器
     *
     * @param targetFile 目标文件
     * @param buffer     缓冲区
     * @param errHandler 错误处理
     */
    public static FileHttpMsgConverter create(File targetFile, byte[] buffer, ThrowingConsumer<Throwable, Throwable> errHandler) {
        return new FileHttpMsgConverter(targetFile, buffer, errHandler);
    }

    public FileHttpMsgConverter(File targetFile) {
        this(targetFile, null, null);
    }

    public FileHttpMsgConverter(File targetFile, byte[] buffer, ThrowingConsumer<Throwable, Throwable> errHandler) {
        this.targetFile = Objects.requireNonNull(targetFile, "targetFile must not be null");
        this.buffer = Objects.nonNull(buffer) ? buffer : new byte[2048];
        this.errHandler = Objects.nonNull(errHandler) ? errHandler : ex -> {
            throw ex;
        };
    }

    @Override
    public Boolean apply(InputStream inputStream) throws Throwable {
        return IOUtil.copy(inputStream, targetFile, buffer, errHandler) >= 0;
    }
}
