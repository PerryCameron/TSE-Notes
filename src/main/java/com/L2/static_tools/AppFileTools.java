package com.L2.static_tools;

import com.L2.dto.EntitlementDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public static ObservableList<EntitlementDTO> getEntitlements(Path path) {
        if (Files.exists(path)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                Object object = ois.readObject();
                if (object instanceof ArrayList<?>) {
                    ArrayList<EntitlementDTO> list = (ArrayList<EntitlementDTO>) object;
                    if (list.stream().allMatch(item -> item instanceof EntitlementDTO)) {
                        return FXCollections.observableArrayList(list);
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
        return FXCollections.observableArrayList(); // return an empty ObservableList if the file doesn't exist or an error occurs
    }



}
