package com.L2.mvci.notelist;

import com.L2.interfaces.Controller;
import com.L2.mvci.main.MainController;
import com.L2.mvci.main.MainMessage;
import com.L2.mvci.note.NoteMessage;
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
        action(NoteListMessage.REFERENCE_PAGE_SIZE);
        action(NoteListMessage.REFERENCE_OFFSET);
    }

    @Override
    public Region getView() {
        mainController.getNotes();
        return noteListView.build();
    }

    @Override
    public void action(NoteListMessage action) {
        switch (action) {
            case REFERENCE_NOTES -> noteListInteractor.setNotes(mainController.getNotes());
            case REFERENCE_BOUND_NOTE_PROPERTY -> noteListInteractor.setBoundNoteProperty(mainController.getBoundNoteProperty());
            case REFERENCE_OFFSET -> noteListInteractor.setOffsetProperty(mainController.getOffsetProperty());
            case REFERENCE_PAGE_SIZE -> noteListInteractor.setPageSizeProperty(mainController.getPageSizeProperty());
            case UPDATE_BOUND_NOTE -> noteListInteractor.updateBoundNote();
            case SAVE_OR_UPDATE_NOTE -> mainController.getNoteController().action(NoteMessage.SAVE_OR_UPDATE_NOTE);
            case SORT_NOTE_TABLEVIEW -> noteListInteractor.sortTableView();
            case SELECT_NOTE_TAB -> mainController.action(MainMessage.SELECT_NOTE_TAB);
            case REFRESH_NOTE_TABLEVIEW -> noteListInteractor.refreshTableView();
            case CHECK_BUTTON_ENABLE -> mainController.action(MainMessage.CHECK_BUTTON_ENABLE);
            case NEXT_NOTE -> noteListInteractor.displayNextNote();
            case PREVIOUS_NOTE -> noteListInteractor.displayPreviousNote();
            case SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT -> noteListInteractor.selectNote();
            case ADD_TO_BOTTOM_OF_LIST -> noteListInteractor.addToBottomOfList(mainController.getExecutorService());
            case ADD_TO_TOP_OF_LIST -> noteListInteractor.addToTopOfList(mainController.getExecutorService());
            case SEARCH -> noteListInteractor.searchParameters(mainController.getExecutorService());
            case UPDATE_RANGE_LABEL -> noteListInteractor.updateRange();
            case UPDATE_TABLE -> noteListInteractor.updateTable();
            case NO_ACTION_TAKEN_FOR_SCROLL -> noteListInteractor.logNoActionForScroll();
            case NO_ACTION_TAKEN_FOR_SEARCH -> noteListInteractor.logNoActionForSearch();
            case NO_ACTION_TAKEN_FOR_KEY_PRESS -> noteListInteractor.logNoActionForKeyPress();
        }
    }
}