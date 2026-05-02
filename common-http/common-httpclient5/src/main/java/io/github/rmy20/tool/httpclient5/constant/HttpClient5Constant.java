package io.github.rmy20.tool.httpclient5.constant;

import io.github.rmy20.tool.httpclient5.HttpClient5Builder;
import org.apache.hc.client5.http.classic.HttpClient;

/**
 * 常量
 *
 * @author sheng
 */
public class HttpClient5Constant {
    /**
     * #{@link HttpClient}
     */
    public static final HttpClient HTTP_CLIENT = HttpClient5Builder.create().build();

    // /**
    //  * #{@link HttpAsyncClient}
    //  */
    // public static final HttpAsyncClient HTTP_ASYNC_CLIENT = HttpAsyncClient5Builder.create().build();
}
