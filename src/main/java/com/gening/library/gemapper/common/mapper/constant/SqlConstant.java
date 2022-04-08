package com.gening.library.gemapper.common.mapper.constant;

/**
 * @author G
 * @version 1.0
 * @className SqlConstant
 * @description Mapper功能层常量定义，包括sql关键字等
 * @date 2022/3/18 16:58
 */
public class SqlConstant {
    public static final String SELECT_COLUMNS_PROPERTY_NAME = "selectColumns";
    public static final String LEFT_JOIN_PROPERTY_NAME = "leftJoins";
    public static final String WHERE_CONTENTS_PROPERTY_NAME = "conditions";
    public static final String ORDER_BYS_PROPERTY_NAME = "orderBys";

    public static final String WHERE_KEYWORD = " WHERE ";
    public static final String LEFT_JOIN_KEYWORD = " LEFT JOIN ";
    public static final String ORDER_BY_KEYWORD = " ORDER BY ";
    public static final String CONDITIONS_CONNECTOR_AND_KEYWORD = " AND ";
    public static final String CONDITIONS_CONNECTOR_OR_KEYWORD = " OR ";
}
