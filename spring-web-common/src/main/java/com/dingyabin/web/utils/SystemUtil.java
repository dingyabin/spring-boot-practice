package com.dingyabin.web.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class SystemUtil {

    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取两个时间之间的所有日期
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 中间的日期
     */
    public static List<LocalDate> getDateBetween(String startTime, String endTime) {
        LocalDate start = LocalDate.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate end = LocalDate.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE);
        //计算中间的间隔天数
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        //如果起始时间比结束时间晚
        if (daysBetween < 0) {
            return Collections.emptyList();
        }
        // +1 为了包含结束日
        return LongStream.range(0, daysBetween + 1).mapToObj(start::plusDays).collect(Collectors.toList());
    }


    /**
     * 获取两个时间之间的所有日期
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 中间的日期
     */
    public static List<String> getDateStrBetween(String startTime, String endTime) {
        return getDateStrBetween(startTime, endTime, DateTimeFormatter.ISO_LOCAL_DATE);
    }


    /**
     * 获取两个时间之间的所有日期
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 中间的日期
     */
    public static List<String> getDateStrBetween(String startTime, String endTime, DateTimeFormatter formatter) {
        List<LocalDate> dateBetween = getDateBetween(startTime, endTime);
        if (Objects.isNull(dateBetween) || dateBetween.isEmpty()) {
            return Collections.emptyList();
        }
        return dateBetween.stream().map(formatter::format).collect(Collectors.toList());
    }


    /**
     * 计算给定日期当天的起始时刻
     *
     * @param date 给定日期
     * @return 起始时刻
     */
    public static LocalDateTime getStartOfDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        return localDate.atTime(LocalTime.MIN);
    }

    /**
     * 计算给定日期当天的起始时刻
     *
     * @param date 给定日期
     * @return 起始时刻
     */
    public static String getStartStrOfDay(String date) {
        LocalDateTime startOfDay = getStartOfDay(date);
        return DEFAULT_FORMATTER.format(startOfDay);
    }


    /**
     * 计算给定日期当天的结束时刻
     *
     * @param date 给定日期
     * @return 结束时刻
     */
    public static LocalDateTime getEndOfDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        return localDate.atTime(LocalTime.MAX);
    }


    /**
     * 计算给定日期当天的结束时刻
     *
     * @param date 给定日期
     * @return 结束时刻
     */
    public static String getEndStrOfDay(String date) {
        LocalDateTime endOfDay = getEndOfDay(date);
        return DEFAULT_FORMATTER.format(endOfDay);
    }


//    public static void main(String[] args) {
//        System.out.println(getStartStrOfDay("2026-01-01"));
//        System.out.println(getEndStrOfDay("2026-01-01"));
//        System.out.println(getDateStrBetween("2026-01-01", "2026-01-10"));
//    }


}
