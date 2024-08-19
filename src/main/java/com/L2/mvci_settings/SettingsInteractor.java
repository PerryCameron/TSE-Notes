package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SettingsInteractor {

    private final SettingsModel settingsModel;

    public SettingsInteractor(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }
    private static final Logger logger = LoggerFactory.getLogger(SettingsInteractor.class);


    public void loadEntitlements() {
        Path homeDir = Paths.get(System.getProperty("user.home"));
        Path settingsDir = homeDir.resolve("tsenotes");
        Path entitlementsFile = settingsDir.resolve("entitlements.settings");

        try {
            // Check if the directory exists; if not, create it
            if (!Files.exists(settingsDir)) {
                Files.createDirectories(settingsDir);
            }


            // Check if the file exists; if it does, load the entitlements
            if (Files.exists(entitlementsFile)) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(entitlementsFile.toFile()))) {
                    ArrayList<EntitlementDTO> entitlements = (ArrayList<EntitlementDTO>) ois.readObject();
                    System.out.println("entitlements: " + entitlements);
                    settingsModel.setEntitlements(entitlements);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveEntitlement() {
        settingsModel.getEntitlements().add(settingsModel.getCurrentEntitlement());
        saveAllEntitlements();
    }

    public void saveAllEntitlements() {
        Path homeDir = Paths.get(System.getProperty("user.home"));
        Path settingsDir = homeDir.resolve("tsenotes");
        Path entitlementsFile = settingsDir.resolve("entitlements.settings");


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(entitlementsFile.toFile()))) {
            oos.writeObject(settingsModel.getEntitlements());
            logger.info("Saved Entitlements");
            settingsModel.getEntitlements().forEach(System.out::println);
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
}
