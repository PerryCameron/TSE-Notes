package com.L2.static_tools;

import com.L2.dto.EntitlementDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class AppFileTools {

    private static final Logger logger = LoggerFactory.getLogger(AppFileTools.class);

    public static void createFileIfNotExists(Path settingsDir) throws IOException {
        if (!Files.exists(settingsDir)) {
            Files.createDirectories(settingsDir);
        }
    }

    public static void startFileLogger() {
        try {
            File outputFile = File.createTempFile("debug", ".log", new File(ApplicationPaths.settingsDir.toString()));
            PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)), true);
            System.setOut(output);
            System.setErr(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
