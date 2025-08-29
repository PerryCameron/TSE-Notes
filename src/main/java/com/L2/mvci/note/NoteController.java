package com.L2.mvci.note;

import com.L2.dto.NoteFx;
import com.L2.dto.UserDTO;
import com.L2.dto.global_spares.RangesFx;
import com.L2.enums.AreaType;
import com.L2.interfaces.Controller;
import com.L2.mvci.main.MainController;
import com.L2.mvci.main.MainMessage;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoteController extends Controller<NoteMessage> {

    private MainController mainController;
    private NoteInteractor noteInteractor;
    private NoteView noteView;
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);


    public NoteController(MainController mc) {
        this.mainController = mc;
        NoteModel noteModel = new NoteModel();
        this.noteInteractor = new NoteInteractor(noteModel);
        this.noteView = new NoteView(noteModel, this::action);
    }

    @Override
    public Region getView() {
        noteInteractor.loadEntitlements();
        noteInteractor.loadNotes();
        noteInteractor.setActiveServiceContract();
        action(NoteMessage.LOAD_USER);
        action(NoteMessage.GET_RANGES);
        action(NoteMessage.ADD_MAIN_CONTROLLER_REFERENCE_TO_MODEL);
        return noteView.build();
    }

    @Override
    public void action(NoteMessage message) {
        switch (message) {
            case ADD_MAIN_CONTROLLER_REFERENCE_TO_MODEL -> setMainController();
            case ADD_WORD_TO_DICT -> noteInteractor.appendToCustomDictionary(mainController.getExecutorService());
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
            case COPY_LOGGED_CALL -> noteInteractor.copyLoggedCall();
            case CHECK_BUTTON_ENABLE -> checkButtonEnable();
            case CLEAR_HIGHLIGHTS_ISSUE -> noteInteractor.clearHighlights(AreaType.issue);
            case CLONE_NOTE -> noteInteractor.cloneNote();
            case DELETE_NOTE -> deleteNote();
            case DELETE_PART_ORDER -> noteInteractor.deleteSelectedPartOrder();
            case DELETE_PART -> noteInteractor.deletePart();
            case GET_RANGES -> noteInteractor.getRanges();
            case INSERT_PART_ORDER -> noteInteractor.insertPartOrder();
            case INSERT_PART -> noteInteractor.insertPart();
            case INITALIZE_DICTIONARY -> noteInteractor.initializeDictionary();
            case LOG_CURRENT_ENTITLEMENT -> noteInteractor.logCurrentEntitlement();
            case NEW_NOTE -> noteInteractor.createNewNote();
            case REFRESH_NOTE_TABLEVIEW -> mainController.action(MainMessage.REFRESH_NOTE_TABLEVIEW);
            case REFRESH_ENTITLEMENT_COMBO_BOX -> noteInteractor.refreshEntitlementComboBox();
            case REFRESH_PART_ORDERS -> noteInteractor.refreshPartOrders();
            case SEARCH_PARTS -> noteInteractor.searchParts();
            case SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT -> mainController.action(MainMessage.SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT);
            case SORT_NOTE_TABLEVIEW -> mainController.action(MainMessage.SORT_NOTE_TABLEVIEW);
            case SAVE_OR_UPDATE_NOTE -> noteInteractor.saveOrUpdateNote();
            case SET_COMPLETE -> noteInteractor.setComplete();
            case SELECT_NOTE_TAB -> mainController.action(MainMessage.SELECT_NOTE_TAB);
            case TRIM_ISSUE -> noteInteractor.trimIssue();
            case TRIM_ADDITIONAL -> noteInteractor.trimAdditional();
            case UPDATE_STATUSBAR_WITH_STRING ->  noteInteractor.setStatusLabelWithNoteInformation();
            case UPDATE_NOTE_TAB_NAME -> mainController.action(MainMessage.UPDATE_NOTE_TAB_NAME);
            case UPDATE_PART_ORDER -> noteInteractor.updatePartOrder();
            case UPDATE_PART -> noteInteractor.updatePart();
            case UPDATE_RANGE_COUNT -> noteInteractor.updateRangeCount();
        }
    }

    private void deleteNote() {
        noteInteractor.deleteNote();
        mainController.action(MainMessage.UPDATE_TABLE);
    }

    private void computeHighlighting(AreaType issue) {
        if (mainController.isSpellCheckedProperty().get()) noteInteractor.computeHighlighting(issue, mainController.getExecutorService());
    }

    private void checkButtonEnable() {
        if(noteInteractor.checkButtonEnable() == NoteMessage.DISABLE_NEXT_BUTTON) {
            mainController.action(MainMessage.DISABLE_NEXT_BUTTON);
        } else {
            mainController.action(MainMessage.ENABLE_NEXT_BUTTON);
        }
    }

    private void setMainController() {
        noteInteractor.setMainController(mainController);
    }

    public UserDTO getUser() { return noteInteractor.getUser(); }

    public ObservableList<NoteFx> getNotes() {
        return noteInteractor.getNotes();
    }

    public ObjectProperty<NoteFx> getBoundNoteProperty() {
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
            noteInteractor.computeHighlighting(AreaType.subject, mainController.getExecutorService());
            noteInteractor.computeHighlighting(AreaType.issue, mainController.getExecutorService());
            noteInteractor.computeHighlighting(AreaType.finish, mainController.getExecutorService());
        } else { // we have turned if off
            noteInteractor.clearHighlights(AreaType.subject);
            noteInteractor.clearHighlights(AreaType.issue);
            noteInteractor.clearHighlights(AreaType.finish);
            noteInteractor.closeHunspell();
        }
    }

    public ObservableList<RangesFx> getRanges() {
        return noteInteractor.getRangesList();
    }

    public void printPartsTableView() {
        noteInteractor.printPartsTableView();
    }

    // sending signal to part mvci
    public void printProductFamilies() {
        noteInteractor.printProductFamilies();
    }
}
