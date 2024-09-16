package com.L2.mvci_notelist;

import com.L2.interfaces.Controller;
import com.L2.mvci_main.*;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoteListController extends Controller<NoteListMessage> {

    private final NoteListInteractor noteListInteractor;
    private final NoteListView noteListView;
    private static final Logger logger = LoggerFactory.getLogger(NoteListController.class);
    private final MainController mainController;

    public NoteListController(MainController mc) {
        this.mainController = mc;
        NoteListModel noteListModel = new NoteListModel();
        noteListInteractor = new NoteListInteractor(noteListModel);
        noteListView = new NoteListView(noteListModel, this::action);
        action(NoteListMessage.REFERENCE_NOTES);
        action(NoteListMessage.REFERENCE_BOUND_NOTE_PROPERTY);
    }

    @Override
    public Region getView() {
        mainController.getNotes();
        return noteListView.build();
    }

    @Override
    public void action(NoteListMessage action) {
        switch (action) {
            case REFERENCE_NOTES: {
                noteListInteractor.setNotes(mainController.getNotes());
                break;
            }
            case REFERENCE_BOUND_NOTE_PROPERTY: {
                noteListInteractor.setBoundNote(mainController.getBoundNote());
                break;
            }
            case UPDATE_BOUND_NOTE: {
                noteListInteractor.updateBoundNote();
            }
        }
    }
}