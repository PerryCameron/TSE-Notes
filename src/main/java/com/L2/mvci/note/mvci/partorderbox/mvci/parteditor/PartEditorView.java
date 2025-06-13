package com.L2.mvci.note.mvci.partorderbox.mvci.parteditor;


import com.L2.mvci.note.NoteView;
import com.L2.widgetFx.DialogueFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PartEditorView implements Builder<Alert> {
    private static final Logger logger = LoggerFactory.getLogger(PartEditorView.class);

    private Consumer<PartEditorMessage> action;
    private NoteView noteView;
    private PartEditorModel partEditorModel;

    public PartEditorView(PartEditorModel partEditorModel, Consumer<PartEditorMessage> message) {
        this.noteView = partEditorModel.getNoteView();
        this.partEditorModel = partEditorModel;
        this.action = message;
    }

    @Override
    public Alert build() {
            partEditorModel.getAlert().setTitle("Search spares");
            // close the alert window. This listener fixes that.
            partEditorModel.getAlert().showingProperty().addListener((obs, wasShowing, isShowing) -> {
                if (isShowing) {
                    Stage stage = (Stage) partEditorModel.getAlert().getDialogPane().getScene().getWindow();
                    stage.setOnCloseRequest(event -> cleanAlertClose());
                }
            });
            partEditorModel.getAlert().setDialogPane(createDialogPane());
            // here is the start of the UI
            partEditorModel.getAlert().getDialogPane().setContent(contentBox());
            DialogueFx.getTitleIcon(partEditorModel.getDialogPane());
            DialogueFx.tieAlertToStage(partEditorModel.getAlert(), partEditorModel.getWidth(), 400);
            return partEditorModel.getAlert();
    }

    private DialogPane createDialogPane() {
        partEditorModel.getDialogPane().getStylesheets().add("css/light.css");
        partEditorModel.getDialogPane().getStyleClass().add("decorative-hbox");
        partEditorModel.getDialogPane().setPrefWidth(partEditorModel.getWidth());
        partEditorModel.getDialogPane().setMinWidth(partEditorModel.getWidth()); // Ensure minimum width is 800
        return partEditorModel.getDialogPane();
    }

    private void cleanAlertClose() {
        partEditorModel.getAlert().setResult(ButtonType.CANCEL);
        partEditorModel.getAlert().close(); // Use close() instead of hide()
        partEditorModel.getAlert().hide();
    }

    private Node contentBox() {
        return new VBox();
    }
}
