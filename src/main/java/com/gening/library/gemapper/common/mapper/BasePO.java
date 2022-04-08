package com.gening.library.gemapper.common.mapper;

import com.gening.library.gemapper.common.mapper.dynamicsql.entity.Condition;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.LeftJoin;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.OrderBy;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.SelectColumn;

/**
 * @author G
 * @version 1.0
 * @className BasePO
 * @description 基础PO接口类
 * @date 2022/3/18 17:45
 */
public interface BasePO {

    /**
     * 新增需要查询的字段
     *
     * @param selectColumn 需要查询的字段
     * @return {@link BasePO}
     */
    void addSelectColumn(SelectColumn selectColumn);

    /**
     * 新增左链接查询对象
     *
     * @param leftJoin 左连接查询对象
     * @return {@link BasePO}
     */
    void addLeftJoin(LeftJoin leftJoin);

    /**
     * 新增where查询条件
     *
     * @param condition 查询条件对象
     * @return {@link BasePO}
     */
    void addCondition(Condition condition);

    /**
     * 新增排序对象
     *
     * @param orderBy 排序对象
     * @return {@link BasePO}
     */
    void addOrderBy(OrderBy orderBy);
}
