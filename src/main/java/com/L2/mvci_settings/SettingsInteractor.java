package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.static_tools.AppFileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.L2.static_tools.ApplicationPaths.entitlementsFile;
import static com.L2.static_tools.ApplicationPaths.settingsDir;


public class SettingsInteractor {

    private final SettingsModel settingsModel;

    public SettingsInteractor(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }

    private static final Logger logger = LoggerFactory.getLogger(SettingsInteractor.class);

    public void loadEntitlements() {
        try {
            // Ensure the directory and file exist
            AppFileTools.createFileIfNotExists(settingsDir);
            // Load the entitlements
            ArrayList<EntitlementDTO> entitlements = AppFileTools.getEntitlements(entitlementsFile);
            if (entitlements != null) {
                settingsModel.setEntitlements(entitlements);
                logger.info("Loaded entitlements: " + entitlements.size());
            } else {
                // arrayList is already initialized so really we do nothing but warn
                logger.warn("Entitlements file is empty or could not be read. Initializing with an empty list.");
            }
        } catch (IOException e) {
            logger.error("Failed to load entitlements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveEntitlement() {
        // make a copy of object
        EntitlementDTO entitlementDTO = new EntitlementDTO(settingsModel.getCurrentEntitlement());
        // add the new object to our list
        settingsModel.getEntitlements().add(entitlementDTO);
        // this also clears the fields of the currentEntitlement
        saveAllEntitlements();
    }

    public void saveAllEntitlements() {
        Path homeDir = Paths.get(System.getProperty("user.home"));
        Path settingsDir = homeDir.resolve("tsenotes");
        Path entitlementsFile = settingsDir.resolve("entitlements.settings");


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(entitlementsFile.toFile()))) {
            oos.writeObject(settingsModel.getEntitlements());
            logger.info("Saved Entitlements");
            // clear our current entitlement
            settingsModel.getCurrentEntitlement().clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCurrentEntitlementDTO() {
        settingsModel.setCurrentEntitlement(new EntitlementDTO());
    }

    public void loadCurrentEntitlement() {
        loadEntitlements();
        loadCurrentEntitlementDTO();
    }

    public void printEntitlements() {
        System.out.println("Printing Entitlements.....");
        settingsModel.getEntitlements().forEach(System.out::println);
    }
}
