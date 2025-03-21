package com.L2.repository.implementations;

import com.L2.repository.interfaces.SettingsRepository;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class SettingsRepositoryImpl implements SettingsRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(SettingsRepositoryImpl.class);


    public SettingsRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getDataSource("Settings Repo"));
    }

    @Override
    public boolean isSpellCheckEnabled() {
        String spellCheckKey = "spell_check_enabled";
        String query = "SELECT value FROM settings WHERE key = ?";
        try {
            // Retrieve the value as a String and convert to boolean
            String value = jdbcTemplate.queryForObject(query, String.class, spellCheckKey);
            return Boolean.parseBoolean(value); // Converts "true" to true, "false" to false, etc.
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            // If the setting doesn't exist, assume a default (e.g., true) and log it
            logger.warn("Spell check setting not found in database; defaulting to true");
            return true; // Or false, depending on your preference
        } catch (Exception e) {
            // Handle other unexpected errors (e.g., database connection issues)
            logger.error("Failed to retrieve spell check setting", e);
            return true; // Default value in case of failure
        }
    }

    @Override
    public void setSpellCheckEnabled(boolean enabled) {
        String spellCheckKey = "spell_check_enabled";
        String value = String.valueOf(enabled); // Convert boolean to "true" or "false"
        String upsertQuery = """
            INSERT INTO settings (key, value, group_name, description) 
            VALUES (?, ?, 'ui', 'Enable or disable spell checking (true/false)')
            ON CONFLICT(key) DO UPDATE SET 
                value = excluded.value, 
                last_modified = CURRENT_TIMESTAMP
            """;
        try {
            jdbcTemplate.update(upsertQuery, spellCheckKey, value);
            logger.info("Spell check setting updated to {}", enabled);
        } catch (Exception e) {
            logger.error("Failed to update spell check setting to {}", enabled, e);
            throw new RuntimeException("Unable to save spell check setting", e);
        }
    }
}
