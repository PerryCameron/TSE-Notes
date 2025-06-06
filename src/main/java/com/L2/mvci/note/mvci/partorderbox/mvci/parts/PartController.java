package com.L2.mvci.note.mvci.partorderbox.mvci.parts;

import com.L2.dto.PartFx;
import com.L2.enums.SaveType;
import com.L2.interfaces.AlertController;
import com.L2.mvci.note.NoteView;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class PartController extends AlertController<PartMessage> {
    private final PartInteractor partInteractor;
    private final PartView partView;

    public PartController(NoteView noteView, TableView<PartFx> tableView) {
        PartModel partModel = new PartModel(noteView.getNoteModel(), tableView);
        this.partInteractor = new PartInteractor(partModel);
        this.partView = new PartView(noteView, partModel, this::action);
    }

    @Override
    public Alert getView() {
        return partView.build();
    }

    @Override
    public void action(PartMessage message)  {
        switch (message) {
            case JSON_MAP_PRODUCT_FAMILIES, REFRESH_TREEVIEW -> partInteractor.mapProductFamiliesJSONtoPOJO();
            case SET_SELECTED_RANGE -> partInteractor.setSelectedRange();
            case SAVE_PART_NOTE -> partInteractor.savePart(SaveType.NOTE);
            case CANCEL_NOTE_UPDATE -> partInteractor.cancelNoteUpdate();
            case SAVE_PIM_TO_JSON -> partInteractor.saveToJson();
            case SAVE_PART_KEYWORDS -> partInteractor.savePart(SaveType.KEYWORD);
            case SAVE_IMAGE_TO_DATABASE -> partInteractor.saveImage();
            case LOAD_IMAGE -> partInteractor.getImage();
            case SAVE_EDIT_HISTORY -> partInteractor.saveEditHistory(partView.getNoteView().getNoteModel().userProperty().get());
            case GET_UPDATE_BY_INFORMATION -> {
                System.out.println("Got it in the controller!!");
                partInteractor.getUpdatedByToPOJO();
            }
        }
    }

    public void printProductFamilies() {
        partInteractor.printProductFamilies();
    }
}
