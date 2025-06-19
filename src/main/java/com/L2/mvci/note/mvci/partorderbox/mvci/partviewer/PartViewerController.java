package com.L2.mvci.note.mvci.partorderbox.mvci.partviewer;

import com.L2.interfaces.AlertController;
import com.L2.mvci.main.MainController;
import com.L2.mvci.note.NoteView;
import com.L2.mvci.note.mvci.partorderbox.PartOrderBoxView;
import javafx.scene.control.Alert;

public class PartViewerController extends AlertController<PartViewerMessage> {

    private final PartViewerInteractor partEditorInteractor;
    private final PartViewerView partEditorView;
    private final NoteView noteView;
    private final MainController mainController;

    public PartViewerController(PartOrderBoxView partOrderBoxView) {
        PartViewerModel partViewerModel = new PartViewerModel(partOrderBoxView);
        this.partEditorInteractor = new PartViewerInteractor(partViewerModel);
        this.partEditorView = new PartViewerView(partViewerModel, this::action);
        this.noteView = partOrderBoxView.getNoteView();
        this.mainController = noteView.getNoteModel().getMainController();
    }

    @Override
    public Alert getView() {
        return partEditorView.build();
    }

    @Override
    public void action(PartViewerMessage message) {
        switch (message) {
            case LOAD_IMAGE -> partEditorInteractor.getImage(mainController.getExecutorService());
        }
    }
}
