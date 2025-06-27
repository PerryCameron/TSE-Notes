package com.L2.mvci.changeset;

import com.L2.interfaces.AlertController;
import com.L2.mvci.main.MainController;
import javafx.scene.control.Alert;

public class ChangeController extends AlertController<ChangeMessage> {

    private final ChangeView changeView;

    public ChangeController(MainController mainController) {
        ChangeModel changeModel = new ChangeModel();
        ChangeInteractor changeInteractor = new ChangeInteractor(changeModel);
        changeView = new ChangeView(changeModel, this::action);
    }

    @Override
    public Alert getView() {
        return changeView.build();
    }

    @Override
    public void action(ChangeMessage message) {

    }
}
