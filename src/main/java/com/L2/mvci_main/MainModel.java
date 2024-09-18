package com.L2.mvci_main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainModel {
    private StringProperty statusLabel = new SimpleStringProperty(""); // keeper here
    private ObjectProperty<TabPane> mainTabPane = new SimpleObjectProperty();
    private ObjectProperty<Tab> noteTab = new SimpleObjectProperty<>();

    public Tab getNoteTab() {
        return noteTab.get();
    }

    public ObjectProperty<Tab> noteTabProperty() {
        return noteTab;
    }

    public void setNoteTab(Tab noteTab) {
        this.noteTab.set(noteTab);
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


    public StringProperty statusLabelProperty() {
        return statusLabel;
    }

}
