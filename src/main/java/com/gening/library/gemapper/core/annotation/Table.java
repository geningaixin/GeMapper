package com.gening.library.gemapper.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author G
 * @version 1.0
 * @className Table
 * @description 实体类对应数据库表，用于注解PO类
 * @date 2022/3/18 16:58
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 当前实体类对应数据库表名
     *
     * @return {@link String}
     */
    String name();
}
