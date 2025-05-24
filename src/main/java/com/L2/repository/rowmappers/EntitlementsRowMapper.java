package com.L2.repository.rowmappers;

import com.L2.dto.EntitlementFx;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntitlementsRowMapper implements RowMapper<EntitlementFx> {
    @Override
    public EntitlementFx mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EntitlementFx(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("includes"),
                rs.getString("not_included")
        );
    }
}
