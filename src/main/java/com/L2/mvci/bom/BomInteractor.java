package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import com.L2.static_tools.bom.BOMExploderClient;
import com.L2.static_tools.bom.XMLChomper;
import com.L2.widgetFx.DialogueFx;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class BomInteractor {
    private static final Logger logger = LoggerFactory.getLogger(BomInteractor.class);
    private final BomModel bomModel;

    public BomInteractor(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    public void searchForComponentBom(ExecutorService executorService) {

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
