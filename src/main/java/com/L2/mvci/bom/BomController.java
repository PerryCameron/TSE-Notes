package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import com.L2.interfaces.Controller;
import com.L2.mvci.main.MainController;
import com.L2.static_tools.ApplicationPaths;
import com.L2.static_tools.bom.BOMExploderClient;
import com.L2.static_tools.bom.XMLChomper;
import com.L2.widgetFx.DialogueFx;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BomController extends Controller<BomMessage> {
    private final MainController mainController;
    private final BomInteractor bomInteractor;
    private final BomView bomView;
    private static final Logger logger = LoggerFactory.getLogger(BomController.class);

    public BomController(MainController mainController) {
        this.mainController = mainController;
        BomModel bomModel = new BomModel();
        bomInteractor = new BomInteractor(bomModel);
        this.bomView = new BomView(bomModel, this::action);
    }

    @Override
    public Region getView() {
        return bomView.build();
    }

    @Override
    public void action(BomMessage actionEnum) {
        switch (actionEnum) {
            case SEARCH -> getBom(false);
            default -> DialogueFx.errorAlert("Unable to perform BOM", "This will probably never pop up");
        }
    }

    private void getBom(boolean fisrtStart) {
        mainController.setSpinnerOffset(50, 50);
        mainController.showLoadingSpinner(true);
        Task<TreeItem<ComponentDTO>> addToBottomTask = new Task<>() {
            @Override
            protected TreeItem<ComponentDTO> call() {
                try {
                    bomInteractor.logBomCall();
                    // search for component typed in text field
                    String output = BOMExploderClient.getBOMExplosionAsString(bomInteractor.getTest(), "BIL", "");
                    // save XML in text file for persistence
                    XMLChomper.saveToBomXml(output, ApplicationPaths.secondaryDbDirectory.resolve("bom.XML"));
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
            bomInteractor.setRoot(treeItemRoot);
            mainController.showLoadingSpinner(false);
        });
        mainController.getExecutorService().submit(addToBottomTask);
    }
}
