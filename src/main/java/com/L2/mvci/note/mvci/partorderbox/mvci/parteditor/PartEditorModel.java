package com.L2.mvci.note.mvci.partorderbox.mvci.parteditor;

import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class PartEditorModel {


    private final NoteModel noteModel;
    private final NoteView noteView;
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DialogPane dialogPane = new DialogPane();

    public PartEditorModel(NoteView noteView) {
    this.noteView = noteView;
    this.noteModel = noteView.getNoteModel();
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }
    public NoteView getNoteView() {
        return noteView;
    }
    public Alert getAlert() {
        return alert;
    }
    public DialogPane getDialogPane() {
        return dialogPane;
    }
    public double getWidth() {
        return 800;
    }
}
