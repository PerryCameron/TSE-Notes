package com.L2.repository.rowmappers;

import com.L2.dto.global_spares.ProductToSparesDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductToSparesRowMapper implements RowMapper<ProductToSparesDTO> {
    @Override
    public ProductToSparesDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductToSparesDTO(
                rs.getString("pim_range"),
                rs.getString("pim_product_family"),
                rs.getString("spare_item"),
                rs.getString("replacement_item"),
                rs.getString("standard_exchange_item"),
                rs.getString("spare_description"),
                rs.getString("catalogue_version"),
                rs.getString("end_of_service_date"),
                rs.getString("last_update"),
                rs.getString("added_to_catalogue"),
                rs.getBoolean("archived"),
                rs.getBoolean("custom_add")
        );
    }
}

