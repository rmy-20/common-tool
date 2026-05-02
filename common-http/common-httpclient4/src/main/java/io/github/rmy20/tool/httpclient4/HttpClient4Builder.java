package io.github.rmy20.tool.httpclient4;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * httpclient 构建
 *
 * @author sheng
 */
public class HttpClient4Builder {
    /**
     * 连接池允许创建的最大连接数量
     */
    private int maxTotal = 500;

    /**
     * 每个路由（域名）连接数
     */
    private int maxPerRoute = 500;

    /**
     * 连接存活时间
     */
    private int timeToLive = 60;

    /**
     * 连接存活时间单位
     */
    private TimeUnit timeToLiveUnit = TimeUnit.SECONDS;

    /**
     * 从连接池获取连接的超时时间
     */
    private int connectionRequestTimeout = 10;

    /**
     * 从连接池获取连接的超时时间单位
     */
    private TimeUnit connectionRequestTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 客户端和服务器建立连接的超时时间
     */
    private int connectTimeout = 10;

    /**
     * 客户端和服务器建立连接的超时时间单位
     */
    private TimeUnit connectTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 客户端和服务器建立连接后，数据传输过程中数据包之间间隔的最大时间
     */
    private int socketTimeout = 10;

    /**
     * 客户端和服务器建立连接后，数据传输过程中数据包之间间隔的最大时间单位
     */
    private TimeUnit socketTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 开启守护线程定时清理空闲连接
     */
    private boolean evictExpiredConnections = true;

    /**
     * 定时清理空闲连接的时间
     */
    private int evictIdleConnections = 5;

    /**
     * 定时清理空闲连接的时间单位
     */
    private TimeUnit evictIdleConnectionsUnit = TimeUnit.SECONDS;

    /**
     * 创建
     */
    public static HttpClient4Builder create() {
        return new HttpClient4Builder();
    }

    /**
     * 构建 httpclient
     */
    public CloseableHttpClient build() {
        // 通过设置validityDeadline的值，控制没有正常关闭的连接释放的时间
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(timeToLive, timeToLiveUnit);
        // 设置连接池允许创建的最大连接数量为500
        connectionManager.setMaxTotal(maxTotal);
        // 每个路由（域名）连接数500
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        // 超时配置
        final RequestConfig requestConfig = RequestConfig.custom()
                // 从连接池获取连接的超时时间
                .setConnectionRequestTimeout((int) connectionRequestTimeoutUnit.toMillis(connectionRequestTimeout))
                // 客户端和服务器建立连接的超时时间
                .setConnectTimeout((int) connectTimeoutUnit.toMillis(connectTimeout))
                // 客户端和服务器建立连接后，数据传输过程中数据包之间间隔的最大时间
                .setSocketTimeout((int) socketTimeoutUnit.toMillis(socketTimeout))
                .build();

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager);
        if (evictExpiredConnections) {
            // 开启守护线程定时清理空闲连接
            httpClientBuilder.evictExpiredConnections()
                    // 定时清理空闲连接的时间
                    .evictIdleConnections(evictIdleConnections, evictIdleConnectionsUnit);
        }
        return httpClientBuilder.build();
    }

    /**
     * 连接池允许创建的最大连接数量
     */
    public HttpClient4Builder maxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        return this;
    }

    /**
     * 每个路由（域名）连接数
     */
    public HttpClient4Builder maxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        return this;
    }

    /**
     * 连接存活时间
     */
    public HttpClient4Builder timeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    /**
     * 连接存活时间单位
     */
    public HttpClient4Builder timeToLiveUnit(TimeUnit timeToLiveUnit) {
        this.timeToLiveUnit = timeToLiveUnit;
        return this;
    }

    /**
     * 连接存活时间
     */
    public HttpClient4Builder timeToLive(int timeToLive, TimeUnit timeToLiveUnit) {
        this.timeToLive = timeToLive;
        this.timeToLiveUnit = timeToLiveUnit;
        return this;
    }

    /**
     * 从连接池获取连接的超时时间
     */
    public HttpClient4Builder connectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    /**
     * 从连接池获取连接的超时时间单位
     */
    public HttpClient4Builder connectionRequestTimeoutUnit(TimeUnit connectionRequestTimeoutUnit) {
        this.connectionRequestTimeoutUnit = connectionRequestTimeoutUnit;
        return this;
    }

    /**
     * 从连接池获取连接的超时时间
     */
    public HttpClient4Builder connectionRequestTimeout(int connectionRequestTimeout, TimeUnit connectionRequestTimeoutUnit) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.connectionRequestTimeoutUnit = connectionRequestTimeoutUnit;
        return this;
    }

    /**
     * 客户端和服务器建立连接的超时时间
     */
    public HttpClient4Builder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 客户端和服务器建立连接的超时时间单位
     */
    public HttpClient4Builder connectTimeoutUnit(TimeUnit connectTimeoutUnit) {
        this.connectTimeoutUnit = connectTimeoutUnit;
        return this;
    }

    /**
     * 客户端和服务器建立连接的超时时间
     */
    public HttpClient4Builder connectTimeout(int connectTimeout, TimeUnit connectTimeoutUnit) {
        this.connectTimeout = connectTimeout;
        this.connectTimeoutUnit = connectTimeoutUnit;
        return this;
    }

    /**
     * 客户端和服务器建立连接后，数据传输过程中数据包之间间隔的最大时间
     */
    public HttpClient4Builder socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * 客户端和服务器建立连接后，数据传输过程中数据包之间间隔的最大时间单位
     */
    public HttpClient4Builder socketTimeoutUnit(TimeUnit socketTimeoutUnit) {
        this.socketTimeoutUnit = socketTimeoutUnit;
        return this;
    }

    /**
     * 客户端和服务器建立连接后，数据传输过程中数据包之间间隔的最大时间
     */
    public HttpClient4Builder socketTimeout(int socketTimeout, TimeUnit socketTimeoutUnit) {
        this.socketTimeout = socketTimeout;
        this.socketTimeoutUnit = socketTimeoutUnit;
        return this;
    }

    /**
     * 开启守护线程定时清理空闲连接
     */
    public HttpClient4Builder evictExpiredConnections(boolean evictExpiredConnections) {
        this.evictExpiredConnections = evictExpiredConnections;
        return this;
    }

    /**
     * 定时清理空闲连接的时间
     */
    public HttpClient4Builder evictIdleConnections(int evictIdleConnections) {
        this.evictIdleConnections = evictIdleConnections;
        return this;
    }

    /**
     * 定时清理空闲连接的时间单位
     */
    public HttpClient4Builder evictIdleConnectionsUnit(TimeUnit evictIdleConnectionsUnit) {
        this.evictIdleConnectionsUnit = evictIdleConnectionsUnit;
        return this;
    }

    /**
     * 定时清理空闲连接的时间
     */
    public HttpClient4Builder evictIdleConnections(int evictIdleConnections, TimeUnit evictIdleConnectionsUnit) {
        this.evictIdleConnections = evictIdleConnections;
        this.evictIdleConnectionsUnit = evictIdleConnectionsUnit;
        return this;
    }
}
