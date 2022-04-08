package com.gening.library.gemapper.common.mapper.dynamicsql.builder;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.InsertOrUpdateMode;
import com.gening.library.gemapper.common.mapper.dynamicsql.helper.UpdateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.stream.Stream;

import static com.gening.library.gemapper.common.mapper.constant.SqlConstant.WHERE_KEYWORD;

/**
 * @author G
 * @version 1.0
 * @className UpdateBuilder
 * @description 数据库修改SQL实现
 * @date 2022/3/18 16:58
 */
@Slf4j
public class UpdateBuilder implements SqlBuilder {

    private final String updateTable;

    private final String updateTableAlias;

    private final String updateContent;

    private final String updateWheres;

    public <PO extends GePO> UpdateBuilder(PO po, InsertOrUpdateMode mode) {
        this.updateTable = PoUtils.getTableName(po);
        this.updateTableAlias = PoUtils.getTableAlias(po);
        this.updateContent = UpdateHelper.buildUpdateContent(po, mode);
        this.updateWheres = UpdateHelper.buildUpdateWheres(po);
    }

    @Override
    public String build() {
        // 定义Update语句
        String updateSql = MessageFormat.format("UPDATE {0} {1} SET {2}", updateTable, updateTableAlias, updateContent);
        String sql = Stream.of(updateWheres)
                // 过滤“where”是否有内容
                .filter(whereSql -> !ObjectUtils.isEmpty(whereSql))
                // 有内容则做WHERE拼接
                .map(WHERE_KEYWORD::concat)
                // 再在update语句后拼接where条件
                .map(updateSql::concat)
                // 得到Optional
                .findFirst()
                // 若Optional里没有内容，则直接用update语句替代
                .orElse(updateSql);
        log.info(sql);
        return sql;
    }
}
