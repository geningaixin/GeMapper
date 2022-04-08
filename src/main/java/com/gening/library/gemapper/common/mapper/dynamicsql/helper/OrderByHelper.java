package com.gening.library.gemapper.common.mapper.dynamicsql.helper;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.constant.SqlConstant;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.OrderBy;
import com.gening.library.gemapper.core.util.BeanUtils;
import com.gening.library.gemapper.core.util.CharsFormatUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.OrderByType;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className OrderByHelper
 * @description OrderBy帮助类
 * @date 2022/3/18 16:58
 */
public class OrderByHelper {

    /**
     * 构建OrderBy内容
     *
     * @param po   PO对象
     * @param <PO> PO泛型
     * @return {@link String}
     */
    public static <PO extends GePO> String buildOrderBys(PO po) {
        // 获取实体类”orderBys“字段值
        List<OrderBy> orderByList = BeanUtils.getValueToListByParentFieldName(po, SqlConstant.ORDER_BYS_PROPERTY_NAME, OrderBy.class);

        return orderByList.stream()
                // 构建OrderBy内容
                .map(ob -> MessageFormat.format("{0} {1}",
                        PoUtils.getColumnByModelAndProperty(ob.getModelClass(), ob.getProperty()),
                        ob.getOrderByType() == OrderByType.ASC ? "ASC" : "DESC"))
                // OrderBy内容拼接
                .reduce((a, b) -> a + "," + b)
                // 默认方案-主键OrderBy倒序Desc
                .orElseGet(() -> Stream.of(po)
                        // 获取poz主键
                        .map(PoUtils::getSimpleKeySqlField)
                        // 按主键倒序排序
                        .map(f -> MessageFormat.format("{0} {1}", PoUtils.getTableAlias(po) + "." + CharsFormatUtils.convertUnderLine(f.getName()), "DESC"))
                        .findFirst()
                        // 未得到主键抛出异常
                        .orElseThrow(() -> new RuntimeException("OrderBy内容构建失败，PO对象缺少主键"))
                );
    }
}
