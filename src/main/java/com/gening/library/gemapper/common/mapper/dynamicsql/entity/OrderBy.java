package com.gening.library.gemapper.common.mapper.dynamicsql.entity;

import com.gening.library.gemapper.common.mapper.dynamicsql.enums.OrderByType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author G
 * @version 1.0
 * @className OrderBy
 * @description OrderBy对象
 * @date 2022/3/18 16:58
 */
@Getter
@AllArgsConstructor
public class OrderBy {

    private final Class<?> modelClass;

    private final String property;

    private final OrderByType orderByType;
}
