package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StartUpManager {
    private static final Logger logger = LoggerFactory.getLogger(StartUpManager.class);


    // Helper method to create directories if they don't exist
    public static String validateDatabase() {
        logger.info("Validating database location...");
        Path path;
        // this is our preferred place to store the database
        String preferredLocation = ApplicationPaths.homeDir + "/OneDrive - Schneider Electric/TSENotes/notes.db";
        String secondLocation = ApplicationPaths.homeDir + "/TSENotes/notes.db";
        // If we can get to the one drive lets make our path: onedrive + TSENotes
        if (Files.exists(Paths.get(preferredLocation))) {
            logger.info("Database location: {}", preferredLocation);
            return preferredLocation;
        } else if (Files.exists(Paths.get(secondLocation))) {
            logger.info("Database is located in the secondary location: {}", secondLocation);
            return secondLocation;
        } else { // database does not exist we need to create.
            String preferredDirectory = ApplicationPaths.homeDir + "/OneDrive - Schneider Electric";
            if (Files.exists(Path.of(preferredDirectory))) {
                logger.info("Preferred directory to store database exists, we are going to use that");
                path = Path.of(preferredDirectory + "/TSENotes");
                SQLiteDatabaseCreator.createDataBase(path);
            } else {
                // we don't have access to the one drive so we will default to the home directory
                logger.info("One Drive is not reachable, using home directory instead");
                String secondaryDirectory = ApplicationPaths.homeDir.toString();
                path = Path.of(secondaryDirectory + "/TSENotes");
                SQLiteDatabaseCreator.createDataBase(path);
            }
        }
        return null;
    }
}
