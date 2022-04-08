package com.gening.library.gemapper.common.mapper.provider;

import com.gening.library.gemapper.common.mapper.provider.entity.TableBaseInfo;
import com.gening.library.gemapper.core.util.BeanUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className ProviderHelper
 * @description Provider帮助类
 * @date 2022/3/18 17:45
 */
public class ProviderHelper {

    /**
     * 获取表名和主键名称
     *
     * @param providerContext ProviderContext
     * @return {@link String}
     */
    public static TableBaseInfo getTableBaseInfo(ProviderContext providerContext) {
        return new TableBaseInfo(getPoClass(providerContext));
    }

    /**
     * 获取表名
     *
     * @param providerContext ProviderContext
     * @return {@link String}
     */
    public static String getTableName(ProviderContext providerContext) {
        return PoUtils.getTableName(getPoClass(providerContext));
    }

    /**
     * 获取PO Class
     *
     * @param providerContext ProviderContext
     * @return {@link Class<>}
     */
    public static Class<?> getPoClass(ProviderContext providerContext) {
        return Stream.of(providerContext)
                .map(ProviderContext::getMapperType)
                .map(BeanUtils::getGeneric)
                .findFirst().get();
    }
}
