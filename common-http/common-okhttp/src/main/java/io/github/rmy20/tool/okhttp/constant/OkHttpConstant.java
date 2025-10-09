package io.github.rmy20.tool.okhttp.constant;

import io.github.rmy20.tool.log.constant.LogLevelEnum;
import io.github.rmy20.tool.okhttp.OkHttpClientBuilder;
import io.github.rmy20.tool.okhttp.log.OkHttpLogLevelEnum;
import io.github.rmy20.tool.okhttp.log.OkHttpSlf4jLog;
import okhttp3.OkHttpClient;

/**
 * okhttp 常量
 *
 * @author sheng
 */
public class OkHttpConstant {
    /**
     * 默认{@link OkHttpClient}
     */
    public static final OkHttpClient HTTP_CLIENT = OkHttpClientBuilder.create()
            .logInterceptor(OkHttpLogLevelEnum.BASIC, OkHttpSlf4jLog.create(LogLevelEnum.INFO))
            .build();
}
