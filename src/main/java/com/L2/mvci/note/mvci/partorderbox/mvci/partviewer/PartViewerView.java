package com.L2.mvci.note.mvci.partorderbox.mvci.partviewer;

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
        partEditorModel.setImageView(imageView);
        action.accept(PartViewerMessage.LOAD_IMAGE);
        return imageView;
    }

    private Node mainInfo() {
        VBox vBox = new VBox(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
        // vBox.setPadding(new Insets(10, 10, 10, 10));
        System.out.println(partEditorModel.getSparesDTO().getSpareItem()); // this properly prints out the part number
        Node titledLabel = LabelFx.titledLabel("Part Number: ",partEditorModel.getSparesDTO().getSpareItem());
        Node titledDescription = LabelFx.titledLabel("Description: ",partEditorModel.getSparesDTO().getSpareDescription());
        Node inCatalogue = LabelFx.titledGraphicBoolean("In Catalogue: ", partEditorModel.getSparesDTO().getArchived());
        Label notes = new Label(partEditorModel.getSparesDTO().getComments());
        notes.setWrapText(true);
        notes.setPrefWidth(350);
        vBox.getChildren().addAll(titledLabel, titledDescription, inCatalogue);  // I can't see this label
        if(partEditorModel.getSparesDTO().getSpareItem() != null) {
            vBox.getChildren().add(LabelFx.titledParagraph("Notes:", partEditorModel.getSparesDTO().getComments(), 350));
        }
        return vBox;
    }


}
