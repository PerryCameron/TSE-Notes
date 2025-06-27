package com.L2.mvci.changeset;

import com.L2.interfaces.Controller;
import javafx.scene.layout.Region;

public class ChangeController extends Controller<ChangeMessage> {

    private final ChangeView changeView;

    public ChangeController() {
        ChangeModel changeModel = new ChangeModel();
        ChangeInteractor changeInteractor = new ChangeInteractor(changeModel);
        changeView = new ChangeView(changeModel, this::action);

    }

    @Override
    public Region getView() {
        return null;
    }

    @Override
    public void action(ChangeMessage message) {

    }
}
