package com.L2.repository.rowmappers;

import com.L2.dto.global_spares.RangesFx;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RangesRowMapper implements RowMapper<RangesFx> {
    @Override
    public RangesFx mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RangesFx(
                rs.getInt("id"),
                rs.getString("range"),
                rs.getString("range_additional"),
                rs.getString("range_type"),
                rs.getString("last_update"),
                rs.getString("last_updated_by")
        );
    }
}
