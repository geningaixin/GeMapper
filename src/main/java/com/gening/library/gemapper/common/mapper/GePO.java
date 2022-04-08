package com.gening.library.gemapper.common.mapper;

import com.gening.library.gemapper.common.mapper.dynamicsql.entity.Condition;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.LeftJoin;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.OrderBy;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.SelectColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author G
 * @version 1.0
 * @className GePO
 * @description PO基类
 * @date 2022/3/18 17:45
 */
@Getter
@NoArgsConstructor
public class GePO implements BasePO {

    /**
     * 查询字段，结果如：user.username | role.roleName
     */
    protected transient List<SelectColumn> selectColumns;

    /**
     * 关联的表
     */
    protected transient List<LeftJoin> leftJoins;

    /**
     * where条件，对应到实体类的属性名称
     */
    protected transient List<Condition> conditions;

    /**
     * 排序条件
     */
    protected transient List<OrderBy> orderBys;

    @Override
    public void addSelectColumn(SelectColumn selectColumn) {
        if (this.selectColumns == null) {
            this.selectColumns = new ArrayList<>();
        }
        this.selectColumns.add(selectColumn);
    }

    @Override
    public void addLeftJoin(LeftJoin leftJoin) {
        if (leftJoins == null) {
            this.leftJoins = new ArrayList<>();
        }
        this.leftJoins.add(leftJoin);
    }

    @Override
    public void addCondition(Condition condition) {
        if (conditions == null) {
            this.conditions = new ArrayList<>();
        }
        this.conditions.add(condition);
    }

    @Override
    public void addOrderBy(OrderBy orderBy) {
        if (orderBys == null) {
            this.orderBys = new ArrayList<>();
        }
        this.orderBys.add(orderBy);
    }
}
