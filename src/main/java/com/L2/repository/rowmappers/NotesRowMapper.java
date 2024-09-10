package com.L2.repository.rowmappers;

import com.L2.dto.CaseDTO;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotesRowMapper implements RowMapper<CaseDTO> {
    @Override
    public CaseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new;
    }
}
