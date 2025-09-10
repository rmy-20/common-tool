package cn.zs.tool.okhttp.request;

import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.request.BaseHttpRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * okhttp请求
 *
 * @author sheng
 */
public class OkHttpRequest extends OkHttpBaseRequest<OkHttpRequest> implements BaseHttpRequest<OkHttpRequest> {
    /**
     * 请求体
     */
    private RequestBody requestBody;

    public OkHttpRequest(final String url, final HttpMethodEnum method) {
        super(url, method);
    }

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static OkHttpRequest create(String url, HttpMethodEnum method) {
        return new OkHttpRequest(url, method);
    }

    // region 请求体

    /**
     * 设置请求体
     */
    @Override
    public OkHttpRequest body(String body, Charset charset) {
        return body(body, charset, null);
    }

    /**
     * 设置请求体
     */
    public OkHttpRequest body(String body, Charset charset, MediaType mediaType) {
        RequestBody requestBody = RequestBody.create(body.getBytes(charset), mediaType);
        return body(requestBody);
    }

    /**
     * 设置请求体
     */
    public OkHttpRequest body(byte[] body) {
        return body(body, (MediaType) null);
    }

    /**
     * 设置请求体
     */
    public OkHttpRequest body(byte[] body, MediaType mediaType) {
        RequestBody requestBody = RequestBody.create(body, mediaType);
        return body(requestBody);
    }

    @Override
    public OkHttpRequest body(byte[] body, cn.zs.tool.http.core.MediaType mediaType, Charset charset) {
        return body(body, Objects.nonNull(mediaType) ? MediaType.parse(mediaType.getMediaType()) : (MediaType) null);
    }

    /**
     * 设置请求体
     */
    public OkHttpRequest body(File file, MediaType mediaType) {
        RequestBody requestBody = RequestBody.create(file, mediaType);
        return body(requestBody);
    }

    @Override
    public OkHttpRequest body(File body, cn.zs.tool.http.core.MediaType mediaType, Charset charset) {
        return body(body, Objects.nonNull(mediaType) ? MediaType.parse(mediaType.getMediaType()) : (MediaType) null);
    }

    /**
     * 设置请求体
     */
    public OkHttpRequest body(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    // endregion

    @Override
    public OkHttpRequest self() {
        return this;
    }

    @Override
    public RequestBody getRequestBody() {
        return requestBody;
    }
}
