package com.gening.library.gemapper.core.util;

import io.vavr.control.Try;

import java.text.SimpleDateFormat;

/**
 * @author G
 * @version 1.0
 * @className TimeUtils
 * @description 时间工具类
 * @date 2022/3/18 16:58
 */
public class TimeUtils {

    /**
     * 检查字符串是否为时间格式字符串
     *
     * @param time 目标字符串
     * @return {@link boolean}
     */
    public static boolean isValidDate(final String time) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        format.setLenient(false);
        return Try.of(() -> format.parse(time))
                .map(date -> true)
                .getOrElse(false);
    }
}
