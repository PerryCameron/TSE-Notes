package com.L2.mvci_note.components;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.dto.EntitlementDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class ServicePlan implements Builder<Region> {


    private final NoteModel noteModel;
    private final NoteView noteView;

    public ServicePlan(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
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
}
