package com.L2.mvci.changeset;

import com.L2.dto.UpdatedByDTO;
import com.L2.dto.UserDTO;
import com.L2.dto.global_spares.SparePictureDTO;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.repository.implementations.ChangeSetRepositoryImpl;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.ApplicationPaths;
import com.L2.static_tools.SQLiteDatabaseCreator;
import com.L2.widgetFx.DialogueFx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class ChangeInteractor {

    private static final Logger logger = LoggerFactory.getLogger(ChangeInteractor.class);
    private final ChangeModel changeModel;
    private final GlobalSparesRepositoryImpl globalSparesRepo;
    private final ChangeSetRepositoryImpl changeSetRepo;

    public ChangeInteractor(ChangeModel changeModel) {
        this.changeModel = changeModel;
        this.globalSparesRepo = new GlobalSparesRepositoryImpl();
        this.changeSetRepo = new ChangeSetRepositoryImpl();
    }

    public void createChangeSet(ExecutorService executorService) {
        Task<Void> createChangeSet = new Task<>() {
            @Override
            protected Void call() {
                // create folder changeset if it does not exist
                AppFileTools.createFileIfNotExists(ApplicationPaths.changeSetDir);
                // clear contents of directory if any exist
                try {
                    AppFileTools.clearDirectory(ApplicationPaths.changeSetDir); // why did this not  delete my file???
                } catch (IOException e) {
                    Platform.runLater(() ->
                            DialogueFx.errorAlert("Error Deleting old change set", e.getMessage()));
                    logger.error("Error clearing directory: {}", e.getMessage());
                } catch (IllegalArgumentException e) {
                    logger.error("Invalid directory: {}", e.getMessage());
                    Platform.runLater(() ->
                            DialogueFx.errorAlert("Illegal Argument ", e.getMessage()));
                }
                // create database with note and note_image tables
                SQLiteDatabaseCreator.createChangeSetDB();
                // write tables that fit into category into change set
                List<SparesDTO> spares = globalSparesRepo.findSparesUpdatedWithinDays(changeModel.numberOfDaysProperty().get());
                spares.forEach(sparesDTO -> {
                    // turn updated by JSON into UpdatedByDTO's
                    deserializeUpdatedBy(sparesDTO);
                    // find the UpdatedByDTO which matches the main lastUpdated timestamp
                    UpdatedByDTO updatedByDTO = findCorrectEntry(sparesDTO);
                    if (updatedByDTO != null) {
                        if (changeModel.isIncludeAll()) {
                            changeSetRepo.insertSpare(sparesDTO);
                            if (updatedByDTO.getChangeMade() != null) {
                                Optional<SparePictureDTO> sparePictureDTO = globalSparesRepo.findSparePictureByName(sparesDTO.getSpareItem());
                                if (sparePictureDTO.isPresent()) {
                                    changeSetRepo.insertSparePicture(sparePictureDTO.get());
                                }
                            }
                        } else { // we are only going to include entries by the user
                            if (updatedByDTO.getUpdatedBy().equals(changeModel.getUser().getFullName())) {
                                changeSetRepo.insertSpare(sparesDTO);
                                Optional<SparePictureDTO> sparePictureDTO = globalSparesRepo.findSparePictureByName(sparesDTO.getSpareItem());
                                if (sparePictureDTO.isPresent()) {
                                    changeSetRepo.insertSparePicture(sparePictureDTO.get());
                                }
                            }
                        }
                    }
                });
                return null;
            }
        };
        createChangeSet.setOnSucceeded(event -> {
            Optional<Alert> finishAlert = DialogueFx.conformationAlert("Change Set successfully created","Would you like to open the file location?");
            finishAlert.ifPresent(alert -> {
                Optional<ButtonType> result = alert.showAndWait();
                result.ifPresent(buttonType -> {
                    if (buttonType == ButtonType.YES) {
                        try {
                            java.awt.Desktop.getDesktop().open(new java.io.File(ApplicationPaths.changeSetDir.toString()));
                        } catch (IOException e) {
                            System.err.println("Failed to open directory: " + ApplicationPaths.changeSetDir);
                            e.printStackTrace();
                            // Optionally show an error alert to the user
                            DialogueFx.errorAlert("Error", "Unable to open the directory: " + ApplicationPaths.changeSetDir);
                        }
                    }
                });
            });
        });
        executorService.submit(createChangeSet);
    }

    // makes sure the date from the main tuple matches the updatedBy date entry, time is ignored.
    private UpdatedByDTO findCorrectEntry(SparesDTO sparesDTO) {
        String lastUpdate = sparesDTO.getLastUpdate();
        if (lastUpdate == null || lastUpdate.length() < 10) {
            return null;
        }
        return changeModel.getUpdatedBys().stream()
                .filter(updatedByDTO -> {
                    String updatedDateTime = updatedByDTO.getUpdatedDateTime();
                    return updatedDateTime != null
                            && updatedDateTime.length() >= 10
                            && updatedDateTime.substring(0, 10).equals(lastUpdate.substring(0, 10));
                })
                .findFirst()
                .orElse(null);
    }

    private void deserializeUpdatedBy(SparesDTO spare) {
        ObjectMapper mapper = new ObjectMapper();
        // Early exit if no selected spare or no lastUpdatedBy data
        String lastUpdateJSON = spare.getLastUpdatedBy();
        // Early exit if JSON is null or empty
        if (lastUpdateJSON == null || lastUpdateJSON.trim().isEmpty()) {
            return;
        }
        try {
            // Configure ObjectMapper to accept either "updated_by" or "updatedDateTime" as valid field names
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // Deserialize JSON into List<UpdatedByDTO>
            List<UpdatedByDTO> deserializedList = mapper.readValue(lastUpdateJSON,
                    mapper.getTypeFactory().constructCollectionType(List.class, UpdatedByDTO.class));
            // Clear existing list to avoid duplicates
            changeModel.getUpdatedBys().clear();
            // Add deserialized items to the list
            changeModel.getUpdatedBys().addAll(deserializedList);
        } catch (JsonProcessingException e) {
            DialogueFx.errorAlert("Object mapping failed for " + spare.getSpareItem(), e.getMessage());
        }
    }

    public void setUser(UserDTO user) {
        changeModel.userProperty().set(user);
    }
}
