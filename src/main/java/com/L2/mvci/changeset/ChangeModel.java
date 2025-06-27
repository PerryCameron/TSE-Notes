package com.L2.mvci.changeset;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class ChangeModel {
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DialogPane dialogPane = new DialogPane();



    public Alert getAlert() {
        return alert;
    }

    public DialogPane getDialogPane() {
        return dialogPane;
    }
}
