package com.L2.mvci_note.components;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.dto.EntitlementDTO;
import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.static_tools.StringChecker;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
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
    private final VBox root;
    private final TextField[] textFields = new TextField[7];
    private final ComboBox<EntitlementDTO> servicePlanComboBox = new ComboBox<>();
    private final ComboBox<String> schedulingTermsComboBox = new ComboBox<>();
    private final ComboBox<String> serviceLevelComboBox = new ComboBox<>();
    private final ToggleSwitch toggleSwitch = new ToggleSwitch();
    private final ComboBox<String> statusComboBox = new ComboBox<>();

    public BasicInformation(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 5, 5));
    }

    @Override
    public Region build() {
        root.getStyleClass().add("decorative-hbox");
        HBox hBox = new HBox(); // box to hold basic info and service plan
        hBox.setPadding(new Insets(0, 5, 5, 5));
        hBox.getChildren().addAll(callInInfo(), servicePlan());

//        Button copyButton = ButtonFx.utilityButton(() -> {
//            flash();
//            noteView.getAction().accept(NoteMessage.COPY_BASIC_INFORMATION);
//        }, "Copy", "/images/copy-16.png");
//        copyButton.setTooltip(ToolTipFx.of("Copy Basic Information"));

//        Button clearButton = ButtonFx.utilityButton(() -> {
//            clear();
//        }, "Clear", "/images/clear-16.png");
//        clearButton.setTooltip(ToolTipFx.of("Clear Basic Information"));

        Button pasteButton = ButtonFx.utilityButton(() -> {
        }, "Pasta", "/images/paste-16.png");
        pasteButton.setTooltip(ToolTipFx.of("Clear Basic Information"));

        Button[] buttons = new Button[]{pasteButton};
        root.getChildren().addAll(TitleBarFx.of("Basic Information", buttons), hBox);
        return root;
    }

    private Node servicePlan() {
        VBox vBox = VBoxFx.of(8.0, new Insets(0, 0, 0, 0));
        vBox.getChildren().addAll(setEntitlementBox(), setSchedulingTermsBox(), setServiceLevelBox(), setStatusBox(), loadSupportedBox());
        return vBox;
    }

    private void refreshEntitlementListener() {
        noteModel.refreshEntitlementsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Re-bind the items in the ComboBox to the ObservableList from noteModel
                servicePlanComboBox.setItems(FXCollections.observableArrayList(noteModel.getEntitlements()));
            }
        });
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
            for(EntitlementDTO entitlementDTO: servicePlanComboBox.getItems()) {
                if(entitlementDTO.getName().equals(noteModel.getBoundNote().getActiveServiceContract())) {
                    servicePlanComboBox.getSelectionModel().select(entitlementDTO);
                }
            }
        }
        // Listener to update activeServiceContract when currentEntitlement changes
        noteModel.currentEntitlementProperty().addListener((obs, oldEntitlement, newEntitlement) -> {
            if (newEntitlement != null) {
                noteModel.getBoundNote().setActiveServiceContract(newEntitlement.getName());
            } else {
                noteModel.getBoundNote().setActiveServiceContract("");  // Or handle null appropriately
            }
        });
        servicePlanComboBox.setOnAction(e -> {

            if(servicePlanComboBox.getValue() != null) {
                noteModel.getBoundNote().setActiveServiceContract(servicePlanComboBox.getValue().toString());
                noteModel.setCurrentEntitlement(servicePlanComboBox.getValue());
                noteView.getAction().accept(NoteMessage.LOG_CURRENT_ENTITLEMENT);
                noteView.getServicePlanDetails().updateDetails();
            }
        });
        vBox.getChildren().addAll(label, servicePlanComboBox);
        refreshEntitlementListener();
        return vBox;
    }

    private Node setSchedulingTermsBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Scheduling terms:");
        schedulingTermsComboBox.setPrefWidth(200);
        schedulingTermsComboBox.getItems().addAll("5x8", "5x24", "7x24");
        vBox.getChildren().addAll(label, schedulingTermsComboBox);
        return vBox;
    }

    private Node loadSupportedBox() {
        Label label = new Label("Load Supported:");
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(label, toggleSwitch);
        return hBox;
    }

    private Node setStatusBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Status of the UPS:");
        statusComboBox.setPrefWidth(200);
        statusComboBox.getItems().addAll("Online", "Bypass", "Offline");
        vBox.getChildren().addAll(label, statusComboBox);
        return vBox;
    }

    private Node setServiceLevelBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Service Level:");
        serviceLevelComboBox.setPrefWidth(200);
        serviceLevelComboBox.getItems().addAll("4-Hour", "8-Hour", "Next Business Day");
        vBox.getChildren().addAll(label, serviceLevelComboBox);
        return vBox;
    }

    private Node callInInfo() {
        VBox vBox = VBoxFx.of(5.5, new Insets(0, 40, 0, 0));
        textFields[0] = TextFieldFx.createValidatedTextField(200,"WO-", StringChecker::formatWorkOrder, noteView);
        textFields[1] = TextFieldFx.createValidatedTextField(200,"Case-", StringChecker::formatCaseNumber, noteView);
        textFields[2] = TextFieldFx.standardTextField(200, "Model", noteView);
        textFields[3] = TextFieldFx.standardTextField(200, "Serial", noteView);
        textFields[4] = TextFieldFx.createValidatedTextField(200, "Call-in Contact", StringChecker::formatName, noteView);
        textFields[5] = TextFieldFx.createValidatedTextField(200,"Call-in Phone", StringChecker::formatPhoneNumber, noteView);
        textFields[6] = TextFieldFx.createValidatedTextField(200,"Call-in Email", StringChecker::formatEmail, noteView);
        for (TextField textField : textFields) {
            vBox.getChildren().add(textField);
        }
        bindTextFields();
        return vBox;
    }

    public void bindTextFields() {
        textFields[0].textProperty().bindBidirectional(noteModel.getBoundNote().workOrderProperty());
        textFields[1].textProperty().bindBidirectional(noteModel.getBoundNote().caseNumberProperty());
        textFields[2].textProperty().bindBidirectional(noteModel.getBoundNote().modelNumberProperty());
        textFields[3].textProperty().bindBidirectional(noteModel.getBoundNote().serialNumberProperty());
        textFields[4].textProperty().bindBidirectional(noteModel.getBoundNote().callInPersonProperty());
        textFields[5].textProperty().bindBidirectional(noteModel.getBoundNote().callInPhoneNumberProperty());
        textFields[6].textProperty().bindBidirectional(noteModel.getBoundNote().callInEmailProperty());
        toggleSwitch.selectedProperty().bindBidirectional(noteModel.getBoundNote().loadSupportedProperty());
        serviceLevelComboBox.valueProperty().bindBidirectional(noteModel.getBoundNote().serviceLevelProperty());
        statusComboBox.valueProperty().bindBidirectional(noteModel.getBoundNote().upsStatusProperty());
        schedulingTermsComboBox.valueProperty().bindBidirectional(noteModel.getBoundNote().schedulingTermsProperty());
    }

    public void clear() {
        for (TextField textField : textFields) {
            textField.setText("");
        }
    }

    @Override
    public void refreshFields() {
        for(EntitlementDTO entitlementDTO: servicePlanComboBox.getItems()) {
            if(entitlementDTO.getName().equals(noteModel.getBoundNote().getActiveServiceContract())) {
                servicePlanComboBox.getSelectionModel().select(entitlementDTO);
            }
        }
    }

    @Override
    public void flash() {
        root.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(event -> root.setStyle("")); // Reset the style
        pause.play();
    }
}
