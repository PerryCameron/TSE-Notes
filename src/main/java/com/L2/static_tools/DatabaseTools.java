package com.L2.static_tools;

import com.L2.BaseApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class DatabaseTools {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseTools.class);

    // Private constructor to prevent instantiation
    private DatabaseTools() {
    }

    // performed during the init() function during JavaFX start-up
    public static void checkForDatabaseChanges() {
        try {
            SQLiteDataSource dataSource = DatabaseConnector.getDataSource("DatabaseTools");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            // Check current schema version
            String currentVersion = getSchemaVersion(jdbcTemplate);
            if (currentVersion == null) {
                // No version exists, assume it's version 1 (original schema) and initialize
                jdbcTemplate.update("INSERT INTO settings (key, value, group_name, description) VALUES ('schema_version', '1', 'schema', 'Original schema')");
                currentVersion = "1";
            }

            // Convert version to integer for comparison
            int version = Integer.parseInt(currentVersion);
            logger.info("Current schema version is {}", currentVersion);

            // Apply migrations based on version
            if (version < 2) {
                // Upgrade from version 1 to 2
                if (!columnExists(dataSource, "Parts", "lineType")) {
                    addLineTypeColumn(jdbcTemplate);
                }
                if (!columnExists(dataSource, "PartOrders", "showType")) {
                    addShowTypeColumn(jdbcTemplate);
                }
                if (!columnExists(dataSource, "Notes", "t_and_m")) {
                    addTMColumn(jdbcTemplate);
                }
                // Update schema version to 2
                updateSchemaVersion(jdbcTemplate, 2, "Added lineType to Parts, showType to PartOrders, and t_and_m to Notes");
            }

            // Check for spell check setting (independent of schema version)
            checkSpellCheckSetting(jdbcTemplate);
            getTheme(jdbcTemplate);

        } catch (Exception e) {
            logger.error("Error during database schema check/update", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    // Helper method to check and initialize spell check setting
    private static void checkSpellCheckSetting(JdbcTemplate jdbcTemplate) {
        String spellCheckKey = "spell_check_enabled";
        String query = "SELECT value FROM settings WHERE key = ?";

        try {
            String spellCheckValue = jdbcTemplate.queryForObject(query, String.class, spellCheckKey);
            logger.info("Spell check setting found: {}", spellCheckValue);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            // No result found, insert default value (e.g., "true" for enabled)
            String defaultValue = "true";
            jdbcTemplate.update(
                    "INSERT INTO settings (key, value, group_name, description) VALUES (?, ?, ?, ?)",
                    spellCheckKey, defaultValue, "ui", "Enable or disable spell checking (true/false)"
            );
            logger.info("Spell check setting not found; initialized to {}", defaultValue);
        }
    }

    // Helper method to get the latest schema version
    private static String getSchemaVersion(JdbcTemplate jdbcTemplate) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT value FROM settings WHERE key = 'schema_version' " +
                            "ORDER BY CAST(value AS INTEGER) DESC LIMIT 1",
                    String.class
            );
        } catch (EmptyResultDataAccessException e) {
            // No schema_version row exists yet
            return null;
        }
    }

    // Helper method to update the schema version
    private static void updateSchemaVersion(JdbcTemplate jdbcTemplate, int newVersion, String versionDescription) {
        logger.info("upgrading schema version to {}", newVersion);
        jdbcTemplate.update(
                "INSERT OR REPLACE INTO settings (key, value, group_name, description, last_modified) " +
                        "VALUES ('schema_version', ?, 'schema', ?, CURRENT_TIMESTAMP)",
                String.valueOf(newVersion), versionDescription
        );
    }

    private static boolean columnExists(SQLiteDataSource dataSource, String tableName, String columnName) {
        try (var conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getColumns(null, null, tableName, columnName)) {
                boolean exists = rs.next();
                logger.debug("Column {} in table {} exists: {}", columnName, tableName, exists);
                return exists;
            }
        } catch (SQLException e) {
            logger.error("Error checking for column existence", e);
            throw new RuntimeException("Failed to check column existence", e);
        }
    }

    // this is using sqlite
    private static void addLineTypeColumn(JdbcTemplate jdbcTemplate) {
        String sql = "ALTER TABLE Parts ADD COLUMN lineType TEXT DEFAULT 'Advanced Exchange' NOT NULL";
        jdbcTemplate.execute(sql);
        logger.info("Added lineType column to Parts table");
    }

    private static void addShowTypeColumn(JdbcTemplate jdbcTemplate) {
        String sql = "ALTER TABLE PartOrders ADD COLUMN showType INTEGER DEFAULT 0 NOT NULL CHECK (showType IN (0, 1))";
        jdbcTemplate.execute(sql);
        logger.info("Added showType column to PartOrders table");
    }

    private static void addTMColumn(JdbcTemplate jdbcTemplate) {
        String sql = "ALTER TABLE Notes ADD COLUMN t_and_m TEXT DEFAULT '' NOT NULL";
        jdbcTemplate.execute(sql);
        logger.info("Added t_and_m column to Notes table");
    }

    public static void backupDatabase() throws IOException {
        String sourcePath = BaseApplication.dataBaseLocation + "/" + BaseApplication.dataBase;
        File sourceFile = new File(sourcePath);
        String BACKUP_DIR = ApplicationPaths.backupDir.toString();

        if (!sourceFile.exists()) {
            logger.warn("No database file found to backup at: {}", sourcePath);
            return;
        }

        // Create backup directory if it doesn't exist
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            boolean created = backupDir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create backup directory: " + BACKUP_DIR);
            }
        }

        // Get existing backups and sort by last modified time (oldest first)
        File[] existingBackups = backupDir.listFiles((dir, name) -> name.endsWith(".backup"));
        if (existingBackups != null && existingBackups.length >= 3) {
            // Sort files by last modified time (oldest first)
            Arrays.sort(existingBackups, Comparator.comparingLong(File::lastModified));

            // Delete oldest files until we're down to 2 (to leave room for the new one)
            for (int i = 0; i < existingBackups.length - 2; i++) {
                if (existingBackups[i].delete()) {
                    logger.info("Deleted old backup: {}", existingBackups[i].getAbsolutePath());
                } else {
                    logger.warn("Failed to delete old backup: {}", existingBackups[i].getAbsolutePath());
                }
            }
        } else {
            logger.info("There are only {} backup files, so none will be deleted.", existingBackups.length);
        }


        // Create backup filename with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFileName = String.format("%s_%s.backup",
                BaseApplication.dataBase.replace(".db", ""),
                timestamp);
        File backupFile = new File(BACKUP_DIR, backupFileName);


        try {
            Files.copy(sourceFile.toPath(),
                    backupFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            logger.info("Database backup created: {}", backupFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to create database backup", e);
            throw e;
        }
    }

    // Helper method to check and initialize spell check setting
    private static void getTheme(JdbcTemplate jdbcTemplate) {
        String themeKey = "theme";
        String query = "SELECT value FROM settings WHERE key = ?";
        try {
            BaseApplication.theme = jdbcTemplate.queryForObject(query, String.class, themeKey);
            logger.info("theme found: {}", BaseApplication.theme);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            // No result found, insert default value (e.g., "true" for enabled)
            String defaultValue = "light";
            jdbcTemplate.update(
                    "INSERT INTO settings (key, value, group_name, description) VALUES (?, ?, ?, ?)",
                    themeKey, defaultValue, "ui", "App theme"
            );
            logger.info("theme setting not found; initialized to {}", defaultValue);
        }
    }
}

