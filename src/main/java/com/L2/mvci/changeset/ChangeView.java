package com.L2.mvci.changeset;

import javafx.scene.control.Alert;
import javafx.util.Builder;

import java.util.function.Consumer;

public class ChangeView implements Builder<Alert> {

    private final ChangeModel changeModel;
    Consumer<ChangeMessage> action;


    public ChangeView(ChangeModel changeModel, Consumer<ChangeMessage> action) {
        this.changeModel = changeModel;
    }

    @Override
    public Alert build() {
        return null;
    }
}
