package com.L2.mvci.settings;

import com.L2.interfaces.Controller;
import com.L2.mvci.main.MainMessage;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.main.MainController;
import com.L2.mvci.settings.components.PreferenceMenu;
import com.L2.mvci.settings.components.EntitlementsMenu;
import com.L2.mvci.settings.components.GlobalSparesMenu;
import com.L2.mvci.settings.components.UserMenu;
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
            // case PRINT_ENTITLEMENTS -> settingsInteractor.printEntitlements();
            case SHOW_USER -> settingsInteractor.changeMenu(new UserMenu(settingsView).build());
            case SHOW_ENTITLEMENTS -> settingsInteractor.changeMenu(new EntitlementsMenu(settingsView).build());
            case SHOW_DICTIONARY -> settingsInteractor.changeMenu(new PreferenceMenu(settingsView).build());
            case SHOW_GLOBAL_SPARES -> settingsInteractor.changeMenu(new GlobalSparesMenu(settingsView).build());
            case NEW_ENTITLEMENT -> settingsInteractor.createNewEntitlement();
            case DELETE_ENTITLEMENT -> settingsInteractor.deleteEntitlement();
            case MAKE_REFERENCE_TO_USER -> settingsInteractor.setUser(mainController.getUser());
            case SAVE_USER -> settingsInteractor.saveUser();
            case REFRESH_ENTITLEMENT_COMBO_BOX -> mainController.action(MainMessage.REFRESH_ENTITLEMENT_COMBO_BOX);
            case TOGGLE_SPELLCHECK -> toggleSpellCheck();
            case VERIFY_PARTS_DATABASE -> settingsInteractor.checkDatabase();
            case INSTALL_PART_DATABASE -> { settingsInteractor.installPartsDatabase(); }
            case GET_RANGES -> mainController.getNoteController().action(NoteMessage.GET_RANGES);
            case GET_RANGES_REFERENCE -> getRangesReference();
            case DELETE_RANGE -> settingsInteractor.deleteRange();
            case SAVE_RANGES -> settingsInteractor.saveRanges();
            case ADD_RANGE -> settingsInteractor.addRange();
            case UPDATE_RANGE_IN_LIST -> settingsInteractor.updateRangeInList();
            case UPDATE_NUMBER_OF_SPARES -> settingsInteractor.updateNumberOfSpares();
            case PERSIST_THEME -> settingsInteractor.persistTheme();
        };
    }

    // sends signals different directions
    private void toggleSpellCheck() {
        logger.info("Spell Check set to: {}", mainController.isSpellCheckedProperty().get());
        settingsInteractor.saveSpellCheckStatus();
        mainController.getNoteController().resetSpellCheckAreas();
    }

    private void getRangesReference() {
        settingsInteractor.setRanges(mainController.getNoteController().getRanges());
    }
}
