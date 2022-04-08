package com.gening.library.gemapper.core.util;

import java.util.regex.Pattern;

/**
 * @author G
 * @version 1.0
 * @className NumberUtils
 * @description 数字相关工具类
 * @date 2022/3/21 17:29
 */
public class NumberUtils {

    /**
     * 是否为数字
     * @param target 目标字符串
     * @return {@link Boolean}
     */
    public static Boolean isNumber(String target) {
        String rule = "[0-9]*\\.?[0-9]+";
        return Pattern.compile(rule).matcher(target).matches();
    }
}
