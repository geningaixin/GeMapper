package com.gening.library.gemapper.common.mapper.dynamicsql.helper;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.constant.SqlConstant;
import com.gening.library.gemapper.common.mapper.exception.ModelResolveException;
import com.gening.library.gemapper.core.util.BeanUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.LeftJoin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className LeftJoinHelper
 * @description LeftJoin帮助类
 * @date 2022/3/18 16:58
 */
@Slf4j
public class LeftJoinHelper {

    /**
     * 构建PO的左关联查询内容
     *
     * @param po   po对象
     * @param <PO> PO泛型
     * @return {@link String}
     */
    public static <PO extends GePO> String buildLeftJoins(final PO po) {
        // 定义自增索引
        AtomicInteger index = new AtomicInteger(0);
        // 获取实体类leftJoins字段值
        List<LeftJoin> leftJoinList = BeanUtils.getValueToListByParentFieldName(po, SqlConstant.LEFT_JOIN_PROPERTY_NAME, LeftJoin.class);
        // 获取流进行数据处理并返回
        return leftJoinList.stream()
                // 构建LeftJoin内容
                .map(lj -> LeftJoinHelper.buildLeftJoin(lj, MessageFormat.format("{0}[{1}]", SqlConstant.LEFT_JOIN_PROPERTY_NAME, index.getAndIncrement())))
                // 拼接内容
                .collect(Collectors.joining());
    }

    /**
     * 构建LeftJoin
     *
     * @param leftJoin   LeftJoin对象
     * @param expression 当前LeftJoin值对象表达式，即“leftJoins[n]”
     * @return {@link String}
     */
    private static String buildLeftJoin(final LeftJoin leftJoin, String expression) {
        // 定义自增索引
        AtomicInteger index = new AtomicInteger(0);

        final String leftJoinOn = Stream.of(leftJoin)
                // 过滤有空的情况
                .filter(lj -> !ObjectUtils.isEmpty(lj.getJoinClz()) && !ObjectUtils.isEmpty(lj.getJoinOns()))
                // 获取joinOns
                .map(LeftJoin::getJoinOns)
                // 得到新List流
                .flatMap(List::stream)
                // 构建JoinOn内容
                .map(joinOn -> ConditionHelper.buildConditionContent(joinOn, MessageFormat.format("{0}.joinOns[{1}].value", expression, index.getAndIncrement())))
                // 多个JoinOn内容使用”AND“拼接
                .reduce((a, b) -> a + SqlConstant.CONDITIONS_CONNECTOR_AND_KEYWORD + b)
                // leftJoin的joinClz或joinOns为空时抛出异常
                .orElseThrow(() -> new ModelResolveException("动态SQL构建失败，LeftJoin对象未创建完整"));

        return MessageFormat.format(" LEFT JOIN {0} {1} ON {2}",
                PoUtils.getTableName(leftJoin.getJoinClz()),
                PoUtils.getTableAlias(leftJoin.getJoinClz()),
                leftJoinOn);
    }
}
