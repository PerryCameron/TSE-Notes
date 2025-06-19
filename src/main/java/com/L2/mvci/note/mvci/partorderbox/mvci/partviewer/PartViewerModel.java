package com.L2.mvci.note.mvci.partorderbox.mvci.partviewer;

import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.mvci.partorderbox.PartOrderBoxModel;
import com.L2.mvci.note.mvci.partorderbox.PartOrderBoxView;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;

public class PartViewerModel {
    
    private final NoteModel noteModel;
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DialogPane dialogPane = new DialogPane();
    private final PartOrderBoxModel partOrderBoxModel;
    private final SparesDTO sparesDTO;
    private final PartOrderBoxView partOrderBoxView;
    private ImageView imageView;


    public PartViewerModel(PartOrderBoxView partOrderBoxView) {
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
    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
