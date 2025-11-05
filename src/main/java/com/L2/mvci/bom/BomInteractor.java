package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import com.L2.static_tools.bom.BOMExploderClient;
import com.L2.static_tools.bom.XMLChomper;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BomInteractor {
    private static final Logger logger = LoggerFactory.getLogger(BomInteractor.class);
    private final BomModel bomModel;

    public BomInteractor(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    public void searchForComponentBom() {
        try {
            // search for component typed in text field
            String output = BOMExploderClient.getBOMExplosionAsString(bomModel.searchComponentProperty().get(), "BIL", "");
            // create a XML component, with nested Lists
            ComponentXML xmlRoot = XMLChomper.parseBomXml(output);
            // create the root tree Item from the XML Component object
            TreeItem<ComponentDTO> treeItemRoot = XMLChomper.buildTree(xmlRoot);
            // set expanded
            treeItemRoot.setExpanded(true);
            // set root to tree
            // TODO make this go on another thread and set this on success
            bomModel.getTreeTable().setRoot(treeItemRoot);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
