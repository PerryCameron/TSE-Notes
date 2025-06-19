package com.L2.mvci.note.mvci.partorderbox.mvci.partviewer;

import com.L2.mvci.note.NoteView;
import com.L2.widgetFx.DialogueFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PartViewerView implements Builder<Alert> {
    private static final Logger logger = LoggerFactory.getLogger(PartViewerView.class);

    private Consumer<PartViewerMessage> action;
    private PartViewerModel partEditorModel;

    public PartViewerView(PartViewerModel partEditorModel, Consumer<PartViewerMessage> message) {
        this.partEditorModel = partEditorModel;
        this.action = message;
    }

    @Override
    public Alert build() {
            partEditorModel.getAlert().setTitle("Part Viewer");
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
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPrefHeight(400.0);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.getChildren().addAll(mainInfo(), image());
        return hBox;
    }

    private Node image() {
        partEditorModel.setImageView(new ImageView());
        action.accept(PartViewerMessage.LOAD_IMAGE);
        return partEditorModel.getImageView();
    }

    private Node mainInfo() {
        VBox vBox = new VBox(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
        // vBox.setPadding(new Insets(10, 10, 10, 10));
        System.out.println(partEditorModel.getSparesDTO().getSpareItem()); // this properly prints out the part number
        Label partNumber = new Label("Part Number: " + partEditorModel.getSparesDTO().getSpareItem());
        Label partDescription = new Label("Description: " + partEditorModel.getSparesDTO().getSpareDescription());
        vBox.getChildren().addAll(partNumber, partDescription);  // I can't see this label
        return vBox;
    }
}
