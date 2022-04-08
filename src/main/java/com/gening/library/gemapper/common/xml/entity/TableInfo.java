package com.gening.library.gemapper.common.xml.entity;

import com.gening.library.gemapper.core.annotation.*;
import com.gening.library.gemapper.core.util.CharsFormatUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.session.GeSqlSessionFactoryBean;
import lombok.Data;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author G
 * @version 1.0
 * @className TableInfo
 * @description 封装的表信息
 * @date 2022/3/18 17:45
 */
@Data
public class TableInfo {

    private static final Map<Class<?>, TableInfo> TABLE_CACHE = new ConcurrentHashMap<>(64);

    /**
     * 表名
     */
    private String tableName;

    /**
     * 类名
     */
    private String clzName;

    /**
     * xml对应命名空间
     */
    private String namespace;

    /**
     * 类型
     */
    private Class<?> clzType;

    /**
     * 主键属性名
     */
    private String pkProperty;

    /**
     * 主键对应的列名
     */
    private String pkColumn;

    /**
     * 属性名和字段名映射关系的List
     */
    private List<FieldInfo> fieldInfoList;

    private TableInfo(Class<?> clazz) {
        initTableInfo(clazz);
    }

    public static TableInfo forClass(Class<?> entityClass) {
        TableInfo tableMataDate = TABLE_CACHE.get(entityClass);
        if (tableMataDate == null) {
            tableMataDate = new TableInfo(entityClass);
            TABLE_CACHE.put(entityClass, tableMataDate);
        }
        return tableMataDate;
    }

    /**
     * 根据注解初始化表信息，
     *
     * @param clazz 实体类的 class
     */
    private void initTableInfo(Class<?> clazz) {
        // 获取表名称
        this.tableName = PoUtils.getTableName(clazz);
        // 类名
        this.clzName = clazz.getName();
        // XML空间名
        this.namespace = clazz.getName().replace("." + GeSqlSessionFactoryBean.PO_PACKAGE_KEYWORD, "." + GeSqlSessionFactoryBean.DAO_PACKAGE_KEYWORD) + "Mapper";

        this.fieldInfoList = Arrays.stream(PoUtils.getSelfFields(clazz))
                .filter(f -> !Modifier.isStatic(f.getModifiers()) && !f.isAnnotationPresent(Transient.class))
                .peek(f -> {
                    if (f.isAnnotationPresent(SqlKey.class)) {
                        this.pkProperty = f.getName();
                        this.pkColumn = CharsFormatUtils.convertUnderLine(clazz.getSimpleName()) + "$" + CharsFormatUtils.convertUnderLine(f.getName());
                    }
                })
                .map(f -> new FieldInfo(
                        f.getName(),
                        CharsFormatUtils.convertUnderLine(clazz.getSimpleName()) + "$" + CharsFormatUtils.convertUnderLine(f.getName()),
                        f.getType(),
                        f.getAnnotation(OneToOne.class),
                        f.getAnnotation(OneToMany.class)
                ))
                .collect(Collectors.toList());
    }
}
