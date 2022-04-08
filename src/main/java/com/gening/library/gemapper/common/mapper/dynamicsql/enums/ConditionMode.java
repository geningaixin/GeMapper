package com.gening.library.gemapper.common.mapper.dynamicsql.enums;

/**
 * @author G
 * @version 1.0
 * @className ConditionMode
 * @description 条件类型
 * @date 2022/3/18 16:58
 */
public enum ConditionMode {
    /**
     * 自身条件比较
     */
    TO_SELF,

    /**
     * 比较属性
     */
    TO_PROPERTY,

    /**
     * 比较传入值
     */
    TO_VALUE
}
