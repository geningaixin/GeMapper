package com.gening.library.gemapper.common.mapper.dynamicsql.builder;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.dynamicsql.helper.WhereHelper;
import com.gening.library.gemapper.core.constant.Constant;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.session.GeSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.stream.Stream;

import static com.gening.library.gemapper.common.mapper.constant.SqlConstant.*;
import static io.vavr.API.*;
import static io.vavr.Predicates.is;

/**
 * @author G
 * @version 1.0
 * @className DeleteBuilder
 * @description 数据库删除SQL实现
 * @date 2022/3/18 16:58
 */
@Slf4j
public class DeleteBuilder implements SqlBuilder {

    /**
     * 删除表名
     */
    private final String DELETE_TABLE_NAME;

    /**
     * 表别名
     */
    private final String TABLE_ALIAS;

    /**
     * 删除条件
     */
    private final String DELETE_WHERES;

    public <PO extends GePO> DeleteBuilder(PO po) {
        this.DELETE_TABLE_NAME = PoUtils.getTableName(po);
        this.TABLE_ALIAS = PoUtils.getTableAlias(po);
        this.DELETE_WHERES = WHERE_KEYWORD + WhereHelper.buildWhereContent(po);
    }

    @Override
    public String build() {
        // 不同类型的数据删除语句不同，这里采用vavr的“Match”语法替代“IF-ELSE”，先构建基础“delete”语句
        String deleteSql = Match(GeSqlSessionFactoryBean.JDBC_TYPE).of(
                Case($(is(Constant.DB_TYPE_MYSQL)), MessageFormat.format("DELETE {0} FROM {1} {2}", TABLE_ALIAS, DELETE_TABLE_NAME, TABLE_ALIAS)),
                Case($(is(Constant.DB_TYPE_ORACLE)), MessageFormat.format("DELETE FROM {0} {1}", DELETE_TABLE_NAME, TABLE_ALIAS)),
                Case($(is(Constant.DB_TYPE_SQLSERVER)), MessageFormat.format("DELETE FROM {0} {1}", DELETE_TABLE_NAME, TABLE_ALIAS)),
                Case($(is(Constant.DB_TYPE_POSTGRESQL)), MessageFormat.format("DELETE FROM {0} {1}", DELETE_TABLE_NAME, TABLE_ALIAS)),
                Case($(is(Constant.DB_TYPE_UNKNOWN)), MessageFormat.format("DELETE FROM {0} {1}", DELETE_TABLE_NAME, TABLE_ALIAS)),
                Case($(), () -> {
                    throw new RuntimeException("未获取到数据库类型");
                })
        );
        String sql = Stream.of(DELETE_WHERES)
                // 过滤“where”是否有内容
                .filter(whereSql -> !ObjectUtils.isEmpty(whereSql))
                // 再在delete语句后拼接where条件
                .map(deleteSql::concat)
                // 得到Optional
                .findFirst()
                // 若Optional里没有内容，则直接用delete语句替代
                .orElse(deleteSql);
        log.info(sql);
        return sql;
    }
}
