package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GlobalSparesSQLiteDatabaseCreator {


    private static final Logger logger = LoggerFactory.getLogger(GlobalSparesSQLiteDatabaseCreator.class);

    public static void createDataBase(String databaseName) {
    Path path = AppFileTools.getDbPath();
        logger.info("Creating database...{}", path.toString());
        String url = "jdbc:sqlite:" + ApplicationPaths.globalSparesDir.resolve(databaseName);

        // SQL commands for creating tables
        String createTables = """
                                CREATE TABLE IF NOT EXISTS product_to_spares (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    pim_range TEXT,
                                    pim_product_family TEXT,
                                    spare_item TEXT,
                                    replacement_item TEXT,
                                    standard_exchange_item TEXT,
                                    spare_description TEXT,
                                    catalogue_version TEXT,
                                    end_of_service_date TEXT,
                                    last_update TEXT,
                                    added_to_catalogue TEXT,
                                    removed_from_catalogue TEXT,
                                    comments TEXT,
                                    keywords TEXT,
                                    archived INTEGER NOT NULL CHECK (archived IN (0, 1)), 
                                    custom_add INTEGER NOT NULL CHECK (custom_add IN (0, 1)),
                                    last_updated_by TEXT                         
                                );

                                CREATE TABLE  IF NOT EXISTS spares (
                                    id                     INTEGER PRIMARY KEY AUTOINCREMENT,
                                    pim                    TEXT, -- JSON storing pim_range and pim_product_family
                                    spare_item             TEXT,
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
              
                                CREATE TABLE IF NOT EXISTS replacement_cr (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    item TEXT,
                                    replacement TEXT,
                                    comment TEXT,
                                    old_qty REAL,
                                    new_qty REAL,
                                    last_update TEXT,
                                    last_updated_by TEXT
                                );
                
                                CREATE TABLE IF NOT EXISTS properties (
                                    created_by TEXT,
                                    last_modified_by TEXT,
                                    creation_date TEXT,
                                    last_modification_date TEXT
                                );

                                CREATE TABLE IF NOT EXISTS ranges (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    range TEXT,
                                    range_additional TEXT,
                                    range_type TEXT,
                                    last_update TEXT DEFAULT CURRENT_TIMESTAMP,
                                    last_updated_by TEXT
                                );

INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Galaxy VX', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Galaxy VM', '3ph', '0G-GVM', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Power Distribution', '3ph', '0G-PD', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE Sinewave', '3ph', 'ACCUSINE', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Easy UPS 3L', '3ph', '3LUPS', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Easy UPS 3M', '3ph', '3MUPS', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Easy UPS 3S', '3ph', '3SUPS', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE EPS 6000', '3ph', 'E6TUPS800', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE Galaxy PW', '3ph', 'EPW', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Galaxy 3500', '3ph', 'G35T', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Galaxy VS', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Galaxy VX', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Galaxy PX', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('Galaxy VL', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE EPS 8000', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE EPSILON STS', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE Galaxy 4000', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE Galaxy 5000', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE Galaxy 7000', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('MGE PMM', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('', '3ph', '', CURRENT_TIMESTAMP, null);
INSERT INTO ranges (range, range_type, range_additional, last_update, last_updated_by) VALUES ('', '3ph', '', CURRENT_TIMESTAMP, null);
                                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Create tables
            stmt.executeUpdate(createTables);
            logger.info("Tables created successfully.");


        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}

