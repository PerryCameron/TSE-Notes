package com.L2.repository.implementations;

import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.repository.interfaces.PartOrderRepository;
import com.L2.repository.rowmappers.PartOrderRowMapper;
import com.L2.repository.rowmappers.PartRowMapper;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class PartOrderRepositoryImpl implements PartOrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PartOrderRepositoryImpl.class);


    public PartOrderRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getDataSource("Part Order Repo"));
    }

    @Override
    public int insertPartOrder(PartOrderDTO partOrder) {
        String sql = "INSERT INTO PartOrders (noteId, orderNumber, showType) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {  // line 36
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, partOrder.getNoteId());
            ps.setString(2, partOrder.getOrderNumber());
            ps.setBoolean(3, partOrder.showTypeProperty().get()); // or partOrder.isShowType()
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
        String sql = "UPDATE PartOrders SET noteId = ?, orderNumber = ?, showType = ? WHERE id = ?";
        return jdbcTemplate.update(sql, partOrder.getNoteId(), partOrder.getOrderNumber(), partOrder.showTypeProperty().get(), partOrder.getId());
    }

    @Override
    public int deletePartOrder(PartOrderDTO partOrderDTO) {
        logger.info("Deleting PartOrder: {}", partOrderDTO);
        String sql = "DELETE FROM PartOrders WHERE id = ?";
        // Execute the update query to delete the PartOrder by its id
        return jdbcTemplate.update(sql, partOrderDTO.getId());
    }

    @Override
    public int insertPart(PartDTO partDTO) {
        String sql = "INSERT INTO Parts (partOrderId, partNumber, partDescription, partQuantity, serialReplaced, partEditable, lineType) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, partDTO.getPartOrderId());
            ps.setString(2, partDTO.getPartNumber());
            ps.setString(3, partDTO.getPartDescription());
            ps.setString(4, partDTO.getPartQuantity());
            ps.setString(5, partDTO.getSerialReplaced());
            ps.setInt(6, partDTO.isPartEditable() ? 1 : 0);
            ps.setString(7, partDTO.getLineType());
            return ps;
        }, keyHolder);

        // Return the generated key (ID)
        return keyHolder.getKey().intValue();
    }

    @Override
    public int updatePart(PartDTO partDTO) {
        String sql = "UPDATE Parts SET partOrderId = ?, partNumber = ?, partDescription = ?, partQuantity = ?, serialReplaced = ?, partEditable = ?, lineType= ?  WHERE id = ?";
        return jdbcTemplate.update(sql,
                partDTO.getPartOrderId(),
                partDTO.getPartNumber(),
                partDTO.getPartDescription(),
                partDTO.getPartQuantity(),
                partDTO.getSerialReplaced(),
                partDTO.isPartEditable() ? 1 : 0,
                partDTO.getLineType(),
                partDTO.getId());
    }

    @Override
    public int deletePart(PartDTO partDTO) {
        logger.info("Deleting part: {}", partDTO.getId());
        String sql = "DELETE FROM Parts WHERE id = ?";
        return jdbcTemplate.update(sql, partDTO.getId());
    }

    @Override
    public List<PartDTO> getPartsByPartOrder(PartOrderDTO partOrderDTO) {
        String sql = "SELECT * FROM Parts WHERE partOrderId = ?";
        // Use partOrderDTO to get the partOrderId and pass it to the query
        return jdbcTemplate.query(sql, new PartRowMapper(), partOrderDTO.getId());
    }
}
