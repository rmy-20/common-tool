package io.github.rmy20.tool.core.date;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期工具类
 *
 * @author sheng
 */
@Slf4j
@Getter
public enum DateTool {
    /**
     * {@link DateConstants#yyyy_MM_dd_HH_mm_ss yyyy-MM-dd HH:mm:ss}，如 2024-07-07 01:14:27
     */
    yyyy_MM_dd_HH_mm_ss(DateConstants.yyyy_MM_dd_HH_mm_ss),

    /**
     * {@link DateConstants#yyyy_MM_dd_HH_mm_ss_SSS yyyy-MM-dd HH:mm:ss.SSS}，如 2024-07-07 01:14:27.471
     */
    yyyy_MM_dd_HH_mm_ss_SSS(DateConstants.yyyy_MM_dd_HH_mm_ss_SSS),

    /**
     * {@link DateConstants#yyyy_MM_dd_T_HH_mm_ss_SSSZ yyyy-MM-dd'T'HH:mm:ss.SSSZ}，如 2024-07-07T01:14:27.471+0800
     * <p>
     * <ol>
     *     <li>yyyy_MM_dd_T_HH_mm_ss_SSSZ.format(ZonedDateTime.now())</li>
     *     <li>yyyy_MM_dd_T_HH_mm_ss_SSSZ.format(new Date())</li>
     * </ol>
     */
    yyyy_MM_dd_T_HH_mm_ss_SSSZ(DateConstants.yyyy_MM_dd_T_HH_mm_ss_SSSZ),

    /**
     * {@link DateConstants#yyyy_MM_dd_T_HH_mm_ssXXX yyyy-MM-dd'T'HH:mm:ssXXX}，如 2024-05-14T17:32:38+08:00
     * <p>
     * <ol>
     *     <li>yyyy_MM_dd_T_HH_mm_ssXXX.format(OffsetDateTime.now())</li>
     *     <li>yyyy_MM_dd_T_HH_mm_ssXXX.format(new Date())</li>
     * </ol>
     */
    yyyy_MM_dd_T_HH_mm_ssXXX(DateConstants.yyyy_MM_dd_T_HH_mm_ssXXX),

    /**
     * {@link DateConstants#yyyy_MM_dd_T_HH_mm_ss_SSSXXX yyyy-MM-dd'T'HH:mm:ss.SSSXXX}，如 2024-05-14T17:32:38.314+08:00
     * <p>
     * <ol>
     *     <li>yyyy_MM_dd_T_HH_mm_ss_SSSXXX.format(OffsetDateTime.now())</li>
     *     <li>yyyy_MM_dd_T_HH_mm_ss_SSSXXX.format(new Date())</li>
     * </ol>
     */
    yyyy_MM_dd_T_HH_mm_ss_SSSXXX(DateConstants.yyyy_MM_dd_T_HH_mm_ss_SSSXXX),

    /**
     * {@link DateConstants#yyyy_MM_dd_T_HH_mm_ss_nXXX yyyy-MM-dd'T'HH:mm:ss.nXXX}，如 2024-05-14T17:32:38.314360800+08:00
     * <p>
     * <ol>
     *     <li>yyyy_MM_dd_T_HH_mm_ss_nXXX.format(OffsetDateTime.now())</li>
     *     <li>yyyy_MM_dd_T_HH_mm_ss_nXXX.format(new Date())</li>
     * </ol>
     */
    yyyy_MM_dd_T_HH_mm_ss_nXXX(DateConstants.yyyy_MM_dd_T_HH_mm_ss_nXXX),

    /**
     * {@link DateConstants#yyyyMMddHHmmss yyyyMMddHHmmss}，如 20240707011427
     */
    yyyyMMddHHmmss(DateConstants.yyyyMMddHHmmss),

    /**
     * {@link DateConstants#yyMMddHHmmss yyMMddHHmmss}，如 240707011427
     */
    yyMMddHHmmss(DateConstants.yyMMddHHmmss),

    /**
     * {@link DateConstants#yyyy_MM_dd_HH_mm yyyy-MM-dd HH:mm}，如 2024-07-07 01:14
     */
    yyyy_MM_dd_HH_mm(DateConstants.yyyy_MM_dd_HH_mm),

    /**
     * {@link DateConstants#yyyyMMddHHmm yyyyMMddHHmm}，如 202407070114
     */
    yyyyMMddHHmm(DateConstants.yyyyMMddHHmm),

    /**
     * {@link DateConstants#yyyy_MM_dd yyyy-MM-dd}，如 2024-07-07
     */
    yyyy_MM_dd(DateConstants.yyyy_MM_dd),

    /**
     * {@link DateConstants#yyyyMMdd yyyyMMdd}，如 20240707
     */
    yyyyMMdd(DateConstants.yyyyMMdd),

    /**
     * {@link DateConstants#HH_mm_ss HH:mm:ss}，如 01:14:27
     */
    HH_mm_ss(DateConstants.HH_mm_ss),

    /**
     * {@link DateConstants#HHmmss HHmmss}，如 011427
     */
    HHmmss(DateConstants.HHmmss),

    /**
     * {@link DateConstants#HH_mm HH:mm}，如：15:15
     */
    HH_mm(DateConstants.HH_mm),

    /**
     * {@link DateConstants#HHmm HHmm}，如：151427
     */
    HHmm(DateConstants.HHmm),
    ;

    /**
     * 时间格式
     */
    public static final Map<String, DateTimeFormatter> FORMATTER_MAP = new ConcurrentHashMap<>(16);

    static {
        Arrays.stream(values()).forEach(value -> FORMATTER_MAP.put(value.getFormat(), value.getFormatter()));
    }

    /**
     * 根据格式获取{@link DateTimeFormatter}
     *
     * @param format 时间格式
     * @return {@link DateTimeFormatter}
     */
    public static DateTimeFormatter getFormatter(String format) {
        DateTimeFormatter formatter = FORMATTER_MAP.get(format);
        if (Objects.isNull(formatter)) {
            synchronized (DateTool.class) {
                formatter = FORMATTER_MAP.get(format);
                if (Objects.isNull(formatter)) {
                    formatter = DateTimeFormatter.ofPattern(format);
                    FORMATTER_MAP.put(format, formatter);
                }
            }
        }
        return formatter;
    }

    /**
     * 将传入的时间转化为{@link LocalDateTime}
     *
     * @param dateTime 时间
     * @param format   时间格式
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime parse(String dateTime, String format) {
        return parse(dateTime, getFormatter(format));
    }

    /**
     * 将传入的时间转化为{@link LocalDateTime}
     *
     * @param dateTime  时间
     * @param formatter {@link DateTimeFormatter} 格式
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime parse(String dateTime, DateTimeFormatter formatter) {
        return parse(dateTime, formatter, LocalDateTime::from);
    }

    /**
     * 将传入的时间转化为{@link LocalTime}
     *
     * @param time   时间
     * @param format 日期时间格式，如HH:mm
     * @return {@link LocalTime}
     */
    public static LocalTime parseLocalTime(String time, String format) {
        return parseLocalTime(time, getFormatter(format));
    }

    /**
     * 将传入的时间转化为{@link LocalTime}
     *
     * @param time      时间
     * @param formatter {@link DateTimeFormatter} 格式
     * @return {@link LocalTime}
     */
    public static LocalTime parseLocalTime(String time, DateTimeFormatter formatter) {
        return parse(time, formatter, LocalTime::from);
    }

    /**
     * 将传入的时间转化为{@link LocalDate}
     *
     * @param date   日期
     * @param format 日期时间格式，如yyyy-MM-dd
     * @return {@link LocalDate}
     */
    public static LocalDate parseLocalDate(String date, String format) {
        return parse(date, format, LocalDate::from);
    }

    /**
     * 将传入的时间转化为{@link LocalDate}
     *
     * @param date      时间
     * @param formatter {@link DateTimeFormatter} 格式
     * @return {@link LocalDate}
     */
    public static LocalDate parseLocalDate(String date, DateTimeFormatter formatter) {
        return parse(date, formatter, LocalDate::from);
    }

    /**
     * 将传入的时间转化为{@link T}
     *
     * @param time   时间
     * @param format 时间格式
     * @param query  {@link LocalDateTime#from 如 LocalDateTime::from}
     */
    public static <T> T parse(String time, String format, TemporalQuery<T> query) {
        return parse(time, getFormatter(format), query);
    }

    /**
     * 将传入的时间转化为{@link T}
     *
     * @param time      时间
     * @param formatter {@link DateTimeFormatter} 格式
     * @param query     {@link TemporalQuery }
     *                  <ol>
     *                      <li>{@link LocalDateTime#from LocalDateTime::from}</li>
     *                      <li>{@link ZonedDateTime#from ZonedDateTime::from}</li>
     *                      <li>{@link OffsetDateTime#from OffsetDateTime::from}</li>
     *                  </ol>
     */
    public static <T> T parse(String time, DateTimeFormatter formatter, TemporalQuery<T> query) {
        try {
            return formatter.parse(time, query);
        } catch (Exception e) {
            log.error("字符串转时间异常,date[{}],pattern[{}]", time, formatter, e);
        }
        return null;
    }

    /**
     * 将传入的时间转化为{@link Date}
     *
     * @param time   时间
     * @param format 时间格式
     * @return {@link Date}
     */
    public static Date parseDate(String time, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(time);
        } catch (Exception e) {
            log.error("字符串转时间异常,time[{}],format[{}]", time, format, e);
        }
        return null;
    }

    /**
     * 格式化
     *
     * @param time      {@link TemporalAccessor}时间
     * @param formatter {@link DateTimeFormatter} 格式
     * @return 格式化后的字符串
     */
    public static String format(TemporalAccessor time, DateTimeFormatter formatter) {
        if (Objects.nonNull(time)) {
            try {
                return formatter.format(time);
            } catch (Exception e) {
                log.error("格式化异常,time[{}],format[{}]", time, formatter, e);
            }
        }
        return null;
    }

    /**
     * 格式化
     *
     * @param time   {@link TemporalAccessor}时间
     * @param format 时间格式
     * @return 格式化后的字符串
     */
    public static String format(TemporalAccessor time, String format) {
        return format(time, getFormatter(format));
    }

    /**
     * 格式化
     *
     * @param date   {@link Date} 时间
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String format) {
        if (Objects.nonNull(date)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.format(date);
            } catch (Exception e) {
                log.error("格式化异常,time[{}],format[{}]", date, format, e);
            }
        }
        return null;
    }

    /**
     * 将传入的时间转化为{@link LocalDateTime}
     *
     * @param dateTime 时间
     * @return {@link LocalDateTime}
     */
    public LocalDateTime parse(String dateTime) {
        return parse(dateTime, getFormatter());
    }

    /**
     * 将传入的时间转化为{@link LocalTime}
     *
     * @param time 时间
     * @return {@link LocalTime}
     */
    public LocalTime parseLocalTime(String time) {
        return parseLocalTime(time, getFormatter());
    }

    /**
     * 将传入的时间转化为{@link LocalDate}
     *
     * @param date 日期
     * @return {@link LocalDate}
     */
    public LocalDate parseLocalDate(String date) {
        return parseLocalDate(date, getFormatter());
    }

    /**
     * 将传入的时间转化为{@link T}
     *
     * @param time  时间
     * @param query {@link TemporalQuery }
     *              <ol>
     *                  <li>{@link LocalDateTime#from LocalDateTime::from}</li>
     *                  <li>{@link ZonedDateTime#from ZonedDateTime::from}</li>
     *                  <li>{@link OffsetDateTime#from OffsetDateTime::from}</li>
     *              </ol>
     */
    public <T> T parse(String time, TemporalQuery<T> query) {
        return parse(time, getFormatter(), query);
    }

    /**
     * 将传入的时间转化为{@link Date}
     *
     * @param time 时间
     * @return {@link Date}
     */
    public Date parseDate(String time) {
        try {
            return getSimpleFormatter().parse(time);
        } catch (Exception e) {
            log.error("字符串转时间异常,time[{}],format[{}]", time, getFormat(), e);
        }
        return null;
    }

    /**
     * 格式化
     *
     * @param time {@link TemporalAccessor}时间
     * @return 格式化后的字符串
     */
    public String format(TemporalAccessor time) {
        return format(time, getFormatter());
    }

    /**
     * 格式化
     *
     * @param date {@link Date} 时间
     * @return 格式化后的字符串
     */
    public String format(Date date) {
        if (Objects.nonNull(date)) {
            try {
                return getSimpleFormatter().format(date);
            } catch (Exception e) {
                log.error("格式化异常,time[{}],format[{}]", date, getFormat(), e);
            }
        }
        return null;
    }

    /**
     * 判断time是否符合时间格式，校验时间
     *
     * @param time 时间
     * @return true为符合
     */
    public boolean conform(String time) {
        try {
            formatter.parse(time);
            return true;
        } catch (Exception e) {
            log.debug("时间格式不符合,time[{}],format[{}]", time, getFormat(), e);
        }
        return false;
    }

    DateTool(String format) {
        this.format = format;
        this.formatter = DateTimeFormatter.ofPattern(format);
    }

    /**
     * 格式
     */
    private final String format;

    /**
     * 格式
     */
    private final DateTimeFormatter formatter;

    /**
     * 获取当前实例格式的{@link SimpleDateFormat}
     */
    public SimpleDateFormat getSimpleFormatter() {
        return new SimpleDateFormat(format);
    }
}
