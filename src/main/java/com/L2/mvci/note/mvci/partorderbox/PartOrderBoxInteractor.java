package com.L2.mvci.note.mvci.partorderbox;

import com.L2.dto.global_spares.SparesDTO;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PartOrderBoxInteractor {
    private static final Logger logger = LoggerFactory.getLogger(PartOrderBoxInteractor.class);
    private final PartOrderBoxModel partOrderBoxModel;
    private final GlobalSparesRepositoryImpl globalSparesRepo;

    public PartOrderBoxInteractor(PartOrderBoxModel partOrderBoxModel) {
        this.partOrderBoxModel = partOrderBoxModel;
        this.globalSparesRepo = new GlobalSparesRepositoryImpl();
    }

    public void flash() {
        partOrderBoxModel.flash();
    }

    public void refreshFields() {
        partOrderBoxModel.refreshFields();
    }

    public void printPartsTableView() {
        System.out.println("Parts table view ----------------------------");
        partOrderBoxModel.getTableView().getItems().forEach(item -> {
            System.out.println(item.toTestString());
        });
    }

    // sending signal to part mvci
    public void printProductFamilies() {
        if (partOrderBoxModel.getPartController() == null) {
            System.out.println("No part controller available");
        }
        partOrderBoxModel.getPartController().printProductFamilies();
    }

    public void viewPartAsSpare() {
        SparesDTO sparesDTO = globalSparesRepo.findBySpareItem(partOrderBoxModel.getNoteModel().selectedPartProperty().get().getPartNumber());
        if (sparesDTO == null) {
            // is a new part
            partOrderBoxModel.setMessage(PartOrderBoxMessage.NEW_PART);
        } else {
            partOrderBoxModel.setSpare(sparesDTO);
            partOrderBoxModel.setMessage(PartOrderBoxMessage.PART_EXISTS);
        }
    }

    public void resetPartListener() {
        partOrderBoxModel.setMessage(PartOrderBoxMessage.NONE);
    }

    /**
     * Adds a new part to the database if it does not already exist.
     * Uses the part number and part description from the selected part in the partOrderBoxModel.
     * creates a timestamp and records person who created the part
     * If the part is successfully inserted, updates the partOrderBoxModel message to indicate the part exists.
     */
    public void addPartToDb(String fullName) {
        SparesDTO sparesDTO = new SparesDTO(
                partOrderBoxModel.getNoteModel().selectedPartProperty().get().getPartNumber(),
                partOrderBoxModel.getNoteModel().selectedPartProperty().get().getPartDescription());
        String timestamp = ZonedDateTime.now(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
        String updatedBy = "[ {\n" +
                "  \"updated_by\" : \"" + fullName + "\",\n" +
                "  \"updated_date_time\" : \"" + timestamp + "\",\n" +
                "  \"change_made\" : \"ADD\"\n" +
                "} ]";
        sparesDTO.setLastUpdatedBy(updatedBy);
        if(globalSparesRepo.insertSpare(sparesDTO) == 1) {
            // need to  add spare here: partViewerModel.getSparesDTO().getSpareItem()
            partOrderBoxModel.setSpare(sparesDTO);
            partOrderBoxModel.setMessage(PartOrderBoxMessage.PART_EXISTS);
        }
    }
}
