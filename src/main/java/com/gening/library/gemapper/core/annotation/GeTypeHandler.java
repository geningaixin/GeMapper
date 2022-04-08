package com.gening.library.gemapper.core.annotation;

import java.lang.annotation.*;

/**
 * @author G
 * @version 1.0
 * @className GeTypeHandler
 * @description Mybatis自定义TypeHandler注解，被注解后会被Mybatis扫描注入，扫描代码在GeSqlSessionFactoryBean.java中
 * @date 2022/3/18 16:58
 */
@Documented
@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface GeTypeHandler {
}
