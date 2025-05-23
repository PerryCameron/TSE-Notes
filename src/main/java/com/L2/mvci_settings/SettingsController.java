package com.L2.mvci_settings;

import com.L2.interfaces.Controller;
import com.L2.mvci_main.MainMessage;
import com.L2.mvci_note.NoteController;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_main.MainController;
import com.L2.mvci_settings.components.DictionaryMenu;
import com.L2.mvci_settings.components.EntitlementsMenu;
import com.L2.mvci_settings.components.GlobalSparesMenu;
import com.L2.mvci_settings.components.UserMenu;
import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsController extends Controller<SettingsMessage> {

    MainController mainController;
    SettingsInteractor settingsInteractor;
    SettingsView settingsView;
    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    public SettingsController(MainController mc) {
        this.mainController = mc;
        SettingsModel settingsModel = new SettingsModel();
        this.settingsInteractor = new SettingsInteractor(settingsModel);
        this.settingsView = new SettingsView(settingsModel, this::action);
        settingsInteractor.referenceSpellCheckProperty(mainController.isSpellCheckedProperty());
        referenceExternalModels();
    }

    private void referenceExternalModels() {
        NoteModel noteModel = mainController.getNoteController().getCaseView().getNoteModel();
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
            case SHOW_DICTIONARY -> settingsInteractor.changeMenu(new DictionaryMenu(settingsView).build());
            case SHOW_GLOBAL_SPARES -> settingsInteractor.changeMenu(new GlobalSparesMenu(settingsView).build());
            case NEW_ENTITLEMENT -> settingsInteractor.createNewEntitlement();
            case DELETE_ENTITLEMENT -> settingsInteractor.deleteEntitlement();
            case MAKE_REFERENCE_TO_USER -> settingsInteractor.setUser(mainController.getUser());
            case SAVE_USER -> settingsInteractor.saveUser();
            case REFRESH_ENTITLEMENT_COMBO_BOX -> mainController.action(MainMessage.REFRESH_ENTITLEMENT_COMBO_BOX);
            case TOGGLE_SPELLCHECK -> toggleSpellCheck();
        };
    }

    // sends signals different directions
    private void toggleSpellCheck() {
        logger.info("Spell Check set to: {}", mainController.isSpellCheckedProperty().get());
        settingsInteractor.saveSpellCheckStatus();
        mainController.getNoteController().resetSpellCheckAreas();
    }
}
