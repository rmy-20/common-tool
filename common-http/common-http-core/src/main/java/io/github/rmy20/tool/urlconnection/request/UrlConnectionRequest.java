package io.github.rmy20.tool.urlconnection.request;

import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.body.Body;
import io.github.rmy20.tool.http.core.body.ByteArrayBody;
import io.github.rmy20.tool.http.core.body.FileBody;
import io.github.rmy20.tool.http.core.body.StringBody;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseHttpRequest;

import java.io.File;
import java.nio.charset.Charset;

/**
 * url connection 请求
 *
 * @author sheng
 */
public class UrlConnectionRequest extends UrlConnectionBaseRequest<UrlConnectionRequest> implements BaseHttpRequest<UrlConnectionRequest> {
    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static UrlConnectionRequest create(String url, HttpMethodEnum method) {
        return new UrlConnectionRequest(url, method);
    }

    public UrlConnectionRequest(String url, HttpMethodEnum method) {
        super(url, method);
    }

    // region 请求体

    @Override
    public UrlConnectionRequest body(String body, Charset charset) {
        return body(StringBody.create(body, charset));
    }

    @Override
    public UrlConnectionRequest body(byte[] body, MediaType mediaType, Charset charset) {
        return body(ByteArrayBody.create(body, mediaType));
    }

    @Override
    public UrlConnectionRequest body(File body, MediaType mediaType, Charset charset) {
        return body(FileBody.create(body, mediaType));
    }

    /**
     * 设置请求体
     */
    public UrlConnectionRequest body(Body body) {
        this.body = body;
        return this;
    }

    // endregion

    @Override
    public UrlConnectionRequest self() {
        return this;
    }
}
