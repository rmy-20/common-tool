package cn.zs.tool.core.io;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * IO 工具类
 *
 * @author sheng
 */
@Slf4j
public class IOUtil {
    /**
     * 将字节缓存区转换为字节数组
     *
     * @param byteBuffer 字节缓冲区
     */
    public static byte[] toByteArray(final ByteBuffer byteBuffer) {
        final int remaining = byteBuffer.remaining();
        // 使用缓冲区
        if (byteBuffer.hasArray()) {
            final byte[] byteArray = byteBuffer.array();
            if (remaining == byteArray.length) {
                byteBuffer.position(remaining);
                return byteArray;
            }
        }

        final byte[] byteArray = new byte[remaining];
        byteBuffer.get(byteArray);
        return byteArray;
    }

    /**
     * 读取流，转化为 byte[]
     *
     * @param inputStream 待读取的流
     */
    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        return readAllBytes(inputStream, 1024);
    }

    /**
     * 读取流，转化为 byte[]
     *
     * @param inputStream 待读取的流
     * @param bufferSize  每次读取输入流缓冲区大小
     */
    public static byte[] readAllBytes(InputStream inputStream, int bufferSize) throws IOException {
        if (Objects.isNull(inputStream) || bufferSize <= 0) {
            return new byte[0];
        }
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            bot.write(buffer, 0, read);
        }
        return bot.toByteArray();
    }

    /**
     * 关闭流
     */
    public static void close(Closeable closeable) throws IOException {
        if (Objects.nonNull(closeable)) {
            closeable.close();
        }
    }

    /**
     * 关闭流
     */
    public static void close(Closeable... closes) throws IOException {
        if (Objects.nonNull(closes)) {
            for (Closeable closeable : closes) {
                close(closeable);
            }
        }
    }

    /**
     * 静默关闭流
     *
     * @param closeable 待关闭流
     */
    public static void closeQuietly(Closeable closeable) {
        closeQuietly(closeable, null);
    }

    /**
     * 静默关闭流
     *
     * @param closeable  待关闭流
     * @param errHandler 异常处理器
     */
    @SuppressWarnings("unchecked")
    public static void closeQuietly(Closeable closeable, ThrowingConsumer<Throwable, ?> errHandler) {
        if (Objects.nonNull(closeable)) {
            try {
                closeable.close();
            } catch (Exception e) {
                if (Objects.nonNull(errHandler)) {
                    ((ThrowingConsumer<Throwable, RuntimeException>) errHandler).accept(e);
                }
            }
        }
    }

    /**
     * 静默关闭流
     */
    public static void closeQuietly(Closeable... closes) {
        if (Objects.nonNull(closes)) {
            for (Closeable closeable : closes) {
                closeQuietly(closeable);
            }
        }
    }
}
