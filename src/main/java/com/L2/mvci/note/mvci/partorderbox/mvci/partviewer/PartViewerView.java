package com.L2.mvci.note.mvci.partorderbox.mvci.partviewer;

import com.L2.BaseApplication;
import com.L2.widgetFx.DialogueFx;
import com.L2.widgetFx.LabelFx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private PartViewerModel partViewerModel;

    public PartViewerView(PartViewerModel partEditorModel, Consumer<PartViewerMessage> message) {
        this.partViewerModel = partEditorModel;
        this.action = message;
    }

    @Override
    public Alert build() {
            partViewerModel.getAlert().setTitle("Part Viewer");
            // close the alert window. This listener fixes that.
            partViewerModel.getAlert().showingProperty().addListener((obs, wasShowing, isShowing) -> {
                if (isShowing) {
                    Stage stage = (Stage) partViewerModel.getAlert().getDialogPane().getScene().getWindow();
                    stage.setOnCloseRequest(event -> cleanAlertClose());
                }
            });
            partViewerModel.getAlert().setDialogPane(createDialogPane());
            // here is the start of the UI
            partViewerModel.getAlert().getDialogPane().setContent(contentBox());
            DialogueFx.getTitleIcon(partViewerModel.getDialogPane());
            DialogueFx.tieAlertToStage(partViewerModel.getAlert(), partViewerModel.getWidth(), 400);
            return partViewerModel.getAlert();
    }

    private DialogPane createDialogPane() {
        //partViewerModel.getDialogPane().getStylesheets().add("css/light.css");
        partViewerModel.getDialogPane().getStylesheets().add("css/" + BaseApplication.theme + ".css");
        partViewerModel.getDialogPane().getStyleClass().add("decorative-hbox");
        partViewerModel.getDialogPane().setPrefWidth(partViewerModel.getWidth());
        partViewerModel.getDialogPane().setMinWidth(partViewerModel.getWidth()); // Ensure minimum width is 800
        return partViewerModel.getDialogPane();
    }

    private void cleanAlertClose() {
        partViewerModel.getAlert().setResult(ButtonType.CANCEL);
        partViewerModel.getAlert().close(); // Use close() instead of hide()
    }

    private Node contentBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER_LEFT); // Align content to the left
        Node mainInfo = mainInfo();
        Node image = image();
        HBox.setHgrow(mainInfo, Priority.ALWAYS); // Main info takes available space
        HBox.setHgrow(image, Priority.NEVER); // Image uses its natural size
        hBox.getChildren().addAll(mainInfo, image);
        return hBox;
    }

    private Node image() {
        ImageView imageView = new ImageView();
        // Set size constraints to prevent oversized images
        imageView.setFitWidth(357); // Adjust as needed
        imageView.setFitHeight(265);
        imageView.setPreserveRatio(true); // Maintain aspect ratio
        partViewerModel.setImageView(imageView);
        action.accept(PartViewerMessage.LOAD_IMAGE);
        return imageView;
    }

    private Node mainInfo() {
        VBox vBox = new VBox(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
        Node titledLabel = LabelFx.titledLabel("Part Number: ", partViewerModel.getSparesDTO().getSpareItem());
        Node titledDescription = LabelFx.titledLabel("Description: ", partViewerModel.getSparesDTO().getSpareDescription());
        Node inCatalogue = LabelFx.titledGraphicBoolean("In Catalogue: ", partViewerModel.getSparesDTO().getArchived());
        Label notes = new Label(partViewerModel.getSparesDTO().getComments());
        notes.setWrapText(true);
        notes.setPrefWidth(350);
        vBox.getChildren().addAll(titledLabel, titledDescription, inCatalogue);  // I can't see this label
        if(partViewerModel.getSparesDTO().getSpareItem() != null) {
            vBox.getChildren().add(LabelFx.titledParagraph("Notes:", partViewerModel.getSparesDTO().getComments(), 350));
        }
        return vBox;
    }


}
