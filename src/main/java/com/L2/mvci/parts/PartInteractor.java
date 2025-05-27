package com.L2.mvci.parts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartInteractor {
    private static final Logger logger = LoggerFactory.getLogger(PartInteractor.class);
    private final PartModel partModel;

    public PartInteractor(PartModel partModel) {
    this.partModel = partModel;
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

//    public void setSelectedRange() {
//        RangesFx selectedRange = partModel.getRanges().stream()
//                .filter(range -> range.getRange().equals(newValue))
//                .findFirst()
//                .orElse(null);
//        if (selectedRange != null) {
//            noteModel.selectedRangeProperty().set(selectedRange);
//        } else {
//            logger.error("No matching range found for: {}", newValue);
//            if (!noteModel.getRanges().isEmpty()) {
//                logger.warn("Defaulting to first range");
//                noteModel.selectedRangeProperty().set(noteModel.getRanges().getFirst());
//            } else {
//                logger.error("Ranges list is empty, setting selectedRange to null");
//                noteModel.selectedRangeProperty().set(null);
//            }
//        }
//    }
}
