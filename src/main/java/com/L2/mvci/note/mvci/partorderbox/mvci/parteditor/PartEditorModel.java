package com.L2.mvci.note.mvci.partorderbox.mvci.parteditor;

import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.mvci.partorderbox.PartOrderBoxModel;
import com.L2.mvci.note.mvci.partorderbox.PartOrderBoxView;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class PartEditorModel {
    
    private final NoteModel noteModel;
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DialogPane dialogPane = new DialogPane();
    private final PartOrderBoxModel partOrderBoxModel;
    private final SparesDTO sparesDTO;
    private final PartOrderBoxView partOrderBoxView;

    public PartEditorModel(PartOrderBoxView partOrderBoxView) {
        this.partOrderBoxView = partOrderBoxView;
        this.partOrderBoxModel = partOrderBoxView.getPartOrderBoxModel();
        this.noteModel = partOrderBoxModel.getNoteModel();
        this.sparesDTO = partOrderBoxModel.getSpare();
    }

    public NoteModel getNoteModel() {
        return noteModel;
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
    public PartOrderBoxModel getPartOrderBoxModel() {
        return partOrderBoxModel;
    }
    public SparesDTO getSparesDTO() {
        return sparesDTO;
    }
    public PartOrderBoxView getPartOrderBoxView() {
        return partOrderBoxView;
    }
}
