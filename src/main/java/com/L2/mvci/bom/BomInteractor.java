package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import com.L2.static_tools.ApplicationPaths;
import com.L2.static_tools.bom.BOMExploderClient;
import com.L2.static_tools.bom.XMLChomper;
import com.L2.widgetFx.DialogueFx;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BomInteractor {
    private static final Logger logger = LoggerFactory.getLogger(BomInteractor.class);
    private final BomModel bomModel;

    public BomInteractor(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    public void setRoot(TreeItem<ComponentDTO> treeItemRoot) {
        bomModel.getTreeTable().setRoot(treeItemRoot);
    }

    public TreeItem<ComponentDTO> buildTreeRoot(boolean firstStart) {
        logger.info("Searching bom for {}", bomModel.searchComponentProperty().get());
        String output;
        try {
            if (firstStart) {
                output = XMLChomper.readXMLFromFile(ApplicationPaths.secondaryDbDirectory.resolve("bom.XML"));
                if (output.isEmpty()) return null;
            } else {
                // search for component typed in text field
                output = BOMExploderClient.getBOMExplosionAsString(bomModel.searchComponentProperty().get(), "BIL", "");
                // save XML in text file for persistence
                XMLChomper.saveToBomXml(output, ApplicationPaths.secondaryDbDirectory.resolve("bom.XML"));
            }
            // create an XML component, with nested Lists
            ComponentXML xmlRoot = XMLChomper.parseBomXml(output);
            // create the root tree Item from the XML Component object
            TreeItem<ComponentDTO> treeItemRoot = XMLChomper.buildTree(xmlRoot);
            // Get Statistics
            Integer[] levels = XMLChomper.getStats(treeItemRoot);
            // this changes the UI after successfully putting ip into a treeView
            Platform.runLater(() -> bomModel.setLevels(levels));
            // set expanded
            treeItemRoot.setExpanded(true);
            return treeItemRoot;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> DialogueFx.errorAlert("Unable to get BOM", e.getMessage()));
        }
        return null;
    }
}
