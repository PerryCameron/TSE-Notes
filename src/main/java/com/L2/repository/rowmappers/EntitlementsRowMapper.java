package com.L2.repository.rowmappers;

import com.L2.dto.EntitlementDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class EntitlementsRowMapper implements RowMapper<EntitlementDTO> {

    // Formatter to parse the timestamp from the ResultSet (stored as TEXT)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
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
