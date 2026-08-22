package io.github.rmy20.tool.jackson.v3;

import io.github.rmy20.tool.core.date.DateTool;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.OffsetDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.ZonedDateTimeSerializer;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Jackson 模块配置
 *
 * @author sheng
 */
public class SimpleModuleConfig {
    /**
     * java8 时间模块
     */
    public static final SimpleModule JAVA_TIME_MODULE = new SimpleModule()
            // LocalDateTime 默认格式
            .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTool.yyyy_MM_dd_HH_mm_ss.getFormatter()))
            .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTool.yyyy_MM_dd_HH_mm_ss.getFormatter()))
            // LocalDate 默认格式
            .addSerializer(new LocalDateSerializer(DateTool.yyyy_MM_dd.getFormatter()))
            .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTool.yyyy_MM_dd.getFormatter()))
            // LocalTime 默认格式
            .addSerializer(new LocalTimeSerializer(DateTool.HH_mm_ss.getFormatter()))
            .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTool.HH_mm_ss.getFormatter()))
            // ZonedDateTime 默认格式
            .addSerializer(new ZonedDateTimeSerializer(DateTool.yyyy_MM_dd_T_HH_mm_ss_SSSZ.getFormatter()))
            // OffsetDateTime 默认格式
            .addSerializer(OffsetDateTimeSerializer.INSTANCE);

    /**
     * 大数格式化
     */
    public static final SimpleModule BIG_NUM_MODULE = new SimpleModule()
            // long 大数字精度丢失
            .addSerializer(Long.class, ToStringSerializer.instance)
            .addSerializer(Long.TYPE, ToStringSerializer.instance)
            // BigInteger、BigDecimal
            .addSerializer(BigInteger.class, ToStringSerializer.instance)
            .addSerializer(BigDecimal.class, ToStringSerializer.instance);
}
