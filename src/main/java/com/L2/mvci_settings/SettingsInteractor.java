package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.mvci_case.CaseModel;
import com.L2.static_tools.AppFileTools;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsInteractor {

    private final SettingsModel settingsModel;

    public SettingsInteractor(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }

    private static final Logger logger = LoggerFactory.getLogger(SettingsInteractor.class);

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

    public void printEntitlements() {
        System.out.println("Printing Entitlements.....");
        for(EntitlementDTO entitlementDTO : settingsModel.getEntitlements()) {
            System.out.println(entitlementDTO.toFancyString());
        }
    }

    public void changeMenu(Region userRegion) {
        settingsModel.setCurrentMenu(userRegion);
    }

    public void referenceExternalModels(CaseModel caseModel) {
        settingsModel.setEntitlements(caseModel.getEntitlements());
    }
}
