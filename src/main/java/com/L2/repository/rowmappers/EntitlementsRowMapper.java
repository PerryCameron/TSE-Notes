package com.L2.repository.rowmappers;

import com.L2.dto.EntitlementDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntitlementsRowMapper implements RowMapper<EntitlementDTO> {
    @Override
    public EntitlementDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EntitlementDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("includes"),
                rs.getString("not_included")
        );
    }
}
