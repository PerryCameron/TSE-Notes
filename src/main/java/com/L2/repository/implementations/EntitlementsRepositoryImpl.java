package com.L2.repository.implementations;

import com.L2.dto.EntitlementDTO;
import com.L2.repository.interfaces.EntitlementsRepository;
import com.L2.repository.rowmappers.EntitlementsRowMapper;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class EntitlementsRepositoryImpl implements EntitlementsRepository {

    private static final Logger logger = LoggerFactory.getLogger(EntitlementsRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // Constructor to pass in the manually created JdbcTemplate
    public EntitlementsRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getDataSource("Entitlements Repo"));
    }

    @Override
    public List<EntitlementDTO> getAllEntitlements() {
        // SQL query to retrieve all entitlements from the entitlements table
        String sql = "SELECT * FROM entitlements";
        return jdbcTemplate.query(sql, new EntitlementsRowMapper());
    }

    // Insert a new entitlement row into the database
    @Override
    public int insertEntitlement(EntitlementDTO entitlement) {
        String sql = "INSERT INTO entitlements (name, includes, not_included) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entitlement.getName());
            ps.setString(2, entitlement.getIncludes());
            ps.setString(3, entitlement.getNotIncludes());
            return ps;
        }, keyHolder);
        // Retrieve the generated ID from the KeyHolder
        return keyHolder.getKey().intValue();
    }

    // Update an existing entitlement row based on its id
    @Override
    public int updateEntitlement(EntitlementDTO entitlement) {
        String sql = "UPDATE entitlements SET name = ?, includes = ?, not_included = ? WHERE id = ?";
        return jdbcTemplate.update(sql, entitlement.getName(), entitlement.getIncludes(), entitlement.getNotIncludes(), entitlement.getId());
    }

    // Delete an entitlement row based on its id
    @Override
    public int deleteEntitlement(EntitlementDTO entitlement) {
        String sql = "DELETE FROM entitlements WHERE id = ?";
        return jdbcTemplate.update(sql, entitlement.getId());
    }
}

