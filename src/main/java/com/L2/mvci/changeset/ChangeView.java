package com.L2.mvci.changeset;

import com.L2.BaseApplication;
import com.L2.widgetFx.DialogueFx;
import com.L2.widgetFx.HBoxFx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.controlsfx.control.ToggleSwitch;

import java.util.function.Consumer;

public class ChangeView implements Builder<Alert> {

    private final ChangeModel changeModel;
    Consumer<ChangeMessage> action;


    public ChangeView(ChangeModel changeModel, Consumer<ChangeMessage> action) {
        this.changeModel = changeModel;
        this.action = action;
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
        // Create CheckBox for all records vs. user-only
        ToggleSwitch includeAllCheckBox = new ToggleSwitch("Include all records (uncheck for user-only)");
        includeAllCheckBox.setSelected(true); // Default to all records
        // Create Button for changeset creation
        Button createChangeSetButton = new Button("Create Changeset");
        createChangeSetButton.setOnAction(event -> {
            changeModel.numberOfDaysProperty().set(changeModel.getDaysComboBox().getValue() != null ? changeModel.getDaysComboBox().getValue() : 7);
            changeModel.includeAllProperty().set(includeAllCheckBox.isSelected());
            action.accept(ChangeMessage.CREATE_CHANGESET);
            cleanAlertClose();
        });
        // Add all components to VBox
        vBox.getChildren().addAll(daysToIncludeBox(), includeAllCheckBox, createChangeSetButton);
        return vBox;
    }

    private Node daysToIncludeBox() {
        HBox hBox = HBoxFx.of(10.0, Pos.CENTER_LEFT);
        // Create and configure the ComboBox (1-30 days)
        ComboBox<Integer> daysComboBox = changeModel.getDaysComboBox();
        for (int i = 1; i <= 60; i++) {
            daysComboBox.getItems().add(i);
        }
        daysComboBox.setValue(7); // Default to 7 days
        daysComboBox.setPromptText("Select days");
        Label daysLabel = new Label("Number of days to include in changeset");
        // Create label for ComboBox
        daysLabel.setLabelFor(daysComboBox);
        hBox.getChildren().addAll(daysLabel, daysComboBox);
        return hBox;
    }

    private DialogPane createDialogPane() {
        changeModel.getDialogPane().getStyleClass().add("decorative-hbox");
        changeModel.getDialogPane().setPrefWidth(400);
        changeModel.getDialogPane().setMinWidth(400); // Ensure minimum width is 800
        changeModel.getDialogPane().getStylesheets().add("css/" + BaseApplication.theme + ".css");
        return changeModel.getDialogPane();
    }

    private void cleanAlertClose() {
        changeModel.getAlert().setResult(ButtonType.CANCEL);
        changeModel.getAlert().close(); // Use close() instead of hide()
        changeModel.getAlert().hide();
    }
}
