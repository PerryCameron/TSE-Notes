package com.L2.mvci.changeset;

import com.L2.mvci.note.NoteMessage;
import com.L2.widgetFx.DialogueFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;

import java.util.function.Consumer;

public class ChangeView implements Builder<Alert> {

    private final ChangeModel changeModel;
    Consumer<ChangeMessage> action;


    public ChangeView(ChangeModel changeModel, Consumer<ChangeMessage> action) {
        this.changeModel = changeModel;
    }

    @Override
    public Alert build() {
        changeModel.getAlert().setTitle("Change Set");

        changeModel.getAlert().showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                Stage stage = (Stage) changeModel.getAlert().getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(event -> cleanAlertClose());
            }
        });
        changeModel.getAlert().setDialogPane(createDialogPane());
        // here is the start of the UI for search
        changeModel.getDialogPane().setContent(contentBox());
        DialogueFx.getTitleIcon(changeModel.getDialogPane());
        DialogueFx.tieAlertToStage(changeModel.getAlert(), 400, 400);
        return changeModel.getAlert();
    }

    private Node contentBox() {
        VBox vBox = new VBox(10); // Set spacing between elements
        vBox.setPadding(new Insets(10)); // Add padding around VBox
        // Create and configure the ComboBox (1-30 days)
        ComboBox<Integer> daysComboBox = new ComboBox<>();
        for (int i = 1; i <= 30; i++) {
            daysComboBox.getItems().add(i);
        }
        daysComboBox.setValue(7); // Default to 7 days
        daysComboBox.setPromptText("Select days");
        // Create label for ComboBox
        Label daysLabel = new Label("Number of days to include in changeset");
        daysLabel.setLabelFor(daysComboBox);
        // Create CheckBox for all records vs. user-only
        CheckBox includeAllCheckBox = new CheckBox("Include all records (uncheck for user-only)");
        includeAllCheckBox.setSelected(true); // Default to all records
        // Create Button for changeset creation
        Button createChangeSetButton = new Button("Create Changeset");
        createChangeSetButton.setOnAction(event -> {
            int days = daysComboBox.getValue() != null ? daysComboBox.getValue() : 7;
            boolean includeAll = includeAllCheckBox.isSelected();
            // Placeholder for changeset creation logic
            System.out.println("Creating changeset for " + days + " days, include all: " + includeAll);
            // Add your changeset creation logic here, e.g., call a method with days and includeAll
        });
        // Add all components to VBox
        vBox.getChildren().addAll(daysLabel, daysComboBox, includeAllCheckBox, createChangeSetButton);
        return vBox;
    }

    private DialogPane createDialogPane() {
        changeModel.getDialogPane().getStylesheets().add("css/light.css");
        changeModel.getDialogPane().getStyleClass().add("decorative-hbox");
        changeModel.getDialogPane().setPrefWidth(400);
        changeModel.getDialogPane().setMinWidth(400); // Ensure minimum width is 800
        return changeModel.getDialogPane();
    }

    private void cleanAlertClose() {
        changeModel.getAlert().setResult(ButtonType.CANCEL);
        changeModel.getAlert().close(); // Use close() instead of hide()
        changeModel.getAlert().hide();
    }
}
