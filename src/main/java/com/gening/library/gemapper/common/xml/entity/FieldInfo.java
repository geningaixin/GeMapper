package com.gening.library.gemapper.common.xml.entity;

import com.gening.library.gemapper.core.annotation.OneToMany;
import com.gening.library.gemapper.core.annotation.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author G
 * @version 1.0
 * @className FieldInfo
 * @description 封装的表字段信息
 * @date 2022/3/18 17:45
 */
@Getter
@Setter
@AllArgsConstructor
public class FieldInfo {

    /**
     * 对应Java属性名
     */
    private String property;

    /**
     * 列名
     */
    private String columnName;

    /**
     * java类型
     */
    private Class<?> javaType;

    /**
     * 一对一关联注解
     */
    private OneToOne toModel;

    /**
     * 一对多关联注解
     */
    private OneToMany toList;
}
