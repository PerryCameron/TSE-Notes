package com.L2.mvci_note.components;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.dto.EntitlementDTO;
import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BasicInformation implements Component<Region> {

    private final NoteModel noteModel;
    private final NoteView noteView;
    private VBox root;
    private final TextField[] textFields = new TextField[7];
    private final ComboBox<EntitlementDTO> servicePlanComboBox = new ComboBox<>();
    private final ComboBox<String> schedulingTermsComboBox = new ComboBox<>();
    private final ComboBox<String> serviceLevelComboBox = new ComboBox<>();
    private final ToggleSwitch toggleSwitch = new ToggleSwitch();
    private final ComboBox<String> statusComboBox = new ComboBox<>();

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

        Button copyButton = ButtonFx.utilityButton(() -> {
            flash();
            noteView.getAction().accept(NoteMessage.COPY_BASIC_INFORMATION);
        }, "Copy","/images/copy-16.png");
        copyButton.setTooltip(ToolTipFx.of("Copy Basic Information"));

        Button clearButton = ButtonFx.utilityButton(() -> {
            for(TextField textField : textFields) {
                textField.setText("");
            }
        }, "Clear", "/images/clear-16.png");
        clearButton.setTooltip(ToolTipFx.of("Clear Basic Information"));

        Button pasteButton = ButtonFx.utilityButton(() -> {
        }, "Pasta", "/images/paste-16.png");
        clearButton.setTooltip(ToolTipFx.of("Clear Basic Information"));

        Button[] buttons = new Button[] { clearButton, pasteButton, copyButton };
        root.getChildren().addAll(TitleBarFx.of("Basic Information", buttons), hBox);
        return root;
    }

    private Node servicePlan() {
        VBox vBox = VBoxFx.of(8.0, new Insets(0, 0, 0, 0));
        vBox.getChildren().addAll(setEntitlementBox(), setSchedulingTermsBox(), setServiceLevelBox(), setStatusBox(), loadSupportedBox());
        return vBox;
    }

    private Node setEntitlementBox() {
        // the name of the entitlement is saved in the CaseDTO (String)
        // a list of Entitlements is pulled from hard disk (EntitlementDTO)
        VBox vBox = new VBox(4);
        // Define Labels and Controls
        Label label = new Label("Service Plan:");

        servicePlanComboBox.setPrefWidth(200);
        // I would like to replace the hard coded entitlements below with the ArrayList  caseModel.getEntitlements() using the field EntitlementDTO::name
        servicePlanComboBox.getItems().addAll(noteModel.getEntitlements());
        // Optional: Set a default value if needed
        if (!servicePlanComboBox.getItems().isEmpty()) {
            servicePlanComboBox.setValue(servicePlanComboBox.getItems().getFirst());  // Set the first item as selected by default
        }
        // Listener to update activeServiceContract when currentEntitlement changes
        noteModel.currentEntitlementProperty().addListener((obs, oldEntitlement, newEntitlement) -> {
            if (newEntitlement != null) {
                noteModel.getCurrentNote().setActiveServiceContract(newEntitlement.getName());
            } else {
                noteModel.getCurrentNote().setActiveServiceContract("");  // Or handle null appropriately
            }
        });
        servicePlanComboBox.setOnAction(e -> {
            noteModel.getCurrentNote().setEntitlement(servicePlanComboBox.getValue().toString());
            noteModel.setCurrentEntitlement(servicePlanComboBox.getValue());
            // no longer setting current entitlement
            System.out.println("Current entitlement set to: " + noteModel.getCurrentEntitlement());
            System.out.println(noteModel.getCurrentNote().getEntitlement());
            noteView.getServicePlanDetails().updateDetails();
        });
        vBox.getChildren().addAll(label, servicePlanComboBox);
        return vBox;
    }

    private Node setSchedulingTermsBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Scheduling terms:");
        schedulingTermsComboBox.setPrefWidth(200);
        schedulingTermsComboBox.getItems().addAll("5x8", "5x24", "7x24");
        // sets initial value
        vBox.getChildren().addAll(label, schedulingTermsComboBox);
        schedulingTermsComboBox.setOnAction(e -> noteModel.getCurrentNote().setSchedulingTerms(schedulingTermsComboBox.getValue()));
        return vBox;
    }

    private Node loadSupportedBox() {
        Label label = new Label("Load Supported:");
        HBox hBox = new HBox(10);
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> noteModel.getCurrentNote().setLoadSupported(newValue));
        hBox.getChildren().addAll(label, toggleSwitch);
        return hBox;
    }

    private Node setStatusBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Status of the UPS:");
        statusComboBox.setPrefWidth(200);
        statusComboBox.getItems().addAll("Online", "Bypass", "Offline");
        // sets initial value
        vBox.getChildren().addAll(label, statusComboBox);
        statusComboBox.setOnAction(e -> noteModel.getCurrentNote().setUpsStatus(statusComboBox.getValue()));
        return vBox;
    }

    private Node setServiceLevelBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Service Level:");
        serviceLevelComboBox.setPrefWidth(200);
        serviceLevelComboBox.getItems().addAll("4-Hour", "8-Hour", "Next Business Day");
        vBox.getChildren().addAll(label, serviceLevelComboBox);
        serviceLevelComboBox.setOnAction(e -> noteModel.getCurrentNote().setServiceLevel(serviceLevelComboBox.getValue()));
        return vBox;
    }

    private Node callInInfo() {
        VBox vBox = VBoxFx.of(5.5, new Insets(0, 40, 0, 0));

        textFields[0] = TextFieldFx.of(200, "Work Order");
        ListenerFx.addFocusListener(textFields[0], "Work Order", noteModel.getCurrentNote().workOrderProperty(), noteModel.statusLabelProperty());

        textFields[1] = TextFieldFx.of(200, "Case");
        ListenerFx.addFocusListener(textFields[1], "Case", noteModel.getCurrentNote().caseNumberProperty(), noteModel.statusLabelProperty());

        textFields[2] = TextFieldFx.of(200, 30, "Model", noteModel.getCurrentNote().modelNumberProperty());
        ListenerFx.addFocusListener(textFields[2], "Model", noteModel.getCurrentNote().modelNumberProperty(), noteModel.statusLabelProperty());

        textFields[3] = TextFieldFx.of(200, 30, "Serial", noteModel.getCurrentNote().serialNumberProperty());
        ListenerFx.addFocusListener(textFields[3], "Serial", noteModel.getCurrentNote().serialNumberProperty(), noteModel.statusLabelProperty());

        textFields[4] = TextFieldFx.of(200, 30, "Call-in Contact", noteModel.getCurrentNote().callInPersonProperty());
        ListenerFx.addFocusListener(textFields[4], "Call-in Contact", noteModel.getCurrentNote().callInPersonProperty(), noteModel.statusLabelProperty());

        textFields[5]= TextFieldFx.of(200, "Call-in Phone");
        ListenerFx.addFocusListener(textFields[5], "Call-in Phone", noteModel.getCurrentNote().callInPhoneNumberProperty(), noteModel.statusLabelProperty());

        textFields[6] = TextFieldFx.of(200, 30, "Call-in Email", noteModel.getCurrentNote().callInEmailProperty());
        ListenerFx.addFocusListener(textFields[6], "Call-in Email", noteModel.getCurrentNote().callInEmailProperty(), noteModel.statusLabelProperty());
        for(TextField textField : textFields) {
            vBox.getChildren().add(textField);
        }
        refreshFields();
        return vBox;
    }

    @Override
    public void refreshFields() {
        textFields[0].textProperty().set(noteModel.getCurrentNote().getWorkOrder());
        textFields[1].textProperty().set(noteModel.getCurrentNote().getCaseNumber());
        textFields[2].textProperty().set(noteModel.getCurrentNote().getModelNumber());
        textFields[3].textProperty().set(noteModel.getCurrentNote().getSerialNumber());
        textFields[4].textProperty().set(noteModel.getCurrentNote().getCallInPerson());
        textFields[5].textProperty().set(noteModel.getCurrentNote().getCallInPhoneNumber());
        textFields[6].textProperty().set(noteModel.getCurrentNote().getCallInEmail());
        servicePlanComboBox.valueProperty().set(noteModel.getCurrentEntitlement());
        schedulingTermsComboBox.valueProperty().set(noteModel.getCurrentNote().getSchedulingTerms());
        serviceLevelComboBox.valueProperty().set(noteModel.getCurrentNote().getServiceLevel());
        statusComboBox.valueProperty().set(noteModel.getCurrentNote().getUpsStatus());
        toggleSwitch.selectedProperty().set(noteModel.getCurrentNote().isLoadSupported());
    }

    @Override
    public void flash() {
        root.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(event -> root.setStyle("")); // Reset the style
        pause.play();
    }
}
