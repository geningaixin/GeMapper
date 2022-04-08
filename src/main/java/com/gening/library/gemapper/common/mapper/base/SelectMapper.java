package com.gening.library.gemapper.common.mapper.base;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.provider.BaseDynamicProvider;
import com.gening.library.gemapper.common.mapper.provider.BaseSelectProvider;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author G
 * @version 1.0
 * @className SelectMapper
 * @description 查询Mapper接口
 * @date 2022/3/18 16:58
 */
public interface SelectMapper<PO extends GePO, PK> {

    /**
     * 根据主键进行查询
     *
     * @param id 主键值
     * @return {@link PO}
     */
    @SelectProvider(type = BaseSelectProvider.class, method = "queryById")
    PO queryById(PK id);

    /**
     * 查询所有数据
     *
     * @return {@link List<PO>}
     */
    @SelectProvider(type = BaseSelectProvider.class, method = "queryAll")
    List<PO> queryAll();

    /**
     * 根据条件动态查询
     *
     * @param model 查询实体
     * @return {@link List<PO>}
     */
    @SelectProvider(type = BaseDynamicProvider.class, method = "dynamic")
    @ResultMap("BaseResultMap")
    List<PO> dynamicQuery(PO model);
}
