package com.L2.mvci.parts;

import com.L2.dto.global_spares.RangesFx;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartInteractor {
    private static final Logger logger = LoggerFactory.getLogger(PartInteractor.class);
    private final PartModel partModel;
    private final GlobalSparesRepositoryImpl globalSparesRepo;

    public PartInteractor(PartModel partModel) {
    this.partModel = partModel;
    this.globalSparesRepo = new GlobalSparesRepositoryImpl();
    }

    public void mapProductFamiliesJSONtoPOJO() {
        String jsonResponse = partModel.selectedSpareProperty().get().getPim();
        try {
            partModel.setProductFamilies(partModel.getObjectMapper().readValue(
                    jsonResponse,
                    new TypeReference<>() {
                    }
            ));
        } catch (JsonProcessingException ex) {
            logger.error("Error deserializing JSON: {}", ex.getMessage());
        }

    }

    public void setSelectedRange() {
        String newRange = partModel.comboBoxSelectedRangeProperty().get();
        System.out.println("newRange: " + newRange);
        // getting the range from the list that equals what we a clicked on
        RangesFx selectedRange = partModel.getRanges().stream()
                .filter(range -> range.getRange().equals(newRange))
                .findFirst()
                .orElse(null);
        System.out.println("selectedRange found from list: " + selectedRange);
        // if there is a selected range
        if (selectedRange != null) {
            partModel.setSelectedRange(selectedRange);
            System.out.println("selectedRange found from list and referenced to partModel: " + selectedRange);
        } else {
            logger.error("No matching range found for: {}", newRange);
            if (!partModel.getRanges().isEmpty()) {
                logger.warn("Defaulting to first range");
                partModel.setSelectedRange(partModel.getRanges().getFirst());
            } else {
                logger.error("Ranges list is empty, setting selectedRange to null");
                partModel.setSelectedRange(null);
            }
        }
    }

    public void savePart() {
        int success = globalSparesRepo.updateSpare(partModel.selectedSpareProperty().get());
        partModel.getUpdatedNotesProperty().set(success == 1);
    }

    public void cancelNoteUpdate() {
        partModel.getUpdatedNotesProperty().set(true);
    }
}
