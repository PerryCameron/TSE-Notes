package com.L2.mvci.note.mvci.partorderbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartOrderBoxInteractor {
    private static final Logger logger = LoggerFactory.getLogger(PartOrderBoxInteractor.class);
    private final PartOrderBoxModel partOrderBoxModel;

    public PartOrderBoxInteractor(PartOrderBoxModel partOrderBoxModel) {
        this.partOrderBoxModel = partOrderBoxModel;
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
        if(partOrderBoxModel.getPartController() == null) {
            System.out.println("No part controller available");
        }
        partOrderBoxModel.getPartController().printProductFamilies();
    }
}
