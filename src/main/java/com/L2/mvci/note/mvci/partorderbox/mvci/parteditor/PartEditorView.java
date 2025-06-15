package com.L2.mvci.note.mvci.partorderbox.mvci.parteditor;

import com.L2.widgetFx.DialogueFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PartEditorView implements Builder<Alert> {
    private static final Logger logger = LoggerFactory.getLogger(PartEditorView.class);

    private Consumer<PartEditorMessage> action;
    private PartEditorModel partEditorModel;

    public PartEditorView(PartEditorModel partEditorModel, Consumer<PartEditorMessage> message) {
        this.partEditorModel = partEditorModel;
        this.action = message;
    }

    @Override
    public Alert build() {
            partEditorModel.getAlert().setTitle("Part Editor");
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
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10));
        System.out.println(partEditorModel.getSparesDTO().getSpareItem()); // this properly prints out the part number
        Label partNumber = new Label("Part Number: " + partEditorModel.getSparesDTO().getSpareItem());
        vBox.getChildren().add(partNumber);  // I can't see this label
        return vBox;
    }
}
