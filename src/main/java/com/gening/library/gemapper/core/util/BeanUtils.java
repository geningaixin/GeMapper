package com.gening.library.gemapper.core.util;

import com.gening.library.gemapper.common.mapper.exception.BeanPropertyException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className BeanUtils
 * @description 类操作工具类，如根据字段获取值等
 * @date 2022/3/18 16:58
 */
public class BeanUtils {

    /**
     * 根据字段对象获取自身字段值
     *
     * @param target 目标类
     * @param f      Field
     * @return {@link String}
     */
    public static Object getValueByField(Object target, Field f) {
        f.setAccessible(Boolean.TRUE);
        try {
            return f.get(target);
        } catch (IllegalAccessException e) {
            throw new BeanPropertyException("实体类属性值获取失败！属性名 ： " + f.getName());
        }
    }

    /**
     * 根据字段名称获取自身字段值
     *
     * @param target    实体类
     * @param fieldName 字段名
     * @return {@link Object}
     */
    public static Object getValueByFieldName(Object target, String fieldName) {
        Field field;
        try {
            field = target.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new BeanPropertyException("通过字段名获取自身字段失败!属性名 : " + fieldName);
        }
        return getValueByField(target, field);
    }

    /**
     * 通过字段名称获取值->直接转成M类型
     *
     * @param target    实体类
     * @param fieldName 字段名称
     * @param clz       M泛型class
     * @param <M>       M泛型
     * @return {@link M}
     */
    public static <M> M getValueToEntityByFileName(Object target, String fieldName, Class<M> clz) {
        Object value = getValueByFieldName(target, fieldName);
        return clz.cast(value);
    }

    /**
     * 通过字段名称获取值->直接转成List<T>类型
     *
     * @param target    实体类
     * @param fieldName 字段名称
     * @param clz       List泛型class
     * @param <T>       List泛型
     * @return {@link List<T>}
     */
    public static <T> List<T> getValueToListByFieldName(Object target, String fieldName, Class<T> clz) {
        Object value = getValueByFieldName(target, fieldName);
        return Stream.of(value)
                .filter(obj -> obj instanceof ArrayList<?>)
                .map(obj -> (ArrayList<?>) obj)
                .flatMap(List::stream)
                .map(clz::cast)
                .collect(Collectors.toList());
    }

    /**
     * 根据字段名称获取父类字段值
     *
     * @param target    实体类
     * @param fieldName 字段名
     * @return {@link Object}
     */
    public static Object getValueByParentFieldName(Object target, String fieldName) {
        Field field;
        try {
            field = target.getClass().getSuperclass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new BeanPropertyException("通过字段名获取父类属性失败!属性名 : " + fieldName);
        }
        return getValueByField(target, field);
    }

    /**
     * 根据字段名称获取父类字段值->直接转成M类型
     *
     * @param target    实体类
     * @param fieldName 字段名称
     * @param clz       M泛型class
     * @param <M>       M泛型
     * @return {@link M}
     */
    public static <M> M getValueToEntityByParentFileName(Object target, String fieldName, Class<M> clz) {
        Object value = getValueByParentFieldName(target, fieldName);
        return clz.cast(value);
    }

    /**
     * 根据字段名称获取父类字段值->直接转成List<T>类型
     *
     * @param target    实体类
     * @param fieldName 字段名称
     * @param clz       List泛型class
     * @param <T>       List泛型
     * @return {@link List<T>}
     */
    public static <T> List<T> getValueToListByParentFieldName(Object target, String fieldName, Class<T> clz) {
        Object value = getValueByParentFieldName(target, fieldName);
        return Stream.of(value)
                .filter(obj -> obj instanceof ArrayList<?>)
                .map(obj -> (ArrayList<?>) obj)
                .flatMap(List::stream)
                .map(clz::cast)
                .collect(Collectors.toList());
    }


    /**
     * 根据字段对象赋值
     *
     * @param target 实体类
     * @param f      Field
     * @param value  实际值
     */
    public static void setValueByField(Object target, Field f, Object value) {
        f.setAccessible(Boolean.TRUE);
        try {
            f.set(target, value);
        } catch (IllegalAccessException e) {
            throw new BeanPropertyException("实体类属性值获取失败！属性名 ： " + f.getName());
        }
    }

    /**
     * 获取泛型Class
     *
     * @param target 目标Class
     * @return {@link Class<>}
     */
    public static Class<?> getGeneric(Class<?> target) {
        return Stream.of(target)
                // 获取实例对象父接口
                .map(Class::getGenericInterfaces)
                // 过滤数量大于0
                .filter(interfaces -> interfaces.length > 0)
                // 转化抽象父类为参数类,因为这里我们只有一个父接口,所以下表0位我们需要的
                .map(interfaces -> (ParameterizedType) interfaces[0])
                // 获取父接口的参数类型数组
                .map(ParameterizedType::getActualTypeArguments)
                // 因为我们父接口中泛型参数只有一个,所以泛型类型数组第一个就是我们的泛型类型,Class是Type的子类
                .map(types -> (Class<?>) types[0])
                // 返回
                .findFirst().orElseThrow(() -> new RuntimeException("获取泛型类型失败！"));
    }
}
