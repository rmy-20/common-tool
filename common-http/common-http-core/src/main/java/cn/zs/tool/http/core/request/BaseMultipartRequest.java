package cn.zs.tool.http.core.request;

import java.io.File;
import java.io.InputStream;

/**
 * multipart form
 *
 * @author sheng
 */
public interface BaseMultipartRequest<T extends BaseMultipartRequest<T>> extends BaseRequest<T> {
    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    T addText(String name, String value);

    /**
     * 添加文件
     *
     * @param name key
     * @param file 文件
     */
    T addBinary(String name, File file);

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param file     文件
     */
    T addBinary(String name, String filename, File file);

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param bytes    文件
     */
    T addBinary(String name, String filename, byte[] bytes);

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param stream   文件流
     */
    T addBinary(String name, String filename, InputStream stream);
}
