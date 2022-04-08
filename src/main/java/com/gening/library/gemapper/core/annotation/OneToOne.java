package com.gening.library.gemapper.core.annotation;

import java.lang.annotation.*;

/**
 * @author G
 * @version 1.0
 * @className OneToOne
 * @description 实体类一对一关联关系，用于注解PO属性，表示当前属性跟其他类有一对一关联，在关联查询时会直接将结果映射到对象值
 * @date 2022/3/18 16:58
 */
@Documented
@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {

    /**
     * 一对一关联对象类型
     *
     * @return {@link Class}
     */
    Class<?> modelClz();
}
