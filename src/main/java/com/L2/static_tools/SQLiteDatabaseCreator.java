package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabaseCreator {

    private static final Logger logger = LoggerFactory.getLogger(SQLiteDatabaseCreator.class);

    public static void createDataBase() {
        String path = createDirectoryIfNotExists();
        String url = "jdbc:sqlite:" + path +"/notes.db";

        // SQL commands for creating tables
        String createTables = """
                CREATE TABLE IF NOT EXISTS Notes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    timestamp TEXT NOT NULL,
                    workOrder TEXT,
                    caseNumber TEXT,
                    serialNumber TEXT,
                    modelNumber TEXT,
                    callInPerson TEXT,
                    callInPhoneNumber TEXT,
                    callInEmail TEXT,
                    underWarranty INTEGER NOT NULL,
                    activeServiceContract TEXT,
                    serviceLevel TEXT,
                    schedulingTerms TEXT,
                    upsStatus TEXT,
                    loadSupported INTEGER NOT NULL,
                    issue TEXT,
                    contactName TEXT,
                    contactPhoneNumber TEXT,
                    contactEmail TEXT,
                    street TEXT,
                    installedAt TEXT,
                    city TEXT,
                    state TEXT,
                    zip TEXT,
                    country TEXT,
                    createdWorkOrder TEXT,
                    tex TEXT,
                    partsOrder INTEGER,
                    completed INTEGER NOT NULL,
                    isEmail INTEGER NOT NULL,
                    additionalCorrectiveActionText TEXT,
                    relatedCaseNumber TEXT,
                    title TEXT,
                    CHECK (completed IN (0, 1)),
                    CHECK (isEmail IN (0, 1)),
                    CHECK (loadSupported IN (0, 1)),
                    CHECK (underWarranty IN (0, 1))
                );

                CREATE TABLE IF NOT EXISTS PartOrders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    noteId INTEGER NOT NULL REFERENCES Notes,
                    orderNumber TEXT
                );

                CREATE TABLE IF NOT EXISTS Parts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    partOrderId INTEGER NOT NULL REFERENCES PartOrders,
                    partNumber TEXT,
                    partDescription TEXT,
                    partQuantity TEXT,
                    serialReplaced TEXT,
                    partEditable INTEGER NOT NULL,
                    CHECK (partEditable IN (0, 1))
                );

                CREATE TABLE IF NOT EXISTS user (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT,
                    sesa_number TEXT,
                    url TEXT
                );

                CREATE TABLE IF NOT EXISTS entitlements (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    includes TEXT,
                    not_included TEXT
                );

                CREATE TABLE IF NOT EXISTS settings (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    key TEXT NOT NULL,
                    value TEXT,
                    group_name TEXT,
                    description TEXT,
                    last_modified DATETIME DEFAULT CURRENT_TIMESTAMP
                );
                """;

        // Insert statements for the `entitlements` table
        String insertEntitlements = """
                INSERT INTO entitlements (id, name, includes, not_included) VALUES
                (1, 'Advantage Ultra', 'Labor, Travel, and Parts (Reactive)\n24/7 Technical Support\n1 Annual Preventive Maintenance Visit\n1 Set of air filters if applicable', 'Batteries\nLabor for battery replacement\nParts and labor for proactive parts replacement.'),
                (2, 'Advantage Prime', '24/7 Technical Support\n1 Annual Preventive Maintenance Visit\nLabor, and Travel\nParts at a discounted rate', 'Batteries, or battery replacement labor. (Internal or External)\nParts and labor for proactive parts replacement.'),
                (3, 'Advantage Plus', '24/7 Technical Support\n1 Annual Preventive Maintenance Visit\nLabor at standard Schneider rates\nParts at a discounted rate', 'No discount on batteries (internal or external)'),
                (4, 'On-Site Warranty Extension', 'Labor, Travel, and Parts (Reactive)\n24/7 Technical Support\n1 Site Inspection Visit\n1 set of air filters if applicable', 'Batteries\nParts and labor for proactive parts replacement'),
                (5, 'Warranty', 'Labor, Travel, and Parts (Reactive)\n24/5 Technical Support', 'Equipment that has been damaged by accident, negligence or misapplication\nEquipment that has been altered or modified in any way'),
                (6, 'None', '', '');
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Create tables
            stmt.executeUpdate(createTables);
            logger.info("Tables created successfully.");

            // Insert initial data into the entitlements table
            stmt.executeUpdate(insertEntitlements);
            logger.info("Initial data inserted successfully.");

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    // Helper method to create directories if they don't exist
    private static String createDirectoryIfNotExists() {
        Path path;
        String preferredPath = ApplicationPaths.homeDir + "/OneDrive - Schneider Electric";
        // If we can get to the one drive lets make our path: onedrive + TSENotes
        if(Files.exists(Paths.get(preferredPath))) {
            path = Path.of(preferredPath + "/TSENotes");
        } else {
            // we don't have access to the one drive so we will default to the home directory
            String secondaryPath = ApplicationPaths.homeDir.toString();
            path = Path.of(secondaryPath + "/TSENotes");
        }
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directory created: " + path);
            } catch (Exception e) {
                System.err.println("Failed to create directory: " + e.getMessage());
            }
        }
        return path.toString();
    }
}

