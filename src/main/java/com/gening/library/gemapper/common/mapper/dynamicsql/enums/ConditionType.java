package com.gening.library.gemapper.common.mapper.dynamicsql.enums;

/**
 * @author G
 * @version 1.0
 * @className ConditionType
 * @description 条件类型
 * @date 2022/3/18 16:58
 */
public enum ConditionType {

    /**
     * 是空
     */
    IS_NULL,

    /**
     * 非空
     */
    NOT_NULL,

    /**
     * 包含
     */
    IN,

    /**
     * 不包含
     */
    NOT_IN,

    /**
     * 大于
     */
    GT,

    /**
     * 大于等于
     */
    GTE,

    /**
     * 小于
     */
    LT,

    /**
     * 小于等于
     */
    LTE,

    /**
     * LIKE
     */
    LIKE,

    /**
     * 等于
     */
    EQ,

    /**
     * 不等于
     */
    NOT_EQ
}
