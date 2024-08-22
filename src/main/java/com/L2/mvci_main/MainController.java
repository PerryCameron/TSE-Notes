package com.L2.mvci_main;

import com.L2.interfaces.Controller;
import com.L2.mvci_case.CaseController;
import com.L2.mvci_settings.SettingsController;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends Controller<MainMessage> {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    // sub-controllers
    private CaseController caseController = null;
    private SettingsController settingsController = null;


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
            case OPEN_NEW_CASE -> openCaseTab();
            case OPEN_SETTINGS -> openSettingsTab();
        }
    }

    private void openCaseTab() {
            caseController = new CaseController(this);
            mainView.addNewTab("Note", caseController.getView(), false);
    }

    private void openSettingsTab() {
            settingsController = new SettingsController(this);
            mainView.addNewTab("Settings", settingsController.getView(), true);
    }

    public CaseController getCaseController() {
        return caseController;
    }

    public SettingsController getSettingsController() {
        return settingsController;
    }
}