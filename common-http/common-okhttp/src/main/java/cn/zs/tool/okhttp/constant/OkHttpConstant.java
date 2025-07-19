package cn.zs.tool.okhttp.constant;

import cn.zs.tool.log.constant.LogLevelEnum;
import cn.zs.tool.okhttp.OkHttpClientBuilder;
import cn.zs.tool.okhttp.log.OkHttpLogLevelEnum;
import cn.zs.tool.okhttp.log.OkHttpSlf4jLog;
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
