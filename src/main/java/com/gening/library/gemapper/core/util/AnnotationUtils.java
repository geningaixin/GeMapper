package com.gening.library.gemapper.core.util;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className AnnotationUtils
 * @description 枚举工具类
 * @date 2022/4/1 9:59
 */
public class AnnotationUtils {

    /**
     * 修改类注解值
     *
     * @param clz             类Class
     * @param annotationClass 注解Class
     * @param propertyName    注解属性名
     * @param value           注解属性对应修正值
     * @param <T>             注解泛型
     */
    public static <T extends Annotation> void replaceClassAnnotationValue(final Class<?> clz, final Class<T> annotationClass, final String propertyName, final Object value) {
        replaceAnnotationValue(clz.getAnnotation(annotationClass), propertyName, value);
    }

    /**
     * 修改方法注解值
     *
     * @param method          方法对象
     * @param annotationClass 注解Class
     * @param propertyName    注解属性名
     * @param value           注解属性对应修正值
     * @param <T>             注解泛型
     */
    public static <T extends Annotation> void replaceMethodAnnotationValue(final Method method, final Class<T> annotationClass, final String propertyName, final Object value) {
        replaceAnnotationValue(method.getAnnotation(annotationClass), propertyName, value);
    }

    /**
     * 修改方法注解值
     *
     * @param field           字段对象
     * @param annotationClass 注解Class
     * @param propertyName    注解属性名
     * @param value           注解属性对应修正值
     * @param <T>             注解泛型
     */
    public static <T extends Annotation> void replaceFieldAnnotationValue(final Field field, final Class<T> annotationClass, final String propertyName, final Object value) {
        replaceAnnotationValue(field.getAnnotation(annotationClass), propertyName, value);
    }

    /**
     * 修改指定注解属性值
     *
     * @param annotation   注解对象
     * @param propertyName 注解属性名
     * @param value        注解属性对应修正值
     * @param <T>          注解泛型
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> void replaceAnnotationValue(final T annotation, final String propertyName, final Object value) {
        Stream.of(annotation)
                // 获取“AnnotationInvocationHandler”实例，实例中存贮了当前注解的所有属性值
                .map(Proxy::getInvocationHandler)
                // 转换成“ReplaceAnnotationValueEntity”实体，保存“AnnotationInvocationHandler”对象和“memberValues”字段对象（因为map只能传递一个值给下个操作符，所以用对象封装保存两个值）
                .map(handler -> new ReplaceAnnotationValueEntity(
                        handler,
                        Try.of(() -> handler.getClass().getDeclaredField("memberValues")).get()
                ))
                // 设置字段accessible为true，表示取消Java语言访问检查，达到提升反射速度
                .peek(entity -> entity.getMemberValuesField().setAccessible(true))
                // 获取“memberValues”属性值
                .map(entity -> Try.of(() -> entity.getMemberValuesField().get(entity.getHandler())).get())
                // 过滤当前值是否
                .filter(memberValues -> memberValues instanceof LinkedHashMap)
                .map(memberValues -> (LinkedHashMap<String, Object>) memberValues)
                .findFirst()
                .ifPresentOrElse(values -> values.put(propertyName, value), () -> {
                    throw new RuntimeException("注解获取失败，注解值无法修改");
                });
    }

    /**
     * 替换注解值实体，包括AnnotationInvocationHandler对象和对象中当前注解值存储字段，即memberValues
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ReplaceAnnotationValueEntity {
        private InvocationHandler handler;
        private Field memberValuesField;
    }
}
