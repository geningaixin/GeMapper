package com.gening.library.gemapper.common.typehandler.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className GenericEnumHelper
 * @description 通用枚举帮助类
 * @date 2022/3/18 17:45
 */
public class GenericEnumHelper {

    /**
     * 判断是否是基础枚举类型
     *
     * @param rawType 传入Class
     * @param <T>     泛型
     * @return {@link Boolean}
     */
    public static <T> Boolean isGenericEnum(Class<T> rawType) {
        return Stream.of(rawType)
                .filter(Class::isEnum)
                .map(Class::getInterfaces)
                .flatMap(Arrays::stream)
                .filter(clz -> clz.getName().equals(GenericEnum.class.getName()))
                .map(clz -> true)
                .findFirst()
                .orElse(false);
    }

    /**
     * 获取基础枚举实现类描述内容
     *
     * @param fieldType 实现类
     * @return {@link List<String>}
     */
    public static List<String> buildEnumDescription(Class<?> fieldType) {
        return Stream.of(fieldType)
                .map(Class::getEnumConstants)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(GenericEnum.class::isInstance)
                .map(GenericEnum.class::cast)
                .map(item -> item.getValue() + ":" + item.getDescription())
                .collect(Collectors.toList());
    }

    /**
     * 根据value值获取具体枚举子类对应的枚举值
     *
     * @param enumClass 枚举子类类型
     * @param code      枚举子类对应的value值
     * @param <E>       枚举子类泛型
     * @return {@link E}
     */
    public static <E extends GenericEnum<Object, Object, Enum<?>>> E valueOf(Class<E> enumClass, Object code) {
        return Stream.of(enumClass)
                .map(Class::getEnumConstants)
                .flatMap(Arrays::stream)
                .filter(e -> e.getValue().toString().equals(code.toString()))
                .findFirst()
                .orElse(null);
    }
}
