package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.dto.UserDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.repository.implementations.EntitlementsRepositoryImpl;
import com.L2.repository.implementations.SettingsRepositoryImpl;
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
    private final EntitlementsRepositoryImpl entitlementRepo;
    private final SettingsRepositoryImpl settingsRepo;

    public SettingsInteractor(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
        this.userRepo = new UserRepositoryImpl();
        this.entitlementRepo = new EntitlementsRepositoryImpl();
        this.settingsRepo = new SettingsRepositoryImpl();
    }

    private static final Logger logger = LoggerFactory.getLogger(SettingsInteractor.class);

    public void saveEntitlement() {
        settingsModel.getCurrentEntitlement().setName(settingsModel.getEntitlementTextField().getText());
        settingsModel.getCurrentEntitlement().setIncludes(settingsModel.getIncludeTextArea().getText());
        settingsModel.getCurrentEntitlement().setNotIncludes(settingsModel.getIncludeNotTextArea().getText());
        entitlementRepo.updateEntitlement(settingsModel.getCurrentEntitlement());
    }

    public void createNewEntitlement() {
        EntitlementDTO entitlementDTO = new EntitlementDTO();
        entitlementDTO.setId(entitlementRepo.insertEntitlement(entitlementDTO));
        settingsModel.getEntitlements().add(entitlementDTO);
        // Automatically select the new entitlement in the TableView
        TableView<EntitlementDTO> tableView = settingsModel.getEntitlementsTableView();
        tableView.getSelectionModel().clearSelection(); // this is settingsInteractor.java:56
        tableView.getSelectionModel().select(entitlementDTO);
        tableView.scrollTo(entitlementDTO); // Optionally scroll to the new item
    }

    public void deleteEntitlement() {
        if(settingsModel.getCurrentEntitlement() != null) {
        entitlementRepo.deleteEntitlement(settingsModel.getCurrentEntitlement());
        settingsModel.getEntitlements().remove(settingsModel.getCurrentEntitlement());
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
