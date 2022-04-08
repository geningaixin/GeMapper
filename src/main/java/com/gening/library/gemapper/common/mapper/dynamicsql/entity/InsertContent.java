package com.gening.library.gemapper.common.mapper.dynamicsql.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author G
 * @version 1.0
 * @className InsertContent
 * @description 新增模型对象
 * @date 2022/3/18 16:58
 */
@Getter
@Setter
public class InsertContent {

    private String insertColumns;
    private String insertValues;

    public InsertContent() {
        this.insertColumns = "";
        this.insertValues = "";
    }
}
