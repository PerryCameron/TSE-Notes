package com.L2.mvci_main;

import com.L2.interfaces.Controller;
import com.L2.mvci_note.NoteController;
import com.L2.mvci_settings.SettingsController;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends Controller<MainMessage> {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    // sub-controllers
    private NoteController noteController = null;
    private SettingsController settingsController = null;


    public MainController() {
        MainModel mainModel = new MainModel();
        mainInteractor = new MainInteractor(mainModel);
        mainView = new MainView(mainModel, this::action);
    }

    @Override
    public Region getView() {
        return mainView.build();
    }

    @Override
    public void action(MainMessage action) {
        switch (action) {
            case OPEN_NEW_CASE -> openCaseTab();
            case OPEN_SETTINGS -> openSettingsTab();
        }
    }

    private void openCaseTab() {
            noteController = new NoteController(this);
            mainView.addNewTab("Note", noteController.getView(), false);
    }

    private void openSettingsTab() {
            settingsController = new SettingsController(this);
            mainView.addNewTab("Settings", settingsController.getView(), true);
    }

    public NoteController getCaseController() {
        return noteController;
    }

    public SettingsController getSettingsController() {
        return settingsController;
    }
}