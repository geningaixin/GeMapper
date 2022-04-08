package com.gening.library.gemapper.common.mapper.base;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.provider.BaseDynamicProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

/**
 * @author G
 * @version 1.0
 * @className InsertMapper
 * @description 新增Mapper接口
 * @date 2022/3/18 16:58
 */
public interface InsertMapper<PO extends GePO> {

    /**
     * 新增对象，保存空值（会替换掉数据库中的默认值）
     *
     * @param po 新增实体类
     */
    @InsertProvider(type = BaseDynamicProvider.class, method = "dynamic")
    @Options(keyProperty = "")
    void insert(PO po);

    /**
     * 新增对象，保存空值（会替换掉数据库中的默认值），自动插入主键
     * @param po 新增实体类
     */
    @Options(useGeneratedKeys = true)
    @InsertProvider(type = BaseDynamicProvider.class, method = "dynamic")
    void insertToUseGeneratedKey(PO po);

    /**
     * 新增对象，不保存空值
     *
     * @param po 新增实体类
     */
    @InsertProvider(type = BaseDynamicProvider.class, method = "dynamic")
    void insertSelective(PO po);

    /**
     * 新增对象，不保存空值，自动插入主键
     * @param po 新增实体类
     */
    @Options(useGeneratedKeys = true)
    @InsertProvider(type = BaseDynamicProvider.class, method = "dynamic")
    void insertSelectiveToUseGeneratedKey(PO po);
}
