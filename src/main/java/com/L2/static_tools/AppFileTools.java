package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class AppFileTools {

    private static final Logger logger = LoggerFactory.getLogger(AppFileTools.class);
    public static File outputFile;

    public static void createFileIfNotExists(Path settingsDir)  {
        if (!Files.exists(settingsDir)) {
            try {
                Files.createDirectories(settingsDir);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public static void startFileLogger() {
        try {
            outputFile = File.createTempFile("debug", ".log", new File(ApplicationPaths.secondaryDbDirectory.toString()));
            PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)), true);
            System.setOut(output);
            System.setErr(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getDbPath() {
        if(Files.exists(ApplicationPaths.oneDrive)) {
            logger.info("One-drive found: {}", ApplicationPaths.oneDrive);
            if(!Files.exists(ApplicationPaths.preferredDbDirectory)) {
                createFileIfNotExists(ApplicationPaths.preferredDbDirectory);
            }
            return ApplicationPaths.preferredDbDirectory;
        }
        else {
            logger.info("Preferred path not found using fallback: {}", ApplicationPaths.secondaryDbDirectory);
            if(!Files.exists(ApplicationPaths.homeDir)) {
                logger.error("No path found for database: {}", ApplicationPaths.homeDir);
                System.exit(0);
            } else {
                createFileIfNotExists(ApplicationPaths.secondaryDbDirectory);
            }
            return ApplicationPaths.secondaryDbDirectory;
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
