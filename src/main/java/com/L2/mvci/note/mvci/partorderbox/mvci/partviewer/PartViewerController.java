package com.L2.mvci.note.mvci.partorderbox.mvci.partviewer;

import com.L2.interfaces.AlertController;
import com.L2.mvci.note.mvci.partorderbox.PartOrderBoxView;
import javafx.scene.control.Alert;

public class PartViewerController extends AlertController<PartViewerMessage> {

    private final PartViewerInteractor partEditorInteractor;
    private final PartViewerView partEditorView;

    public PartViewerController(PartOrderBoxView partOrderBoxView) {
        PartViewerModel partEditorModel = new PartViewerModel(partOrderBoxView);
        this.partEditorInteractor = new PartViewerInteractor(partEditorModel);
        this.partEditorView = new PartViewerView(partEditorModel, this::action);
    }

    @Override
    public Alert getView() {
        return partEditorView.build();
    }

    @Override
    public void action(PartViewerMessage message) {
        switch (message) {
        }
    }
}
