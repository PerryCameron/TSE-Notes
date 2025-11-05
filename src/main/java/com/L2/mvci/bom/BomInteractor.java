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
        Task<TreeItem<ComponentDTO>> addToBottomTask = new Task<>() {
            @Override
            protected TreeItem<ComponentDTO> call() {
                try {
                    logger.info("Searching bom for {}", bomModel.searchComponentProperty().get());
                    // search for component typed in text field
                    String output = BOMExploderClient.getBOMExplosionAsString(bomModel.searchComponentProperty().get(), "BIL", "");
                    // create an XML component, with nested Lists
                    ComponentXML xmlRoot = XMLChomper.parseBomXml(output);
                    // create the root tree Item from the XML Component object
                    TreeItem<ComponentDTO> treeItemRoot = XMLChomper.buildTree(xmlRoot);
                    // set expanded
                    treeItemRoot.setExpanded(true);
                    // set root to tree
                    return treeItemRoot;
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    Platform.runLater(() -> DialogueFx.errorAlert("Unable to get BOM", e.getMessage()));
                }
                return null;
            }
        };
        addToBottomTask.setOnSucceeded(event -> {
            TreeItem<ComponentDTO> treeItemRoot = addToBottomTask.getValue();
            bomModel.getTreeTable().setRoot(treeItemRoot);
        });
        executorService.submit(addToBottomTask);
    }
}
