package com.L2.mvci_case;

import com.L2.interfaces.Controller;
import com.L2.mvci_main.MainController;
import javafx.scene.layout.Region;

public class CaseController extends Controller {

    MainController mainController;
    CaseInteractor caseInteractor;
    CaseView caseView;

    public CaseController(MainController mc) {
        this.mainController = mc;
        CaseModel caseModel = new CaseModel();
        this.caseInteractor = new CaseInteractor(caseModel);
        this.caseView = new CaseView(caseModel, this::action);
//        CaseInteractor.setComplete();
    }

    @Override
    public Region getView() {
        return null;
    }

    @Override
    public void action(Enum actionEnum) {

    }
}
