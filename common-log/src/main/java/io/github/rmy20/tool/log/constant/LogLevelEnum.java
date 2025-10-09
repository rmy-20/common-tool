package io.github.rmy20.tool.log.constant;

import lombok.Getter;
import org.slf4j.event.Level;

/**
 * 日志级别
 *
 * @author sheng
 */
@Getter
public enum LogLevelEnum {
    /**
     * TRACE，最低级别
     */
    TRACE(Level.TRACE),

    /**
     * DEBUG，多用于调式
     */
    DEBUG(Level.DEBUG),

    /**
     * INFO，默认级别
     */
    INFO(Level.INFO),

    /**
     * WARN，警告级别
     */
    WARN(Level.WARN),

    /**
     * ERROR，错误级别
     */
    ERROR(Level.ERROR),
    ;

    LogLevelEnum(Level level) {
        this.level = level;
    }

    /**
     * 日志级别
     */
    private final Level level;
}
