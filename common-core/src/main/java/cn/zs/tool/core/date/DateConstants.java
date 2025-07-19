package cn.zs.tool.core.date;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * 时间相关常量
 *
 * @author sheng
 */
public class DateConstants {
    /**
     * GMT+8时区
     */
    public static final String GMT_8 = "GMT+8";

    /**
     * GMT+8时区
     */
    public static final ZoneId GMT_8_ZONE_ID = ZoneId.of(GMT_8);

    /**
     * GMT+8时区
     */
    public static final TimeZone GMT_8_TIME_ZONE = TimeZone.getTimeZone(GMT_8_ZONE_ID);

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ
     */
    public static final String yyyy_MM_dd_T_HH_mm_ss_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**
     * yyyy-MM-dd'T'HH:mm:ssXXX，如 2024-05-14T17:32:38+08:00
     * <p>
     * <ol>
     *     <li>FORMAT_yyyy_MM_dd_T_HH_mm_ssXXX.format(OffsetDateTime.now())</li>
     *     <li>FORMAT_yyyy_MM_dd_T_HH_mm_ssXXX.format(new Date())</li>
     * </ol>
     */
    public static final String yyyy_MM_dd_T_HH_mm_ssXXX = "yyyy-MM-dd'T'HH:mm:ssXXX";

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSSXXXssXXX，如 2024-05-14T17:32:38.314+08:00
     * <p>
     * <ol>
     *     <li>FORMAT_yyyy_MM_dd_T_HH_mm_ss_SSSXXX.format(OffsetDateTime.now())</li>
     *     <li>FORMAT_yyyy_MM_dd_T_HH_mm_ss_SSSXXX.format(new Date())</li>
     * </ol>
     */
    public static final String yyyy_MM_dd_T_HH_mm_ss_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSSXXXssXXX，如 2024-05-14T17:32:38.314360800+08:00
     * <p>
     * <ol>
     *     <li>FORMAT_yyyy_MM_dd_T_HH_mm_ss_SSSXXX.format(OffsetDateTime.now())</li>
     *     <li>FORMAT_yyyy_MM_dd_T_HH_mm_ss_SSSXXX.format(new Date())</li>
     * </ol>
     */
    public static final String yyyy_MM_dd_T_HH_mm_ss_nXXX = "yyyy-MM-dd'T'HH:mm:ss.nXXX";

    /**
     * yyyyMMddHHmmss
     */
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    /**
     * yyMMddHHmmss
     */
    public static final String yyMMddHHmmss = "yyMMddHHmmss";

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

    /**
     * yyyyMMddHHmm
     */
    public static final String yyyyMMddHHmm = "yyyyMMddHHmm";

    /**
     * yyyy-MM-dd
     */
    public static final String yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * yyyyMMdd
     */
    public static final String yyyyMMdd = "yyyyMMdd";

    /**
     * HH:mm:ss
     */
    public static final String HH_mm_ss = "HH:mm:ss";

    /**
     * HHmmss
     */
    public static final String HHmmss = "HHmmss";

    /**
     * HH:mm，时分，如：15:15
     */
    public static final String HH_mm = "HH:mm";

    /**
     * HHmm
     */
    public static final String HHmm = "HHmm";
}
