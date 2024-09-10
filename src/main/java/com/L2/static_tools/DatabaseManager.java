package com.L2.static_tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {

    // Method to create the database and schema
    public static void createSchema() {
        String url = "jdbc:sqlite:" + ApplicationPaths.settingsDir.resolve("notes.db");  // Path to the SQLite database file
        // SQL commands to create the tables
        String createNotesTable = "CREATE TABLE IF NOT EXISTS Notes (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " timestamp TEXT NOT NULL,\n"
                + " workOrder TEXT,\n"
                + " caseNumber TEXT,\n"
                + " serialNumber TEXT,\n"
                + " modelNumber TEXT,\n"
                + " callInPerson TEXT,\n"
                + " callInPhoneNumber TEXT,\n"
                + " callInEmail TEXT,\n"
                + " underWarranty INTEGER NOT NULL CHECK (underWarranty IN (0, 1)),\n"
                + " activeServiceContract TEXT,\n"
                + " serviceLevel TEXT,\n"
                + " schedulingTerms TEXT,\n"
                + " upsStatus TEXT,\n"
                + " loadSupported INTEGER NOT NULL CHECK (loadSupported IN (0, 1)),\n"
                + " issue TEXT,\n"
                + " contactName TEXT,\n"
                + " contactPhoneNumber TEXT,\n"
                + " contactEmail TEXT,\n"
                + " street TEXT,\n"
                + " installedAt TEXT,\n"
                + " city TEXT,\n"
                + " state TEXT,\n"
                + " zip TEXT,\n"
                + " country TEXT,\n"
                + " createdWorkOrder TEXT,\n"
                + " tex TEXT,\n"
                + " partsOrder INTEGER,\n"
                + " entitlement TEXT,\n"
                + " completed INTEGER NOT NULL CHECK (completed IN (0, 1)),\n"
                + " isEmail INTEGER NOT NULL CHECK (isEmail IN (0, 1)),\n"
                + " additionalCorrectiveActionText TEXT\n"
                + " relatedCaseNumber TEXT,\n"
                + ");";

        String createPartOrdersTable = "CREATE TABLE IF NOT EXISTS PartOrders (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " noteId INTEGER NOT NULL,\n"
                + " orderNumber TEXT,\n"
                + " createdWorkOrder TEXT,\n"
                + " selectedPartId INTEGER,\n"
                + " FOREIGN KEY (noteId) REFERENCES Notes(id)\n"
                + ");";

        String createPartsTable = "CREATE TABLE IF NOT EXISTS Parts (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " partOrderId INTEGER NOT NULL,\n"
                + " partNumber TEXT,\n"
                + " partDescription TEXT,\n"
                + " partQuantity TEXT,\n"
                + " serialReplaced TEXT,\n"
                + " partEditable INTEGER NOT NULL CHECK (partEditable IN (0, 1)),\n"
                + " FOREIGN KEY (partOrderId) REFERENCES PartOrders(id)\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // Execute SQL to create tables
            stmt.execute(createNotesTable);
            stmt.execute(createPartOrdersTable);
            stmt.execute(createPartsTable);

            System.out.println("Database schema created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Create the schema when the application starts
        createSchema();
    }
}


