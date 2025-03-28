package com.L2.mvci_note;

import com.L2.dto.NoteDTO;
import com.L2.dto.UserDTO;
import com.L2.enums.AreaType;
import com.L2.interfaces.Controller;
import com.L2.mvci_main.MainController;
import com.L2.mvci_main.MainMessage;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoteController extends Controller<NoteMessage> {

    MainController mainController;
    NoteInteractor noteInteractor;
    NoteView noteView;
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);


    public NoteController(MainController mc) {
        this.mainController = mc;
        NoteModel noteModel = new NoteModel();
        this.noteInteractor = new NoteInteractor(noteModel);
        this.noteView = new NoteView(noteModel, this::action);
        logger.info("NoteController loaded");
    }

    @Override
    public Region getView() {
        noteInteractor.loadEntitlements();
        noteInteractor.loadNotes();
        noteInteractor.setActiveServiceContract();
        action(NoteMessage.LOAD_USER);
        return noteView.build();
    }

    @Override
    public void action(NoteMessage message) {
        switch (message) {
            case ADD_WORD_TO_DICT -> noteInteractor.appendToCustomDictionary();
            case LOAD_USER -> noteInteractor.loadUser();
            case UPDATE_STATUSBAR -> mainController.setStatusBar(noteInteractor.getStatus());
            case REPORT_NUMBER_OF_PART_ORDERS -> noteInteractor.reportNumberOfPartOrders();
            case COPY_PART_ORDER -> noteInteractor.copyPartOrder();
            case COPY_NAME_DATE -> noteInteractor.copyNameDate();
            case SHIPPING_INFORMATION -> noteInteractor.copyShippingInformation();
            case COMPUTE_HIGHLIGHTING_ISSUE_AREA -> computeHighlighting(AreaType.issue);
            case COMPUTE_HIGHLIGHTING_FINISH_AREA -> computeHighlighting(AreaType.finish);
            case COMPUTE_HIGHLIGHTING_SUBJECT_AREA -> computeHighlighting(AreaType.subject);
            case COPY_BASIC_INFORMATION -> noteInteractor.copyBasicInformation();
            case COPY_SUBJECT -> noteInteractor.copySubject();
            case COPY_CUSTOMER_REQUEST -> noteInteractor.copyCustomerRequest();
            case COPY_ISSUE -> noteInteractor.copyIssue();
            case COPY_ANSWER_TO_CUSTOMER -> noteInteractor.copyAnswerToCustomer();
            case SET_COMPLETE -> noteInteractor.setComplete();
            case NEW_NOTE -> noteInteractor.createNewNote();
            case LOG_CURRENT_ENTITLEMENT -> noteInteractor.logCurrentEntitlement();
            case SAVE_OR_UPDATE_NOTE -> noteInteractor.saveOrUpdateNote();
            case INSERT_PART_ORDER -> noteInteractor.insertPartOrder();
            case UPDATE_PART_ORDER -> noteInteractor.updatePartOrder();
            case DELETE_PART_ORDER -> noteInteractor.deleteSelectedPartOrder();
            case DELETE_PART -> noteInteractor.deletePart();
            case INSERT_PART -> noteInteractor.insertPart();
            case INITALIZE_DICTIONARY -> noteInteractor.initializeDictionary();
            case UPDATE_PART -> noteInteractor.updatePart();
            case REFRESH_PART_ORDERS -> noteInteractor.refreshPartOrders();
            case SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT -> mainController.action(MainMessage.SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT);
            case UPDATE_NOTE_TAB_NAME -> mainController.action(MainMessage.UPDATE_NOTE_TAB_NAME);
            case DELETE_NOTE -> deleteNote();
            case UPDATE_STATUSBAR_WITH_STRING ->  noteInteractor.setStatusLabelWithNoteInformation();
            case CLONE_NOTE -> noteInteractor.cloneNote();
            case SELECT_NOTE_TAB -> mainController.action(MainMessage.SELECT_NOTE_TAB);
            case REFRESH_NOTE_TABLEVIEW -> mainController.action(MainMessage.REFRESH_NOTE_TABLEVIEW);
            case SORT_NOTE_TABLEVIEW -> mainController.action(MainMessage.SORT_NOTE_TABLEVIEW);
            case REFRESH_ENTITLEMENT_COMBO_BOX -> noteInteractor.refreshEntitlementComboBox();
            case COPY_LOGGED_CALL -> noteInteractor.copyLoggedCall();
            case CHECK_BUTTON_ENABLE -> checkButtonEnable();
            case CLEAR_HIGHLIGHTS_ISSUE -> noteInteractor.clearHighlights(AreaType.issue);
        }
    }

    private void deleteNote() {
        noteInteractor.deleteNote();
        mainController.action(MainMessage.UPDATE_TABLE);
    }

    private void computeHighlighting(AreaType issue) {
        if (mainController.isSpellCheckedProperty().get()) noteInteractor.computeHighlighting(issue);
    }

    private void checkButtonEnable() {
        if(noteInteractor.checkButtonEnable() == NoteMessage.DISABLE_NEXT_BUTTON) {
            mainController.action(MainMessage.DISABLE_NEXT_BUTTON);
        } else {
            mainController.action(MainMessage.ENABLE_NEXT_BUTTON);
        }
    }

    public UserDTO getUser() { return noteInteractor.getUser(); }

    public ObservableList<NoteDTO> getNotes() {
        return noteInteractor.getNotes();
    }

    public ObjectProperty<NoteDTO> getBoundNoteProperty() {
        return noteInteractor.getBoundNoteProperty();
    }

    public IntegerProperty getPageSizeProperty() {
        return noteInteractor.getPageSizeProperty();
    }

    public IntegerProperty getOffsetProperty() {
        return noteInteractor.getOffsetProperty();
    }

    public NoteView getCaseView() {
        return noteView;
    }

    public void resetSpellCheckAreas() {
        // we have turned it on
        if(mainController.isSpellCheckedProperty().get()) {
            noteInteractor.initializeDictionary();
            noteInteractor.computeHighlighting(AreaType.subject);
            noteInteractor.computeHighlighting(AreaType.issue);
            noteInteractor.computeHighlighting(AreaType.finish);
        } else { // we have turned if off
            noteInteractor.clearHighlights(AreaType.subject);
            noteInteractor.clearHighlights(AreaType.issue);
            noteInteractor.clearHighlights(AreaType.finish);
            noteInteractor.closeHunspell();
        }
    }
}
