package com.L2.repository.implementations;

import com.L2.dto.UserDTO;
import com.L2.repository.interfaces.UserRepository;
import com.L2.repository.rowmappers.UserRowMapper;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserRepositoryImpl implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // Constructor to pass in the manually created JdbcTemplate
    public UserRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getDataSource("User Repo"));
    }

    @Override
    public UserDTO getUser() {
        String sql = "SELECT * FROM user WHERE id = 1";
        logger.info("Loading user information...");
        return jdbcTemplate.queryForObject(sql, new UserRowMapper());
    }

    // Method to update a UserDTO's information
    public int updateUserDTO(UserDTO UserDTO) {
        String sql = "UPDATE user SET first_name = ?, last_name = ?, email = ?, sesa_number = ?, url = ? WHERE id = ?";
        return jdbcTemplate.update(sql, UserDTO.getFirstName(), UserDTO.getLastName(), UserDTO.getEmail(), UserDTO.getSesa(), UserDTO.getProfileLink(), UserDTO.getId());
    }
}
