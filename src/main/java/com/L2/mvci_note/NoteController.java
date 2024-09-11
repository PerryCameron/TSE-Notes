package com.L2.mvci_note;

import com.L2.interfaces.Controller;
import com.L2.mvci_main.MainController;
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
        loadTestData(); // this is for testing
    }

    private void loadTestData() {
        noteInteractor.setFakeTestData(); // this is temporary but will be where initial data comes from
    }

    @Override
    public Region getView() {
        noteInteractor.loadEntitlements();
        noteInteractor.setCurrentEntitlement();
        return noteView.build();
    }

    @Override
    public void action(NoteMessage message) {
        switch (message) {
            case LOAD_USER -> noteInteractor.loadUser();
            case STATUS_BAR_CHANGE -> changeStatusBar();
            case REPORT_NUMBER_OF_PART_ORDERS -> noteInteractor.reportNumberOfPartOrders();
            case COPY_PART_ORDER -> noteInteractor.copyPartOrder();
            case LOG_ORDER_NUMBER_CHANGE -> noteInteractor.logPartOrderNumberChange();
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
        };
    }

    private void changeStatusBar() {
        mainController.setStatusBar(noteInteractor.getStatus());
    }

    public NoteView getCaseView() {
        return noteView;
    }
}
