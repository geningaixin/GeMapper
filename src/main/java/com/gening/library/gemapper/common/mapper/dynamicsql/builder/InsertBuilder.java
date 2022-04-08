package com.gening.library.gemapper.common.mapper.dynamicsql.builder;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.mapper.dynamicsql.entity.InsertContent;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.InsertOrUpdateMode;
import com.gening.library.gemapper.common.mapper.dynamicsql.helper.InsertHelper;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className InsertBuilder
 * @description 数据库新增SQL实现
 * @date 2022/3/18 16:58
 */
@Slf4j
public class InsertBuilder implements SqlBuilder {

    private final String INSERT_TABLE;
    private final String INSERT_COLUMNS;
    private final String INSERT_VALUES;

    public <PO extends GePO> InsertBuilder(PO po, InsertOrUpdateMode mode) {
        InsertContent content = InsertHelper.buildInsertContent(po, mode);
        this.INSERT_TABLE = PoUtils.getTableName(po);
        this.INSERT_COLUMNS = content.getInsertColumns();
        this.INSERT_VALUES = content.getInsertValues();
    }

    @Override
    public String build() {
        return Stream.of("")
                // 得到insert语句
                .map((s) -> MessageFormat.format("INSERT INTO {0} ({1}) VALUES ({2})", INSERT_TABLE, INSERT_COLUMNS, INSERT_VALUES))
                // 打印语句
                .peek(log::info)
                // 得到Optional
                .findFirst()
                // 若返回Optional为空则抛出异常，正常直接返回
                .orElseThrow(() -> new RuntimeException("insert语句生成失败"));
    }
}
