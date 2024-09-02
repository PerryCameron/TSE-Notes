package com.L2.mvci_note.components;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.dto.EntitlementDTO;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Duration;

import java.util.Objects;

public class BasicInformation implements Builder<Region> {

    private final NoteModel noteModel;
    private final NoteView noteView;
    private VBox root;

    public BasicInformation(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
        root = VBoxFx.of(5.0, new Insets(5, 5, 5, 5));
        root.getStyleClass().add("decorative-hbox");
        HBox hBox = new HBox(); // box to hold basic info and service plan
        hBox.setPadding(new Insets(0, 5, 5, 5));
        hBox.getChildren().addAll(callInInfo(), servicePlan());
        root.getChildren().addAll(toolBar(), hBox);
        return root;
    }

    private Node toolBar() {
        HBox toolBar = HBoxFx.of(Pos.CENTER_LEFT, new Insets(0, 5, 0, 0));
        HBox iconBox = HBoxFx.iconBox(); // to hold icons
        iconBox.getChildren().add(copyButton());
        Label label = new Label("Basic Information");
        label.setPadding(new Insets(0, 0, 0, 5));
        toolBar.getChildren().addAll(label, iconBox);
        return toolBar;
    }

    private Node copyButton() {
        Image copyIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/copy-16.png")));
        ImageView imageViewCopy = new ImageView(copyIcon);
        Button copyButton = ButtonFx.of(imageViewCopy, "invisible-button");
        copyButton.setTooltip(ToolTipFx.of("Copy part basic information to clipboard"));
        copyButton.setOnAction(e -> {
            root.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            // Use a PauseTransition to remove the border after 0.2 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> root.setStyle("")); // Reset the style
            pause.play();
            noteView.getAction().accept(NoteMessage.COPY_BASIC_INFORMATION);
        });
        return copyButton;
    }

    private Node servicePlan() {
        VBox vBox = VBoxFx.of(8.0, new Insets(15, 0, 0, 0));
        vBox.getChildren().addAll(setEntitlementBox(), setSchedulingTermsBox(), setServiceLevelBox(), setStatusBox(), loadSupportedBox());
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
        comboBox.valueProperty().set(noteModel.getCurrentEntitlement());
        // Listener to update activeServiceContract when currentEntitlement changes
        noteModel.currentEntitlementProperty().addListener((obs, oldEntitlement, newEntitlement) -> {
            if (newEntitlement != null) {
                noteModel.getCurrentNote().setActiveServiceContract(newEntitlement.getName());
            } else {
                noteModel.getCurrentNote().setActiveServiceContract("");  // Or handle null appropriately
            }
        });
        comboBox.setOnAction(e -> {
            noteModel.setCurrentEntitlement(comboBox.getValue());
            System.out.println("Current entitlement set to: " + noteModel.getCurrentEntitlement());
            noteView.getServicePlanDetails().updateDetails();
        });
        vBox.getChildren().addAll(label, comboBox);
        return vBox;
    }

    private Node setSchedulingTermsBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Scheduling terms:");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        comboBox.getItems().addAll("5x8", "5x24", "7x24");
        // sets initial value
        comboBox.valueProperty().set(noteModel.getCurrentNote().getSchedulingTerms());
        vBox.getChildren().addAll(label, comboBox);
        comboBox.setOnAction(e -> {
            noteModel.getCurrentNote().setSchedulingTerms(comboBox.getValue());
            System.out.println("Scheduling Terms: " + noteModel.getCurrentNote().getSchedulingTerms());
        });
        return vBox;
    }

    private Node loadSupportedBox() {
        Label label = new Label("Load Supported:");
        HBox hBox = new HBox(10);
        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.selectedProperty().set(noteModel.getCurrentNote().isLoadSupported());
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            noteModel.getCurrentNote().setLoadSupported(newValue);
            System.out.println("Load Supported: " + noteModel.getCurrentNote().isLoadSupported());
        });
        hBox.getChildren().addAll(label, toggleSwitch);
        return hBox;
    }

    private Node setStatusBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Status of the UPS:");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        comboBox.getItems().addAll("Online", "Bypass", "Offline");
        // sets initial value
        comboBox.valueProperty().set(noteModel.getCurrentNote().getUpsStatus());
        vBox.getChildren().addAll(label, comboBox);
        comboBox.setOnAction(e -> {
            noteModel.getCurrentNote().setUpsStatus(comboBox.getValue());
            System.out.println("UPS Status: " + noteModel.getCurrentNote().getUpsStatus());
        });
        return vBox;
    }

    private Node setServiceLevelBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Service Level:");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        comboBox.getItems().addAll("4-Hour", "8-Hour", "Next Business Day");
        comboBox.valueProperty().set(noteModel.getCurrentNote().getServiceLevel());
        vBox.getChildren().addAll(label, comboBox);
        comboBox.setOnAction(e -> {
            noteModel.getCurrentNote().setServiceLevel(comboBox.getValue());
            System.out.println("Service level: " + noteModel.getCurrentNote().getServiceLevel());
        });
        return vBox;
    }

    private Node callInInfo() {
        VBox vBox = VBoxFx.of(5.5, new Insets(15, 40, 0, 0));

        TextField tf1 = TextFieldFx.of(200,  "Work Order");
        tf1.textProperty().set(noteModel.getCurrentNote().getWorkOrder());
        ListenerFx.addFocusListener(tf1, "Work Order", noteModel.getCurrentNote().workOrderProperty(), noteModel.statusLabelProperty());

        TextField tf2 = TextFieldFx.of(200, "Case");
        tf2.textProperty().set(noteModel.getCurrentNote().getCaseNumber());
        ListenerFx.addFocusListener(tf2, "Case", noteModel.getCurrentNote().caseNumberProperty(), noteModel.statusLabelProperty());

        TextField tf3 = TextFieldFx.of(200, 30, "Model", noteModel.getCurrentNote().modelNumberProperty());
        tf3.textProperty().set(noteModel.getCurrentNote().getModelNumber());
        ListenerFx.addFocusListener(tf3, "Model", noteModel.getCurrentNote().modelNumberProperty(), noteModel.statusLabelProperty());

        TextField tf4 = TextFieldFx.of(200, 30, "Serial", noteModel.getCurrentNote().serialNumberProperty());
        tf4.textProperty().set(noteModel.getCurrentNote().getSerialNumber());
        ListenerFx.addFocusListener(tf4, "Serial", noteModel.getCurrentNote().serialNumberProperty(), noteModel.statusLabelProperty());

        TextField tf5 = TextFieldFx.of(200, 30, "Call-in Contact", noteModel.getCurrentNote().callInPersonProperty());
        tf5.textProperty().set(noteModel.getCurrentNote().getCallInPerson());
        ListenerFx.addFocusListener(tf5, "Call-in Contact", noteModel.getCurrentNote().callInPersonProperty(), noteModel.statusLabelProperty());

        TextField tf6 = TextFieldFx.of(200, "Call-in Phone");
        tf6.textProperty().set(noteModel.getCurrentNote().getCallInPhoneNumber());
        ListenerFx.addFocusListener(tf6, "Call-in Phone", noteModel.getCurrentNote().callInPhoneNumberProperty(), noteModel.statusLabelProperty());

        TextField tf7 = TextFieldFx.of(200, 30, "Call-in Email", noteModel.getCurrentNote().callInEmailProperty());
        tf7.textProperty().set(noteModel.getCurrentNote().getCallInEmail());
        ListenerFx.addFocusListener(tf7, "Call-in Email", noteModel.getCurrentNote().callInEmailProperty(), noteModel.statusLabelProperty());
        vBox.getChildren().addAll(tf1, tf2, tf3, tf4, tf5, tf6, tf7);
        return vBox;
    }
}
