package com.L2.mvci_note;

import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import com.L2.controls.DateTimePicker;
import com.L2.dto.EntitlementDTO;
import com.L2.mvci_main.MainMessage;
import com.L2.widgetFx.RegionFx;
import com.L2.widgetFx.TextFieldFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Builder;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class NoteView implements Builder<Region> {
    private final NoteModel noteModel;
    Consumer<NoteMessage> action;

    public NoteView(NoteModel noteModel, Consumer<NoteMessage> m) {
        this.noteModel = noteModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(setCenter());
        setUpStatusBarCommunication();
        return borderPane;
    }

    private void setUpStatusBarCommunication() {
        noteModel.statusLabelProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                action.accept(NoteMessage.STATUS_BAR_CHANGE);
            }
        });
    }

    private Node setCenter() {
        VBox vBox = VBoxFx.of(1024, 768, true, true);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.setSpacing(10);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(setBox1Info(), setBox2Info(), setBox3Info());
        vBox.getChildren().addAll(hBox, setIssueBox());
        return vBox;
    }

    private Node setBox3Info() {
        VBox vBox = new VBox(20);
        vBox.setPadding(new Insets(0, 0, 0, 40));
        vBox.getChildren().addAll(new DateTimePicker(this).build(), setServicePlanDetails());
        return vBox;
    }

    private Node setServicePlanDetails() {
        VBox vBox = new VBox();
        noteModel.setPlanDetailsBox(vBox);
        updateDetails();
        return vBox;
    }

    private void updateDetails() {
        VBox vBox = noteModel.getPlanDetailsBox();
        vBox.getChildren().clear();
        Label label = new Label(noteModel.getCurrentEntitlement().getName());
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ff0000;");
        Label label1 = new Label("Includes");
        label1.getStyleClass().add(Styles.TEXT_BOLD);
        String[] includes = noteModel.getCurrentEntitlement().getIncludes().split("\\R");
        String[] notIncludes = noteModel.getCurrentEntitlement().getNotIncludes().split("\\R");
        vBox.getChildren().addAll(label, label1);
        for (String include : includes) {
            vBox.getChildren().add(new Label(include));
        }
        Label label2 = new Label("Does not include:");
        label2.getStyleClass().add(Styles.TEXT_BOLD);
        vBox.getChildren().addAll(RegionFx.regionHeightOf(15), label2);
        for (String notInclude : notIncludes) {
            vBox.getChildren().add(new Label(notInclude));
        }
    }

    private Node setBox1Info() {
        VBox vBox = VBoxFx.of(5.0, new Insets(15, 40, 0, 20));
        TextField tf1 = TextFieldFx.of(200, 30, "Work Order", noteModel.getCurrentNote().workOrderProperty());
        TextField tf2 = TextFieldFx.of(200, 30, "Case", noteModel.getCurrentNote().caseNumberProperty());
        TextField tf3 = TextFieldFx.of(200, 30, "Model", noteModel.getCurrentNote().modelNumberProperty());
        TextField tf4 = TextFieldFx.of(200, 30, "Serial", noteModel.getCurrentNote().serialNumberProperty());
        TextField tf5 = TextFieldFx.of(200, 30, "Call-in Contact", noteModel.getCurrentNote().callInPersonProperty());
        TextField tf6 = TextFieldFx.of(200, 30, "Call-in Phone", noteModel.getCurrentNote().callInPhoneNumberProperty());
        TextField tf7 = TextFieldFx.of(200, 30, "Call-in Email", noteModel.getCurrentNote().callInEmailProperty());
        vBox.getChildren().addAll(tf1, tf2, tf3, tf4, tf5, tf6, tf7);
        return vBox;
    }

    private Node setBox2Info() {
        VBox vBox = VBoxFx.of(17.0, new Insets(15, 0, 0, 20));
        vBox.getChildren().addAll(setEntitlementBox(), setServiceLevelBox(), setStatusBox(), loadSupportedBox());
        return vBox;
    }

    private Node loadSupportedBox() {
        Label label = new Label("Load Supported:");
        HBox hBox = new HBox(10);
        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.selectedProperty().bindBidirectional(noteModel.getCurrentNote().loadSupportedProperty());
        hBox.getChildren().addAll(label, toggleSwitch);
        return hBox;
    }

    private Node setStatusBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Status of the UPS:");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        comboBox.getItems().addAll("Online", "Bypass", "Offline");
        comboBox.setValue("Online"); // Default to "Online"
        comboBox.valueProperty().bindBidirectional(noteModel.getCurrentNote().upsStatusProperty());
        vBox.getChildren().addAll(label, comboBox);
        comboBox.setOnAction(e -> System.out.println("UPS Status: " + noteModel.getCurrentNote().getUpsStatus()));
        return vBox;
    }

    private Node setServiceLevelBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Service Level:");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        comboBox.getItems().addAll("4-Hour", "8-Hour", "Next Business Day");
        comboBox.valueProperty().bindBidirectional(noteModel.getCurrentNote().serviceLevelProperty());
        vBox.getChildren().addAll(label, comboBox);
        comboBox.setOnAction(e -> System.out.println("Service level: " + noteModel.getCurrentNote().getServiceLevel()));
        return vBox;
    }

    private Node setEntitlementBox() {
        VBox vBox = new VBox(4);
        // Define Labels and Controls
        Label label = new Label("Service Plan:");
        ComboBox<EntitlementDTO> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        // I would like to replace the hard coded entitlements below with the ArrayList  caseModel.getEntitlements() using the field EntitlementDTO::name
        comboBox.getItems().addAll(noteModel.getEntitlements());
        // Optional: Set a default value if needed
        if (!comboBox.getItems().isEmpty()) {
            comboBox.setValue(comboBox.getItems().get(0));  // Set the first item as selected by default
        }
        // Bind the valueProperty of the ComboBox to the activeEntitlementProperty of the CaseDTO
        comboBox.valueProperty().bindBidirectional(noteModel.currentEntitlementProperty());
        // Listener to update activeServiceContract when currentEntitlement changes
        noteModel.currentEntitlementProperty().addListener((obs, oldEntitlement, newEntitlement) -> {
            if (newEntitlement != null) {
                noteModel.getCurrentNote().setActiveServiceContract(newEntitlement.getName());
            } else {
                noteModel.getCurrentNote().setActiveServiceContract("");  // Or handle null appropriately
            }
        });
        comboBox.setOnAction(e -> updateDetails());
        vBox.getChildren().addAll(label, comboBox);
        return vBox;
    }


    private Node setIssueBox() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10)); // Optional padding around the GridPane

        // Create the Label
        Label lblIssue = new Label("Issue:");

        // Create the TextArea
        TextArea textAreaIssue = new TextArea();
        textAreaIssue.setWrapText(true); // Enable text wrapping within the TextArea
        textAreaIssue.textProperty().bindBidirectional(noteModel.getCurrentNote().issueProperty());
        textAreaIssue.setPrefHeight(200);
        textAreaIssue.setFont(Font.font(16));

        // Add the Label and TextArea to the GridPane
        gridPane.add(lblIssue, 0, 0); // Label in the upper left corner
        gridPane.add(textAreaIssue, 0, 1); // TextArea below the label

        // Ensure the TextArea takes up the full width of the GridPane
        GridPane.setHgrow(textAreaIssue, Priority.ALWAYS);
        textAreaIssue.setMaxWidth(Double.MAX_VALUE);
        textAreaIssue.setPrefRowCount(5); // Optional: Set a preferred number of rows

        return gridPane;
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }
}
