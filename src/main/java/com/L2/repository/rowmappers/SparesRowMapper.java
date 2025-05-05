package com.L2.repository.rowmappers;

import com.L2.dto.global_spares.ProductToSparesDTO;
import com.L2.dto.global_spares.SparesDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SparesRowMapper implements RowMapper<SparesDTO> {
    @Override
    public SparesDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SparesDTO(
                rs.getInt("id"),
                rs.getString("pim"),
                rs.getString("spare_item"),
                rs.getString("replacement_item"),
                rs.getString("standard_exchange_item"),
                rs.getString("spare_description"),
                rs.getString("catalogue_version"),
                rs.getString("end_of_service_date"),
                rs.getString("last_update"),
                rs.getString("added_to_catalogue"),
                rs.getString("removed_from_catalogue"),
                rs.getString("comments"),
                rs.getString("keywords"),
                rs.getBoolean("archived"),
                rs.getBoolean("custom_add"),
                rs.getString("last_updated_by")
        );
    }
}