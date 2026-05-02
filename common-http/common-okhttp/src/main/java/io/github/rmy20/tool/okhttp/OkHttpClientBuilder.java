package io.github.rmy20.tool.okhttp;

import io.github.rmy20.tool.core.collection.CollectionUtil;
import io.github.rmy20.tool.okhttp.log.OkHttpBaseLogger;
import io.github.rmy20.tool.okhttp.log.OkHttpLogLevelEnum;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OKHttpClient构建器
 *
 * @author sheng
 */
public class OkHttpClientBuilder {
    /**
     * 拦截器
     */
    private final List<Interceptor> interceptorList = new ArrayList<>();

    /**
     * 连接超时时间
     */
    private long connectTimeout = 10L;

    /**
     * 连接超时时间单位
     */
    private TimeUnit connectTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 读取超时时间，数据传输过程中数据包之间间隔的最大时间
     */
    private long readTimeout = 10L;

    /**
     * 读取超时时间单位
     */
    private TimeUnit readTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 写超时时间，数据传输过程中数据包之间间隔的最大时间
     */
    private long writeTimeout = 10L;

    /**
     * 写超时时间单位
     */
    private TimeUnit writeTimeoutUnit = TimeUnit.SECONDS;

    /**
     * 最大空闲连接数
     */
    private int maxIdleConnections = 20;

    /**
     * 保持连接时间
     */
    private long keepAlive = 60L;

    /**
     * 保持连接时间单位
     */
    private TimeUnit keepAliveUnit = TimeUnit.SECONDS;

    /**
     * 全局请求超时时间，未完成的请求，超时后，会取消请求
     */
    private long callTimeout = 0L;

    /**
     * 全局请求超时时间单位
     */
    private TimeUnit callTimeoutUnit = TimeUnit.SECONDS;

    /**
     * ping间隔时间，心跳间隔时间确认连接是否有效
     */
    private long pingInterval = 0L;

    /**
     * ping间隔时间
     */
    private TimeUnit pingIntervalUnit = TimeUnit.SECONDS;

    /**
     * 任务分发器
     */
    private Dispatcher dispatcher;

    /**
     * 代理
     */
    private Proxy proxy;

    /**
     * 代理选择器，仅当{@link #proxy}为null时，才会生效
     */
    private ProxySelector proxySelector;

    /**
     * cookie 处理策略
     */
    private CookieJar cookieJar;

    /**
     * 响应缓存
     */
    private Cache cache;

    /**
     * 主机名IP地址DNS解析
     */
    private Dns dns;

    /**
     * socket工厂
     */
    private SocketFactory socketFactory;

    /**
     * sslSocket工厂
     */
    private SSLSocketFactory sslSocketFactory;

    /**
     * 信任管理器
     */
    private X509TrustManager trustManager;

    /**
     * 主机名验证器
     */
    private HostnameVerifier hostnameVerifier;

    /**
     * 协议
     */
    private List<Protocol> protocols;

    /**
     * 事件监听器
     */
    private EventListener eventListener;

    /**
     * 身份认证器
     */
    private Authenticator authenticator;

    /**
     * 日志级别
     */
    private OkHttpLogLevelEnum logLevel;

    /**
     * 日志打印器
     */
    private OkHttpBaseLogger logger;

    /**
     * 构建{@link OkHttpClientBuilder}
     */
    public static OkHttpClientBuilder create() {
        return new OkHttpClientBuilder();
    }

    /**
     * 构建{@link OkHttpClient}
     */
    public OkHttpClient build() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, connectTimeoutUnit)
                .readTimeout(readTimeout, readTimeoutUnit)
                .writeTimeout(writeTimeout, writeTimeoutUnit)
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAlive, keepAliveUnit));

        if (callTimeout > 0L) {
            builder.callTimeout(callTimeout, callTimeoutUnit);
        }
        if (pingInterval > 0L) {
            builder.pingInterval(pingInterval, pingIntervalUnit);
        }
        if (Objects.nonNull(dispatcher)) {
            builder.dispatcher(dispatcher);
        }
        if (Objects.nonNull(proxy)) {
            builder.proxy(proxy);
        }
        if (Objects.nonNull(proxySelector)) {
            builder.proxySelector(proxySelector);
        }
        if (Objects.nonNull(cookieJar)) {
            builder.cookieJar(cookieJar);
        }
        if (Objects.nonNull(cache)) {
            builder.cache(cache);
        }
        if (Objects.nonNull(dns)) {
            builder.dns(dns);
        }
        if (Objects.nonNull(socketFactory)) {
            builder.socketFactory(socketFactory);
        }
        if (Objects.nonNull(sslSocketFactory) && Objects.nonNull(trustManager)) {
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            if (Objects.isNull(hostnameVerifier)) {
                // 默认信任所有 hostname
                hostnameVerifier = (s, sslSession) -> true;
            }
        }
        if (Objects.nonNull(hostnameVerifier)) {
            builder.hostnameVerifier(hostnameVerifier);
        }
        if (CollectionUtil.isNotEmpty(protocols)) {
            builder.protocols(protocols);
        }
        if (Objects.nonNull(eventListener)) {
            builder.eventListener(eventListener);
        }
        if (Objects.nonNull(authenticator)) {
            builder.authenticator(authenticator);
        }
        if (Objects.nonNull(logLevel) && OkHttpLogLevelEnum.NONE != logLevel && Objects.nonNull(logger)) {
            addInterceptor(new HttpLoggingInterceptor(logger).setLevel(logLevel.getLogLevel()));
        }
        interceptorList.forEach(builder::addInterceptor);
        return builder.build();
    }

    /**
     * 连接超时时间
     */
    public OkHttpClientBuilder connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 连接超时时间单位
     */
    public OkHttpClientBuilder connectTimeoutUnit(TimeUnit connectTimeoutUnit) {
        this.connectTimeoutUnit = connectTimeoutUnit;
        return this;
    }

    /**
     * 连接超时时间
     */
    public OkHttpClientBuilder connectTimeout(long connectTimeout, TimeUnit connectTimeoutUnit) {
        this.connectTimeout = connectTimeout;
        this.connectTimeoutUnit = connectTimeoutUnit;
        return this;
    }

    /**
     * 读取超时时间
     */
    public OkHttpClientBuilder readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * 读取超时时间单位
     */
    public OkHttpClientBuilder readTimeout(TimeUnit readTimeoutUnit) {
        this.readTimeoutUnit = readTimeoutUnit;
        return this;
    }

    /**
     * 读取超时时间
     */
    public OkHttpClientBuilder readTimeout(long readTimeout, TimeUnit readTimeoutUnit) {
        this.readTimeout = readTimeout;
        this.readTimeoutUnit = readTimeoutUnit;
        return this;
    }

    /**
     * 写超时时间
     */
    public OkHttpClientBuilder writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    /**
     * 写超时时间单位
     */
    public OkHttpClientBuilder writeTimeoutUnit(TimeUnit writeTimeoutUnit) {
        this.writeTimeoutUnit = writeTimeoutUnit;
        return this;
    }

    /**
     * 写超时时间
     */
    public OkHttpClientBuilder writeTimeout(long writeTimeout, TimeUnit writeTimeoutUnit) {
        this.writeTimeout = writeTimeout;
        this.writeTimeoutUnit = writeTimeoutUnit;
        return this;
    }

    /**
     * 最大空闲连接数
     */
    public OkHttpClientBuilder maxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
        return this;
    }

    /**
     * 保持连接时间
     */
    public OkHttpClientBuilder keepAlive(long keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    /**
     * 保持连接时间
     */
    public OkHttpClientBuilder keepAliveUnit(TimeUnit keepAliveUnit) {
        this.keepAliveUnit = keepAliveUnit;
        return this;
    }

    /**
     * 保持连接时间
     */
    public OkHttpClientBuilder keepAlive(long keepAlive, TimeUnit keepAliveUnit) {
        this.keepAlive = keepAlive;
        this.keepAliveUnit = keepAliveUnit;
        return this;
    }

    /**
     * 全局请求超时时间，未完成的请求，超时后，会取消请求
     */
    public OkHttpClientBuilder callTimeout(long callTimeout) {
        this.callTimeout = callTimeout;
        return this;
    }

    /**
     * 全局请求超时时间，未完成的请求，超时后，会取消请求
     */
    public OkHttpClientBuilder callTimeoutUnit(TimeUnit callTimeoutUnit) {
        this.callTimeoutUnit = callTimeoutUnit;
        return this;
    }

    /**
     * 全局请求超时时间，未完成的请求，超时后，会取消请求
     */
    public OkHttpClientBuilder callTimeout(long callTimeout, TimeUnit callTimeoutUnit) {
        this.callTimeout = callTimeout;
        this.callTimeoutUnit = callTimeoutUnit;
        return this;
    }

    /**
     * ping间隔时间，心跳间隔时间确认连接是否有效
     */
    public OkHttpClientBuilder pingInterval(long pingInterval) {
        this.pingInterval = pingInterval;
        return this;
    }

    /**
     * ping间隔时间，心跳间隔时间确认连接是否有效
     */
    public OkHttpClientBuilder pingIntervalUnit(TimeUnit pingIntervalUnit) {
        this.pingIntervalUnit = pingIntervalUnit;
        return this;
    }

    /**
     * ping间隔时间，心跳间隔时间确认连接是否有效
     */
    public OkHttpClientBuilder pingInterval(long pingInterval, TimeUnit pingIntervalUnit) {
        this.pingInterval = pingInterval;
        this.pingIntervalUnit = pingIntervalUnit;
        return this;
    }

    /**
     * 请求分发器，用于控制请求的并发数量
     */
    public OkHttpClientBuilder dispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    /**
     * 代理
     */
    public OkHttpClientBuilder proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    /**
     * 代理选择器，仅当{@link #proxy}为null时，才会生效
     */
    public OkHttpClientBuilder proxySelector(ProxySelector proxySelector) {
        this.proxySelector = proxySelector;
        return this;
    }

    /**
     * cookie 处理策略
     */
    public OkHttpClientBuilder cookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
        return this;
    }

    /**
     * 响应缓存
     */
    public OkHttpClientBuilder cache(Cache cache) {
        this.cache = cache;
        return this;
    }

    /**
     * 主机名IP地址DNS解析
     */
    public OkHttpClientBuilder dns(Dns dns) {
        this.dns = dns;
        return this;
    }

    /**
     * socket工厂
     */
    public OkHttpClientBuilder socketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
        return this;
    }

    /**
     * sslSocket工厂
     */
    public OkHttpClientBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    /**
     * 信任管理器
     */
    public OkHttpClientBuilder trustManager(X509TrustManager trustManager) {
        this.trustManager = trustManager;
        return this;
    }

    /**
     * 主机名验证器
     */
    public OkHttpClientBuilder hostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    /**
     * 协议
     */
    public OkHttpClientBuilder protocols(List<Protocol> protocols) {
        this.protocols = protocols;
        return this;
    }

    /**
     * 事件监听器
     */
    public OkHttpClientBuilder eventListener(EventListener eventListener) {
        this.eventListener = eventListener;
        return this;
    }

    /**
     * 身份认证器
     */
    public OkHttpClientBuilder authenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    /**
     * 添加拦截器
     */
    public OkHttpClientBuilder addInterceptor(Interceptor interceptor) {
        interceptorList.add(interceptor);
        return this;
    }

    /**
     * 设置日志打印
     */
    public OkHttpClientBuilder logInterceptor(OkHttpLogLevelEnum logLevel, OkHttpBaseLogger logger) {
        this.logLevel = logLevel;
        this.logger = logger;
        return this;
    }
}
