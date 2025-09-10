package cn.zs.tool.http.core.body.multipart;

import java.io.IOException;
import java.io.OutputStream;

/**
 * multipart
 *
 * @author sheng
 */
public interface Multipart {
    /**
     * 文件名
     */
    String fileName();

    /**
     * 将当前媒体内容写到输出流中
     *
     * @param outputStream 输出流
     */
    void writeTo(OutputStream outputStream) throws IOException;
}
