package cn.zs.tool.http.core.body;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 字节数组请求体
 *
 * @author sheng
 */
public class ByteArrayBody implements Body {
    /**
     * 字节数组
     */
    private final byte[] byteArray;

    /**
     * 创建#{@link ByteArrayBody}
     */
    public static ByteArrayBody create(byte[] byteArray) {
        return new ByteArrayBody(byteArray);
    }

    public ByteArrayBody(byte[] byteArray) {
        this.byteArray = Objects.requireNonNull(byteArray, "byteArray must not be null");
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(byteArray);
        outputStream.flush();
    }

    @Override
    public long contentLength() {
        return byteArray.length;
    }

    @Override
    public boolean repeatable() {
        return true;
    }
}
