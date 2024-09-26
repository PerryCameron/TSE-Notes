package com.L2.repository.rowmappers;

import com.L2.dto.UserDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDTO> {
    @Override
    public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserDTO(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("sesa_number"),
                rs.getString("url")
        );
    }
}
