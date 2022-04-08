package com.gening.library.gemapper.common.mapper.dynamicsql.enums;

/**
 * @author G
 * @version 1.0
 * @className InsertOrUpdateMode
 * @description 修改或新增模式
 * @date 2022/3/18 16:58
 */
public enum InsertOrUpdateMode {
    /**
     * 保存空值，会替换掉数据库设定的默认值
     */
    FULL,
    /**
     * 不保存空值
     */
    SELECTIVE
}
