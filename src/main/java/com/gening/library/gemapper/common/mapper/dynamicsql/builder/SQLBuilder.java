package com.gening.library.gemapper.common.mapper.dynamicsql.builder;

/**
 * @author G
 * @version 1.0
 * @className SqlBuilder
 * @description 数据库动态SQL实现
 * @date 2022/3/18 16:58
 */
public interface SqlBuilder {

    /**
     * 构建SQL语句
     * @return {@link String}
     */
    String build();
}
