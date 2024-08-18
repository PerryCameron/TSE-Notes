package com.L2.mvci_main;

import com.L2.interfaces.Controller;
import com.L2.mvci_case.CaseController;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends Controller<MainMessage> {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


    public MainController() {
        MainModel mainModel = new MainModel();
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
            case OPEN_NEW_CASE -> openCaseTab("Case");
        }
    }

    public void openTab(String tabName) {
        switch (tabName) {
            case "Case" -> System.out.println("Displaying people list");
            case "Notes" -> System.out.println("Displaying notes");
            case "Jotform" -> System.out.println("Opening Jotform");
            default -> System.out.println("Invalid input");
        }
    }

    private void openCaseTab(String tabName) {
            mainView.addNewTab(tabName, new CaseController(this).getView());
    }


}