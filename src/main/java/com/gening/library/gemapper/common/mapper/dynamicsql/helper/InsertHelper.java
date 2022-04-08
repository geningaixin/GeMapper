package com.gening.library.gemapper.common.mapper.dynamicsql.helper;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.InsertField;
import com.gening.library.gemapper.core.annotation.SqlKey;
import com.gening.library.gemapper.core.enums.SqlKeyType;
import com.gening.library.gemapper.core.util.BeanUtils;
import com.gening.library.gemapper.core.util.CharsFormatUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.InsertContent;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.InsertOrUpdateMode;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static io.vavr.API.*;

/**
 * @author G
 * @version 1.0
 * @className InsertHelper
 * @description Insert帮助类
 * @date 2022/3/18 16:58
 */
public class InsertHelper {

    /**
     * InsertContent 组合方法
     */
    private static final BiFunction<InsertContent, InsertField, InsertContent> INSERT_CONTENT_HANDLER = (insertContent, insertField) -> {
        insertContent.setInsertColumns(!ObjectUtils.isEmpty(insertContent.getInsertColumns())
                ? insertContent.getInsertColumns().concat(", ").concat(insertField.getInsertColumn())
                : insertContent.getInsertColumns().concat(insertField.getInsertColumn()));

        insertContent.setInsertValues(!ObjectUtils.isEmpty(insertContent.getInsertValues())
                ? insertContent.getInsertValues().concat(", ").concat(insertField.getInsertValue())
                : insertContent.getInsertValues().concat(insertField.getInsertValue()));
        return insertContent;
    };

    /**
     * InsertContent 组合方法，用于合并多个线程的result值
     */
    private static final BinaryOperator<InsertContent> INSERT_CONTENT_BINARY_OPERATOR = (insertContent, item) -> {
        insertContent.setInsertColumns(insertContent.getInsertColumns().concat(",").concat(item.getInsertColumns()));
        insertContent.setInsertValues(insertContent.getInsertValues().concat(",").concat(item.getInsertValues()));
        return insertContent;
    };

    /**
     * 构建InsertContent
     *
     * @param po   新增实体类对象
     * @param mode 新增模式：保存空值 | 不保存空值
     * @param <PO> 实体类泛型
     * @return {@link InsertContent}
     */
    public static <PO extends GePO> InsertContent buildInsertContent(PO po, InsertOrUpdateMode mode) {
        return Arrays.stream(PoUtils.getSelfFields(po))
                // 获取PO中的所有业务字段，去除静态字段、@Transient，@OneToOne，@OneToMany注解字段
                .filter(PoUtils::isBusinessField)
                // 获取新增字段集合
                .map(f -> handleField(f, po, mode))
                // 过滤值为空值不处理
                .filter(Objects::nonNull).filter(insertField -> !ObjectUtils.isEmpty(insertField.getInsertColumn()) && !ObjectUtils.isEmpty(insertField.getInsertValue()))
                // 组合InsertContent对象
                .reduce(new InsertContent(), INSERT_CONTENT_HANDLER, INSERT_CONTENT_BINARY_OPERATOR);
    }

    /**
     * 字段处理
     *
     * @param field     当前字段
     * @param po        实体类对象
     * @param mode      新增模式：保存空值 | 不保存空值
     * @param <PO>实体类泛型
     * @return {@link InsertField}
     */
    private static <PO extends GePO> InsertField handleField(final Field field, final PO po, final InsertOrUpdateMode mode) {
        return Stream.of(field)
                // 过滤主键，即非主键情况
                .filter(f -> !f.isAnnotationPresent(SqlKey.class))
                // 值不为空且或为全量模式 -> 创建新增字段实体并初始化字段、表达式，否则返回空的字段实体
                .map(f -> !ObjectUtils.isEmpty(BeanUtils.getValueByField(po, f)) || mode == InsertOrUpdateMode.FULL
                        ? new InsertField(CharsFormatUtils.convertUnderLine(f.getName()), "#{" + f.getName() + "}")
                        : new InsertField())
                .findFirst()
                // 处理字段为主键情况
                .orElseGet(() -> handleSqlKeyField(field));
    }

    /**
     * 主键字段处理
     *
     * @param field 当前字段
     * @return {@link InsertField}
     */
    private static InsertField handleSqlKeyField(final Field field) {
        return Match(field.getAnnotation(SqlKey.class).type()).of(
                Case($(SqlKeyType.AUTO), InsertField::new),
                Case($(SqlKeyType.INSERT), () -> new InsertField(CharsFormatUtils.convertUnderLine(field.getName()), "#{" + field.getName() + "}")),
                Case($(SqlKeyType.SEQUENCE), () -> new InsertField(CharsFormatUtils.convertUnderLine(field.getName()), field.getAnnotation(SqlKey.class).sequenceName().concat(".nextval"))),
                Case($(), () -> {
                    throw new RuntimeException("主键为未知类型");
                })
        );
    }
}
