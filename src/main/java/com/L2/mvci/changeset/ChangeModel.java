package com.L2.mvci.changeset;

import com.L2.dto.UpdatedByDTO;
import com.L2.dto.UserDTO;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;

import java.util.ArrayList;
import java.util.List;

public class ChangeModel {
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DialogPane dialogPane = new DialogPane();
    private final BooleanProperty includeAll = new SimpleBooleanProperty(false);
    private final IntegerProperty numberOfDays = new SimpleIntegerProperty(0);
    private final ObjectProperty<UserDTO> user = new SimpleObjectProperty<>();
    private final ObjectProperty<ComboBox<Integer>> daysComboBox = new SimpleObjectProperty<>(new ComboBox<>());
    private final List<UpdatedByDTO> updatedBys = new ArrayList<>();


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

    public UserDTO getUser() {
        return user.get();
    }

    public ObjectProperty<UserDTO> userProperty() {
        return user;
    }

    public List<UpdatedByDTO> getUpdatedBys() {
        return updatedBys;
    }

    public ComboBox<Integer> getDaysComboBox() {
        return daysComboBox.get();
    }

    public ObjectProperty<ComboBox<Integer>> daysComboBoxProperty() {
        return daysComboBox;
    }
}
