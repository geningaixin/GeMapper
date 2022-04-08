package com.gening.library.gemapper.common.mapper.base;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.provider.BaseDeleteProvider;
import com.gening.library.gemapper.common.mapper.provider.BaseDynamicProvider;
import org.apache.ibatis.annotations.DeleteProvider;

/**
 * @author G
 * @version 1.0
 * @className DeleteMapper
 * @description 删除Mapper接口
 * @date 2022/3/18 16:58
 */
public interface DeleteMapper<PO extends GePO, PK> {

    /**
     * 删除数据
     *
     * @param po 对象
     */
    @DeleteProvider(type = BaseDynamicProvider.class, method = "dynamic")
    void delete(PO po);

    /**
     * 根据主键删除数据
     *
     * @param id 主键
     */
    @DeleteProvider(type = BaseDeleteProvider.class, method = "deleteById")
    void deleteById(PK id);
}
