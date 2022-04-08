package com.gening.library.gemapper.common.mapper.base;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.provider.BaseDynamicProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * @author G
 * @version 1.0
 * @className UpdateMapper
 * @description 修改Mapper接口
 * @date 2022/3/18 16:58
 */
public interface UpdateMapper<PO extends GePO> {

    /**
     * 修改对象，保存空值
     *
     * @param po 修改实体
     */
    @UpdateProvider(type = BaseDynamicProvider.class, method = "dynamic")
    void update(PO po);

    /**
     * 修改对象，不保存空值
     *
     * @param po 修改实体
     */
    @UpdateProvider(type = BaseDynamicProvider.class, method = "dynamic")
    void updateSelective(PO po);
}
