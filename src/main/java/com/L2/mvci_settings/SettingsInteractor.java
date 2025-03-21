package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.dto.UserDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.repository.implementations.EntitlementsRepositoryImpl;
import com.L2.repository.implementations.SettingsRepositoryImpl;
import com.L2.repository.implementations.UserRepositoryImpl;
import javafx.beans.property.BooleanProperty;
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
        settingsModel.currentEntitlementProperty().get().setName(settingsModel.entitlementTextFieldProperty().get().getText());
        settingsModel.currentEntitlementProperty().get().setIncludes(settingsModel.includeTextAreaProperty().get().getText());
        settingsModel.currentEntitlementProperty().get().setNotIncludes(settingsModel.includeNotTextAreaProperty().get().getText());
        entitlementRepo.updateEntitlement(settingsModel.currentEntitlementProperty().get());
    }

    public void createNewEntitlement() {
        EntitlementDTO entitlementDTO = new EntitlementDTO();
        entitlementDTO.setId(entitlementRepo.insertEntitlement(entitlementDTO));
        settingsModel.getEntitlements().add(entitlementDTO);
        // Automatically select the new entitlement in the TableView
        TableView<EntitlementDTO> tableView = settingsModel.entitlementsTableViewProperty().get();
        tableView.getSelectionModel().clearSelection(); // this is settingsInteractor.java:56
        tableView.getSelectionModel().select(entitlementDTO);
        tableView.scrollTo(entitlementDTO); // Optionally scroll to the new item
    }

    public void deleteEntitlement() {
        if(settingsModel.currentEntitlementProperty().get() != null) {
        entitlementRepo.deleteEntitlement(settingsModel.currentEntitlementProperty().get());
        settingsModel.getEntitlements().remove(settingsModel.currentEntitlementProperty().get());
        }
    }

    public void printEntitlements() {
        System.out.println("Printing Entitlements.....");
        for(EntitlementDTO entitlementDTO : settingsModel.getEntitlements()) {
            System.out.println(entitlementDTO.toFancyString());
        }
    }

    public void changeMenu(Region userRegion) {
        settingsModel.currentMenuProperty().set(userRegion);
    }

    public void referenceExternalModels(NoteModel noteModel) {
        settingsModel.setEntitlements(noteModel.getEntitlements());
    }

    public void setUser(UserDTO user) {
        settingsModel.userProperty().set(user);
    }

    public void saveUser() {
        userRepo.updateUserDTO(settingsModel.userProperty().get());
    }

    // this is defined in settingsModel but why can't I reference these?
    // private BooleanProperty isSpellChecked;

    public void referenceSpellCheckProperty(BooleanProperty spellCheckedProperty) {
        settingsModel.isSpellCheckProperty().get().selectedProperty().bindBidirectional(spellCheckedProperty);
    }

    public void saveSpellCheckStatus() {
        settingsRepo.setSpellCheckEnabled(settingsModel.isSpellCheckProperty().get().selectedProperty().get());
    }
}
