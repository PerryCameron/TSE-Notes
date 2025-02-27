package com.L2.repository.rowmappers;

import com.L2.dto.PartOrderDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartOrderRowMapper implements RowMapper<PartOrderDTO> {
    @Override
    public PartOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PartOrderDTO(
                rs.getInt("id"),
                rs.getInt("noteId"),
                rs.getString("orderNumber"),
                rs.getBoolean("showType")
        );
    }
}
