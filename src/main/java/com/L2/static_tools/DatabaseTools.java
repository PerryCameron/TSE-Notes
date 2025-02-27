package com.L2.static_tools;

import com.L2.BaseApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static void checkForDatabaseChanges() {
        try {
            SQLiteDataSource dataSource = DatabaseConnector.getDataSource("DatabaseTools");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            // Check and add order_type column if it doesn't exist
            if (!columnExists(dataSource, "Parts", "lineType")) {
                addLineTypeColumn(jdbcTemplate);
            }

        } catch (Exception e) {
            logger.error("Error during database schema check/update", e);
            throw new RuntimeException("Database initialization failed", e);
        }
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

    private static void addLineTypeColumn(JdbcTemplate jdbcTemplate) {
        String sql = "ALTER TABLE Parts ADD COLUMN lineType TEXT DEFAULT 'Advanced Exchange' NOT NULL";
        jdbcTemplate.execute(sql);
        logger.info("Added lineType column to Parts table");
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

}

