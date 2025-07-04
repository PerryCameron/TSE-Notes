package com.L2.static_tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.DirectoryStream;
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

    public static void clearDirectory(Path dir) throws IOException {
        // Check if the path exists and is a directory
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Path does not exist or is not a directory: " + dir);
        }

        // Iterate over directory contents and delete files
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    Files.delete(entry);
                    logger.info("file deleted: {}", entry);
                }
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

    public static Path getOrCreateGlobalSparesFolder() {
        if (Files.exists(ApplicationPaths.globalSparesDir)) {
            logger.info("Global Spares directory found: {}", ApplicationPaths.globalSparesDir);
            return ApplicationPaths.globalSparesDir;
        } else {
            try {
                Files.createDirectories(ApplicationPaths.globalSparesDir);
                logger.info("Global spares directory created: {}", ApplicationPaths.globalSparesDir);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return ApplicationPaths.globalSparesDir;
    }
}
