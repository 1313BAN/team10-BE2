package com.ssafy.enjoytrip.common.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.time.LocalTime;

@MappedTypes(LocalTime.class)
public class LocalTimeTypeHandler implements TypeHandler<LocalTime> {

    @Override
    public void setParameter(PreparedStatement ps, int i, LocalTime parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setTime(i, Time.valueOf(parameter));
        } else {
            ps.setNull(i, Types.TIME);
        }
    }

    @Override
    public LocalTime getResult(ResultSet rs, String columnName) throws SQLException {
        Time time = rs.getTime(columnName);
        return time != null ? time.toLocalTime() : null;
    }

    @Override
    public LocalTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        Time time = rs.getTime(columnIndex);
        return time != null ? time.toLocalTime() : null;
    }

    @Override
    public LocalTime getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Time time = cs.getTime(columnIndex);
        return time != null ? time.toLocalTime() : null;
    }
}