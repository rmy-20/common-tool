package cn.zs.tool.httpclient5;

import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.SchemePortResolver;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.function.Resolver;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpConnectionFactory;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.Timeout;

import java.util.concurrent.TimeUnit;

/**
 * httpclient 构建者
 *
 * @author sheng
 */
public class HttpClient5Builder {
    /**
     * 连接池允许创建的最大连接数量
     */
    private int maxTotal = 500;

    /**
     * 每个路由（域名）连接数
     */
    private int maxPerRoute = 50;

    /**
     * 默认的TLS策略
     */
    private TlsSocketStrategy tlsSocketStrategy = DefaultClientTlsStrategy.createDefault();

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
     * 套接字配置解析器
     */
    private Resolver<HttpRoute, SocketConfig> socketConfigResolver;

    /**
     * 连接配置解析器
     */
    private Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver;

    /**
     * TLS配置解析器
     */
    private Resolver<HttpHost, TlsConfig> tlsConfigResolver;

    /**
     * 连接池的连接工厂
     */
    private HttpConnectionFactory<ManagedHttpClientConnection> connFactory;

    /**
     * socket超时时间
     */
    private int socketTimeout = 2;

    /**
     * socket超时时间单位
     */
    private TimeUnit socketTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 连接超时时间
     */
    private int connectTimeout = 2;

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
    private int connectionRequestTimeout = 2;

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
     * 创建工程
     */
    public static HttpClient5Builder create() {
        return new HttpClient5Builder();
    }

    /**
     * 构建httpclient
     */
    public CloseableHttpClient build() {
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

        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(maxTotal)
                .setMaxConnPerRoute(maxPerRoute)
                .setConnectionFactory(connFactory)
                .setDefaultConnectionConfig(connectionConfig)
                .setPoolConcurrencyPolicy(poolConcurrencyPolicy)
                .setTlsSocketStrategy(tlsSocketStrategy)
                .setConnPoolPolicy(poolReusePolicy)
                .setSchemePortResolver(schemePortResolver)
                .setDnsResolver(dnsResolver)
                .setSocketConfigResolver(socketConfigResolver)
                .setConnectionConfigResolver(connectionConfigResolver)
                .setTlsConfigResolver(tlsConfigResolver)
                .build();

        // 超时配置
        final RequestConfig requestConfig = RequestConfig.custom()
                // 从连接池获取连接的超时时间
                .setConnectionRequestTimeout(Timeout.ofSeconds(connectionRequestTimeoutUnit.toSeconds(connectionRequestTimeout)))
                .build();

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager);
        // 开启守护线程定时清理空闲连接
        if (evictExpiredConnections) {
            // 定时清理空闲连接的时间
            httpClientBuilder.evictExpiredConnections()
                    .evictIdleConnections(Timeout.ofSeconds(evictIdleConnectionsUnit.toSeconds(evictIdleConnections)));
        }
        return httpClientBuilder.build();
    }

    /**
     * 设置最大连接数
     */
    public HttpClient5Builder maxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        return this;
    }

    /**
     * 设置每个路由最大连接数
     */
    public HttpClient5Builder maxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        return this;
    }

    /**
     * 设置TLS套接字策略
     */
    public HttpClient5Builder tlsSocketStrategy(TlsSocketStrategy tlsSocketStrategy) {
        this.tlsSocketStrategy = tlsSocketStrategy;
        return this;
    }

    /**
     * 协议端口解析器
     */
    public HttpClient5Builder schemePortResolver(SchemePortResolver schemePortResolver) {
        this.schemePortResolver = schemePortResolver;
        return this;
    }

    /**
     * 设置DNS解析器
     */
    public HttpClient5Builder dnsResolver(DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
        return this;
    }

    /**
     * 设置连接池并发策略
     */
    public HttpClient5Builder poolConcurrencyPolicy(PoolConcurrencyPolicy poolConcurrencyPolicy) {
        this.poolConcurrencyPolicy = poolConcurrencyPolicy;
        return this;
    }

    /**
     * 设置连接池的复用策略
     */
    public HttpClient5Builder poolReusePolicy(PoolReusePolicy poolReusePolicy) {
        this.poolReusePolicy = poolReusePolicy;
        return this;
    }

    /**
     * 设置连接池的socket配置解析器
     */
    public HttpClient5Builder socketConfigResolver(Resolver<HttpRoute, SocketConfig> socketConfigResolver) {
        this.socketConfigResolver = socketConfigResolver;
        return this;
    }

    /**
     * 获取连接池的连接配置解析器
     */
    public HttpClient5Builder connectionConfigResolver(Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver) {
        this.connectionConfigResolver = connectionConfigResolver;
        return this;
    }

    /**
     * 获取连接池的TLS配置解析器
     */
    public HttpClient5Builder tlsConfigResolver(Resolver<HttpHost, TlsConfig> tlsConfigResolver) {
        this.tlsConfigResolver = tlsConfigResolver;
        return this;
    }

    /**
     * 设置连接池的连接工厂
     */
    public HttpClient5Builder connFactory(HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
        this.connFactory = connFactory;
        return this;
    }

    /**
     * 设置socket超时时间
     */
    public HttpClient5Builder socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * 设置socket超时时间单位
     */
    public HttpClient5Builder socketTimeoutUnit(TimeUnit socketTimeoutUnit) {
        this.socketTimeoutUnit = socketTimeoutUnit;
        return this;
    }

    /**
     * 设置连接超时时间
     */
    public HttpClient5Builder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 设置连接超时时间单位
     */
    public HttpClient5Builder connectTimeoutUnit(TimeUnit connectTimeoutUnit) {
        this.connectTimeoutUnit = connectTimeoutUnit;
        return this;
    }

    /**
     * 设置验证连接是否活跃
     */
    public HttpClient5Builder validateAfterInactivity(int validateAfterInactivity) {
        this.validateAfterInactivity = validateAfterInactivity;
        return this;
    }

    /**
     * 设置验证连接是否活跃单位
     */
    public HttpClient5Builder validateAfterInactivityUnit(TimeUnit validateAfterInactivityUnit) {
        this.validateAfterInactivityUnit = validateAfterInactivityUnit;
        return this;
    }

    /**
     * 设置连接存活时间
     */
    public HttpClient5Builder timeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    /**
     * 设置连接存活时间单位
     */
    public HttpClient5Builder timeToLiveUnit(TimeUnit timeToLiveUnit) {
        this.timeToLiveUnit = timeToLiveUnit;
        return this;
    }

    /**
     * 设置从连接池获取连接的超时时间
     */
    public HttpClient5Builder connectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    /**
     * 设置从连接池获取连接的超时时间单位
     */
    public HttpClient5Builder connectionRequestTimeoutUnit(TimeUnit connectionRequestTimeoutUnit) {
        this.connectionRequestTimeoutUnit = connectionRequestTimeoutUnit;
        return this;
    }

    /**
     * 设置是否开启守护线程定时清理空闲连接
     */
    public HttpClient5Builder evictExpiredConnections(boolean evictExpiredConnections) {
        this.evictExpiredConnections = evictExpiredConnections;
        return this;
    }

    /**
     * 设置守护线程定时清理空闲连接的时间间隔
     */
    public HttpClient5Builder evictIdleConnections(int evictIdleConnections) {
        this.evictIdleConnections = evictIdleConnections;
        return this;
    }

    /**
     * 设置守护线程定时清理空闲连接的时间间隔单位
     */
    public HttpClient5Builder evictIdleConnectionsUnit(TimeUnit evictIdleConnectionsUnit) {
        this.evictIdleConnectionsUnit = evictIdleConnectionsUnit;
        return this;
    }
}
