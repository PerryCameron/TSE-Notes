package com.L2.static_tools;

import com.L2.dto.EntitlementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

    public static ArrayList<EntitlementDTO> getEntitlements(Path path) {
        if (Files.exists(path)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                // Attempt to read the object
                Object object = ois.readObject();
                // Ensure the object is an ArrayList of EntitlementDTO
                if (object instanceof ArrayList<?> list) {
                    // Check if all elements in the list are instances of EntitlementDTO
                    if (list.stream().allMatch(item -> item instanceof EntitlementDTO)) {
                        @SuppressWarnings("unchecked")
                        ArrayList<EntitlementDTO> entitlements = (ArrayList<EntitlementDTO>) list;
                        // Optionally check for empty list or perform further validation
                        if (!entitlements.isEmpty()) {
                            return entitlements;
                        } else {
                            logger.info("No entitlements found.");
                        }
                    } else {
                        logger.error("Invalid data in entitlements file.");
                    }
                } else {
                    logger.error("Unexpected data format in entitlements file.");
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}
