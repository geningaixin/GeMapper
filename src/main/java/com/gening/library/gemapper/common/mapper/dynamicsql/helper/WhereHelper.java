package com.gening.library.gemapper.common.mapper.dynamicsql.helper;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.core.util.BeanUtils;
import com.gening.library.gemapper.core.util.CharsFormatUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.mapper.constant.SqlConstant;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.Condition;

import static io.vavr.API.*;
import static io.vavr.Predicates.*;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author G
 * @version 1.0
 * @className WhereHelper
 * @description Where条件帮助类
 * @date 2022/3/18 16:58
 */
public class WhereHelper {

    /**
     * 构建WHERE条件内容
     *
     * @param po   实体类
     * @param <PO> 实体类泛型
     * @return {@link String}
     */
    public static <PO extends GePO> String buildWhereContent(final PO po) {
        // 获取实体类条件集合属性值
        List<Condition> conditions = BeanUtils.getValueToListByParentFieldName(po, SqlConstant.WHERE_CONTENTS_PROPERTY_NAME, Condition.class);
        // 若条件集合取出为空，则解析实体类属性值进行条件拼接，默认只实现EQ的运算，否则解析ConditionList进行条件拼接
        return CollectionUtils.isEmpty(conditions) ? buildWhereContentByProperties(po) : buildWhereContentByConditions(conditions);
    }

    /**
     * 根据实体类属性进行WHERE条件构建
     *
     * @param po   实体类
     * @param <PO> 实体类泛型
     * @return {@link String}
     */
    private static <PO extends GePO> String buildWhereContentByProperties(final PO po) {
        return Arrays.stream(PoUtils.getSelfFields(po))
                .filter(PoUtils::isBusinessField)
                .filter(field -> !ObjectUtils.isEmpty(BeanUtils.getValueByField(po, field)))
                .map(field -> MessageFormat.format("{0} = {1}",
                        PoUtils.getTableAlias(po) + "." + CharsFormatUtils.convertUnderLine(field.getName()),
                        "#{" + field.getName() + "}"))
                .collect(Collectors.joining(SqlConstant.CONDITIONS_CONNECTOR_AND_KEYWORD));
    }

    /**
     * 根据Condition内容进行WHERE条件构建
     *
     * @param conditions 实体类conditions值
     * @return {@link String}
     */
    private static String buildWhereContentByConditions(final List<Condition> conditions) {
        return buildWhereContentByConditions(conditions, null, null);
    }

    /**
     * 根据Condition内容进行WHERE条件构建
     *
     * @param conditions       实体类conditions值
     * @param connector        连接符，如“and”或“or”
     * @param expressionPrefix 表达式前缀，默认为 conditions，进行OR递归后为 conditions[i].orConditionGroup.conditionGroups
     * @return {@link String}
     */
    private static String buildWhereContentByConditions(final List<Condition> conditions, String connector, String expressionPrefix) {
        // 连接符：AND、OR
        connector = !ObjectUtils.isEmpty(connector) ? connector : SqlConstant.CONDITIONS_CONNECTOR_AND_KEYWORD;
        // 表达式前缀 默认为 conditions，进行OR递归后为 conditions[i].orConditionGroup.conditionGroups
        final String prefix = !ObjectUtils.isEmpty(expressionPrefix) ? expressionPrefix + ".orGroups" : SqlConstant.WHERE_CONTENTS_PROPERTY_NAME;

        // 定义自增索引
        final AtomicInteger index = new AtomicInteger(0);
        return conditions.stream()
                .map(condition -> Match(condition.getOrGroups()).of(
                        Case($(isNotNull()), () -> buildOrGroupContent(condition.getOrGroups(), prefix, index.getAndIncrement())),
                        Case($(isNull()), () -> buildConditionContent(condition, prefix, index.getAndIncrement()))
                ))
                .collect(Collectors.joining(connector));
    }

    /**
     * 构建OR条件内容片段
     *
     * @param conditions OrGroup里的Condition List内容
     * @param prefix     当前值表达式前缀
     * @param index      当前下标
     * @return {@link String}
     */
    private static String buildOrGroupContent(final List<Condition> conditions, final String prefix, final int index) {
        final String expression = MessageFormat.format("{0}[{1}]", prefix, index);
        return "(" + buildWhereContentByConditions(conditions, SqlConstant.CONDITIONS_CONNECTOR_OR_KEYWORD, expression) + ")";
    }

    /**
     * 单个Condition内容片段
     *
     * @param condition 单个Condition对象
     * @param prefix    当前值表达式前缀
     * @param index     当前下标
     * @return {@link String}
     */
    private static String buildConditionContent(final Condition condition, final String prefix, final int index) {
        final String expression = MessageFormat.format("#'{'{0}[{1}].value'}'", prefix, index);
        return ConditionHelper.buildConditionContent(condition, expression);
    }
}
