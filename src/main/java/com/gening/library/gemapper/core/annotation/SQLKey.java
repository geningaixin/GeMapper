package com.gening.library.gemapper.core.annotation;

import com.gening.library.gemapper.core.enums.SqlKeyType;

import java.lang.annotation.*;

/**
 * @author G
 * @version 1.0
 * @className SqlKey
 * @description 实体类对应数据库主键，用于注解PO属性，表示当前属性为对应表的主键字段
 * @date 2022/3/18 16:58
 */
@Documented
@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlKey {

    /**
     * 主键生成方式
     */
    SqlKeyType type() default SqlKeyType.AUTO;

    /**
     * 序列名称
     * @return {@link String}
     */
    String sequenceName() default "";
}
