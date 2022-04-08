package com.gening.library.gemapper.common.mapper.provider.entity;

import com.gening.library.gemapper.common.mapper.exception.ModelResolveException;
import com.gening.library.gemapper.core.util.CharsFormatUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * @author G
 * @version 1.0
 * @className TableBaseInfo
 * @description 实体类/数据库对应信息
 * @date 2022/3/18 16:58
 */
@Getter
@Setter
public class TableBaseInfo {
    /**
     * 当前实体类Class
     */
    private Class<?> modelClz;

    /**
     * 对应表名
     */
    private String tableName;

    /**
     * 别名
     */
    private String tableAlias;

    /**
     * 对应数据库主键名
     */
    private String tableKeySqlName;

    /**
     * 当前实体类主键属性名
     */
    private String tableKeyPropertyName;

    public TableBaseInfo(Class<?> clz) {
        this.modelClz = clz;
        this.tableName = PoUtils.getTableName(clz);
        this.tableAlias = PoUtils.getTableAlias(clz);
        Field f = PoUtils.getSimpleKeySqlField(clz);
        if (f == null) {
            throw new ModelResolveException("当前实体类未设置主键");
        }
        this.tableKeySqlName = CharsFormatUtils.convertUnderLine(f.getName());
        this.tableKeyPropertyName = f.getName();
    }
}
