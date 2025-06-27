package com.L2.mvci.changeset;

import com.L2.dto.global_spares.SparesDTO;
import com.L2.repository.implementations.ChangeSetRepositoryImpl;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.ApplicationPaths;
import com.L2.static_tools.SQLiteDatabaseCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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

    public void createChangeSet() {
        System.out.println("Creating change set with" + changeModel.numberOfDaysProperty().get() + " days");
        System.out.println("This will include all records: " + changeModel.includeAllProperty().get());
        // create folder changeset if it does not exist
        AppFileTools.createFileIfNotExists(ApplicationPaths.changeSetDir);
        // clear contents of directory if any exist
        try {
            AppFileTools.clearDirectory(ApplicationPaths.changeSetDir);
        } catch (IOException e) {
            logger.error("Error clearing directory: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid directory: {}", e.getMessage());
        }
        // create database with note and note_image tables
        SQLiteDatabaseCreator.createChangeSetDB();
        // write tables that fit into category into change set
        List<SparesDTO> spares = globalSparesRepo.findSparesUpdatedWithinDays(changeModel.numberOfDaysProperty().get());
        spares.forEach(sparesDTO -> {
            changeSetRepo.insertSpare(sparesDTO);
        });
    }
}
