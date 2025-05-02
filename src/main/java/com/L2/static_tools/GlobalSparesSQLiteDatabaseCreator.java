package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
                
                                CREATE TABLE IF NOT EXISTS replacement_cr (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    item TEXT,
                                    replacement TEXT,
                                    comment TEXT,
                                    old_qty REAL,
                                    new_qty REAL
                                );
                
                                CREATE TABLE IF NOT EXISTS properties (
                                    created_by TEXT,
                                    last_modified_by TEXT,
                                    creation_date TEXT,
                                    last_modification_date TEXT
                                );
                
                                CREATE TABLE IF NOT EXISTS pimTypes (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     product TEXT,
                                     range TEXT,
                                     is_power INTEGER NOT NULL CHECK (is_power IN (0, 1))
                                );
                
                
                INSERT INTO pimTypes (product, range, is_power)VALUES ('ETO - Cooling', '0G-EQMCW', 0);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Uniflair', 'Uniflair InRow Cooling Accessories', 0);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Uniflair', 'Uniflair Chilled Water InRow Cooling', 0);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Uniflair', 'Uniflair Direct Expansion InRow Cooling', 0);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Uniflair', 'Uniflair Rack Mounted Room Cooling', 0);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Uniflair', 'Uniflair InRow Cooling Accessories', 0);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Uniflair', 'Uniflair InRow Cooling Accessories', 0);
                
                INSERT INTO pimTypes (product, range, is_power)VALUES ('InRow', 'InRow', 0);
                
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Galaxy VM', '0G-GVMI200KH', 1);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Galaxy VM', '0G-GVMIP200KH', 1);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Galaxy VM', '0G-GVMIP225KG65K', 1);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Galaxy VM', '0G-GVMIPR225KG', 1);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Galaxy VM', '0G-GVMIPR225KG65K', 1);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Galaxy VM', '0G-GVMPB160K180D', 1);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Galaxy VM', '0G-GVMPB160KG', 1);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('Galaxy VM', '0G-GVMPB200K225D', 1);
                INSERT INTO pimTypes (product, range, is_power)VALUES ('ISX PDU', '0G-PD150G6FX', 1);
                
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

