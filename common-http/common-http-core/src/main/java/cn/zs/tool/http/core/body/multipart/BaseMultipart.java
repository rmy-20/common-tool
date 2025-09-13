package cn.zs.tool.http.core.body.multipart;

import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.MediaType;
import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * multipart
 *
 * @author sheng
 */
@Getter
public abstract class BaseMultipart<T extends BaseMultipart<T>> implements Closeable {
    /**
     * 内容描述
     */
    protected String contentDisposition = "form-data";

    /**
     * 字段名称
     */
    protected final String name;

    /**
     * 文件名
     */
    protected String fileName;

    /**
     * 媒体类型
     */
    protected final MediaType contentType;

    public BaseMultipart(String name, MediaType contentType) {
        Assert.isTrue(StringUtil.isNotBlank(name), "name must not be blank");
        this.name = name;
        this.contentType = Objects.requireNonNull(contentType, "contentType must not be null");
    }

    /**
     * 将当前媒体内容写到输出流中
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

    /**
     * 获取当前实例
     */
    protected abstract T self();

    /**
     * 设置内容描述
     */
    public T contentDisposition(String contentDisposition) {
        Assert.isTrue(StringUtil.isNotBlank(contentDisposition), "contentDisposition must not be blank");
        this.contentDisposition = contentDisposition;
        return self();
    }

    /**
     * 设置文件名
     */
    public T fileName(String fileName) {
        this.fileName = fileName;
        return self();
    }
}
