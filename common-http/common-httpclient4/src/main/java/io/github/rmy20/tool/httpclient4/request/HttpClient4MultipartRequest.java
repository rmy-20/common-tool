package io.github.rmy20.tool.httpclient4.request;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseMultipartRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * httpclient4 multipart请求
 *
 * @author sheng
 */
public class HttpClient4MultipartRequest extends HttpClient4BaseRequest<HttpClient4MultipartRequest>
        implements BaseMultipartRequest<HttpClient4MultipartRequest> {
    /**
     * multipart form 参数构建
     */
    private final MultipartEntityBuilder formBuilder;

    /**
     * multipart 模式
     */
    private HttpMultipartMode mode;

    /**
     * 创建
     */
    public static HttpClient4MultipartRequest create(String url, HttpMethodEnum method) {
        return new HttpClient4MultipartRequest(url, method);
    }

    public HttpClient4MultipartRequest(String url, HttpMethodEnum method) {
        super(url, method);
        this.formBuilder = MultipartEntityBuilder.create().setContentType(ContentType.MULTIPART_FORM_DATA);
    }

    /**
     * 设置 multipart 模式
     */
    public HttpClient4MultipartRequest mod(HttpMultipartMode mode) {
        this.mode = mode;
        return self();
    }

    @Override
    public HttpClient4MultipartRequest addText(String name, String value) {
        return addText(name, value, ContentType.DEFAULT_TEXT.withCharset(getDefaultCharset()));
    }

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    public HttpClient4MultipartRequest addText(String name, String value, ContentType contentType) {
        return addPart(name, new StringBody(value, contentType));
    }

    @Override
    public HttpClient4MultipartRequest addBinary(String name, File file) {
        formBuilder.addBinaryBody(name, file);
        return self();
    }

    @Override
    public HttpClient4MultipartRequest addBinary(String name, String filename, File file) {
        return addBinary(name, filename, file, ContentType.DEFAULT_BINARY);
    }

    /**
     * 添加文件
     *
     * @param name        key
     * @param file        文件
     * @param contentType 文件类型
     */
    public HttpClient4MultipartRequest addBinary(String name, File file, ContentType contentType) {
        return addBinary(name, file.getName(), file, contentType);
    }

    /**
     * 添加文件
     *
     * @param name        key
     * @param filename    文件名
     * @param file        文件
     * @param contentType 文件类型
     */
    public HttpClient4MultipartRequest addBinary(String name, String filename, File file, ContentType contentType) {
        return addPart(name, new FileBody(file, contentType, filename));
    }

    @Override
    public HttpClient4MultipartRequest addBinary(String name, String filename, byte[] bytes) {
        return addPart(name, new ByteArrayBody(bytes, filename));
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param bytes    文件
     */
    public HttpClient4MultipartRequest addBinary(String name, String filename, byte[] bytes, ContentType contentType) {
        return addPart(name, new ByteArrayBody(bytes, contentType, filename));
    }

    @Override
    public HttpClient4MultipartRequest addBinary(String name, String filename, InputStream stream) {
        return addPart(name, new InputStreamBody(stream, filename));
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param stream   文件流
     */
    public HttpClient4MultipartRequest addBinary(String name, String filename, InputStream stream, ContentType contentType) {
        return addPart(name, new InputStreamBody(stream, contentType, filename));
    }

    /**
     * 添加文件
     *
     * @param name        key
     * @param contentBody 文件
     */
    public HttpClient4MultipartRequest addPart(String name, ContentBody contentBody) {
        this.formBuilder.addPart(name, contentBody);
        return self();
    }

    @Override
    protected void executeBefore() {
        super.executeBefore();
        HttpMultipartMode mode = this.mode;
        if (Objects.isNull(mode)) {
            mode = HttpMultipartMode.RFC6532;
        }
        getHeaders().removeContentType();
        this.httpEntity = formBuilder.setMode(mode).setCharset(getDefaultCharset()).build();
    }

    @Override
    public HttpClient4MultipartRequest self() {
        return this;
    }
}
