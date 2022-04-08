package com.gening.library.gemapper.core.util;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author G
 * @version 1.0
 * @className CharsFormatUtils
 * @description 字符格式化工具 - 用于驼峰和下划线名称转换
 * @date 2022/3/18 16:58
 */
public class CharsFormatUtils {

    /**
     * 驼峰转下划线
     *
     * @param modelName 实体类名/属性名称
     * @return {@link String}
     */
    public static String convertUnderLine(String modelName) {
        // 首字母转小写
        modelName = Optional.of(modelName)
                .filter(s -> !ObjectUtils.isEmpty(s))
                .map(s -> s.substring(0, 1).toLowerCase() + s.substring(1))
                .orElseThrow(() -> new NullPointerException("实体类名称不能为空"));

        // 截取字符串，获取每个字符得到流对象
        return Arrays.stream(modelName.split(""))
                // 当前字符如果是大写则返回 “_”+字符，否则直接返回
                .map(s -> Character.isUpperCase(s.charAt(0)) ? "_" + s.toLowerCase() : s)
                // 最后拼接
                .collect(Collectors.joining());
    }

    /**
     * 下划线转驼峰
     *
     * @param dbFieldName 目标字符串
     * @return {@link String}
     */
    public static String convertHump(String dbFieldName) {
        // 将首字母转大写，其他转小写
        dbFieldName = Optional.of(dbFieldName)
                .filter(s -> !ObjectUtils.isEmpty(s))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .orElseThrow(() -> new NullPointerException("表名或字段名不能为空"));

        // 截取“_”得到字符数组的流对象
        return Arrays.stream(dbFieldName.split("_"))
                // 非空值校验
                .filter(s -> !ObjectUtils.isEmpty(s))
                // 如果流对象首字母是大写，则证明为第一组，直接将首字母转小写其他保持不变，否则将首字母转大写，其他保持不变，维持驼峰规则
                .map(s -> Character.isUpperCase(s.charAt(0)) ? s.substring(0, 1).toLowerCase() + s.substring(1) : s.substring(0, 1).toUpperCase() + s.substring(1))
                // 最后拼接
                .collect(Collectors.joining());
    }
}
