package com.L2.repository.implementations;

import com.L2.dto.NoteDTO;
import com.L2.repository.interfaces.NoteRepository;
import com.L2.repository.rowmappers.NotesRowMapper;
import com.L2.static_tools.DatabaseConnector;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class NoteRepositoryImpl implements NoteRepository {

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
    public boolean noteExists(NoteDTO note) {
        String sql = "SELECT COUNT(*) FROM Notes WHERE id = ?";
        // Extract the id from the NoteDTO object
        int id = note.getId();
        // Use queryForObject to get the count of rows matching the id
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        // Return true if count is greater than 0, meaning the note exists
        return count != null && count > 0;
    }

    @Override
    public int insertNote(NoteDTO note) {
        String sql = "INSERT INTO Notes (timestamp, workOrder, caseNumber, serialNumber, modelNumber, callInPerson, " +
                "callInPhoneNumber, callInEmail, underWarranty, activeServiceContract, serviceLevel, schedulingTerms, upsStatus, " +
                "loadSupported, issue, contactName, contactPhoneNumber, contactEmail, street, installedAt, city, state, zip, country, " +
                "createdWorkOrder, tex, partsOrder, entitlement, completed, isEmail, additionalCorrectiveActionText, relatedCaseNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, note.getTimestamp().toString());  // Assuming timestamp is a LocalDateTime
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
            ps.setString(28, note.getEntitlement());
            ps.setInt(29, note.isCompleted() ? 1 : 0);
            ps.setInt(30, note.isEmail() ? 1 : 0);
            ps.setString(31, note.getAdditionalCorrectiveActionText());
            ps.setString(32, note.getRelatedCaseNumber());
            return ps;
        }, keyHolder);

        // Return the generated key (ID)
        return keyHolder.getKey().intValue();
    }

    @Override
    public void updateNote(NoteDTO note) {
        String sql = "UPDATE Notes SET " +
                "timestamp = ?, workOrder = ?, caseNumber = ?, serialNumber = ?, modelNumber = ?, " +
                "callInPerson = ?, callInPhoneNumber = ?, callInEmail = ?, underWarranty = ?, " +
                "activeServiceContract = ?, serviceLevel = ?, schedulingTerms = ?, upsStatus = ?, " +
                "loadSupported = ?, issue = ?, contactName = ?, contactPhoneNumber = ?, " +
                "contactEmail = ?, street = ?, installedAt = ?, city = ?, state = ?, zip = ?, " +
                "country = ?, createdWorkOrder = ?, tex = ?, partsOrder = ?, entitlement = ?, " +
                "completed = ?, isEmail = ?, additionalCorrectiveActionText = ?, relatedCaseNumber = ? " +
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
                note.getEntitlement(),
                note.isCompleted() ? 1 : 0,
                note.isEmail() ? 1 : 0,
                note.getAdditionalCorrectiveActionText(),
                note.getRelatedCaseNumber(),
                note.getId()  // WHERE clause based on id
        );
    }
}
