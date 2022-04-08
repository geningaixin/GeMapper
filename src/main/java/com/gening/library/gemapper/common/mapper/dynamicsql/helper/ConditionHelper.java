package com.gening.library.gemapper.common.mapper.dynamicsql.helper;

import com.gening.library.gemapper.common.mapper.dynamicsql.enums.ConditionMode;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.ConditionType;
import com.gening.library.gemapper.core.constant.Constant;
import com.gening.library.gemapper.common.mapper.exception.ModelResolveException;
import com.gening.library.gemapper.core.util.NumberUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.core.util.TimeUtils;
import com.gening.library.gemapper.common.session.GeSqlSessionFactoryBean;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.Condition;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static io.vavr.API.*;
import static io.vavr.Predicates.is;

/**
 * @author G
 * @version 1.0
 * @className ConditionHelper
 * @description Condition操作帮助类
 * @date 2022/3/18 16:58
 */
public class ConditionHelper {

    /**
     * 构建单个Condition内容SQL
     *
     * @param condition  condition对象
     * @param expression 对应字段Mybatis表达式
     * @return {@link String}
     */
    public static String buildConditionContent(final Condition condition, final String expression) {
        final String columnName = PoUtils.getColumnByModelAndProperty(condition.getModelClass(), condition.getProperty());
        return Match(condition.getConditionType()).of(
                Case($(ConditionType.IS_NULL), () -> MessageFormat.format("{0} IS NULL", columnName)),
                Case($(ConditionType.NOT_NULL), () -> MessageFormat.format("{0} IS NOT NULL", columnName)),
                Case($(ConditionType.IN), () -> createInOrNotInSql(true, condition)),
                Case($(ConditionType.NOT_IN), () -> createInOrNotInSql(false, condition)),
                Case($(ConditionType.GT), () -> createLimitsSql(1, condition, expression)),
                Case($(ConditionType.GTE), () -> createLimitsSql(2, condition, expression)),
                Case($(ConditionType.LT), () -> createLimitsSql(3, condition, expression)),
                Case($(ConditionType.LTE), () -> createLimitsSql(4, condition, expression)),
                Case($(ConditionType.LIKE), () -> createLikeSql(condition, expression)),
                Case($(ConditionType.EQ), () -> MessageFormat.format("{0} = {1}", columnName, getTargetColumnOrExpression(condition, expression))),
                Case($(ConditionType.NOT_EQ), () -> MessageFormat.format("{0} != {1}", columnName, getTargetColumnOrExpression(condition, expression))),
                Case($(), () -> {
                    throw new RuntimeException("未知Where条件类型");
                })
        );
    }

    /**
     * 创建in条件表达式
     *
     * @param inOrNotIn in还是not in
     * @param condition Condition对象
     * @return {@link String}
     */
    public static String createInOrNotInSql(final boolean inOrNotIn, final Condition condition) {
        final String columnName = PoUtils.getColumnByModelAndProperty(condition.getModelClass(), condition.getProperty());
        final String content = Stream.of(condition.getValue())
                // 过滤是数组类型
                .filter(val -> val.getClass().isArray())
                // Object转数组
                .map(i -> (Object[]) i)
                // 得到数组元素流
                .flatMap(Arrays::stream)
                // Object转字符串
                .map(String::valueOf)
                // 是否是数字类型，是数字类型直接返回，不是数字类型加单引号包裹
                .map(item -> NumberUtils.isNumber(item) ? item : MessageFormat.format("''{0}''", item))
                // 拼接
                .reduce((a, b) -> a + ", " + b)
                // 处理异常情况
                .orElseThrow(() -> new ModelResolveException("in条件构建失败，传入值只支持数组"));

        return MessageFormat.format("{0} {1} ({2})", columnName, inOrNotIn ? "IN" : "NOT IN", content);
    }

    /**
     * 创建比较条件表达式
     *
     * @param type       类型
     * @param condition  Condition对象
     * @param expression 表达式
     * @return {@link String}
     */
    public static String createLimitsSql(final int type, final Condition condition, final String expression) {
        // 得到比较类型在Mybatis中的表达语句
        String keyword = Match(type).of(
                Case($(1), "&gt;"),
                Case($(2), "&gt;="),
                Case($(3), "&lt;"),
                Case($(4), "&lt;="),
                Case($(), () -> {
                    throw new RuntimeException("未知比较类型，必须是大于、大于等于、小于、小于等于");
                })
        );

        final String columnName = PoUtils.getColumnByModelAndProperty(condition.getModelClass(), condition.getProperty());
        final String targetColumnOrExpression = getTargetColumnOrExpression(condition, expression);

        return Optional.ofNullable(GeSqlSessionFactoryBean.JDBC_TYPE)
                // 过滤数据库类型为Oracle，且当前值是时间格式类型
                .filter(jdbc -> jdbc.equals(Constant.DB_TYPE_ORACLE) && TimeUtils.isValidDate(String.valueOf(condition.getValue())))
                // 转换成Oracle时间判断
                .map((jdbc) -> MessageFormat.format("{0} " + keyword + " TO_DATE({1}, ''yyyy/mm/dd hh24:mi:ss'')", columnName, targetColumnOrExpression))
                // 其他情况直接比较
                .orElse(MessageFormat.format("{0} " + keyword + " {1}", columnName, targetColumnOrExpression));
    }

    /**
     * 创建like条件表达式
     *
     * @param condition  Condition对象
     * @param expression 表达式
     * @return {@link String}
     */
    public static String createLikeSql(final Condition condition, final String expression) {
        final String columnName = PoUtils.getColumnByModelAndProperty(condition.getModelClass(), condition.getProperty());
        final String targetColumnOrExpression = getTargetColumnOrExpression(condition, expression);
        return Match(GeSqlSessionFactoryBean.JDBC_TYPE).of(
                Case($(is(Constant.DB_TYPE_MYSQL)), MessageFormat.format("{0} LIKE CONCAT(''%'',{1},''%'')", columnName, targetColumnOrExpression)),
                Case($(is(Constant.DB_TYPE_ORACLE)), MessageFormat.format("{0} LIKE ''%''||{1}||''%''", columnName, targetColumnOrExpression)),
                Case($(is(Constant.DB_TYPE_SQLSERVER)), MessageFormat.format("{0} LIKE ''%''+cast({1} as varchar)+''%''", columnName, targetColumnOrExpression)),
                Case($(is(Constant.DB_TYPE_POSTGRESQL)), MessageFormat.format("{0} LIKE CONCAT(''%'',{1},''%'')", columnName, targetColumnOrExpression)),
                Case($(is(Constant.DB_TYPE_UNKNOWN)), MessageFormat.format("{0} LIKE CONCAT(''%'',{1},''%'')", columnName, targetColumnOrExpression)),
                Case($(), () -> {
                    throw new RuntimeException("未获取到数据库类型");
                })
        );
    }

    /**
     * 根据条件比较模式，获取条件对象内容是其他表属性，还是对应条件值的表达式，如 user_info.create_time > #{conditons[0].value} 还是 user_info.create_time > role_info.tm
     *
     * @param condition  Condition对象
     * @param expression condition对象值Mybatis表达式
     * @return {@link String}
     */
    private static String getTargetColumnOrExpression(final Condition condition, final String expression) {
        return Match(condition.getMode()).of(
                Case($(is(ConditionMode.TO_PROPERTY)), () -> PoUtils.getColumnByModelAndProperty(condition.getTargetModelClass(), condition.getTargetProperty())),
                Case($(is(ConditionMode.TO_VALUE)), () -> expression),
                Case($(), () -> {
                    throw new RuntimeException("Condition Mode错误！当前条件mode必须为TO_PROPERTY或TO_VALUE");
                })
        );
    }
}
