package io.github.rmy20.tool.http.jdkclient.request;

import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.exception.HttpException;
import io.github.rmy20.tool.http.core.request.BaseHttpRequest;

import java.io.File;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;

/**
 * jdk httpclient 请求
 *
 * @author sheng
 */
public class JdkClientRequest extends JdkClientBaseRequest<JdkClientRequest> implements BaseHttpRequest<JdkClientRequest> {
    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static JdkClientRequest create(String url, HttpMethodEnum method) {
        return new JdkClientRequest(url, method);
    }

    public JdkClientRequest(String url, HttpMethodEnum method) {
        super(url, method);
    }

    // region 请求体

    @Override
    public JdkClientRequest body(String body, Charset charset) {
        return body(HttpRequest.BodyPublishers.ofString(body, charset));
    }

    @Override
    public JdkClientRequest body(byte[] body, MediaType mediaType, Charset charset) {
        return body(HttpRequest.BodyPublishers.ofByteArray(body));
    }

    @Override
    public JdkClientRequest body(File body, MediaType mediaType, Charset charset) {
        try {
            return body(HttpRequest.BodyPublishers.ofFile(body.toPath()));
        } catch (Exception e) {
            throw new HttpException("Failed to create file request body", e);
        }
    }

    /**
     * 设置请求体
     */
    public JdkClientRequest body(HttpRequest.BodyPublisher bodyPublisher) {
        this.bodyPublisher = bodyPublisher;
        return this;
    }
    // endregion

    @Override
    public JdkClientRequest self() {
        return this;
    }
}
