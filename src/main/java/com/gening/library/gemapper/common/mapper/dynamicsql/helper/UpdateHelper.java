package com.gening.library.gemapper.common.mapper.dynamicsql.helper;

import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.core.annotation.SqlKey;
import com.gening.library.gemapper.common.mapper.constant.SqlConstant;
import com.gening.library.gemapper.common.mapper.exception.ModelResolveException;
import com.gening.library.gemapper.core.util.BeanUtils;
import com.gening.library.gemapper.core.util.CharsFormatUtils;
import com.gening.library.gemapper.core.util.PoUtils;
import com.gening.library.gemapper.common.mapper.dynamicsql.enums.InsertOrUpdateMode;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author G
 * @version 1.0
 * @className UpdateHelper
 * @description Update或Delete帮助类
 * @date 2022/3/18 16:58
 */
public class UpdateHelper {

    /**
     * 创建 update 内容
     *
     * @param po   修改的实体对象
     * @param mode 修改模式：保存空值 | 不保存空值
     * @param <PO> 实体对象泛型
     * @return {@link String}
     */
    public static <PO extends GePO> String buildUpdateContent(final PO po, final InsertOrUpdateMode mode) {
        // 获取PO中所有业务字段
        Field[] fields = PoUtils.getSelfFields(po);
        return Arrays.stream(fields)
                // 当前字段是业务字段且不为主键
                .filter(field -> PoUtils.isBusinessField(field) && !field.isAnnotationPresent(SqlKey.class))
                // 当前字段值不为空或修改模式为全量模式
                .filter(field -> !ObjectUtils.isEmpty(BeanUtils.getValueByField(po, field)) || mode == InsertOrUpdateMode.FULL)
                // 得到SQL语句片段，即：user_name = #{userName}
                .map(field -> MessageFormat.format("{0} = #'{'{1}'}'", CharsFormatUtils.convertUnderLine(field.getName()), field.getName()))
                // 将匹配字段的SQL语句片段通过“,”拼接
                .reduce((a, b) -> a + ", " + b)
                // 若最终返回的SQL语句为空则抛出异常
                .orElseThrow(() -> new ModelResolveException("UPDATE异常，实体类中未赋值需要修改的值"));
    }

    /**
     * 创建 update where条件内容
     *
     * @param po   修改的实体对象
     * @param <PO> 实体对象泛型
     * @return {@link String}
     */
    public static <PO extends GePO> String buildUpdateWheres(final PO po) {
        final UpdateByIdModelBuildEntity entity = new UpdateByIdModelBuildEntity();
        GePO wherePo = Stream.of("")
                // 获取主键Field
                .map(none -> PoUtils.getSimpleKeySqlField(po))
                // 若非空，则放置Entity中
                .filter(Objects::nonNull).peek(entity::setKey)
                // 获取主键值
                .map(field -> BeanUtils.getValueByField(po, field))
                // 若非空，则放置Entity中
                .filter(Objects::nonNull).peek(entity::setValue)
                // 生成新的PO实体
                .map(value -> Try.of(() -> (GePO) po.getClass().getDeclaredConstructor().newInstance()).get())
                // 为新PO实体的主键Field赋值
                .peek(newInstance -> BeanUtils.setValueByField(newInstance, entity.getKey(), entity.getValue()))
                // 获取
                .findFirst().orElseGet(() -> Optional.of(po)
                        // 若不存在，即未生成新的PO实体，证明传入PO未设置主键或主键值为空，则获取当前PO，若当前PO的conditions条件属性也未设置，则抛出异常
                        .filter(p -> !ObjectUtils.isEmpty(BeanUtils.getValueByParentFieldName(p, SqlConstant.WHERE_CONTENTS_PROPERTY_NAME)))
                        .orElseThrow(() -> new ModelResolveException("修改信息时必须提供where条件或设定主键值"))
                );
        return WhereHelper.buildWhereContent(wherePo);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class UpdateByIdModelBuildEntity {
        private Field key;
        private Object value;
    }
}
