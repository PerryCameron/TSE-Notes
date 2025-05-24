package com.L2.repository.rowmappers;

import com.L2.dto.PartFx;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartRowMapper implements RowMapper<PartFx> {
    @Override
    public PartFx mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PartFx(
                rs.getInt("id"),
                rs.getInt("partOrderId"),
                rs.getString("partNumber"),
                rs.getString("partDescription"),
                rs.getString("partQuantity"),
                rs.getString("serialReplaced"),
                rs.getInt("partEditable") == 1,
                rs.getString("lineType")
        );
    }
}
