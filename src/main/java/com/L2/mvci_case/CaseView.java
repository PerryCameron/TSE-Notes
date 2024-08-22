package com.L2.mvci_case;

import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import com.L2.dto.EntitlementDTO;
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

public class CaseView implements Builder<Region> {
    private final CaseModel caseModel;
    Consumer<CaseMessage> action;

    public CaseView(CaseModel caseModel, Consumer<CaseMessage> m) {
        this.caseModel = caseModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(setCenter());
        return borderPane;
    }

    private Node setCenter() {
        VBox vBox = VBoxFx.of(1024, 768, true, true);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.setSpacing(10);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(setBox1Info(), setBox2Info(), setServicePlanDetails());
        vBox.getChildren().addAll(hBox, setIssueBox());
        return vBox;
    }

    private Node setServicePlanDetails() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15, 0, 0, 40));
        caseModel.setPlanDetailsBox(vBox);
        updateDetails();
        return vBox;
    }

    private void updateDetails() {
        VBox vBox = caseModel.getPlanDetailsBox();
        vBox.getChildren().clear();
        Label label = new Label(caseModel.getCurrentEntitlement().getName());
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ff0000;");
        Label label1 = new Label("Includes");
        label1.getStyleClass().add(Styles.TEXT_BOLD);
        String[] includes = caseModel.getCurrentEntitlement().getIncludes().split("\\R");
        String[] notIncludes = caseModel.getCurrentEntitlement().getNotIncludes().split("\\R");
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
        TextField tf1 = TextFieldFx.of(150, 30, "Work Order", caseModel.getCurrentCase().workOrderProperty());
        TextField tf2 = TextFieldFx.of(150, 30, "Case", caseModel.getCurrentCase().caseNumberProperty());
        TextField tf3 = TextFieldFx.of(150, 30, "Model", caseModel.getCurrentCase().modelNumberProperty());
        TextField tf4 = TextFieldFx.of(150, 30, "Serial", caseModel.getCurrentCase().serialNumberProperty());
        TextField tf5 = TextFieldFx.of(150, 30, "Call-in Contact", caseModel.getCurrentCase().callInPersonProperty());
        TextField tf6 = TextFieldFx.of(150, 30, "Call-in Phone", caseModel.getCurrentCase().callInPhoneNumberProperty());
        TextField tf7 = TextFieldFx.of(150, 30, "Call-in Email", caseModel.getCurrentCase().callInEmailProperty());
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
        toggleSwitch.selectedProperty().bindBidirectional(caseModel.getCurrentCase().loadSupportedProperty());
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
        comboBox.valueProperty().bindBidirectional(caseModel.getCurrentCase().upsStatusProperty());
        vBox.getChildren().addAll(label, comboBox);
        comboBox.setOnAction(e -> System.out.println("UPS Status: " + caseModel.getCurrentCase().getUpsStatus()));
        return vBox;
    }

    private Node setServiceLevelBox() {
        VBox vBox = new VBox(4);
        Label label = new Label("Service Level:");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        comboBox.getItems().addAll("4-Hour", "8-Hour", "Next Business Day");
        comboBox.valueProperty().bindBidirectional(caseModel.getCurrentCase().serviceLevelProperty());
        vBox.getChildren().addAll(label, comboBox);
        comboBox.setOnAction(e -> System.out.println("Service level: " + caseModel.getCurrentCase().getServiceLevel()));
        return vBox;
    }

    private Node setEntitlementBox() {
        VBox vBox = new VBox(4);
        // Define Labels and Controls
        Label label = new Label("Service Plan:");
        ComboBox<EntitlementDTO> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        // I would like to replace the hard coded entitlements below with the ArrayList  caseModel.getEntitlements() using the field EntitlementDTO::name
        comboBox.getItems().addAll(caseModel.getEntitlements());
        // Optional: Set a default value if needed
        if (!comboBox.getItems().isEmpty()) {
            comboBox.setValue(comboBox.getItems().get(0));  // Set the first item as selected by default
        }
        // Bind the valueProperty of the ComboBox to the activeEntitlementProperty of the CaseDTO
        comboBox.valueProperty().bindBidirectional(caseModel.currentEntitlementProperty());
        // Listener to update activeServiceContract when currentEntitlement changes
        caseModel.currentEntitlementProperty().addListener((obs, oldEntitlement, newEntitlement) -> {
            if (newEntitlement != null) {
                caseModel.getCurrentCase().setActiveServiceContract(newEntitlement.getName());
            } else {
                caseModel.getCurrentCase().setActiveServiceContract("");  // Or handle null appropriately
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
        textAreaIssue.textProperty().bindBidirectional(caseModel.getCurrentCase().issueProperty());
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

    public CaseModel getCaseModel() {
        return caseModel;
    }
}
