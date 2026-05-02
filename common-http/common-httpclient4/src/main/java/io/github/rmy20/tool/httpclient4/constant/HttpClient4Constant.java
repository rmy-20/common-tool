package io.github.rmy20.tool.httpclient4.constant;

import io.github.rmy20.tool.httpclient4.HttpClient4Builder;
import org.apache.http.client.HttpClient;

/**
 * 常量
 *
 * @author sheng
 */
public class HttpClient4Constant {
    /**
     * #{@link HttpClient}
     */
    public static final HttpClient HTTP_CLIENT = HttpClient4Builder.create().build();
}
