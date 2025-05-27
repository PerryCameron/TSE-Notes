package com.L2.mvci.parts;

import com.L2.dto.PartFx;
import com.L2.interfaces.AlertController;
import com.L2.mvci.note.NoteView;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class PartController extends AlertController<PartMessage> {
    private NoteView noteView = null;
    private final PartInteractor partInteractor;
    private final PartView partView;

    public PartController(NoteView noteView, TableView<PartFx> tableView) {
        this.noteView = noteView;
        PartModel partModel = new PartModel(noteView.getNoteModel(), tableView);
        this.partInteractor = new PartInteractor(partModel);
        this.partView = new PartView(noteView, partModel, this::action);
    }


    @Override
    public Alert getView() {
        return partView.build();
    }

    @Override
    public void action(PartMessage message) {
        switch (message) {
            case JSON_MAP_PRODUCT_FAMILIES -> partInteractor.mapProductFamiliesJSONtoPOJO();
//            case SET_SELECTED_RANGE -> partInteractor.setSelectedRange();
        }
    }


}
