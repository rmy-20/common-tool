package io.github.rmy20.tool.httpclient5;

import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.SchemePortResolver;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.function.Resolver;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * httpAsyncClient 构建者
 *
 * @author sheng
 */
public class HttpAsyncClient5Builder {
    /**
     * 连接池允许创建的最大连接数量
     */
    private int maxTotal = 500;

    /**
     * 每个路由（域名）连接数
     */
    private int maxPerRoute = 50;

    /**
     * 协议端口解析器
     */
    private SchemePortResolver schemePortResolver;

    /**
     * DNS解析器
     */
    private DnsResolver dnsResolver;

    /**
     * 连接池并发策略
     */
    private PoolConcurrencyPolicy poolConcurrencyPolicy = PoolConcurrencyPolicy.STRICT;

    /**
     * 连接池重用策略
     */
    private PoolReusePolicy poolReusePolicy = PoolReusePolicy.LIFO;

    /**
     * 连接配置解析器
     */
    private Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver;

    /**
     * TLS配置解析器
     */
    private Resolver<HttpHost, TlsConfig> tlsConfigResolver;

    /**
     * socket超时时间
     */
    private int socketTimeout = 10;

    /**
     * socket超时时间单位
     */
    private TimeUnit socketTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 连接超时时间
     */
    private int connectTimeout = 10;

    /**
     * 连接超时时间单位
     */
    private TimeUnit connectTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 验证连接是否活跃
     */
    private int validateAfterInactivity = 2;

    /**
     * 验证连接是否活跃单位
     */
    private TimeUnit validateAfterInactivityUnit = TimeUnit.SECONDS;

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
     * true 为开启守护线程定时清理空闲连接
     */
    private boolean evictExpiredConnections = true;

    /**
     * 守护线程定时清理空闲连接的时间间隔
     */
    private int evictIdleConnections = 30;

    /**
     * 守护线程定时清理空闲连接的时间间隔单位
     */
    private TimeUnit evictIdleConnectionsUnit = TimeUnit.SECONDS;

    /**
     * I/O reactor 配置
     */
    private IOReactorConfig ioReactorConfig;

    /**
     * 是否启动
     */
    private boolean start = true;

    /**
     * 创建工程
     */
    public static HttpAsyncClient5Builder create() {
        return new HttpAsyncClient5Builder();
    }

    /**
     * 构建httpclient
     */
    public CloseableHttpAsyncClient build() {
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                // 客户端和服务器建立连接的超时时间
                .setConnectTimeout(Timeout.ofSeconds(connectTimeoutUnit.toSeconds(connectTimeout)))
                // 客户端和服务器建立连接后，数据传输过程中数据包之间间隔的最大时间
                .setSocketTimeout(Timeout.ofSeconds(socketTimeoutUnit.toSeconds(socketTimeout)))
                // 连接活动时间，超过该时间将关闭连接
                .setTimeToLive(Timeout.ofSeconds(timeToLiveUnit.toSeconds(timeToLive)))
                // 连接校验时间，不活动后验证连接
                .setValidateAfterInactivity(Timeout.ofSeconds(validateAfterInactivityUnit.toSeconds(validateAfterInactivity)))
                .build();

        PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnTotal(maxTotal)
                .setMaxConnPerRoute(maxPerRoute)
                .setDefaultConnectionConfig(connectionConfig)
                .setPoolConcurrencyPolicy(poolConcurrencyPolicy)
                .setConnPoolPolicy(poolReusePolicy)
                .setSchemePortResolver(schemePortResolver)
                .setDnsResolver(dnsResolver)
                .setConnectionConfigResolver(connectionConfigResolver)
                .setTlsConfigResolver(tlsConfigResolver)
                .build();

        // 超时配置
        final RequestConfig requestConfig = RequestConfig.custom()
                // 从连接池获取连接的超时时间
                .setConnectionRequestTimeout(Timeout.ofSeconds(connectionRequestTimeoutUnit.toSeconds(connectionRequestTimeout)))
                .build();

        HttpAsyncClientBuilder httpClientBuilder = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager);

        if (Objects.nonNull(ioReactorConfig)) {
            httpClientBuilder.setIOReactorConfig(ioReactorConfig);
        }
        // 开启守护线程定时清理空闲连接
        if (evictExpiredConnections) {
            // 定时清理空闲连接的时间
            httpClientBuilder.evictExpiredConnections()
                    .evictIdleConnections(Timeout.ofSeconds(evictIdleConnectionsUnit.toSeconds(evictIdleConnections)));
        }
        CloseableHttpAsyncClient asyncClient = httpClientBuilder.build();
        if (start) {
            asyncClient.start();
        }
        return asyncClient;
    }

    /**
     * 设置最大连接数
     */
    public HttpAsyncClient5Builder maxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        return this;
    }

    /**
     * 设置每个路由最大连接数
     */
    public HttpAsyncClient5Builder maxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        return this;
    }

    /**
     * 协议端口解析器
     */
    public HttpAsyncClient5Builder schemePortResolver(SchemePortResolver schemePortResolver) {
        this.schemePortResolver = schemePortResolver;
        return this;
    }

    /**
     * 设置DNS解析器
     */
    public HttpAsyncClient5Builder dnsResolver(DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
        return this;
    }

    /**
     * 设置连接池并发策略
     */
    public HttpAsyncClient5Builder poolConcurrencyPolicy(PoolConcurrencyPolicy poolConcurrencyPolicy) {
        this.poolConcurrencyPolicy = poolConcurrencyPolicy;
        return this;
    }

    /**
     * 设置连接池的复用策略
     */
    public HttpAsyncClient5Builder poolReusePolicy(PoolReusePolicy poolReusePolicy) {
        this.poolReusePolicy = poolReusePolicy;
        return this;
    }

    /**
     * 获取连接池的连接配置解析器
     */
    public HttpAsyncClient5Builder connectionConfigResolver(Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver) {
        this.connectionConfigResolver = connectionConfigResolver;
        return this;
    }

    /**
     * 获取连接池的TLS配置解析器
     */
    public HttpAsyncClient5Builder tlsConfigResolver(Resolver<HttpHost, TlsConfig> tlsConfigResolver) {
        this.tlsConfigResolver = tlsConfigResolver;
        return this;
    }

    /**
     * 设置socket超时时间
     */
    public HttpAsyncClient5Builder socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * 设置socket超时时间单位
     */
    public HttpAsyncClient5Builder socketTimeoutUnit(TimeUnit socketTimeoutUnit) {
        this.socketTimeoutUnit = socketTimeoutUnit;
        return this;
    }

    /**
     * 设置连接超时时间
     */
    public HttpAsyncClient5Builder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 设置连接超时时间单位
     */
    public HttpAsyncClient5Builder connectTimeoutUnit(TimeUnit connectTimeoutUnit) {
        this.connectTimeoutUnit = connectTimeoutUnit;
        return this;
    }

    /**
     * 设置验证连接是否活跃
     */
    public HttpAsyncClient5Builder validateAfterInactivity(int validateAfterInactivity) {
        this.validateAfterInactivity = validateAfterInactivity;
        return this;
    }

    /**
     * 设置验证连接是否活跃单位
     */
    public HttpAsyncClient5Builder validateAfterInactivityUnit(TimeUnit validateAfterInactivityUnit) {
        this.validateAfterInactivityUnit = validateAfterInactivityUnit;
        return this;
    }

    /**
     * 设置连接存活时间
     */
    public HttpAsyncClient5Builder timeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    /**
     * 设置连接存活时间单位
     */
    public HttpAsyncClient5Builder timeToLiveUnit(TimeUnit timeToLiveUnit) {
        this.timeToLiveUnit = timeToLiveUnit;
        return this;
    }

    /**
     * 设置从连接池获取连接的超时时间
     */
    public HttpAsyncClient5Builder connectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    /**
     * 设置从连接池获取连接的超时时间单位
     */
    public HttpAsyncClient5Builder connectionRequestTimeoutUnit(TimeUnit connectionRequestTimeoutUnit) {
        this.connectionRequestTimeoutUnit = connectionRequestTimeoutUnit;
        return this;
    }

    /**
     * 设置是否开启守护线程定时清理空闲连接
     */
    public HttpAsyncClient5Builder evictExpiredConnections(boolean evictExpiredConnections) {
        this.evictExpiredConnections = evictExpiredConnections;
        return this;
    }

    /**
     * 设置守护线程定时清理空闲连接的时间间隔
     */
    public HttpAsyncClient5Builder evictIdleConnections(int evictIdleConnections) {
        this.evictIdleConnections = evictIdleConnections;
        return this;
    }

    /**
     * 设置守护线程定时清理空闲连接的时间间隔单位
     */
    public HttpAsyncClient5Builder evictIdleConnectionsUnit(TimeUnit evictIdleConnectionsUnit) {
        this.evictIdleConnectionsUnit = evictIdleConnectionsUnit;
        return this;
    }

    /**
     * 设置 I/O reactor 配置
     */
    public HttpAsyncClient5Builder ioReactorConfig(IOReactorConfig ioReactorConfig) {
        this.ioReactorConfig = ioReactorConfig;
        return this;
    }

    /**
     * 设置是否启动
     */
    public HttpAsyncClient5Builder start(boolean start) {
        this.start = start;
        return this;
    }
}
