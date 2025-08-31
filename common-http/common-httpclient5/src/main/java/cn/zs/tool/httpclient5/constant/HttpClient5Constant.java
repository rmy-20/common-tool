package cn.zs.tool.httpclient5.constant;

import cn.zs.tool.httpclient5.HttpAsyncClient5Builder;
import cn.zs.tool.httpclient5.HttpClient5Builder;
import org.apache.hc.client5.http.async.HttpAsyncClient;
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

    /**
     * #{@link HttpAsyncClient}
     */
    public static final HttpAsyncClient HTTP_ASYNC_CLIENT = HttpAsyncClient5Builder.create().build();
}
