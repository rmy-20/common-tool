package io.github.rmy20.tool.httpclient4.request;

import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseHttpRequest;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * httpclient4请求
 *
 * @author sheng
 */
public class HttpClient4Request extends HttpClient4BaseRequest<HttpClient4Request> implements BaseHttpRequest<HttpClient4Request> {
    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static HttpClient4Request create(String url, HttpMethodEnum method) {
        return new HttpClient4Request(url, method);
    }

    public HttpClient4Request(String url, HttpMethodEnum method) {
        super(url, method);
    }

    // region 请求体

    @Override
    public HttpClient4Request body(String body, Charset charset) {
        return body(new StringEntity(body, charset));
    }

    /**
     * 设置请求体
     */
    public HttpClient4Request body(byte[] body, ContentType contentType) {
        return body(body, contentType, getDefaultCharset());
    }

    /**
     * 设置请求体
     */
    public HttpClient4Request body(byte[] body, ContentType contentType, Charset charset) {
        ByteArrayEntity entity = new ByteArrayEntity(body, contentType);
        entity.setContentEncoding(charset.name());
        return body(entity);
    }

    @Override
    public HttpClient4Request body(byte[] body, MediaType mediaType, Charset charset) {
        ByteArrayEntity entity = new ByteArrayEntity(body, Objects.nonNull(mediaType) ? ContentType.create(mediaType.getMediaType()) : null);
        entity.setContentEncoding(charset.name());
        return body(entity);
    }

    /**
     * 设置请求体
     */
    public HttpClient4Request body(File body, ContentType contentType) {
        return body(body, contentType, getDefaultCharset());
    }

    /**
     * 设置请求体
     */
    public HttpClient4Request body(File body, ContentType contentType, Charset charset) {
        FileEntity fileEntity = new FileEntity(body, contentType);
        fileEntity.setContentEncoding(charset.name());
        return body(fileEntity);
    }

    @Override
    public HttpClient4Request body(File body, MediaType mediaType, Charset charset) {
        FileEntity fileEntity = new FileEntity(body, Objects.nonNull(mediaType) ? ContentType.create(mediaType.getMediaType()) : null);
        fileEntity.setContentEncoding(charset.name());
        return body(fileEntity);
    }

    /**
     * 设置请求体
     */
    public HttpClient4Request body(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        return self();
    }

    // endregion

    @Override
    public HttpClient4Request self() {
        return this;
    }
}
