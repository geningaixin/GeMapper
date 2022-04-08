package com.gening.library.gemapper.common.mapper;

import com.gening.library.gemapper.common.mapper.base.DeleteMapper;
import com.gening.library.gemapper.common.mapper.base.InsertMapper;
import com.gening.library.gemapper.common.mapper.base.SelectMapper;
import com.gening.library.gemapper.common.mapper.base.UpdateMapper;

/**
 * @author G
 * @version 1.0
 * @className GeMapper
 * @description 通用Mapper
 * @date 2022/3/18 17:45
 */
public interface GeMapper<PO extends GePO, PK> extends SelectMapper<PO, PK>, InsertMapper<PO>, UpdateMapper<PO>, DeleteMapper<PO, PK> {
}
