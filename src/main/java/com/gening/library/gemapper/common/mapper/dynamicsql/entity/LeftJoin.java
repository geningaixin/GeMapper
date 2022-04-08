package com.gening.library.gemapper.common.mapper.dynamicsql.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author G
 * @version 1.0
 * @className LeftJoin
 * @description 左关联对象
 * @date 2022/3/18 16:58
 */
@Getter
public class LeftJoin {

    private Class<?> joinClz;

    private List<Condition> joinOns;

    private LeftJoin() {
    }

    public static LeftJoin create() {
        return new LeftJoin();
    }

    public LeftJoin setClz(Class<?> joinClz) {
        this.joinClz = joinClz;
        return this;
    }

    public LeftJoin addOn(Condition joinOn) {
        if (this.joinOns == null) {
            this.joinOns = new ArrayList<>();
        }
        this.joinOns.add(joinOn);
        return this;
    }
}
