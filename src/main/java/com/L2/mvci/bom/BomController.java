package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.interfaces.Controller;
import com.L2.mvci.main.MainController;
import com.L2.widgetFx.DialogueFx;
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
        action(BomMessage.LOAD_BOM_FROM_XML);
    }

    @Override
    public Region getView() {
        return bomView.build();
    }

    @Override
    public void action(BomMessage actionEnum) {
        switch (actionEnum) {
            case SEARCH -> getBom(false);
            case LOAD_BOM_FROM_XML -> getBom(true);
            default -> DialogueFx.errorAlert("Unable to perform BOM", "This will probably never pop up");
        }
    }

    private void getBom(boolean firstStart) {
        mainController.setSpinnerOffset(50, 50);
        mainController.showLoadingSpinner(true);
        Task<TreeItem<ComponentDTO>> addToBottomTask = new Task<>() {
            @Override
            protected TreeItem<ComponentDTO> call() {
                    return bomInteractor.buildTreeRoot(firstStart);
            }
        };
        addToBottomTask.setOnSucceeded(event -> {
            TreeItem<ComponentDTO> treeItemRoot = addToBottomTask.getValue();
            // we have never used it before so we do not have a bom.xml file
            if(treeItemRoot == null) return;
            bomInteractor.setRoot(treeItemRoot);
            mainController.showLoadingSpinner(false);
        });
        mainController.getExecutorService().submit(addToBottomTask);
    }
}
