package com.gening.library.gemapper.common.mapper.provider;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.common.mapper.base.DeleteMapper;
import com.gening.library.gemapper.common.mapper.base.InsertMapper;
import com.gening.library.gemapper.common.mapper.base.SelectMapper;
import com.gening.library.gemapper.common.mapper.base.UpdateMapper;
import com.gening.library.gemapper.common.mapper.dynamicsql.builder.DeleteBuilder;
import com.gening.library.gemapper.common.mapper.dynamicsql.builder.InsertBuilder;
import com.gening.library.gemapper.common.mapper.dynamicsql.builder.SelectBuilder;
import com.gening.library.gemapper.common.mapper.dynamicsql.builder.UpdateBuilder;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.InsertOrUpdateMode;
import org.apache.ibatis.builder.annotation.ProviderContext;

import static io.vavr.API.*;
import static io.vavr.Predicates.is;

/**
 * @author G
 * @version 1.0
 * @className BaseDynamicProvider
 * @description 增删改查动态生成提供商
 * @date 2022/3/18 17:45
 */
public class BaseDynamicProvider {

    public <PO extends GePO> String dynamic(ProviderContext providerContext, PO po) {

        return Match(providerContext.getMapperMethod().getDeclaringClass().getName()).of(

                Case($(is(InsertMapper.class.getName())), () -> providerContext.getMapperMethod().getName().contains("Selective")
                        ? new InsertBuilder(po, InsertOrUpdateMode.SELECTIVE) : new InsertBuilder(po, InsertOrUpdateMode.FULL)),

                Case($(is(DeleteMapper.class.getName())), () -> new DeleteBuilder(po)),

                Case($(is(UpdateMapper.class.getName())), () -> providerContext.getMapperMethod().getName().contains("Selective")
                        ? new UpdateBuilder(po, InsertOrUpdateMode.SELECTIVE) : new UpdateBuilder(po, InsertOrUpdateMode.FULL)),

                Case($(is(SelectMapper.class.getName())), () -> new SelectBuilder(po)),

                Case($(), () -> {
                    throw new RuntimeException("动态SQL生成失败，找不到对应Mapper接口");
                })

        ).build();
    }
}
