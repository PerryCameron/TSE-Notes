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
}
