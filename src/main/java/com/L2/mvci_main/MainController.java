package com.L2.mvci_main;

import com.L2.interfaces.Controller;
import com.L2.mvci_note.NoteController;
import com.L2.mvci_note.NoteMessage;
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
            case OPEN_NOTES -> openNoteTab();
            case OPEN_SETTINGS -> openSettingsTab();
            case PREVIOUS_NOTE -> noteController.action(NoteMessage.PREVIOUS_NOTE);
            case NEXT_NOTE -> noteController.action(NoteMessage.NEXT_NOTE);
            case SAVE_NOTE -> noteController.action(NoteMessage.SAVE_NOTE);
            case SET_COMPLETE -> noteController.action(NoteMessage.SET_COMPLETE);
            case NEW_NOTE -> noteController.action(NoteMessage.NEW_NOTE);
            case TEST -> noteController.action(NoteMessage.TEST);
        }
    }

    public void setStatusBar(String status) {
        mainInteractor.setStatusBar(status);
    }

    private void openNoteTab() {
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