package com.L2.repository.rowmappers;

import com.L2.dto.PartOrderFx;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartOrderRowMapper implements RowMapper<PartOrderFx> {
    @Override
    public PartOrderFx mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PartOrderFx(
                rs.getInt("id"),
                rs.getInt("noteId"),
                rs.getString("orderNumber"),
                rs.getBoolean("showType")
        );
    }
}
