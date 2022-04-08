package com.gening.library.gemapper.common.mapper.provider;

import com.gening.library.gemapper.common.session.GeSqlSessionFactoryBean;
import com.gening.library.gemapper.core.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.text.MessageFormat;
import java.util.stream.Stream;

import static io.vavr.API.*;
import static io.vavr.Predicates.is;

/**
 * @author G
 * @version 1.0
 * @className BaseDeleteProvider
 * @description TODO
 * @date 2022/4/1 17:00
 */
@Slf4j
public class BaseDeleteProvider {

    /**
     * 根据主键删除
     *
     * @param providerContext ProviderContext对象
     * @return {@link String} 解析实体类，返回可执行SQL
     */
    public String deleteById(ProviderContext providerContext) {
        return Stream.of(providerContext)
                .map(ProviderHelper::getTableBaseInfo)
                .map(t -> Match(GeSqlSessionFactoryBean.JDBC_TYPE).of(
                        Case($(is(Constant.DB_TYPE_MYSQL)),
                                MessageFormat.format("DELETE {0} FROM {1} {2} WHERE {3} = #'{'{4}'}'", t.getTableAlias(), t.getTableName(), t.getTableAlias(), t.getTableKeySqlName(), t.getTableKeyPropertyName())),
                        Case($(is(Constant.DB_TYPE_ORACLE)),
                                MessageFormat.format("DELETE FROM {0} {1} WHERE {2} = #'{'{3}'}'", t.getTableName(), t.getTableAlias(), t.getTableKeySqlName(), t.getTableKeyPropertyName())),
                        Case($(is(Constant.DB_TYPE_SQLSERVER)),
                                MessageFormat.format("DELETE FROM {0} {1} WHERE {2} = #'{'{3}'}'", t.getTableName(), t.getTableAlias(), t.getTableKeySqlName(), t.getTableKeyPropertyName())),
                        Case($(is(Constant.DB_TYPE_POSTGRESQL)),
                                MessageFormat.format("DELETE FROM {0} {1} WHERE {2} = #'{'{3}'}'", t.getTableName(), t.getTableAlias(), t.getTableKeySqlName(), t.getTableKeyPropertyName())),
                        Case($(is(Constant.DB_TYPE_UNKNOWN)),
                                MessageFormat.format("DELETE FROM {0} {1} WHERE {2} = #'{'{3}'}'", t.getTableName(), t.getTableAlias(), t.getTableKeySqlName(), t.getTableKeyPropertyName())),
                        Case($(), () -> {
                            throw new RuntimeException("未获取到数据库类型");
                        })
                ))
                .peek(log::info)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("deleteById语句生成失败"));
    }
}
