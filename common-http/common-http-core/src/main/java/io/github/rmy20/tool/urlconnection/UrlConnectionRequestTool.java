package io.github.rmy20.tool.urlconnection;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseRequestTool;
import io.github.rmy20.tool.urlconnection.request.UrlConnectionBaseRequest;
import io.github.rmy20.tool.urlconnection.request.UrlConnectionFormRequest;
import io.github.rmy20.tool.urlconnection.request.UrlConnectionMultipartRequest;
import io.github.rmy20.tool.urlconnection.request.UrlConnectionRequest;

import java.net.Proxy;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

/**
 * Url Connection 请求工具类
 *
 * @author sheng
 */
public class UrlConnectionRequestTool implements BaseRequestTool {
    /**
     * 代理
     */
    protected Proxy proxy;

    /**
     * 连接超时时间，单位秒
     */
    private int connectTimeout = -1;

    /**
     * 读超时，单位秒
     */
    private int readTimeout = -1;

    /**
     * 块模式分块长度
     */
    private int chunkSize = 4096;

    /**
     * 异步执行线程池
     */
    protected ExecutorService executorService;

    /**
     * 默认编码字符集，默认 UTF-8
     */
    protected Charset defaultCharset = StandardCharsets.UTF_8;

    /**
     * 创建#{@link UrlConnectionRequestTool}
     */
    public static UrlConnectionRequestTool create() {
        return new UrlConnectionRequestTool();
    }

    @Override
    public UrlConnectionRequest get(String url) {
        return addConfig(UrlConnectionRequest.create(url, HttpMethodEnum.GET));
    }

    @Override
    public UrlConnectionRequest get(URI url) {
        return addConfig(UrlConnectionRequest.create(url.toString(), HttpMethodEnum.GET));
    }

    @Override
    public UrlConnectionRequest post(String url) {
        return addConfig(UrlConnectionRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public UrlConnectionRequest post(URI url) {
        return addConfig(UrlConnectionRequest.create(url.toString(), HttpMethodEnum.POST));
    }

    @Override
    public UrlConnectionRequest put(String url) {
        return addConfig(UrlConnectionRequest.create(url, HttpMethodEnum.PUT));
    }

    @Override
    public UrlConnectionRequest put(URI url) {
        return addConfig(UrlConnectionRequest.create(url.toString(), HttpMethodEnum.PUT));
    }

    @Override
    public UrlConnectionRequest delete(String url) {
        return addConfig(UrlConnectionRequest.create(url, HttpMethodEnum.DELETE));
    }

    @Override
    public UrlConnectionRequest delete(URI url) {
        return addConfig(UrlConnectionRequest.create(url.toString(), HttpMethodEnum.DELETE));
    }

    @Override
    public UrlConnectionRequest request(String url, HttpMethodEnum method) {
        return addConfig(UrlConnectionRequest.create(url, method));
    }

    @Override
    public UrlConnectionRequest request(URI url, HttpMethodEnum method) {
        return addConfig(UrlConnectionRequest.create(url.toString(), method));
    }

    @Override
    public UrlConnectionFormRequest form(String url) {
        return addConfig(UrlConnectionFormRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public UrlConnectionFormRequest form(String url, HttpMethodEnum method) {
        return addConfig(UrlConnectionFormRequest.create(url, method));
    }

    @Override
    public UrlConnectionMultipartRequest multipart(String url) {
        return addConfig(UrlConnectionMultipartRequest.create(url, HttpMethodEnum.POST));
    }

    @Override
    public UrlConnectionMultipartRequest multipart(String url, HttpMethodEnum method) {
        return addConfig(UrlConnectionMultipartRequest.create(url, method));
    }

    private <T extends UrlConnectionBaseRequest<T>> T addConfig(T t) {
        return t.proxy(proxy)
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .chunkSize(chunkSize)
                .executorService(executorService)
                .defaultCharset(defaultCharset);
    }

    /**
     * 设置代理
     */
    public UrlConnectionRequestTool proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    /**
     * 设置连接超时时间，单位秒
     */
    public UrlConnectionRequestTool connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 设置读超时时间，单位秒
     */
    public UrlConnectionRequestTool readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * 设置块大小
     */
    public UrlConnectionRequestTool chunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }

    /**
     * 设置默认编码字符集
     */
    public UrlConnectionRequestTool defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }

    /**
     * 设置线程池
     */
    public UrlConnectionRequestTool executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }
}
