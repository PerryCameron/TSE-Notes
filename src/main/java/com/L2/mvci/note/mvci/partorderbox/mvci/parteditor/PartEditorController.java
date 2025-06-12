package com.L2.mvci.note.mvci.partorderbox.mvci.parteditor;

import com.L2.interfaces.AlertController;
import com.L2.mvci.note.NoteView;
import javafx.scene.control.Alert;

public class PartEditorController extends AlertController<PartEditorMessage> {

    private final PartEditorInteractor partEditorInteractor;
    private final PartEditorView partEditorView;

    public PartEditorController(NoteView noteView) {
        PartEditorModel partEditorModel = new PartEditorModel(noteView.getNoteModel());
        this.partEditorInteractor = new PartEditorInteractor(partEditorModel);
        this.partEditorView = new PartEditorView(noteView, partEditorModel, this::action);
    }

    @Override
    public Alert getView() {
        return null ;
    }

    @Override
    public void action(PartEditorMessage message) {
        switch (message) {

        }
    }
}
