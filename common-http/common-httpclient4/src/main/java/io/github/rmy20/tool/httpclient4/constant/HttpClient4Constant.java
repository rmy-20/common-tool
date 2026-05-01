package io.github.rmy20.tool.httpclient4.constant;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * 常量
 *
 * @author sheng
 */
public class HttpClient4Constant {
    public static final HttpClient HTTP_CLIENT;

    static {
        // 通过设置validityDeadline的值，控制没有正常关闭的连接释放的时间
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(60, TimeUnit.SECONDS);
        // 设置连接池允许创建的最大连接数量为500
        connectionManager.setMaxTotal(500);
        // 每个路由（域名）连接数500
        connectionManager.setDefaultMaxPerRoute(500);
        // 超时配置
        final RequestConfig requestConfig = RequestConfig.custom()
                // 从连接池获取连接的超时时间
                .setConnectionRequestTimeout(2000)
                // 客户端和服务器建立连接的超时时间
                .setConnectTimeout(2000)
                // 客户端和服务器建立连接后，数据传输过程中数据包之间间隔的最大时间
                .setSocketTimeout(5000)
                .build();

        HTTP_CLIENT = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                // 开启守护线程定时清理空闲连接
                .evictExpiredConnections()
                // 定时清理空闲连接的时间
                .evictIdleConnections(5, TimeUnit.SECONDS)
                .build();
    }
}
