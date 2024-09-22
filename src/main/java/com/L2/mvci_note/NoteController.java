package com.L2.mvci_note;

import com.L2.dto.NoteDTO;
import com.L2.interfaces.Controller;
import com.L2.mvci_main.MainController;
import com.L2.mvci_main.MainMessage;
import com.L2.mvci_notelist.NoteListMessage;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;

public class NoteController extends Controller<NoteMessage> {

    MainController mainController;
    NoteInteractor noteInteractor;
    NoteView noteView;

    public NoteController(MainController mc) {
        this.mainController = mc;
        NoteModel noteModel = new NoteModel();
        this.noteInteractor = new NoteInteractor(noteModel);
        this.noteView = new NoteView(noteModel, this::action);
        action(NoteMessage.LOAD_USER);
    }

    @Override
    public Region getView() {
        noteInteractor.loadEntitlements();
        noteInteractor.loadNotes();
        noteInteractor.setActiveServiceContract();
        return noteView.build();
    }

    @Override
    public void action(NoteMessage message) {
        switch (message) {
            case LOAD_USER -> noteInteractor.loadUser();
            case UPDATE_STATUSBAR -> changeStatusBar();
            case REPORT_NUMBER_OF_PART_ORDERS -> noteInteractor.reportNumberOfPartOrders();
            case COPY_PART_ORDER -> noteInteractor.copyPartOrder();
            case COPY_NAME_DATE -> noteInteractor.copyNameDate();
            case SHIPPING_INFORMATION -> noteInteractor.copyShippingInformation();
            case COPY_BASIC_INFORMATION -> noteInteractor.copyBasicInformation();
            case COPY_CUSTOMER_REQUEST -> noteInteractor.copyCustomerRequest();
            case COPY_ISSUE -> noteInteractor.copyIssue();
            case COPY_CORRECTIVE_ACTION -> noteInteractor.copyCorrectiveAction();
            case SET_COMPLETE -> noteInteractor.setComplete();
            case NEW_NOTE -> noteInteractor.createNewNote();
            case LOG_CURRENT_ENTITLEMENT -> noteInteractor.logCurrentEntitlement();
            case NEXT_NOTE -> noteInteractor.displayNextNote();
            case PREVIOUS_NOTE -> noteInteractor.displayPreviousNote();
            case SAVE_OR_UPDATE_NOTE -> noteInteractor.saveOrUpdateNote();
            case INSERT_PART_ORDER -> noteInteractor.insertPartOrder();
            case UPDATE_PART_ORDER -> noteInteractor.updatePartOrder();
            case DELETE_PART_ORDER -> noteInteractor.deleteSelectedPartOrder();
            case DELETE_PART -> noteInteractor.deletePart();
            case INSERT_PART -> noteInteractor.insertPart();
            case UPDATE_PART -> noteInteractor.updatePart();
            case TEST -> noteInteractor.test();
            case REFRESH_PART_ORDERS -> noteInteractor.refreshPartOrders();
            case SELECT_BOUND_NOTE_IN_TABLE -> mainController.getNoteListController().action(NoteListMessage.SELECT_BOUND_NOTE_IN_TABLE);
            case UPDATE_NOTE_TAB_NAME -> mainController.action(MainMessage.UPDATE_NOTE_TAB_NAME);
            case DELETE_NOTE -> noteInteractor.deleteNote();
        };
    }

    public ObservableList<NoteDTO> getNotes() {
        return noteInteractor.getNotes();
    }

    public ObjectProperty<NoteDTO> getBoundNote() {
        return noteInteractor.getBoundNoteProperty();
    }

    private void changeStatusBar() {
        mainController.setStatusBar(noteInteractor.getStatus());
        System.out.println("Setting Status Bar: " + noteInteractor.getStatus());
    }

    public NoteView getCaseView() {
        return noteView;
    }
}
