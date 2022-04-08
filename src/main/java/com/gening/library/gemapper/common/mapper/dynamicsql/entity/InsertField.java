package com.gening.library.gemapper.common.mapper.dynamicsql.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author G
 * @version 1.0
 * @className InsertField
 * @description 新增模型字段对象
 * @date 2022/3/18 16:58
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertField {
    private String insertColumn;
    private String insertValue;
}
