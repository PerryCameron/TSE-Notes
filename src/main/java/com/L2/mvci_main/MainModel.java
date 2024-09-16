package com.L2.mvci_main;

import com.L2.dto.NoteDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;

public class MainModel {
    private StringProperty statusLabel = new SimpleStringProperty(""); // keeper here
    private ObjectProperty<TabPane> mainTabPane = new SimpleObjectProperty();
    private ObservableList<NoteDTO> notes = FXCollections.observableArrayList();


    public ObservableList<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(ObservableList<NoteDTO> notes) {
        this.notes = notes;
    }

    public TabPane getMainTabPane() {
        return mainTabPane.get();
    }

    public ObjectProperty<TabPane> mainTabPaneProperty() {
        return mainTabPane;
    }

    public void setMainTabPane(TabPane mainTabPane) {
        this.mainTabPane.set(mainTabPane);
    }

    public String getStatusLabel() {
        return statusLabel.get();
    }

    public StringProperty statusLabelProperty() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel.set(statusLabel);
    }
}
