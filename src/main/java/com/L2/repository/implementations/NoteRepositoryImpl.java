package com.L2.repository.implementations;

import com.L2.dto.NoteDTO;
import com.L2.repository.interfaces.NoteRepository;
import com.L2.repository.rowmappers.NotesRowMapper;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class NoteRepositoryImpl implements NoteRepository {

    private static final Logger logger = LoggerFactory.getLogger(NoteRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // Constructor to pass in the manually created JdbcTemplate
    public NoteRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getDataSource());
    }

    @Override
    public List<NoteDTO> getAllNotes() {
        // SQL query to retrieve all notes from the Notes table
        String sql = "SELECT * FROM Notes";

        // Use JdbcTemplate to execute the query and map each row to a NoteDTO
        return jdbcTemplate.query(sql, new NotesRowMapper());
    }

    @Override
    public List<NoteDTO> getPaginatedNotes(int pageSize, int offset) {
        String sql = "SELECT * FROM Notes ORDER BY timestamp DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new NotesRowMapper(), pageSize, offset);
    }

    @Override
    public boolean noteExists(NoteDTO note) {
        String sql = "SELECT COUNT(*) FROM Notes WHERE id = ?";
        // Extract the id from the NoteDTO object
        int id = note.getId();
        // Use the new varargs version of queryForObject
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        // Return true if count is greater than 0, meaning the note exists
        return count != null && count > 0;
    }

    @Override
    public int insertNote(NoteDTO note) {
        logger.info("Creating note {} with timestamp: ", note.getId(), note.getTimestamp().toString());
        String sql = "INSERT INTO Notes (timestamp, workOrder, caseNumber, serialNumber, modelNumber, callInPerson, " +
                "callInPhoneNumber, callInEmail, underWarranty, activeServiceContract, serviceLevel, schedulingTerms, upsStatus, " +
                "loadSupported, issue, contactName, contactPhoneNumber, contactEmail, street, installedAt, city, state, zip, country, " +
                "createdWorkOrder, tex, partsOrder, completed, isEmail, additionalCorrectiveActionText, relatedCaseNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println("TimeStamp: " + note.getTimestamp().toString());
            ps.setString(1, note.getTimestamp().toString());
            ps.setString(2, note.getWorkOrder());
            ps.setString(3, note.getCaseNumber());
            ps.setString(4, note.getSerialNumber());
            ps.setString(5, note.getModelNumber());
            ps.setString(6, note.getCallInPerson());
            ps.setString(7, note.getCallInPhoneNumber());
            ps.setString(8, note.getCallInEmail());
            ps.setInt(9, note.isUnderWarranty() ? 1 : 0);
            ps.setString(10, note.getActiveServiceContract());
            ps.setString(11, note.getServiceLevel());
            ps.setString(12, note.getSchedulingTerms());
            ps.setString(13, note.getUpsStatus());
            ps.setInt(14, note.isLoadSupported() ? 1 : 0);
            ps.setString(15, note.getIssue());
            ps.setString(16, note.getContactName());
            ps.setString(17, note.getContactPhoneNumber());
            ps.setString(18, note.getContactEmail());
            ps.setString(19, note.getStreet());
            ps.setString(20, note.getInstalledAt());
            ps.setString(21, note.getCity());
            ps.setString(22, note.getState());
            ps.setString(23, note.getZip());
            ps.setString(24, note.getCountry());
            ps.setString(25, note.getCreatedWorkOrder());
            ps.setString(26, note.getTex());
            ps.setInt(27, note.getPartsOrder());
            ps.setInt(28, note.isCompleted() ? 1 : 0);
            ps.setInt(29, note.isEmail() ? 1 : 0);
            ps.setString(30, note.getAdditionalCorrectiveActionText());
            ps.setString(31, note.getRelatedCaseNumber());
            return ps;
        }, keyHolder);

        // Return the generated key (ID)
        return keyHolder.getKey().intValue();
    }

    @Override
    public void updateNote(NoteDTO note) {
        logger.info("Saving note {} with timestamp: {}", note.getId(), note.getTimestamp().toString());
        String sql = "UPDATE Notes SET " +
                "timestamp = ?, workOrder = ?, caseNumber = ?, serialNumber = ?, modelNumber = ?, " +
                "callInPerson = ?, callInPhoneNumber = ?, callInEmail = ?, underWarranty = ?, " +
                "activeServiceContract = ?, serviceLevel = ?, schedulingTerms = ?, upsStatus = ?, " +
                "loadSupported = ?, issue = ?, contactName = ?, contactPhoneNumber = ?, " +
                "contactEmail = ?, street = ?, installedAt = ?, city = ?, state = ?, zip = ?, " +
                "country = ?, createdWorkOrder = ?, tex = ?, partsOrder = ?, " +
                "completed = ?, isEmail = ?, additionalCorrectiveActionText = ?, relatedCaseNumber = ?, title = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                note.getTimestamp().toString(),  // Assuming timestamp is a LocalDateTime
                note.getWorkOrder(),
                note.getCaseNumber(),
                note.getSerialNumber(),
                note.getModelNumber(),
                note.getCallInPerson(),
                note.getCallInPhoneNumber(),
                note.getCallInEmail(),
                note.isUnderWarranty() ? 1 : 0,
                note.getActiveServiceContract(),
                note.getServiceLevel(),
                note.getSchedulingTerms(),
                note.getUpsStatus(),
                note.isLoadSupported() ? 1 : 0,
                note.getIssue(),
                note.getContactName(),
                note.getContactPhoneNumber(),
                note.getContactEmail(),
                note.getStreet(),
                note.getInstalledAt(),
                note.getCity(),
                note.getState(),
                note.getZip(),
                note.getCountry(),
                note.getCreatedWorkOrder(),
                note.getTex(),
                note.getPartsOrder(),
                note.isCompleted() ? 1 : 0,
                note.isEmail() ? 1 : 0,
                note.getAdditionalCorrectiveActionText(),
                note.getRelatedCaseNumber(),
                note.getTitle(),
                note.getId()  // WHERE clause based on id
        );
    }

    @Override
    public int deleteNote(NoteDTO noteDTO) {
        logger.info("Deleting Note: {}", noteDTO.getId());
        String sql = "DELETE FROM Notes WHERE id = ?";
        return jdbcTemplate.update(sql, noteDTO.getId());
    }
}
