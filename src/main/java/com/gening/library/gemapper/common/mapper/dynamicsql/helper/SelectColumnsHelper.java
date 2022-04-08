package com.gening.library.gemapper.common.mapper.dynamicsql.helper;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.constant.SqlConstant;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.LeftJoin;

import com.gening.library.gemapper.common.mapper.dynamicsql.entity.SelectColumn;
import com.gening.library.gemapper.core.util.BeanUtils;
import com.gening.library.gemapper.core.util.CharsFormatUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className SelectColumnsHelper
 * @description SelectColumns帮助类
 * @date 2022/3/18 16:58
 */
@Slf4j
public class SelectColumnsHelper {

    /**
     * 对PO进行查询字段的SQL语句片段构建
     *
     * @param po   实体类
     * @param <PO> 实体类泛型
     * @return {@link String}
     */
    public static <PO extends GePO> String buildSelectColumns(final PO po) {
        // 获取selectColumns字段值
        List<SelectColumn> selectColumns = BeanUtils.getValueToListByParentFieldName(po, SqlConstant.SELECT_COLUMNS_PROPERTY_NAME, SelectColumn.class);
        return CollectionUtils.isEmpty(selectColumns) ? buildAllColumnsByModel(po) : buildColumnsBySelectColumns(selectColumns);
    }

    /**
     * 根据 selectColumns 字段值进行查询字段的构建
     *
     * @param selectColumns 指定查询字段值
     * @return {@link String}
     */
    private static String buildColumnsBySelectColumns(final List<SelectColumn> selectColumns) {
        return selectColumns.stream()
                .map(mp -> MessageFormat.format("{0} AS {1}", PoUtils.getColumnByModelAndProperty(mp.getModelClass(), mp.getPropertyName()), PoUtils.getColumnAliasByModelAndProperty(mp.getModelClass(), mp.getPropertyName())))
                .collect(Collectors.joining(", "));
    }

    /**
     * 未设置“selectColumns”值，根据实体类自身及关联查询实体类进行全字段的SQL语句片段构建
     *
     * @param po   实体类
     * @param <PO> 实体类泛型
     * @return {@link String}
     */
    private static <PO extends GePO> String buildAllColumnsByModel(final PO po) {
        final List<Class<?>> modelClasses = Stream.of(po).map(Object::getClass).collect(Collectors.toList());
        return BeanUtils.getValueToListByParentFieldName(po, SqlConstant.LEFT_JOIN_PROPERTY_NAME, LeftJoin.class).stream()
                .map(LeftJoin::getJoinClz)
                .collect(() -> modelClasses, List::add, List::addAll)
                .stream()
                .map(SelectColumnsHelper::buildAllColumnsByPo)
                .collect(Collectors.joining(","));
    }

    /**
     * 拼接全字段SQL片段
     *
     * @param clz 实体类类型
     * @return {@link String}
     */
    private static String buildAllColumnsByPo(final Class<?> clz) {
        // 获取表别名
        final String tableAlias = PoUtils.getTableAlias(clz);
        return Arrays.stream(PoUtils.getSelfFields(clz))
                // 过滤业务字段
                .filter(PoUtils::isBusinessField)
                // 进行SQL拼接，即 user.user_info AS user&user_info
                .map(f -> MessageFormat.format("{0} AS {1}",
                        tableAlias + "." + CharsFormatUtils.convertUnderLine(f.getName()),
                        tableAlias + "$" + CharsFormatUtils.convertUnderLine(f.getName())))
                .collect(Collectors.joining(", "));
    }
}
