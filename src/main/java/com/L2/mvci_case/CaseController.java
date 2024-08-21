package com.L2.mvci_case;

import com.L2.interfaces.Controller;
import com.L2.mvci_main.MainController;
import javafx.scene.layout.Region;

public class CaseController extends Controller<CaseMessage> {

    MainController mainController;
    CaseInteractor caseInteractor;
    CaseView caseView;

    public CaseController(MainController mc) {
        this.mainController = mc;
        CaseModel caseModel = new CaseModel();
        this.caseInteractor = new CaseInteractor(caseModel);
        this.caseView = new CaseView(caseModel, this::action);
    }

    @Override
    public Region getView() {
        caseInteractor.loadEntitlements();
        caseInteractor.setFakeTestData(); // this is temporary but will be where initial data comes from
        caseInteractor.setCurrentEntitlement();
        return caseView.build();
    }

    @Override
    public void action(CaseMessage message) {
        switch (message) {
//            case OPEN -> mainController.openTab(welcomeInteractor.getTab());

        };
    }

    public CaseView getCaseView() {
        return caseView;
    }
}
