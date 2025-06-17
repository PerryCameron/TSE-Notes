package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder;

import com.L2.dto.PartFx;
import com.L2.enums.SaveType;
import com.L2.interfaces.AlertController;
import com.L2.mvci.main.MainController;
import com.L2.mvci.note.NoteView;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class PartFinderController extends AlertController<PartFinderMessage> {
    private final PartFinderInteractor partInteractor;
    private final PartFinderView partView;
    private final MainController mainController;

    public PartFinderController(NoteView noteView, TableView<PartFx> tableView) {
        PartFinderModel partModel = new PartFinderModel(noteView.getNoteModel(), tableView);
        this.partInteractor = new PartFinderInteractor(partModel);
        this.partView = new PartFinderView(noteView, partModel, this::action);
        this.mainController = noteView.getNoteModel().getMainController();
    }

    @Override
    public Alert getView() {
        return partView.build();
    }

    @Override
    public void action(PartFinderMessage message)  {
        switch (message) {
            case JSON_MAP_PRODUCT_FAMILIES, REFRESH_TREEVIEW -> partInteractor.mapProductFamiliesJSONtoPOJO();
            case SET_SELECTED_RANGE -> partInteractor.setSelectedRange();
            case SAVE_PART_NOTE -> partInteractor.savePart(SaveType.NOTE);
            case CANCEL_NOTE_UPDATE -> partInteractor.cancelNoteUpdate();
            case SAVE_PIM_TO_JSON -> partInteractor.saveToJson();
            case SAVE_PART_KEYWORDS -> partInteractor.savePart(SaveType.KEYWORD);
            case SAVE_IMAGE_TO_DATABASE -> partInteractor.saveImage(SaveType.IMAGE);
            case LOAD_IMAGE -> partInteractor.getImage(mainController.getExecutorService());
//            case SAVE_EDIT_HISTORY -> partInteractor.saveEditHistory();
            case GET_UPDATE_BY_INFORMATION -> partInteractor.getUpdatedByToPOJO();
            case REFRESH_PART_INFO -> partInteractor.refreshPartInfo();
        }
    }

    public void printProductFamilies() {
        partInteractor.printProductFamilies();
    }
}
