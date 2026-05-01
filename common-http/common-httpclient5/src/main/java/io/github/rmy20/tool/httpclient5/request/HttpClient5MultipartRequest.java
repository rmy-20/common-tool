package io.github.rmy20.tool.httpclient5.request;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseMultipartRequest;
import org.apache.hc.client5.http.entity.mime.ByteArrayBody;
import org.apache.hc.client5.http.entity.mime.ContentBody;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.InputStreamBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.core5.http.ContentType;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * httpclient5 multipart form
 *
 * @author sheng
 */
public class HttpClient5MultipartRequest extends HttpClient5BaseRequest<HttpClient5MultipartRequest>
        implements BaseMultipartRequest<HttpClient5MultipartRequest> {
    /**
     * multipart form 参数构建
     */
    private final MultipartEntityBuilder formBuilder;

    /**
     * multipart 模式
     */
    private HttpMultipartMode mode;

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static HttpClient5MultipartRequest create(String url, HttpMethodEnum method) {
        return new HttpClient5MultipartRequest(url, method);
    }

    public HttpClient5MultipartRequest(String url, HttpMethodEnum method) {
        super(url, method);
        this.formBuilder = MultipartEntityBuilder.create().setContentType(ContentType.MULTIPART_FORM_DATA);
    }

    /**
     * 设置 multipart 模式
     */
    public HttpClient5MultipartRequest mod(HttpMultipartMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    @Override
    public HttpClient5MultipartRequest addText(String name, String value) {
        formBuilder.addTextBody(name, value);
        return this;
    }

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    public HttpClient5MultipartRequest addText(String name, String value, ContentType contentType) {
        return addPart(name, new StringBody(value, contentType));
    }

    /**
     * 添加文件
     *
     * @param name key
     * @param file 文件
     */
    @Override
    public HttpClient5MultipartRequest addBinary(String name, File file) {
        formBuilder.addBinaryBody(name, file);
        return this;
    }

    @Override
    public HttpClient5MultipartRequest addBinary(String name, String filename, File file) {
        return addBinary(name, filename, file, ContentType.DEFAULT_BINARY);
    }

    /**
     * 添加文件
     *
     * @param name        key
     * @param file        文件
     * @param contentType 文件类型
     */
    public HttpClient5MultipartRequest addBinary(String name, File file, ContentType contentType) {
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
    public HttpClient5MultipartRequest addBinary(String name, String filename, File file, ContentType contentType) {
        return addPart(name, new FileBody(file, contentType, filename));
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param bytes    文件
     */
    @Override
    public HttpClient5MultipartRequest addBinary(String name, String filename, byte[] bytes) {
        return addPart(name, new ByteArrayBody(bytes, filename));
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param bytes    文件
     */
    public HttpClient5MultipartRequest addBinary(String name, String filename, byte[] bytes, ContentType contentType) {
        return addPart(name, new ByteArrayBody(bytes, contentType, filename));
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param stream   文件流
     */
    @Override
    public HttpClient5MultipartRequest addBinary(String name, String filename, InputStream stream) {
        return addPart(name, new InputStreamBody(stream, filename));
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param stream   文件流
     */
    public HttpClient5MultipartRequest addBinary(String name, String filename, InputStream stream, ContentType contentType) {
        return addPart(name, new InputStreamBody(stream, contentType, filename));
    }

    /**
     * 添加文件
     *
     * @param name        key
     * @param contentBody 文件
     */
    public HttpClient5MultipartRequest addPart(String name, ContentBody contentBody) {
        this.formBuilder.addPart(name, contentBody);
        return this;
    }

    @Override
    protected void executeBefore() {
        super.executeBefore();
        HttpMultipartMode mode = this.mode;
        if (Objects.isNull(mode)) {
            mode = HttpMultipartMode.LEGACY;
        }
        getHeaders().removeContentType();
        this.httpEntity = formBuilder.setMode(mode).setCharset(getDefaultCharset()).build();
    }

    @Override
    public HttpClient5MultipartRequest self() {
        return this;
    }
}
