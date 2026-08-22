package io.github.rmy20.tool.http.jdkclient;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseRequestTool;
import io.github.rmy20.tool.http.jdkclient.request.JdkClientBaseRequest;
import io.github.rmy20.tool.http.jdkclient.request.JdkClientFormRequest;
import io.github.rmy20.tool.http.jdkclient.request.JdkClientMultipartRequest;
import io.github.rmy20.tool.http.jdkclient.request.JdkClientRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author sheng
 */
public class JdkClientRequestTool implements BaseRequestTool {
    /**
     * http客户端
     */
    private HttpClient httpClient;

    /**
     * 协议版本
     */
    private HttpClient.Version version;

    /**
     * 超时时间
     */
    private Duration timeout;

    /**
     * 默认编码字符集，默认 UTF-8
     */
    protected Charset defaultCharset = StandardCharsets.UTF_8;

    /**
     * 创建#{@link JdkClientRequestTool}
     */
    public static JdkClientRequestTool create() {
        return new JdkClientRequestTool();
    }

    @Override
    public JdkClientRequest get(String url) {
        return addConfig(JdkClientRequest.create(url, HttpMethodEnum.GET));
    }

    @Override
    public JdkClientRequest get(URI url) {
        return addConfig(JdkClientRequest.create(url.toString(), HttpMethodEnum.GET));
    }

    @Override
    public JdkClientRequest post(String url) {
        return addConfig(JdkClientRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public JdkClientRequest post(URI url) {
        return addConfig(JdkClientRequest.create(url.toString(), HttpMethodEnum.POST));
    }

    @Override
    public JdkClientRequest put(String url) {
        return addConfig(JdkClientRequest.create(url, HttpMethodEnum.PUT));
    }

    @Override
    public JdkClientRequest put(URI url) {
        return addConfig(JdkClientRequest.create(url.toString(), HttpMethodEnum.PUT));
    }

    @Override
    public JdkClientRequest delete(String url) {
        return addConfig(JdkClientRequest.create(url, HttpMethodEnum.DELETE));
    }

    @Override
    public JdkClientRequest delete(URI url) {
        return addConfig(JdkClientRequest.create(url.toString(), HttpMethodEnum.DELETE));
    }

    @Override
    public JdkClientRequest request(String url, HttpMethodEnum method) {
        return addConfig(JdkClientRequest.create(url, method));
    }

    @Override
    public JdkClientRequest request(URI url, HttpMethodEnum method) {
        return addConfig(JdkClientRequest.create(url.toString(), method));
    }

    @Override
    public JdkClientFormRequest form(String url) {
        return addConfig(JdkClientFormRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public JdkClientFormRequest form(String url, HttpMethodEnum method) {
        return addConfig(JdkClientFormRequest.create(url, method));
    }

    @Override
    public JdkClientMultipartRequest multipart(String url) {
        return addConfig(JdkClientMultipartRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public JdkClientMultipartRequest multipart(String url, HttpMethodEnum method) {
        return addConfig(JdkClientMultipartRequest.create(url, method));
    }

    private <T extends JdkClientBaseRequest<T>> T addConfig(T t) {
        return t.httpClient(httpClient)
                .version(version)
                .timeout(timeout)
                .defaultCharset(defaultCharset);
    }

    /**
     * 设置#{@link HttpClient}
     */
    public JdkClientRequestTool httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * 协议版本
     */
    public JdkClientRequestTool version(HttpClient.Version version) {
        this.version = version;
        return this;
    }

    /**
     * 超时时间
     */
    public JdkClientRequestTool timeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 超时时间
     */
    public JdkClientRequestTool timeout(long timeout, ChronoUnit unit) {
        this.timeout = Duration.of(timeout, unit);
        return this;
    }

    /**
     * 设置默认编码字符集
     */
    public JdkClientRequestTool defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }
}
