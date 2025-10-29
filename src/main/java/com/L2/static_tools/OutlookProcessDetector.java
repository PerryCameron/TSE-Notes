package com.L2.static_tools;

import com.L2.enums.OutlookType;
import com.L2.mvci.note.NoteInteractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class OutlookProcessDetector {

    private static final Logger logger = LoggerFactory.getLogger(OutlookProcessDetector.class);

    public static OutlookType isClassicOutlook() {
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            logger.error("This tool only works on Windows.");
            return OutlookType.UNKNOWN;
        }

        try {
            ProcessBuilder builder = new ProcessBuilder("tasklist", "/fo", "csv", "/nh");
            Process process = builder.start();

            boolean classicRunning;
            boolean newRunning;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                classicRunning = false;
                newRunning = false;

                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip empty lines
                    if (line.trim().isEmpty()) continue;

                    // Parse CSV line: split by comma, handle quoted fields
                    List<String> fields = parseCsvLine(line);

                    if (fields.size() < 2) continue; // Malformed line

                    String imageName = fields.get(0).trim().toLowerCase();

                    if ("outlook.exe".equals(imageName)) {
                        classicRunning = true;
                    } else if ("olk.exe".equals(imageName)) {
                        newRunning = true;
                    }
                }
            }

            process.waitFor(); // Ensure process completes

            // Output result
            if (classicRunning && !newRunning) {
                logger.info("Classic Outlook is currently running.");
                return OutlookType.CLASSIC;
            } else if (newRunning && !classicRunning) {
                logger.info("New Outlook is currently running.");
                return OutlookType.NEW;
            } else if (classicRunning && newRunning) {
                logger.error("Both Outlook versions are running.");
                return OutlookType.BOTH;
            } else {
                logger.warn("No Outlook instance is currently running.");
                return OutlookType.NONE;
            }
        } catch (Exception e) {
            logger.error("Error detecting Outlook processes: {}", e.getMessage());
            return OutlookType.ERROR;
        }
    }

    // Simple CSV parser that handles quoted fields
    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        result.add(field.toString()); // last field
        return result;
    }
}