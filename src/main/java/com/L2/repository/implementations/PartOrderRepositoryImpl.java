package com.L2.repository.implementations;

import com.L2.dto.PartOrderDTO;
import com.L2.repository.interfaces.PartOrderRepository;
import com.L2.repository.rowmappers.PartOrderRowMapper;
import com.L2.static_tools.DatabaseConnector;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class PartOrderRepositoryImpl implements PartOrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public PartOrderRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getDataSource());
    }

    @Override
    public int insertPartOrder(PartOrderDTO partOrder) {
        String sql = "INSERT INTO PartOrders (noteId, orderNumber) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, partOrder.getNoteId());
            ps.setString(2, partOrder.getOrderNumber());
            return ps;
        }, keyHolder);

        // Return the generated key (ID)
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public List<PartOrderDTO> findAllPartOrdersByNoteId(int noteId) {
        String sql = "SELECT * FROM PartOrders WHERE noteId = ?";
        return jdbcTemplate.query(sql, new PartOrderRowMapper(), noteId);
    }

    @Override
    public List<PartOrderDTO> findAllPartOrders() {
        String sql = "SELECT * FROM PartOrders";
        return jdbcTemplate.query(sql, new PartOrderRowMapper());
    }

    @Override
    public int updatePartOrder(PartOrderDTO partOrder) {
        String sql = "UPDATE PartOrders SET noteId = ?, orderNumber = ? WHERE id = ?";
        return jdbcTemplate.update(sql, partOrder.getNoteId(), partOrder.getOrderNumber(), partOrder.getId());
    }



}
