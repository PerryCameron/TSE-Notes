package com.L2.mvci_settings;

import com.L2.dto.EntitlementFx;
import com.L2.dto.UserDTO;
import com.L2.dto.global_spares.RangesDTO;
import com.L2.dto.global_spares.RangesFx;
import com.L2.mvci_note.NoteModel;
import com.L2.repository.implementations.EntitlementsRepositoryImpl;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.L2.repository.implementations.SettingsRepositoryImpl;
import com.L2.repository.implementations.UserRepositoryImpl;
import com.L2.static_tools.ApplicationPaths;
import com.L2.widgetFx.DialogueFx;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.Optional;

public class SettingsInteractor {

    private final SettingsModel settingsModel;
    private final UserRepositoryImpl userRepo;
    private final EntitlementsRepositoryImpl entitlementRepo;
    private final SettingsRepositoryImpl settingsRepo;
    private final GlobalSparesRepositoryImpl globalSparesRepo;

    public SettingsInteractor(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
        this.userRepo = new UserRepositoryImpl();
        this.entitlementRepo = new EntitlementsRepositoryImpl();
        this.settingsRepo = new SettingsRepositoryImpl();
        this.globalSparesRepo = new GlobalSparesRepositoryImpl();
    }

    private static final Logger logger = LoggerFactory.getLogger(SettingsInteractor.class);

    public void saveEntitlement() {
        settingsModel.currentEntitlementProperty().get().setName(settingsModel.entitlementTextFieldProperty().get().getText());
        settingsModel.currentEntitlementProperty().get().setIncludes(settingsModel.includeTextAreaProperty().get().getText());
        settingsModel.currentEntitlementProperty().get().setNotIncludes(settingsModel.includeNotTextAreaProperty().get().getText());
        entitlementRepo.updateEntitlement(settingsModel.currentEntitlementProperty().get());
    }

    public void createNewEntitlement() {
        EntitlementFx entitlementDTO = new EntitlementFx();
        entitlementDTO.setId(entitlementRepo.insertEntitlement(entitlementDTO));
        settingsModel.getEntitlements().add(entitlementDTO);
        // Automatically select the new entitlement in the TableView
        TableView<EntitlementFx> tableView = settingsModel.entitlementsTableViewProperty().get();
        tableView.getSelectionModel().clearSelection(); // this is settingsInteractor.java:56
        tableView.getSelectionModel().select(entitlementDTO);
        tableView.scrollTo(entitlementDTO); // Optionally scroll to the new item
    }

    public void deleteEntitlement() {
        if (settingsModel.currentEntitlementProperty().get() != null) {
            entitlementRepo.deleteEntitlement(settingsModel.currentEntitlementProperty().get());
            settingsModel.getEntitlements().remove(settingsModel.currentEntitlementProperty().get());
        }
    }

    public void printEntitlements() {
        System.out.println("Printing Entitlements.....");
        for (EntitlementFx entitlementDTO : settingsModel.getEntitlements()) {
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

    public void referenceSpellCheckProperty(BooleanProperty spellCheckedProperty) {
        settingsModel.isSpellCheckProperty().get().selectedProperty().bindBidirectional(spellCheckedProperty);
    }

    public void saveSpellCheckStatus() {
        settingsRepo.setSpellCheckEnabled(settingsModel.isSpellCheckProperty().get().selectedProperty().get());
    }

    public void checkDatabase() {
        Path globalSparesDir = ApplicationPaths.globalSparesDir.resolve("global-spares.db");

        // Step 1: Check if the file exists
        if (!Files.exists(globalSparesDir)) {
            logger.info("Parts Database is not available: {}", globalSparesDir);
            settingsModel.togglePartsDbAvailable(false);
            return;
        }

        // Step 2: Check if the file is readable
        if (!Files.isReadable(globalSparesDir)) {
            logger.error("Parts Database is not readable: {}", globalSparesDir);
            settingsModel.togglePartsDbAvailable(false);
            return;
        }

        // Step 3: Attempt to connect to the SQLite database and query the spares table
        String dbUrl = "jdbc:sqlite:" + globalSparesDir;
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            // Query to select one row from spares to confirm data availability
            String query = "SELECT id, pim, spare_item FROM spares LIMIT 1";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Data found, database and table are usable
                    logger.info("Successfully queried spares table. Sample row: id={}, pim={}, spare_item={}",
                            rs.getInt("id"), rs.getString("pim"), rs.getString("spare_item"));
                    settingsModel.togglePartsDbAvailable(true);
                } else {
                    // No data in spares table
                    logger.warn("No data found in spares table: {}", globalSparesDir);
                    settingsModel.togglePartsDbAvailable(false);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to connect to Parts Database or query spares table: {}. Error: {}", globalSparesDir, e.getMessage());
            settingsModel.togglePartsDbAvailable(false);
        }
    }

    public void installPartsDatabase() {
        Path globalSparesDir = ApplicationPaths.globalSparesDir.resolve("global-spares.db");
        Task<SettingsMessage> task = new Task<>() {
            @Override
            protected SettingsMessage call() throws Exception {
                try {
                    // Ensure the target directory exists
                    Files.createDirectories(globalSparesDir.getParent());
                    Path droppedFile = settingsModel.droppedFileProperty().get();
                    // Copy the file to the target location
                    Files.copy(droppedFile, globalSparesDir, StandardCopyOption.REPLACE_EXISTING);
                    logger.info("Copied global-spares.db to: {}", globalSparesDir);
                    // Store the dropped file path in the model
                    settingsModel.filePathProperty().set(droppedFile.toAbsolutePath().toString());
                    // Verify the copied database
                    return SettingsMessage.INSTALL_PART_DATABASE_SUCCESS;
                } catch (IOException e) {
                    return SettingsMessage.INSTALL_PART_DATABASE_FAILED;
                }
            }
        };
        task.setOnSucceeded(event -> {
            if (task.getValue() == SettingsMessage.INSTALL_PART_DATABASE_SUCCESS) {
                Platform.runLater(() -> {
                    checkDatabase();
                });
            }
        });
        // Handle task failure
        task.setOnFailed(event -> {
            Throwable e = task.getException();
            logger.error("Database installation failed: {}", e.getMessage());
            Platform.runLater(() -> DialogueFx.errorAlert("Installation Error", "Failed to install database: " + e.getMessage()));
        });
        // Run the task in a background thread
        new Thread(task).start();
    }

    public void setRanges(ObservableList<RangesDTO> ranges) {
        settingsModel.setRanges(ranges);
    }

    public void deleteRange() {
        RangesFx rangesFx = settingsModel.boundRangeFxProperty().get();
        if(globalSparesRepo.deleteRange(rangesFx) == 1) {
            Optional<RangesDTO> deletedRange = Optional.of(settingsModel.getRanges().stream()
                    .filter(rangesDTO -> rangesDTO.getId() == rangesFx.getId()).findFirst().get());
            if (deletedRange.isPresent()) {
                settingsModel.getRanges().remove(deletedRange.get());
            }
            if (!settingsModel.getRanges().isEmpty()) {
                rangesFx.copyFrom(settingsModel.getRanges().getFirst());
            }
        } else {
            logger.error("Failed to delete range: {}", rangesFx);
        }
    }

    public void saveRanges() {
        try {
            globalSparesRepo.saveRanges(settingsModel.getRanges());
        } catch (Exception e) {
            logger.error("Failed to save ranges: {}", e.getMessage());
        }

    }

    public void addRange() {
        saveRanges();
    }

    public void updateRangeInList() {
        if(settingsModel.getRanges() != null) {
            RangesDTO rangesDTO = settingsModel.getRanges().stream().filter(range -> range.getId() == settingsModel.selectedRangeProperty().get().getId()).findFirst().get();
            if(rangesDTO != null) {
                rangesDTO.copyFx(settingsModel.boundRangeFxProperty.get());
            }
        }
    }
}
