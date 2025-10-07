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
    public String getTheme() {
        String query = "SELECT value FROM settings WHERE key = 'theme'";
        try {
            // Retrieve the value as a String
            return jdbcTemplate.queryForObject(query, String.class); // Return the theme string (e.g., "dark", "light")
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            // If the setting doesn't exist, return a default theme and log it
            logger.warn("Theme setting not found in database; defaulting to 'light'");
            return "light"; // Default theme, adjust as needed (e.g., "dark")
        } catch (Exception e) {
            // Handle other unexpected errors (e.g., database connection issues)
            logger.error("Failed to retrieve theme setting", e);
            return "light"; // Default theme in case of failure
        }
    }

    @Override
    public String setTheme(String theme) {
        String query = "UPDATE settings SET value = ? WHERE key = 'theme'";
        try {
            // Execute the update query with the provided theme
            int rowsAffected = jdbcTemplate.update(query, theme);
            if (rowsAffected == 0) {
                // No rows updated (e.g., key 'theme' doesn't exist)
                logger.warn("No theme setting found to update; returning default 'light'");
                return "light"; // Default theme if no rows were updated
            }
            return theme; // Return the theme that was set
        } catch (Exception e) {
            // Handle unexpected errors (e.g., database connection issues)
            logger.error("Failed to update theme to '{}'", theme, e);
            return "light"; // Default theme in case of failure
        }
    }

    @Override
    public void setSpellCheckEnabled(boolean enabled) {
        String value = String.valueOf(enabled); // Convert boolean to "true" or "false"
        String updateQuery = """
        UPDATE settings
        SET value = ?,
            last_modified = CURRENT_TIMESTAMP
        WHERE key = 'spell_check_enabled'
        """;
        try {
            int rowsAffected = jdbcTemplate.update(updateQuery, value);
            if (rowsAffected == 0) {
                logger.warn("No rows updated for spell check setting. Key 'spell_check_enabled' might not exist.");
            } else {
                logger.info("Spell check setting updated to {}", enabled);
            }
        } catch (Exception e) {
            logger.error("Failed to update spell check setting to {}", enabled, e);
            throw new RuntimeException("Unable to save spell check setting", e);
        }
    }
}
