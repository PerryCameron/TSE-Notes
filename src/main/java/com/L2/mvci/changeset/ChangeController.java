package com.L2.mvci.changeset;

import com.L2.interfaces.AlertController;
import com.L2.mvci.main.MainController;
import javafx.scene.control.Alert;

public class ChangeController extends AlertController<ChangeMessage> {

    private final ChangeView changeView;
    private final ChangeInteractor changeInteractor;

    public ChangeController(MainController mainController) {
        ChangeModel changeModel = new ChangeModel();
        changeInteractor = new ChangeInteractor(changeModel);
        changeView = new ChangeView(changeModel, this::action);
    }

    @Override
    public Alert getView() {
        return changeView.build();
    }

    @Override
    public void action(ChangeMessage message) {
        switch (message) {
            case CREATE_CHANGESET -> changeInteractor.createChangeSet();
        }

    }
}
