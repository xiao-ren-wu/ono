package org.ywb.ono.toolkit;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Date util
 *
 * @author yuwenbo
 * @version v1.0.0
 * @since 2020/4/28 19:03
 */
public class DateUtils {

    private static final String DEFAULT_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳
     */
    public static Long getCurrentTimeStamp() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当前秒数
     *
     * @return 当前秒数
     */
    public static Long getCurrentTimeSecond() {
        return Instant.now().getEpochSecond();
    }

    /**
     * Date转换为LocalDateTime
     *
     * @param date {@link Date}
     * @since 1.0.1
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        //An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        Instant instant = date.toInstant();
        //A time-zone ID, such as {@code Europe/Paris}.(时区)
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param localDateTime {@link LocalDateTime}
     * @since 1.0.1
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        //Combines this date-time with a time-zone to create a  ZonedDateTime.
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 时间戳转换成{@link LocalDateTime}
     *
     * @param timestamp long
     * @return LocalDateTime
     */
    public static LocalDateTime timeStamp2LocalDateTime(Long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
    }

    /**
     * 时间戳格式化，默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp long
     * @return String
     */
    public static String format(Long timestamp) {
        return format(timeStamp2LocalDateTime(timestamp), DEFAULT_FORMATTER);
    }

    /**
     * 时间戳格式化,指定格式化格式
     *
     * @param timestamp long
     * @param formatter String
     * @return String
     */
    public static String format(Long timestamp, String formatter) {
        return format(timeStamp2LocalDateTime(timestamp), formatter);
    }

    /**
     * 时间格式化，默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime LocalDatetime
     * @return String
     */
    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, DEFAULT_FORMATTER);
    }

    /**
     * 时间格式化
     *
     * @param localDateTime LocalDatetime
     * @param formatter     String
     * @return String
     */
    public static String format(LocalDateTime localDateTime, String formatter) {
        return formatCore(localDateTime, formatter);
    }

    /**
     * 当前时间格式化
     *
     * @param formatter String
     * @return String
     */
    public static String format(String formatter) {
        return format(LocalDateTime.now(), formatter);
    }

    /**
     * 当前时间格式化
     *
     * @return String
     */
    public static String format() {
        return format(LocalDateTime.now(), DEFAULT_FORMATTER);
    }

    /**
     * 秒转localDateTime
     *
     * @param second second
     * @return localDateTime
     */
    public static LocalDateTime second2LocalDateTime(long second) {
        return LocalDateTime.ofEpochSecond(second, 0, ZoneOffset.ofHours(8));
    }

    /**
     * 毫秒转localDateTime
     *
     * @param millis millis second
     * @return localDateTime
     */
    public static LocalDateTime millionSecond2LocalDateTime(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    private static String formatCore(LocalDateTime localDateTime, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        return dateTimeFormatter.format(localDateTime);
    }

}
