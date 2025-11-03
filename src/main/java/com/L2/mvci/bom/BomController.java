package com.L2.mvci.bom;

import com.L2.interfaces.Controller;
import com.L2.mvci.main.MainController;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BomController extends Controller<BomMessage> {
    private MainController mainController;
    private BomInteractor bomInteractor;
    private BomView bomView;
    private static final Logger logger = LoggerFactory.getLogger(BomController.class);

    public BomController(MainController mainController) {
        this.mainController = mainController;
        BomModel bomModel = new BomModel();
        bomInteractor = new BomInteractor(bomModel);
        this.bomView = new BomView(bomModel, this::action);
    }

    @Override
    public Region getView() {
        return null;
    }

    @Override
    public void action(BomMessage actionEnum) {
        switch (actionEnum) {

        }
    }
}
