package com.L2.mvci.changeset;

import com.L2.widgetFx.DialogueFx;
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

        return DialogueFx.errorAlert("This is a test", "because it is easy");
    }
}
