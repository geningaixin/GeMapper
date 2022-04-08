package com.gening.library.gemapper.common.mapper.dynamicsql.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author G
 * @version 1.0
 * @className SelectColumn
 * @description 指定查询字段
 * @date 2022/4/6 13:06
 */
@Getter
@AllArgsConstructor
public class SelectColumn {
    private final Class<?> modelClass;
    private final String propertyName;
}
