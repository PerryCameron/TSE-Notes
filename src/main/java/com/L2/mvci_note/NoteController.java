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
    }

    @Override
    public Region getView() {
        noteInteractor.loadEntitlements();
        noteInteractor.setFakeTestData(); // this is temporary but will be where initial data comes from
        noteInteractor.setCurrentEntitlement();
        return noteView.build();
    }

    @Override
    public void action(NoteMessage message) {
        switch (message) {
            case STATUS_BAR_CHANGE -> changeStatusBar();
            case REPORT_NUMBER_OF_PART_ORDERS -> noteInteractor.reportNumberOfPartOrders();
            case COPY_PART_ORDER -> noteInteractor.copyPartOrder();
        };
    }

    private void changeStatusBar() {
        mainController.setStatusBar(noteInteractor.getStatus());
    }

    public NoteView getCaseView() {
        return noteView;
    }
}
