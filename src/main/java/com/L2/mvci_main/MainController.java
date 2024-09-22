package com.L2.mvci_main;

import com.L2.dto.NoteDTO;
import com.L2.interfaces.Controller;
import com.L2.mvci_note.NoteController;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_notelist.NoteListController;
import com.L2.mvci_notelist.NoteListMessage;
import com.L2.mvci_settings.SettingsController;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends Controller<MainMessage> {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    private final MainModel mainModel;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    // sub-controllers
    private NoteController noteController = null;
    private SettingsController settingsController = null;
    private NoteListController noteListController = null;

    public MainController() {
        mainModel = new MainModel();
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
            case OPEN_NOTES_LIST -> openNoteListTab();
            case PREVIOUS_NOTE -> noteController.action(NoteMessage.PREVIOUS_NOTE);
            case NEXT_NOTE -> noteController.action(NoteMessage.NEXT_NOTE);
            case SAVE_OR_UPDATE_NOTE -> noteController.action(NoteMessage.SAVE_OR_UPDATE_NOTE);
            case SET_COMPLETE -> noteController.action(NoteMessage.SET_COMPLETE);
            case NEW_NOTE -> noteController.action(NoteMessage.NEW_NOTE);
            case TEST -> noteController.action(NoteMessage.TEST); // TODO delete
            case SELECT_BOUND_NOTE_IN_TABLE -> noteController.action(NoteMessage.SELECT_BOUND_NOTE_IN_TABLE);
            case REFRESH_NOTE_TABLEVIEW -> noteListController.action(NoteListMessage.REFRESH_NOTE_TABLEVIEW);
            case UPDATE_NOTE_TAB_NAME -> mainInteractor.updateNoteTabName(getBoundNote());
            case DELETE_NOTE -> noteController.action(NoteMessage.DELETE_NOTE);
        }
    }

    public ObservableList<NoteDTO> getNotes() {
        return noteController.getNotes();
    }

    public ObjectProperty<NoteDTO> getBoundNote() {
        return noteController.getBoundNote();
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

    private void openNoteListTab() {
        noteListController = new NoteListController(this);
        mainView.addNewTab("Manage Notes", noteListController.getView(), true);
    }

    public NoteController getNoteController() {
        return noteController;
    }

    public NoteListController getNoteListController() {
        return noteListController;
    }

    public SettingsController getSettingsController() {
        return settingsController;
    }

    public MainModel getMainModel() {
        return mainModel;
    }
}