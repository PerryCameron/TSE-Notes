package com.L2.repository.rowmappers;

import com.L2.dto.NoteDTO;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotesRowMapper implements RowMapper<NoteDTO> {

    // Formatter to parse the timestamp from the ResultSet (stored as TEXT)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    @Override
    public NoteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NoteDTO(
                rs.getInt("id"),
                LocalDateTime.parse(rs.getString("timestamp"), formatter), // this is row 21
                rs.getString("workOrder"),
                rs.getString("caseNumber"),
                rs.getString("serialNumber"),
                rs.getString("modelNumber"),
                rs.getString("callInPerson"),
                rs.getString("callInPhoneNumber"),
                rs.getString("callInEmail"),
                rs.getInt("underWarranty") == 1,  // Convert INTEGER to Boolean
                rs.getString("activeServiceContract"),
                rs.getString("serviceLevel"),
                rs.getString("schedulingTerms"),
                rs.getString("upsStatus"),
                rs.getInt("loadSupported") == 1,  // Convert INTEGER to Boolean
                rs.getString("title"),
                rs.getString("issue"),
                rs.getString("contactName"),
                rs.getString("contactPhoneNumber"),
                rs.getString("contactEmail"),
                rs.getString("street"),
                rs.getString("installedAt"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zip"),
                rs.getString("country"),
                rs.getString("createdWorkOrder"),
                rs.getString("tex"),
                rs.getInt("partsOrder"),
                rs.getInt("completed") == 1,      // Convert INTEGER to Boolean
                rs.getInt("isEmail") == 1,        // Convert INTEGER to Boolean
                rs.getString("additionalCorrectiveActionText"),
                rs.getString("relatedCaseNumber")
        );
    }
}
