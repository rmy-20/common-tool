package io.github.rmy20.tool.urlconnection.request;

import io.github.rmy20.tool.http.core.body.MultipartFormBody;
import io.github.rmy20.tool.http.core.body.multipart.BaseMultipart;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseMultipartRequest;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * url connection multipart request
 *
 * @author sheng
 */
public class UrlConnectionMultipartRequest extends UrlConnectionBaseRequest<UrlConnectionMultipartRequest>
        implements BaseMultipartRequest<UrlConnectionMultipartRequest> {
    /**
     * 表单数据
     */
    private final MultipartFormBody formBody;

    /**
     * 创建#{@link UrlConnectionMultipartRequest}
     */
    public static UrlConnectionMultipartRequest create(String url, HttpMethodEnum method) {
        return new UrlConnectionMultipartRequest(url, method);
    }

    /**
     * 创建#{@link UrlConnectionMultipartRequest}
     */
    public static UrlConnectionMultipartRequest create(String url, HttpMethodEnum method, MultipartFormBody formBody) {
        return new UrlConnectionMultipartRequest(url, method, formBody);
    }

    public UrlConnectionMultipartRequest(String url, HttpMethodEnum method) {
        this(url, method, MultipartFormBody.create());
    }

    public UrlConnectionMultipartRequest(String url, HttpMethodEnum method, MultipartFormBody formBody) {
        super(url, method);
        this.formBody = Objects.requireNonNull(formBody, "formBody must not be null");
        super.body = this.formBody;
    }

    @Override
    public UrlConnectionMultipartRequest addText(String name, String value) {
        formBody.addText(name, value);
        return this;
    }

    /**
     * 添加文本
     *
     * @param name    key
     * @param value   value
     * @param charset 文本编码
     */
    public UrlConnectionMultipartRequest addText(String name, String value, Charset charset) {
        formBody.addText(name, value, charset);
        return this;
    }

    @Override
    public UrlConnectionMultipartRequest addBinary(String name, File file) {
        formBody.addBinary(name, file);
        return this;
    }

    @Override
    public UrlConnectionMultipartRequest addBinary(String name, String filename, File file) {
        formBody.addBinary(name, filename, file);
        return this;
    }

    @Override
    public UrlConnectionMultipartRequest addBinary(String name, String filename, byte[] bytes) {
        formBody.addBinary(name, filename, bytes);
        return this;
    }

    @Override
    public UrlConnectionMultipartRequest addBinary(String name, String filename, InputStream stream) {
        formBody.addBinary(name, filename, stream);
        return this;
    }

    /**
     * 添加 multipart part
     */
    public UrlConnectionMultipartRequest addPart(BaseMultipart<?> part) {
        formBody.addPart(part);
        return this;
    }

    @Override
    public UrlConnectionMultipartRequest self() {
        return this;
    }
}
