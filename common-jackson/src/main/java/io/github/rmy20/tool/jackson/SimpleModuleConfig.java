package io.github.rmy20.tool.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import io.github.rmy20.tool.core.date.DateTool;

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
    public static final SimpleModule JAVA_TIME_MODULE = new JavaTimeModule()
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
            .addSerializer(new ZonedDateTimeSerializer(DateTool.yyyy_MM_dd_T_HH_mm_ss_SSSXXX.getFormatter()))
            // OffsetDateTime 默认格式
            .addSerializer(new OffsetDateTimeSerializer(OffsetDateTimeSerializer.INSTANCE, false,
                    DateTool.yyyy_MM_dd_T_HH_mm_ss_SSSXXX.getFormatter(), JsonFormat.Shape.ANY));

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
