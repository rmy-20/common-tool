package cn.zs.tool.okhttp.log;

import cn.zs.tool.log.constant.LogLevelEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * okhttp slf4j日志
 *
 * @author sheng
 */
@Slf4j
public class OkHttpSlf4jLog extends OkHttpBaseLogger {
    /**
     * 日志级别
     */
    private final LogLevelEnum logLevel;

    /**
     * 创建{@link OkHttpSlf4jLog}
     *
     * @param logLevel 日志级别
     */
    public static OkHttpSlf4jLog create(LogLevelEnum logLevel) {
        return new OkHttpSlf4jLog(logLevel);
    }

    OkHttpSlf4jLog(LogLevelEnum logLevel) {
        this.logLevel = Objects.requireNonNull(logLevel, "logLevel must not be null");
    }

    @Override
    public void log(String msg) {
        log.atLevel(logLevel.getLevel()).log(msg);
    }
}
