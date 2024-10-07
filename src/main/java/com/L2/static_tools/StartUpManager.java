package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StartUpManager {
    private static final Logger logger = LoggerFactory.getLogger(StartUpManager.class);


    // Helper method to create directories if they don't exist
    public static String validateDatabase(String database) {
        logger.info("Validating database location...");

        // Construct the full paths to the potential database locations
        Path preferredPath = ApplicationPaths.preferredDbDirectory.resolve(database);
        Path secondaryPath = ApplicationPaths.secondaryDbDirectory.resolve(database);

        // Check if the database exists at the preferred location
        if (Files.exists(preferredPath) && Files.isRegularFile(preferredPath)) {
            logger.info("Database location: {}", preferredPath);
            return preferredPath.toString();
        }
        // Check if the database exists at the secondary location
        else if (Files.exists(secondaryPath) && Files.isRegularFile(secondaryPath)) {
            logger.info("Database is located in the secondary location: {}", secondaryPath);
            return secondaryPath.toString();
        }
        // No database file found at either location
        else {
            logger.info("No Database could be found at preferred or secondary location");
            return "no-database";
        }
    }

}
