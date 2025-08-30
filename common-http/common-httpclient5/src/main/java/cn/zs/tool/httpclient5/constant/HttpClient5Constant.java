package cn.zs.tool.httpclient5.constant;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

/**
 * 常量
 *
 * @author sheng
 */
public class HttpClient5Constant {
    /**
     * #{@link HttpClient}
     */
    public static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    /**
     * #{@link CloseableHttpAsyncClient}
     */
    public static final CloseableHttpAsyncClient HTTP_ASYNC_CLIENT = HttpAsyncClients.createDefault();

    static {
        HTTP_ASYNC_CLIENT.start();
    }
}
