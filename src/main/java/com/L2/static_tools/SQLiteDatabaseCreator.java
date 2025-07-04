package com.L2.static_tools;

import com.L2.BaseApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SQLiteDatabaseCreator {

    private static final Logger logger = LoggerFactory.getLogger(SQLiteDatabaseCreator.class);

    public static void main(String[] args) {
        createDataBase("test-notes.db");
    }

    public static void createDataBase(String databaseName) {
        Path path = AppFileTools.getDbPath();
        logger.info("Creating database...{}", path.toString());
        String url = "jdbc:sqlite:" + BaseApplication.dataBaseLocation.resolve(databaseName);

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
                    t_and_m TEXT DEFAULT '' NOT NULL,
                    CHECK (completed IN (0, 1)),
                    CHECK (isEmail IN (0, 1)),
                    CHECK (loadSupported IN (0, 1)),
                    CHECK (underWarranty IN (0, 1))
                );
                
                CREATE TABLE IF NOT EXISTS PartOrders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    noteId INTEGER NOT NULL REFERENCES Notes,
                    orderNumber TEXT,
                    showType INTEGER NOT NULL CHECK (showType IN (0, 1)) -- Boolean: 0 = false, 1 = true
                );
                
                CREATE TABLE IF NOT EXISTS Parts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    partOrderId INTEGER NOT NULL REFERENCES PartOrders,
                    partNumber TEXT,
                    partDescription TEXT,
                    partQuantity TEXT,
                    serialReplaced TEXT,
                    partEditable INTEGER NOT NULL CHECK (partEditable IN (0, 1)), -- Boolean: 0 = false, 1 = true
                    lineType TEXT DEFAULT 'Advanced Exchange' NOT NULL
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
                ('Advantage Ultra', 'Labor, Travel, and Parts (Reactive)
                24/7 Technical Support
                1 Annual Preventive Maintenance Visit
                1 Set of air filters if applicable', 'Batteries
                Labor for battery replacement
                Parts and labor for proactive parts replacement.'),
                ('Advantage Prime', '24/7 Technical Support
                1 Annual Preventive Maintenance Visit
                Labor, and Travel
                Parts at a discounted rate', 'Batteries, or battery replacement labor. (Internal or External)
                Parts and labor for proactive parts replacement.'),
                ('Advantage Plus', '24/7 Technical Support
                1 Annual Preventive Maintenance Visit
                Labor at standard Schneider rates
                Parts at a discounted rate', 'No discount on batteries (internal or external)'),
                ('On-Site Warranty Extension', 'Labor, Travel, and Parts (Reactive)
                24/7 Technical Support
                1 Site Inspection Visit
                1 set of air filters if applicable', 'Batteries
                Parts and labor for proactive parts replacement'),
                ('Warranty', 'Labor, Travel, and Parts (Reactive)
                24/5 Technical Support', 'Equipment that has been damaged by accident, negligence or misapplication
                Equipment that has been altered or modified in any way'),
                ('None', '', '');
                """;

        String insertBlankUser = """
                INSERT INTO user (first_name, last_name, email, sesa_number, url) VALUES
                ('','','','','');
                """;

        String timeStamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
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

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        DatabaseTools.checkForDatabaseChanges();
    }


    public static void createChangeSetDB() {
        logger.info("Creating change set database...");
        String url = "jdbc:sqlite:" + ApplicationPaths.changeSetDir.resolve("change_set.sqlite");

        String createTables = """
                                            CREATE TABLE  IF NOT EXISTS spares (
                                                    id                     INTEGER PRIMARY KEY AUTOINCREMENT,
                                                    pim                    TEXT, -- JSON storing pim_range and pim_product_family
                                                    spare_item             TEXT UNIQUE, -- I want no duplicates here its a key
                                                    replacement_item       TEXT,
                                                    standard_exchange_item TEXT,
                                                    spare_description      TEXT,
                                                    catalogue_version      TEXT,
                                                    end_of_service_date    TEXT,
                                                    last_update            TEXT,
                                                    added_to_catalogue     TEXT,
                                                    removed_from_catalogue TEXT,
                                                    comments               TEXT,
                                                    keywords               TEXT,
                                                    archived               INTEGER NOT NULL,
                                                    custom_add             INTEGER NOT NULL,
                                                    last_updated_by        TEXT,
                                                    CHECK (archived IN (0, 1)),
                                                    CHECK (custom_add IN (0, 1))
                                            );
                                                
                                            CREATE TABLE spare_pictures (
                                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                    spare_name TEXT NOT NULL UNIQUE,
                                                    picture BLOB NOT NULL,
                                                    FOREIGN KEY (spare_name) REFERENCES spares(spare_item) ON DELETE CASCADE
                                            );
                """;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Create tables
            stmt.executeUpdate(createTables);
            logger.info("Change Set Tables created successfully.");

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}

