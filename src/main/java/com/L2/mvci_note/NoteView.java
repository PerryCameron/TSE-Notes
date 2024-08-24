package com.L2.mvci_note;

import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import com.L2.mvci_note.components.DateTimePicker;
import com.L2.dto.EntitlementDTO;
import com.L2.mvci_note.components.SiteInformation;
import com.L2.widgetFx.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.util.Builder;
import javafx.scene.control.*;

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
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(setCenter());
        setUpStatusBarCommunication();
        return scrollPane;
    }

    private void setUpStatusBarCommunication() {
        noteModel.statusLabelProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                action.accept(NoteMessage.STATUS_BAR_CHANGE);
            }
        });
    }

    private Node setCenter() {
        VBox vBox = VBoxFx.of( true, 10, new Insets(10, 20, 0, 20));
        HBox hBox = new HBox();
        hBox.getChildren().addAll(setBox1Info(), setBox2Info(), setBox3Info());
        vBox.getChildren().addAll(hBox, setIssueBox(), rowThreeBox());
        return vBox;
    }

    private Node rowThreeBox() {
        HBox hBox = new HBox();
        hBox.getChildren().add(new SiteInformation(this).build());
        return hBox;
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

    private Node setBox1Info() {
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

    private Node setBox2Info() {
        VBox vBox = VBoxFx.of(8.0, new Insets(15, 0, 0, 0));
        vBox.getChildren().addAll(setEntitlementBox(), setSchedulingTermsBox(), setServiceLevelBox(), setStatusBox(), loadSupportedBox());
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
            updateDetails();
        });
        vBox.getChildren().addAll(label, comboBox);
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

    private Node setIssueBox() {
        VBox vBox = new VBox(4);
        Label lblIssue = new Label("Issue:");
        TextArea textAreaIssue = TextAreaFx.of(true, 200, 16, 5);
        textAreaIssue.setPrefWidth(980);
        textAreaIssue.setText(noteModel.getCurrentNote().issueProperty().get());
        ListenerFx.addFocusListener(textAreaIssue, "Issue field", noteModel.getCurrentNote().issueProperty(), noteModel.statusLabelProperty());
        vBox.getChildren().addAll(lblIssue, textAreaIssue);
        return vBox;
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }
}
