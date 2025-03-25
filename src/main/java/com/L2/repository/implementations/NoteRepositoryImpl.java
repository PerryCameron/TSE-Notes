package com.L2.repository.implementations;

import com.L2.dto.NoteDTO;
import com.L2.repository.interfaces.NoteRepository;
import com.L2.repository.rowmappers.NotesRowMapper;
import com.L2.static_tools.DatabaseConnector;
import com.L2.static_tools.NoteTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NoteRepositoryImpl implements NoteRepository {

    private static final Logger logger = LoggerFactory.getLogger(NoteRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // Constructor to pass in the manually created JdbcTemplate
    public NoteRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getDataSource("Note Repo"));
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

    // for creating note that already has a filled out object
    @Override
    public int insertNote(NoteDTO noteDTO) {
        String sql = "INSERT INTO Notes (timestamp, workOrder, caseNumber, serialNumber, modelNumber, callInPerson, " +
                "callInPhoneNumber, callInEmail, underWarranty, activeServiceContract, serviceLevel, schedulingTerms, upsStatus, " +
                "loadSupported, title, issue, contactName, contactPhoneNumber, contactEmail, street, installedAt, city, state, zip, country, " +
                "createdWorkOrder, tex, partsOrder, completed, isEmail, additionalCorrectiveActionText, relatedCaseNumber, t_and_m) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, noteDTO.getTimestamp().toString());
            ps.setString(2, noteDTO.getWorkOrder());
            ps.setString(3, noteDTO.getCaseNumber());
            ps.setString(4, noteDTO.getSerialNumber());
            ps.setString(5, noteDTO.getModelNumber());
            ps.setString(6, noteDTO.getCallInPerson());
            ps.setString(7, noteDTO.getCallInPhoneNumber());
            ps.setString(8, noteDTO.getCallInEmail());
            ps.setInt(9, noteDTO.isUnderWarranty() ? 1 : 0);
            ps.setString(10, noteDTO.getActiveServiceContract());
            ps.setString(11, noteDTO.getServiceLevel());
            ps.setString(12, noteDTO.getSchedulingTerms());
            ps.setString(13, noteDTO.getUpsStatus());
            ps.setInt(14, noteDTO.isLoadSupported() ? 1 : 0);
            ps.setString(15, noteDTO.getTitle()); // Added title
            ps.setString(16, noteDTO.getIssue());
            ps.setString(17, noteDTO.getContactName());
            ps.setString(18, noteDTO.getContactPhoneNumber());
            ps.setString(19, noteDTO.getContactEmail());
            ps.setString(20, noteDTO.getStreet());
            ps.setString(21, noteDTO.getInstalledAt());
            ps.setString(22, noteDTO.getCity());
            ps.setString(23, noteDTO.getState());
            ps.setString(24, noteDTO.getZip());
            ps.setString(25, noteDTO.getCountry());
            ps.setString(26, noteDTO.getCreatedWorkOrder());
            ps.setString(27, noteDTO.getTex());
            ps.setInt(28, noteDTO.getPartsOrder());
            ps.setInt(29, noteDTO.isCompleted() ? 1 : 0);
            ps.setInt(30, noteDTO.isEmail() ? 1 : 0);
            ps.setString(31, noteDTO.getAdditionalCorrectiveActionText());
            ps.setString(32, noteDTO.getRelatedCaseNumber());
            ps.setString(33, noteDTO.gettAndM());
            return ps;
        }, keyHolder);
        logger.info("Creating note {} with timestamp: ", noteDTO.getId(), noteDTO.getTimestamp().toString());
        // Return the generated key (ID)
        return keyHolder.getKey().intValue();
    }

    // For creating a blank new note
    @Override
    public NoteDTO insertBlankNote() {
        NoteDTO noteDTO = new NoteDTO(0, false);
        String sql = "INSERT INTO Notes (timestamp, workOrder, caseNumber, serialNumber, modelNumber, callInPerson, " +
                "callInPhoneNumber, callInEmail, underWarranty, activeServiceContract, serviceLevel, schedulingTerms, upsStatus, " +
                "loadSupported, title, issue, contactName, contactPhoneNumber, contactEmail, street, installedAt, city, state, zip, country, " +
                "createdWorkOrder, tex, partsOrder, completed, isEmail, additionalCorrectiveActionText, relatedCaseNumber, t_and_m) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // Use Timestamp for database compatibility instead of toString()
            ps.setTimestamp(1, noteDTO.getTimestamp() != null ? Timestamp.valueOf(noteDTO.getTimestamp()) : null);
            ps.setString(2, noteDTO.getWorkOrder());
            ps.setString(3, noteDTO.getCaseNumber());
            ps.setString(4, noteDTO.getSerialNumber());
            ps.setString(5, noteDTO.getModelNumber());
            ps.setString(6, noteDTO.getCallInPerson());
            ps.setString(7, noteDTO.getCallInPhoneNumber());
            ps.setString(8, noteDTO.getCallInEmail());
            ps.setInt(9, noteDTO.isUnderWarranty() ? 1 : 0);
            ps.setString(10, noteDTO.getActiveServiceContract());
            ps.setString(11, noteDTO.getServiceLevel());
            ps.setString(12, noteDTO.getSchedulingTerms());
            ps.setString(13, noteDTO.getUpsStatus());
            ps.setInt(14, noteDTO.isLoadSupported() ? 1 : 0);
            ps.setString(15, noteDTO.getTitle()); // Added title
            ps.setString(16, noteDTO.getIssue());
            ps.setString(17, noteDTO.getContactName());
            ps.setString(18, noteDTO.getContactPhoneNumber());
            ps.setString(19, noteDTO.getContactEmail());
            ps.setString(20, noteDTO.getStreet());
            ps.setString(21, noteDTO.getInstalledAt());
            ps.setString(22, noteDTO.getCity());
            ps.setString(23, noteDTO.getState());
            ps.setString(24, noteDTO.getZip());
            ps.setString(25, noteDTO.getCountry());
            ps.setString(26, noteDTO.getCreatedWorkOrder());
            ps.setString(27, noteDTO.getTex());
            ps.setInt(28, noteDTO.getPartsOrder());
            ps.setInt(29, noteDTO.isCompleted() ? 1 : 0);
            ps.setInt(30, noteDTO.isEmail() ? 1 : 0);
            ps.setString(31, noteDTO.getAdditionalCorrectiveActionText());
            ps.setString(32, noteDTO.getRelatedCaseNumber());
            ps.setString(33, noteDTO.gettAndM());
            return ps;
        }, keyHolder);
        // Get the generated ID
        int generatedId = keyHolder.getKey().intValue();
        // Update the input NoteDTO with the generated ID
        noteDTO.idProperty().set(generatedId);
        logger.info("Created noteDTO {} with timestamp: {}", generatedId, noteDTO.getTimestamp() != null ? noteDTO.getTimestamp().toString() : "null");
        return noteDTO;
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
                "completed = ?, isEmail = ?, additionalCorrectiveActionText = ?, relatedCaseNumber = ?, title = ?, t_and_m = ? " +
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
                note.gettAndM(),
                note.getId()  // WHERE clause based on id
        );
    }

    @Override
    public int deleteNote(NoteDTO noteDTO) {
        logger.info("Deleting Note: {}", noteDTO.getId());
        String sql = "DELETE FROM Notes WHERE id = ?";
        return jdbcTemplate.update(sql, noteDTO.getId());
    }

    @Override
    public List<NoteDTO> searchNotesWithScoring(String inputKeywords) {
        String[] keywords = inputKeywords.split("\\s+");
        StringBuilder scoreBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        for (int k = 0; k < keywords.length; k++) {
            if (k > 0) {
                scoreBuilder.append(" + ");
                whereBuilder.append(" OR ");
            }
            scoreBuilder.append("""
                (CASE WHEN workOrder LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN caseNumber LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN serialNumber LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN modelNumber LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN callInPerson LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN callInEmail LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN issue LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN contactName LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN contactEmail LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN street LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN city LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN state LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN country LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN additionalCorrectiveActionText LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN relatedCaseNumber LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN title LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN timestamp LIKE '%' || ? || '%' THEN 1 ELSE 0 END) +
                (CASE WHEN createdWorkOrder LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
                (CASE WHEN tex LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END)
            """);
            whereBuilder.append("""
                workOrder LIKE '%' || ? || '%' COLLATE NOCASE
                OR caseNumber LIKE '%' || ? || '%' COLLATE NOCASE
                OR serialNumber LIKE '%' || ? || '%' COLLATE NOCASE
                OR modelNumber LIKE '%' || ? || '%' COLLATE NOCASE
                OR callInPerson LIKE '%' || ? || '%' COLLATE NOCASE
                OR callInEmail LIKE '%' || ? || '%' COLLATE NOCASE
                OR issue LIKE '%' || ? || '%' COLLATE NOCASE
                OR contactName LIKE '%' || ? || '%' COLLATE NOCASE
                OR contactEmail LIKE '%' || ? || '%' COLLATE NOCASE
                OR street LIKE '%' || ? || '%' COLLATE NOCASE
                OR city LIKE '%' || ? || '%' COLLATE NOCASE
                OR state LIKE '%' || ? || '%' COLLATE NOCASE
                OR country LIKE '%' || ? || '%' COLLATE NOCASE
                OR additionalCorrectiveActionText LIKE '%' || ? || '%' COLLATE NOCASE
                OR relatedCaseNumber LIKE '%' || ? || '%' COLLATE NOCASE
                OR title LIKE '%' || ? || '%' COLLATE NOCASE
                OR timestamp LIKE '%' || ? || '%'
                OR createdWorkOrder LIKE '%' || ? || '%' COLLATE NOCASE
                OR tex LIKE '%' || ? || '%' COLLATE NOCASE
            """);
        }
        String sql = String.format("""
            SELECT *, (%s) AS match_score
            FROM Notes
            WHERE %s
            ORDER BY match_score DESC
        """, scoreBuilder.toString(), whereBuilder.toString());
        List<Object> params = new ArrayList<>();
        for (String keyword : keywords) {
            String normalizedKeyword = NoteTools.normalizeDate(keyword);
            String likeKeyword = "%" + normalizedKeyword + "%";
            // Add parameters for scoring
            for (int i = 0; i < 19; i++) { // Adjusted to 19 columns
                params.add(likeKeyword);
            }
            // Add parameters for WHERE clause
            for (int i = 0; i < 19; i++) { // Adjusted to 19 columns
                params.add(likeKeyword);
            }
        }
        return jdbcTemplate.query(sql, ps -> {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i)); // JDBC indices are 1-based
            }
        }, new NotesRowMapper());
    }

    public boolean isNewest(NoteDTO note) {
        String sql = """
                    SELECT COUNT(*) > 0 
                    FROM Notes 
                    WHERE id = ? AND timestamp = (SELECT MAX(timestamp) FROM Notes)
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, note.getId());
    }

    public boolean isOldest(NoteDTO note) {
        String sql = """
                    SELECT COUNT(*) > 0 
                    FROM Notes 
                    WHERE id = ? AND timestamp = (SELECT MIN(timestamp) FROM Notes)
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, note.getId());
    }
}
