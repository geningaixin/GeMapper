package com.gening.library.gemapper.core.annotation;

import java.lang.annotation.*;

/**
 * @author G
 * @version 1.0
 * @className Transient
 * @description 用于表示实体类属性与数据库无关联，用于注解PO属性
 * @date 2022/3/18 16:58
 */
@Documented
@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient {
}
