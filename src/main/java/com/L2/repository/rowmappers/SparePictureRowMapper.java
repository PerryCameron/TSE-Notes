package com.L2.repository.rowmappers;

import com.L2.dto.global_spares.SparePictureDTO;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SparePictureRowMapper implements RowMapper<SparePictureDTO> {
    @Override
    public SparePictureDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        SparePictureDTO sparePicture = new SparePictureDTO();
        sparePicture.setId(rs.getLong("id"));
        sparePicture.setSpareName(rs.getString("spare_name"));
        sparePicture.setPicture(rs.getBytes("picture"));
        return sparePicture;
    }
}
