package com.L2.mvci.main;

import javafx.beans.property.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainModel {
    private StringProperty statusLabelStringProperty = new SimpleStringProperty(""); // keeper here
    private StringProperty dataBaseLocation = new SimpleStringProperty("");
    private ObjectProperty<TabPane> mainTabPane = new SimpleObjectProperty<>();
    private ObjectProperty<Tab> noteTab = new SimpleObjectProperty<>();
    private final BooleanProperty nextButtonDisabled = new SimpleBooleanProperty(true);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    // settings below here
    private BooleanProperty spellCheck = new SimpleBooleanProperty(true);


    public BooleanProperty nextButtonDisabledProperty() {
        return nextButtonDisabled;
    }
    public StringProperty dataBaseLocationProperty() {
        return dataBaseLocation;
    }
    public ObjectProperty<Tab> noteTabProperty() {
        return noteTab;
    }
    public ObjectProperty<TabPane> mainTabPaneProperty() {
        return mainTabPane;
    }
    public StringProperty statusStringProperty() {
        return statusLabelStringProperty;
    }
    public BooleanProperty spellCheckProperty() { return spellCheck; }
    public ExecutorService getExecutor() {
        return executor;
    }
}
