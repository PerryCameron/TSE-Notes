package com.L2.mvci_settings;

import com.L2.interfaces.Controller;
import com.L2.mvci_main.MainController;
import javafx.scene.layout.Region;

public class SettingsController extends Controller<SettingsMessage> {

    MainController mainController;
    SettingsInteractor settingsInteractor;
    SettingsView settingsView;

    public SettingsController(MainController mc) {
        this.mainController = mc;
        SettingsModel settingsModel = new SettingsModel();
        this.settingsInteractor = new SettingsInteractor(settingsModel);
        this.settingsView = new SettingsView(settingsModel, this::action);
        settingsInteractor.setComplete(); // this is temporary to make fake data
    }

    @Override
    public Region getView() {
        return settingsView.build();
    }

    @Override
    public void action(SettingsMessage message) {
        switch (message) {
//            case OPEN -> mainController.openTab(welcomeInteractor.getTab());

        };
    }
}
