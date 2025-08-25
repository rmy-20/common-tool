package cn.zs.tool.httpclient5.constant;

import org.apache.hc.client5.http.classic.HttpClient;
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
}
