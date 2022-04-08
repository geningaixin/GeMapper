package com.gening.library.gemapper.core.enums;

/**
 * @author G
 * @version 1.0
 * @className SqlKeyType
 * @description 表示主键生成规则
 * @date 2022/3/18 16:58
 */
public enum SqlKeyType {
    /**
     * 自增长
     */
    AUTO,
    /**
     * 序列
     */
    SEQUENCE,
    /**
     * 手动插入值
     */
    INSERT
}
