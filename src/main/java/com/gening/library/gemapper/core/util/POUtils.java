package com.gening.library.gemapper.core.util;

import com.gening.library.gemapper.core.annotation.*;
import com.gening.library.gemapper.common.mapper.exception.ModelLoadingException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author G
 * @version 1.0
 * @className PoUtils
 * @description Po实体类相关操作工具类
 * @date 2022/3/18 16:58
 */
public class PoUtils {

    /**
     * 获取实体类在数据库里的对应的表名
     *
     * @param po 实体类
     * @return {@link String}
     */
    public static String getTableName(Object po) {
        return getTableName(po.getClass());
    }

    /**
     * 获取实体类在数据库里别名，Alias
     *
     * @param po 实体类
     * @return {@link String}
     */
    public static String getTableAlias(Object po) {
        return getTableAlias(po.getClass());
    }

    /**
     * 获取实体类在数据库里的对应的表名
     *
     * @param clz 实体类Class
     * @return {@link String}
     */
    public static String getTableName(Class<?> clz) {
        return Optional.ofNullable(clz)
                .filter(c -> c.isAnnotationPresent(Table.class))
                .map(c -> c.getAnnotation(Table.class).name())
                .orElseThrow(() -> new ModelLoadingException("实体类类型为空或未存在对应表名"));
    }

    /**
     * 获取实体类在数据库里别名，Alias
     *
     * @param clz 实体类Class
     * @return {@link String}
     */
    public static String getTableAlias(Class<?> clz) {
        return CharsFormatUtils.convertUnderLine(clz.getSimpleName());
    }

    /**
     * 获取自身所有字段数据
     *
     * @param po 实体类
     * @return {@link Field[]}
     */
    public static Field[] getSelfFields(Object po) {
        return getSelfFields(po.getClass());
    }

    /**
     * 获取自身所有字段数据
     *
     * @param clz Class
     * @return {@link Field[]}
     */
    public static Field[] getSelfFields(Class<?> clz) {
        Field[] fields = clz.getDeclaredFields();
        if (fields.length <= 0) {
            throw new ModelLoadingException("实体类未定义字段!实体类 ：" + clz.getName());
        }
        return fields;
    }

    /**
     * 获取单个主键字段
     *
     * @param po 实体类
     * @return {@link Field}
     */
    public static Field getSimpleKeySqlField(Object po) {
        return getSimpleKeySqlField(po.getClass());
    }

    /**
     * 获取单个主键字段
     *
     * @param clz Class
     * @return {@link Field}
     */
    public static Field getSimpleKeySqlField(Class<?> clz) {
        return Arrays.stream(getSelfFields(clz))
                .filter(f -> f.isAnnotationPresent(SqlKey.class))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取业务字段
     *
     * @param field 实体列字段
     * @return {@link Boolean}
     */
    public static boolean isBusinessField(Field field) {
        return !Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(Transient.class)
                && !field.isAnnotationPresent(OneToOne.class) && !field.isAnnotationPresent(OneToMany.class);
    }

    /**
     * 通过实体类.属性名称得到数据库字段名全称，如：userInfo.userName转成user_info.user_name
     *
     * @param model    实体类
     * @param property 实体类对应属性
     * @return {@link String}
     */
    public static String getColumnByModelAndProperty(Class<?> model, String property) {
        return CharsFormatUtils.convertUnderLine(model.getSimpleName()) + "." + CharsFormatUtils.convertUnderLine(property);
    }

    /**
     * 通过实体类.属性名称得到数据库字段名全称，如：userInfo.userName转成user_info.user_name
     *
     * @param model    实体类
     * @param property 实体类对应属性
     * @return {@link String}
     */
    public static String getColumnAliasByModelAndProperty(Class<?> model, String property) {
        return CharsFormatUtils.convertUnderLine(model.getSimpleName()) + "$" + CharsFormatUtils.convertUnderLine(property);
    }
}
