package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.dto.UserDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.repository.implementations.EntitlementsRepositoryImpl;
import com.L2.repository.implementations.SettingsRepositoryImpl;
import com.L2.repository.implementations.UserRepositoryImpl;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.ExcelRipper;
import com.L2.static_tools.GlobalSparesSQLiteDatabaseCreator;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SettingsInteractor {

    private final SettingsModel settingsModel;
    private final UserRepositoryImpl userRepo;
    private final EntitlementsRepositoryImpl entitlementRepo;
    private final SettingsRepositoryImpl settingsRepo;
    private boolean isProcessing = false; // Extra flag to prevent multiple runs
    private Task<Void> currentTask;

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
        if (settingsModel.currentEntitlementProperty().get() != null) {
            entitlementRepo.deleteEntitlement(settingsModel.currentEntitlementProperty().get());
            settingsModel.getEntitlements().remove(settingsModel.currentEntitlementProperty().get());
        }
    }

    public void printEntitlements() {
        System.out.println("Printing Entitlements.....");
        for (EntitlementDTO entitlementDTO : settingsModel.getEntitlements()) {
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


    public void convertExcelToSql() {
        if (isProcessing) {
            System.out.println("Already processing, skipping new run.");
            return;
        }
        if (currentTask != null && !currentTask.isDone()) {
            System.out.println("Task still running, skipping new run.");
            return;
        }

        isProcessing = true;
        System.out.println("Starting convertExcelToSql: " + System.currentTimeMillis());
        logMemory("Before task start");

        currentTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String filePath = settingsModel.filePathProperty().get();
                if (filePath == null || filePath.isEmpty()) {
                    System.out.println("No file path provided.");
                    return null;
                }
                logMemory("Before workbook load");
                try (FileInputStream fis = new FileInputStream(filePath);
                     XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
                    logMemory("After workbook load");
                    // create the folder to hold database if it does not exist
                    AppFileTools.getOrCreateGlobalSparesFolder();
                    // creates the database and puts it in database folder
                    GlobalSparesSQLiteDatabaseCreator.createDataBase("global-spares.db");
                    // extracts information from xlsx file and updates database with extracted information
                    ExcelRipper.extractWorkbookToSql(workbook);
                    logMemory("Before workbook close");
                }
                logMemory("After workbook close");

                return null;
            }
        };

        currentTask.setOnSucceeded(e -> {
            System.out.println("Conversion complete");
            logMemory("Task succeeded");
            currentTask = null;
            isProcessing = false;
            System.gc(); // Ensure full release
            logMemory("After task GC");
        });
        currentTask.setOnFailed(e -> {
            System.out.println("Task failed: " + currentTask.getException());
            logMemory("Task failed");
            currentTask = null;
            isProcessing = false;
            System.gc();
            logMemory("After task GC");
        });
        currentTask.setOnCancelled(e -> {
            System.out.println("Task cancelled");
            logMemory("Task cancelled");
            currentTask = null;
            isProcessing = false;
            System.gc();
            logMemory("After task GC");
        });

        new Thread(currentTask).start();
    }


    private void logMemory(String point) {
        Runtime rt = Runtime.getRuntime();
        long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        System.out.println(point + ": " + usedMB + " MB");
    }
}
