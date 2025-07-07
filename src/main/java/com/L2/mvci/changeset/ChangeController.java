package com.L2.mvci.changeset;

import com.L2.interfaces.AlertController;
import com.L2.mvci.main.MainController;
import javafx.scene.control.Alert;

public class ChangeController extends AlertController<ChangeMessage> {

    private final ChangeView changeView;
    private final ChangeInteractor changeInteractor;
    private final MainController mainController;

    public ChangeController(MainController mainController) {
        ChangeModel changeModel = new ChangeModel();
        changeInteractor = new ChangeInteractor(changeModel);
        changeView = new ChangeView(changeModel, this::action);
        this.mainController = mainController;
        setUser();
    }

    @Override
    public Alert getView() {
        return changeView.build();
    }

    public void setUser() {
        changeInteractor.setUser(mainController.getNoteController().getUser());
    }

    @Override
    public void action(ChangeMessage message) {
        switch (message) {
            case CREATE_CHANGESET -> changeInteractor.createChangeSet(mainController.getExecutorService());
        }

    }
}
