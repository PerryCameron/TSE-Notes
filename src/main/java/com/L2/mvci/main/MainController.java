package com.L2.mvci.main;

import com.L2.dto.NoteFx;
import com.L2.dto.UserDTO;
import com.L2.interfaces.Controller;
import com.L2.mvci.bom.BomController;
import com.L2.mvci.changeset.ChangeController;
import com.L2.mvci.load.LoadingController;
import com.L2.mvci.note.NoteController;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.notelist.NoteListController;
import com.L2.mvci.notelist.NoteListMessage;
import com.L2.mvci.settings.SettingsController;
import com.L2.static_tools.ImageResources;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class MainController extends Controller<MainMessage> {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    private final MainModel mainModel;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private NoteController noteController = null;
    private SettingsController settingsController = null;
    private NoteListController noteListController = null;
    private BomController bomController = null;
    private LoadingController loadingController;


    public MainController() {
        mainModel = new MainModel();
        mainInteractor = new MainInteractor(mainModel);
        mainView = new MainView(mainModel, this::action);
        logger.info("Main controller loaded");
        mainInteractor.loadAppSettings();
        logger.info("Default Locale: {}", Locale.getDefault());
        createLoadingController();
    }

    @Override
    public Region getView() {
        return mainView.build();
    }

    @Override
    public void action(MainMessage action) {
        switch (action) {
            case OPEN_NOTE_TAB -> openNoteTab();
            case OPEN_SETTINGS -> openSettingsTab();
            case OPEN_BOM -> openBomTab();
            case OPEN_NOTESLIST_TAB -> openNoteListTab();
            case PREVIOUS_NOTE -> noteListController.action(NoteListMessage.PREVIOUS_NOTE);
            case NEXT_NOTE -> noteListController.action(NoteListMessage.NEXT_NOTE);
            case SAVE_OR_UPDATE_NOTE -> noteController.action(NoteMessage.SAVE_OR_UPDATE_NOTE);
            case SET_COMPLETE -> noteController.action(NoteMessage.SET_COMPLETE);
            case NEW_NOTE -> noteController.action(NoteMessage.NEW_NOTE);
            case SORT_NOTE_TABLEVIEW -> noteListController.action(NoteListMessage.SORT_NOTE_TABLEVIEW);
            case REFRESH_NOTE_TABLEVIEW -> noteListController.action(NoteListMessage.REFRESH_NOTE_TABLEVIEW);
            case UPDATE_NOTE_TAB_NAME -> mainInteractor.updateNoteTabName(getBoundNoteProperty());
            case DELETE_NOTE -> noteController.action(NoteMessage.DELETE_NOTE);
            case SELECT_NOTE_TAB -> mainInteractor.selectNoteTab();
            case UPDATE_STATUSBAR_WITH_STRING -> noteController.action(NoteMessage.UPDATE_STATUSBAR_WITH_STRING);
            case REFRESH_PART_ORDERS -> noteController.action(NoteMessage.REFRESH_PART_ORDERS);
            case CLONE_NOTE -> noteController.action(NoteMessage.CLONE_NOTE);
            case REFRESH_ENTITLEMENT_COMBO_BOX -> noteController.action(NoteMessage.REFRESH_ENTITLEMENT_COMBO_BOX);
            case SHOW_LOG -> mainInteractor.showLog();
            case ENABLE_NEXT_BUTTON -> mainInteractor.disableNextButton(false);
            case DISABLE_NEXT_BUTTON -> mainInteractor.disableNextButton(true);
            case CHECK_BUTTON_ENABLE -> noteController.action(NoteMessage.CHECK_BUTTON_ENABLE);
            case SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT -> noteListController.action(NoteListMessage.SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT);
            case UPDATE_TABLE -> noteListController.action(NoteListMessage.UPDATE_TABLE);
            case PRINT_RANGES -> mainInteractor.printRanges(noteController.getRanges());
            case PRINT_PARTS -> noteController.printPartsTableView();
            case PRINT_PRODUCT_FAMILIES -> printProductFamilies();
            case SHUTDOWN_EXECUTOR_SERVICE -> mainInteractor.shutDownExecutorService();
            case LAUNCH_CHANGE_SET_ALERT -> openChangeSetAlert();
            case OPEN_MANUAL -> mainInteractor.openManual();
        }
    }

    public UserDTO getUser() {
        return noteController.getUser();
    }

    public ObservableList<NoteFx> getNotes() {
        return noteController.getNotes();
    }

    public ObjectProperty<NoteFx> getBoundNoteProperty() {
        return noteController.getBoundNoteProperty();
    }

    public IntegerProperty getOffsetProperty() {
        return noteController.getOffsetProperty();
    }

    public IntegerProperty getPageSizeProperty() {
        return noteController.getPageSizeProperty();
    }

    public BooleanProperty isSpellCheckedProperty() { return mainInteractor.isSpellChecked(); }

    public void setStatusBar(String status) {
        mainInteractor.setStatusBar(status);
    }

    private void openNoteTab() {
        noteController = new NoteController(this);
        mainView.addNewTab("Note", noteController.getView(), false, ImageResources.NOTES);
    }

    private void openSettingsTab() {
        settingsController = new SettingsController(this);
        mainView.addNewTab("Settings", settingsController.getView(), true, ImageResources.GEAR);
    }

    private void openNoteListTab() {
        noteListController = new NoteListController(this);
        mainView.addNewTab("Notes", noteListController.getView(), false, ImageResources.LIST);
    }

    private void openBomTab() {
        bomController = new BomController(this);
        mainView.addNewTab("BOM", bomController.getView(), false, ImageResources.BOM);
    }

    private void openChangeSetAlert() {
        Optional<Alert> alert = Optional.ofNullable(new ChangeController(this).getView());

        alert.ifPresent(Dialog::showAndWait);
    }

    public void showLoadingSpinner(boolean isVisible) {
        loadingController.showLoadSpinner(isVisible);
    }

    public void setSpinnerOffset(double x, double y) {
        loadingController.setOffset(x, y);
    }

    public void createLoadingController() {
        loadingController = new LoadingController();
        loadingController.getStage().setScene(new Scene(loadingController.getView(), Color.TRANSPARENT));
    }
    public NoteController getNoteController() {
        return noteController;
    }
    public NoteListController getNoteListController() {
        return noteListController;
    }
    public SettingsController getSettingsController() {
        return settingsController;
    }
    public MainModel getMainModel() {
        return mainModel;
    }
    public ExecutorService getExecutorService() { return mainInteractor.getExecutorService(); }

    // sending signal all the way to part mvci
    public void printProductFamilies() {
        getNoteController().printProductFamilies();
    }
}