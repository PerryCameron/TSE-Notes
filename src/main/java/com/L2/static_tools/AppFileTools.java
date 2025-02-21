package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;


public class AppFileTools {

    private static final Logger logger = LoggerFactory.getLogger(AppFileTools.class);
    public static File outputFile;

    public static void createFileIfNotExists(Path path) {
        if (!Files.exists(path)) {
            logger.info("Database directory does not exist: {}", path);
            try {
                Files.createDirectories(path);
                logger.info("Directory created: {}", path);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public static void startFileLogger() {
        try {
            File logDir = new File(ApplicationPaths.secondaryDbDirectory.toString());
            // Create directory if it doesn't exist
            if (!logDir.exists()) {
                boolean created = logDir.mkdirs();
                if (!created) {
                    throw new IOException("Failed to create log directory: " + logDir.getAbsolutePath());
                }
            }
            // Create new log file first
            outputFile = File.createTempFile("debug", ".log", logDir);
            PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)), true);
            System.setOut(output);
            System.setErr(output);
            // Now check and manage existing log files
            File[] existingLogs = logDir.listFiles((dir, name) -> name.startsWith("debug") && name.endsWith(".log"));
            if (existingLogs != null && existingLogs.length > 3) {  // Note: > 3 since we already created the new one
                // Sort files by last modified time (oldest first)
                Arrays.sort(existingLogs, Comparator.comparingLong(File::lastModified));
                // Delete oldest files until we're down to 3 total (including the new one)
                for (int i = 0; i < existingLogs.length - 3; i++) {
                    if (existingLogs[i].delete()) {
                        logger.info("Deleted old log file: {}", existingLogs[i].getAbsolutePath());
                    } else {
                        logger.error("Failed to delete old log file: {}", existingLogs[i].getAbsolutePath());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Path getDbPath() {
        if (Files.exists(ApplicationPaths.oneDrive)) {
            logger.info("One-drive found: {}", ApplicationPaths.oneDrive);
            if (!Files.exists(ApplicationPaths.preferredDbDirectory)) {
                createFileIfNotExists(ApplicationPaths.preferredDbDirectory);
            }
            return ApplicationPaths.preferredDbDirectory;
        } else {
            logger.info("Preferred path not found using fallback: {}", ApplicationPaths.secondaryDbDirectory);
            if (!Files.exists(ApplicationPaths.homeDir)) {
                logger.error("No path found for database: {}", ApplicationPaths.homeDir);
                System.exit(0);
            } else {
                createFileIfNotExists(ApplicationPaths.secondaryDbDirectory);
            }
            return ApplicationPaths.secondaryDbDirectory;
        }
    }
}
