package com.gening.library.gemapper.common.mapper.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.text.MessageFormat;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className BaseDynamicProvider
 * @description 基础查询SQL供应商
 * @date 2022/3/18 17:45
 */
@Slf4j
public class BaseSelectProvider {

    /**
     * 根据主键查询
     *
     * @param providerContext ProviderContext对象
     * @return {@link String} 解析实体类，返回可执行SQL
     */
    public String queryById(ProviderContext providerContext) {
        return Stream.of(providerContext)
                .map(ProviderHelper::getTableBaseInfo)
                .map(t -> MessageFormat.format("SELECT * FROM {0} WHERE {1} = #'{'{2}'}'",
                        t.getTableName(), t.getTableKeySqlName(), t.getTableKeyPropertyName()))
                .peek(log::info)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("queryById语句生成失败"));
    }

    /**
     * 查询所有
     *
     * @param providerContext ProviderContext对象
     * @return {@link String} 解析实体类，返回可执行SQL
     */
    public String queryAll(ProviderContext providerContext) {
        return Stream.of(providerContext)
                .map(ProviderHelper::getTableName)
                .map(name -> MessageFormat.format("SELECT * FROM {0}", ProviderHelper.getTableName(providerContext)))
                .peek(log::info)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("queryAll语句生成失败"));
    }
}
