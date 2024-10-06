package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class StartUpManager {
    private static final Logger logger = LoggerFactory.getLogger(StartUpManager.class);


    // Helper method to create directories if they don't exist
    public static String validateDatabase(String database) {
        logger.info("Validating database location...");
        // this is our preferred place to store the database
        String preferredLocation = ApplicationPaths.homeDir + "\\OneDrive - Schneider Electric\\TSENotes\\" + database;
        String secondLocation = ApplicationPaths.homeDir + "\\TSENotes\\" + database;
        // If we can get to the one drive lets make our path: onedrive + TSENotes
        if (Files.exists(Paths.get(preferredLocation))) {
            logger.info("Database location: {}", preferredLocation);
            return preferredLocation;
        } else if (Files.exists(Paths.get(secondLocation))) {
            logger.info("Database is located in the secondary location: {}", secondLocation);
            return secondLocation;
        } else { // database does not exist we need to create.
            logger.info("No Database could be found at preferred or secondary location");
            return "no-database";
        }
    }
}
