package cn.zs.tool.okhttp.log;

import lombok.Getter;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * okhttp 日志级别
 *
 * @author sheng
 */
@Getter
public enum OkHttpLogLevelEnum {
    /**
     * 无
     */
    NONE(HttpLoggingInterceptor.Level.NONE),

    /**
     * 请求、响应行
     */
    BASIC(HttpLoggingInterceptor.Level.BASIC),

    /**
     * 请求、响应行及各自头信息
     */
    HEADERS(HttpLoggingInterceptor.Level.HEADERS),

    /**
     * 请求、响应行及各自头信息，请求体、响应体
     */
    BODY(HttpLoggingInterceptor.Level.BODY),
    ;

    OkHttpLogLevelEnum(HttpLoggingInterceptor.Level logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * 日志级别
     */
    private final HttpLoggingInterceptor.Level logLevel;
}
