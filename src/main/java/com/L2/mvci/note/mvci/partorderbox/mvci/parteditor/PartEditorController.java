package com.L2.mvci.note.mvci.partorderbox.mvci.parteditor;

import com.L2.interfaces.AlertController;
import com.L2.mvci.note.mvci.partorderbox.PartOrderBoxView;
import javafx.scene.control.Alert;

public class PartEditorController extends AlertController<PartEditorMessage> {

    private final PartEditorInteractor partEditorInteractor;
    private final PartEditorView partEditorView;

    public PartEditorController(PartOrderBoxView partOrderBoxView) {
        PartEditorModel partEditorModel = new PartEditorModel(partOrderBoxView.getNoteView());
        this.partEditorInteractor = new PartEditorInteractor(partEditorModel);
        this.partEditorView = new PartEditorView(partEditorModel, this::action);
    }

    @Override
    public Alert getView() {
        return partEditorView.build();
    }

    @Override
    public void action(PartEditorMessage message) {
        switch (message) {

        }
    }
}
