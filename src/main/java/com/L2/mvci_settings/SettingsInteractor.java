package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.dto.UserDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.repository.implementations.UserRepositoryImpl;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SettingsInteractor {

    private final SettingsModel settingsModel;
    private final UserRepositoryImpl userRepo;

    public SettingsInteractor(SettingsModel settingsModel) {
        
        this.settingsModel = settingsModel;
        this.userRepo = new UserRepositoryImpl();
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
            logger.error("{} located in: SettingsInteractor::saveAllEntitlements", e.getMessage());
        }
    }

    public void createNewEntitlement() {
        int id = settingsModel.getEntitlements().stream()
                .mapToInt(EntitlementDTO::getId)
                .max()
                .orElse(0) + 1;

        EntitlementDTO entitlementDTO = new EntitlementDTO(id);
        settingsModel.getEntitlements().add(entitlementDTO);
        // Automatically select the new entitlement in the TableView
        TableView<EntitlementDTO> tableView = settingsModel.getEntitlementsTableView();
        tableView.getSelectionModel().clearSelection(); // this is settingsInteractor.java:56
        tableView.getSelectionModel().select(entitlementDTO);
        tableView.scrollTo(entitlementDTO); // Optionally scroll to the new item
        saveAllEntitlements();
    }

    public void deleteEntitlement() {
        settingsModel.getEntitlements().remove(settingsModel.getCurrentEntitlement());
        if(!settingsModel.getEntitlements().isEmpty()) {
            settingsModel.setCurrentEntitlement(settingsModel.getEntitlements().get(0));
        } else {
            createNewEntitlement();
        }
        saveAllEntitlements();
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

    public void referenceExternalModels(NoteModel noteModel) {
        settingsModel.setEntitlements(noteModel.getEntitlements());
    }

    public void setUser(UserDTO user) {
        settingsModel.setUser(user);
    }

    public void saveUser() {
        userRepo.updateUserDTO(settingsModel.getUser());
    }
}
