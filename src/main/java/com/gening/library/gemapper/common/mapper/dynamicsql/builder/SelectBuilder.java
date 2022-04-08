package com.gening.library.gemapper.common.mapper.dynamicsql.builder;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.mapper.dynamicsql.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className SelectBuilder
 * @description 数据库查询SQL实现
 * @date 2022/3/18 16:58
 */
@Slf4j
public class SelectBuilder implements SqlBuilder {

    private final String selectColumns;
    private final String mainTable;
    private final String leftJoin;
    private String whereContent;
    private String orderByContent;

    public <PO extends GePO> SelectBuilder(PO po) {
        this.selectColumns = SelectColumnsHelper.buildSelectColumns(po);
        this.mainTable = PoUtils.getTableName(po) + " " + PoUtils.getTableAlias(po);
        this.leftJoin = LeftJoinHelper.buildLeftJoins(po);
        this.whereContent = WhereHelper.buildWhereContent(po);
        this.orderByContent = OrderByHelper.buildOrderBys(po);
    }

    @Override
    public String build() {
        String base = MessageFormat.format("SELECT {0} FROM {1}", selectColumns, mainTable);

        this.whereContent = Optional.ofNullable(this.whereContent)
                .filter((str) -> !ObjectUtils.isEmpty(str))
                .map((str)-> " WHERE " + str)
                .orElse(null);
        this.orderByContent = Optional.ofNullable(this.orderByContent)
                .filter((str) -> !ObjectUtils.isEmpty(str))
                .map((str)-> " ORDER BY " + str)
                .orElse(null);

        String sql = Stream.of(this.leftJoin, this.whereContent, this.orderByContent)
                .filter(Objects::nonNull)
                .reduce(base, String::concat);
        log.info(sql);
        return "<script>" + sql + "</script>";
    }
}
