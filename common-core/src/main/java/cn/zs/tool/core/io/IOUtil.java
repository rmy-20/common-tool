package cn.zs.tool.core.io;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public static byte[] readAllBytes(InputStream inputStream) {
        return readAllBytes(inputStream, byteArray());
    }

    /**
     * 读取流，转化为 byte[]
     *
     * @param inputStream 待读取的流
     * @param buffer      缓冲区
     */
    public static byte[] readAllBytes(InputStream inputStream, byte[] buffer) {
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        long length = copy(inputStream, bot, buffer, ex -> {
            throw ex;
        });
        return length >= 0 ? bot.toByteArray() : new byte[0];
    }

    /**
     * 获取一个默认的 byte[]
     */
    static byte[] byteArray() {
        return new byte[8192];
    }

    /**
     * 拷贝流，并返回拷贝的字节数，当有异常时将抛出异常
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @return 拷贝的字节数
     */
    public static long copy(InputStream inputStream, OutputStream outputStream) {
        return copy(inputStream, outputStream, ex -> {
            throw ex;
        });
    }

    /**
     * 拷贝流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param errHandler   异常处理器
     * @return 拷贝的字节数, -1 表示异常
     */
    public static long copy(InputStream inputStream, OutputStream outputStream, ThrowingConsumer<Throwable, ? extends Throwable> errHandler) {
        return copy(inputStream, outputStream, byteArray(), errHandler);
    }

    /**
     * 拷贝流，并返回拷贝的字节数
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param buffer       缓冲区
     * @param errHandler   异常处理器
     * @return 拷贝的字节数, -1 表示异常
     */
    @SuppressWarnings("unchecked")
    public static long copy(InputStream inputStream, OutputStream outputStream, byte[] buffer,
                            ThrowingConsumer<Throwable, ? extends Throwable> errHandler) {
        try {
            long totalCount = 0L;
            int readLen;
            while ((readLen = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readLen);
                totalCount += readLen;
            }
            outputStream.flush();
            return totalCount;
        } catch (Throwable ex) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errHandler).accept(ex);
        }
        return -1;
    }

    /**
     * 拷贝文件到输出流，并返回拷贝的字节数
     *
     * @param sourceFile   文件
     * @param outputStream 输出流
     * @return 拷贝的字节数, -1 表示异常
     */
    public static long copy(File sourceFile, OutputStream outputStream) {
        return copy(sourceFile, outputStream, ex -> {
            throw ex;
        });
    }

    /**
     * 拷贝文件到输出流，并返回拷贝的字节数
     *
     * @param sourceFile   文件
     * @param outputStream 输出流
     * @param errHandler   异常处理器
     * @return 拷贝的字节数, -1 表示异常
     */
    public static long copy(File sourceFile, OutputStream outputStream, ThrowingConsumer<Throwable, ? extends Throwable> errHandler) {
        return copy(sourceFile, outputStream, byteArray(), errHandler);
    }

    /**
     * 拷贝文件到输出流，并返回拷贝的字节数
     *
     * @param sourceFile   文件
     * @param outputStream 输出流
     * @param buffer       缓冲区
     * @param errHandler   异常处理器
     * @return 拷贝的字节数, -1 表示异常
     */
    @SuppressWarnings("unchecked")
    public static long copy(File sourceFile, OutputStream outputStream, byte[] buffer, ThrowingConsumer<Throwable, ? extends Throwable> errHandler) {
        if (sourceFile.exists()) {
            try (FileInputStream inputStream = new FileInputStream(sourceFile);) {
                return copy(inputStream, outputStream, buffer, errHandler);
            } catch (Throwable ex) {
                ((ThrowingConsumer<Throwable, RuntimeException>) errHandler).accept(ex);
            }
        }
        return -1;
    }

    /**
     * 拷贝流，并返回拷贝的字节数；如果目标文件所在文件夹不存在，则创建；有异常则抛出异常
     *
     * @param inputStream 输入流
     * @param targetFile  目标文件
     * @return 拷贝的字节数, -1 表示异常
     */
    public static long copy(InputStream inputStream, File targetFile) {
        return copy(inputStream, targetFile, ex -> {
            throw ex;
        });
    }

    /**
     * 拷贝流，并返回拷贝的字节数；如果目标文件所在文件夹不存在，则创建
     *
     * @param inputStream 输入流
     * @param targetFile  目标文件
     * @param errHandler  异常处理器
     * @return 拷贝的字节数, -1 表示异常
     */
    public static long copy(InputStream inputStream, File targetFile, ThrowingConsumer<Throwable, ? extends Throwable> errHandler) {
        return copy(inputStream, targetFile, byteArray(), errHandler);
    }

    /**
     * 拷贝流，并返回拷贝的字节数；如果目标文件所在文件夹不存在，则创建
     *
     * @param inputStream 输入流
     * @param targetFile  目标文件
     * @param buffer      缓冲区
     * @param errHandler  异常处理器
     * @return 拷贝的字节数, -1 表示异常
     */
    @SuppressWarnings("unchecked")
    public static long copy(InputStream inputStream, File targetFile, byte[] buffer, ThrowingConsumer<Throwable, ? extends Throwable> errHandler) {
        try {
            File parentFolder = targetFile.getParentFile();
            if (!parentFolder.exists() && !parentFolder.mkdirs()) {
                throw new IllegalArgumentException("Can't create directory : " + parentFolder);
            }

            try (FileOutputStream outputStream = new FileOutputStream(targetFile);) {
                return copy(inputStream, outputStream, buffer, errHandler);
            }
        } catch (Throwable ex) {
            ((ThrowingConsumer<Throwable, RuntimeException>) errHandler).accept(ex);
        }
        return -1;
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
    public static void closeQuietly(Closeable closeable, ThrowingConsumer<Throwable, ? extends Throwable> errHandler) {
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
