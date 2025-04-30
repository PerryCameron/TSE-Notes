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
                    archived INTEGER NOT NULL CHECK (archived IN (0, 1)),
                    custom_add INTEGER NOT NULL CHECK (custom_add IN (0, 1))
                );
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

