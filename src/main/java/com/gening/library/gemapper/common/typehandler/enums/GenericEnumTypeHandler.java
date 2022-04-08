package com.gening.library.gemapper.common.typehandler.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author G
 * @version 1.0
 * @className GenericEnumTypeHandler
 * @description 通用枚举Mybatis类型转换器
 * @date 2022/3/18 17:45
 */
public class GenericEnumTypeHandler<E extends GenericEnum<Object, Object, Enum<?>>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public GenericEnumTypeHandler(Class<E> type) {
        if (type != null) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E e, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setObject(i, e.getValue());
        } else {
            ps.setObject(i, e.getValue(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object code = rs.getObject(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return GenericEnumHelper.valueOf(this.type, code);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object code = rs.getObject(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return GenericEnumHelper.valueOf(this.type, code);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object code = cs.getObject(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return GenericEnumHelper.valueOf(this.type, code);
    }
}
