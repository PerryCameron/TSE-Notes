package com.L2.mvci_settings;

import com.L2.interfaces.Controller;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_main.MainController;
import com.L2.mvci_settings.menus.EntitlementsMenu;
import com.L2.mvci_settings.menus.UserMenu;
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
        referenceExternalModels();
    }

    private void referenceExternalModels() {
        NoteModel noteModel = mainController.getCaseController().getCaseView().getNoteModel();
        settingsInteractor.referenceExternalModels(noteModel);
    }

    @Override
    public Region getView() {
        return settingsView.build();
    }

    @Override
    public void action(SettingsMessage message) {
        switch (message) {
            case SAVE_ENTITLEMENTS -> settingsInteractor.saveEntitlement();
            case PRINT_ENTITLEMENTS -> settingsInteractor.printEntitlements();
            case SHOW_USER -> settingsInteractor.changeMenu(new UserMenu(settingsView).build());
            case SHOW_ENTITLEMENTS -> settingsInteractor.changeMenu(new EntitlementsMenu(settingsView).build());
            case NEW_ENTITLEMENT -> settingsInteractor.createNewEntitlement();
            case DELETE_ENTITLEMENT -> settingsInteractor.deleteEntitlement();
        };
    }
}
