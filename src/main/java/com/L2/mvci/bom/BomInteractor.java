package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BomInteractor {
    private static final Logger logger = LoggerFactory.getLogger(BomInteractor.class);
    private final BomModel bomModel;

    public BomInteractor(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    public void logBomCall() {
        logger.info("Searching bom for {}", bomModel.searchComponentProperty().get());
    }

    public String getTest() {
        return bomModel.searchComponentProperty().get();
    }

    public void setRoot(TreeItem<ComponentDTO> treeItemRoot) {
        bomModel.getTreeTable().setRoot(treeItemRoot);
    }
}
