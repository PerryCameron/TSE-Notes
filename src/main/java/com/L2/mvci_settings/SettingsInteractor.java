package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.mvci_case.CaseModel;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SettingsInteractor {

    private final SettingsModel settingsModel;

    public SettingsInteractor(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }

    private static final Logger logger = LoggerFactory.getLogger(SettingsInteractor.class);

    public void saveEntitlement() {
        settingsModel.getCurrentEntitlement().setName(settingsModel.gettFEntitlement().getText());
        settingsModel.getCurrentEntitlement().setIncludes(settingsModel.gettFInclude().getText());
        settingsModel.getCurrentEntitlement().setNotIncludes(settingsModel.gettFIncludeNot().getText());
        saveAllEntitlements();
    }

    public void saveAllEntitlements() {
        Path homeDir = Paths.get(System.getProperty("user.home"));
        Path settingsDir = homeDir.resolve("tsenotes");
        Path entitlementsFile = settingsDir.resolve("entitlements.settings");
        ArrayList<EntitlementDTO> serializableList = new ArrayList<>(settingsModel.getEntitlements());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(entitlementsFile.toFile()))) {
            oos.writeObject(serializableList);
            logger.info("Saved Entitlements");
        } catch (IOException e) {
            logger.error(e.getMessage());
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
