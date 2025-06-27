package com.L2.mvci.changeset;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class ChangeModel {
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DialogPane dialogPane = new DialogPane();
    private final BooleanProperty includeAll = new SimpleBooleanProperty(false);
    private final IntegerProperty numberOfDays = new SimpleIntegerProperty(0);



    public Alert getAlert() {
        return alert;
    }

    public DialogPane getDialogPane() {
        return dialogPane;
    }

    public boolean isIncludeAll() {
        return includeAll.get();
    }

    public BooleanProperty includeAllProperty() {
        return includeAll;
    }

    public int getNumberOfDays() {
        return numberOfDays.get();
    }

    public IntegerProperty numberOfDaysProperty() {
        return numberOfDays;
    }
}
