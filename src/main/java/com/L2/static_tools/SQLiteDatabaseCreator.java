package com.L2.static_tools;

import com.L2.repository.implementations.NoteRepositoryImpl;
import com.L2.repository.interfaces.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SQLiteDatabaseCreator {

    private static final Logger logger = LoggerFactory.getLogger(SQLiteDatabaseCreator.class);

    public static void main(String[] args) {
        createDataBase(Path.of(ApplicationPaths.homeDir + "/TSENotes"), "test-notes.db");
    }

    public static void createDataBase(Path path, String databaseName) {
        NoteRepository noteRepository = new NoteRepositoryImpl();

        logger.info("Creating database..." + path.toString());

        Path dirPath = createDirectoryIfNotExists(path);
        System.out.println("dirPath: " + dirPath);

        String url = "jdbc:sqlite:" + dirPath.resolve(databaseName);
        System.out.println("url: " + url);

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
                INSERT INTO entitlements (name, includes, not_included) VALUES
                ('Advantage Ultra', 'Labor, Travel, and Parts (Reactive)\n24/7 Technical Support\n1 Annual Preventive Maintenance Visit\n1 Set of air filters if applicable', 'Batteries\nLabor for battery replacement\nParts and labor for proactive parts replacement.'),
                ('Advantage Prime', '24/7 Technical Support\n1 Annual Preventive Maintenance Visit\nLabor, and Travel\nParts at a discounted rate', 'Batteries, or battery replacement labor. (Internal or External)\nParts and labor for proactive parts replacement.'),
                ('Advantage Plus', '24/7 Technical Support\n1 Annual Preventive Maintenance Visit\nLabor at standard Schneider rates\nParts at a discounted rate', 'No discount on batteries (internal or external)'),
                ('On-Site Warranty Extension', 'Labor, Travel, and Parts (Reactive)\n24/7 Technical Support\n1 Site Inspection Visit\n1 set of air filters if applicable', 'Batteries\nParts and labor for proactive parts replacement'),
                ('Warranty', 'Labor, Travel, and Parts (Reactive)\n24/5 Technical Support', 'Equipment that has been damaged by accident, negligence or misapplication\nEquipment that has been altered or modified in any way'),
                ('None', '', '');
                """;

        String insertBlankUser = """
                INSERT INTO user (first_name, last_name, email, sesa_number, url) VALUES
                ('','','','','');
                """;

        String timeStamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("""
                                        INSERT INTO Notes (
                                          timestamp,
                                          workOrder,
                                          caseNumber,
                                          serialNumber,
                                          modelNumber,
                                          callInPerson,
                                          callInPhoneNumber,
                                          callInEmail,
                                          underWarranty,
                                          activeServiceContract,
                                          serviceLevel,
                                          schedulingTerms,
                                          upsStatus,
                                          loadSupported,
                                          issue,
                                          contactName,
                                          contactPhoneNumber,
                                          contactEmail,
                                          street,
                                          installedAt,
                                          city,
                                          state,
                                          zip,
                                          country,
                                          createdWorkOrder,
                                          tex,
                                          partsOrder,
                                          completed,
                                          isEmail,
                                          additionalCorrectiveActionText,
                                          relatedCaseNumber,
                                          title
                                        ) VALUES (
                        """)
                .append("'").append(timeStamp).append("'").append(',') // timestamp
                .append("''").append(",") // workOrder
                .append("''").append(",") // caseNumber
                .append("''").append(",") // serialNumber
                .append("''").append(",") // modelNumber
                .append("''").append(",") // callInPerson
                .append("''").append(",") // callInPhoneNumber
                .append("''").append(",") // callInEmail
                .append("0").append(",") // underWarranty
                .append("''").append(",") // activeServiceContract
                .append("''").append(",") // serviceLevel
                .append("''").append(",") // schedulingTerms
                .append("''").append(",") // upsStatus
                .append("0").append(",") // isLoadSupported
                .append("''").append(",") // issue
                .append("''").append(",") // contactName
                .append("''").append(",") // contactPhoneNumber
                .append("''").append(",") // contactEmail
                .append("''").append(",") // street
                .append("''").append(",") // installedAt
                .append("''").append(",") // city
                .append("''").append(",") // state
                .append("''").append(",") // zip
                .append("''").append(",") // country
                .append("''").append(",") // createdWorkOrder
                .append("''").append(",") // tex
                .append("''").append(",") // partsOrder
                .append("0").append(",") // isCompleted
                .append("0").append(",") // isEmail
                .append("''").append(",") // additionalCorrectiveActionText
                .append("''").append(",") // relatedCaseNumber
                .append("'')"); // title


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Create tables
            stmt.executeUpdate(createTables);
            logger.info("Tables created successfully.");

            // Insert initial data into the entitlements table
            stmt.executeUpdate(insertEntitlements);
            logger.info("Initial data inserted successfully.");

            stmt.executeUpdate(insertBlankUser);
            logger.info("Created a blank user.");

            stmt.executeUpdate(stringBuilder.toString());
            logger.info("Created a blank note.");

            System.exit(0);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    // Helper method to create directories if they don't exist
    private static Path createDirectoryIfNotExists(Path path) {
        if (!Files.exists(path)) {
            logger.info("Database directory does not exist: {}", path);
            try {
                Files.createDirectories(path);
                logger.info("Directory created: {}", path);
            } catch (Exception e) {
                logger.error("Failed to create directory: {}", e.getMessage());
            }
        } else {
            logger.info("Directory already exists: {}", path);
        }
        return path;
    }
}

