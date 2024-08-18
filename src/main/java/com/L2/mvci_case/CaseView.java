package com.L2.mvci_case;

import com.L2.widgetFx.VBoxFx;
import javafx.geometry.HPos;
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
        VBox vBox = VBoxFx.of(1024,768,true, true);
        vBox.setPadding(new Insets(10,0,0,10));
        vBox.setSpacing(10);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(setBox1Info(),setBox2Info());
        vBox.getChildren().addAll(hBox, setIssueBox());
        return vBox;
    }

    private Node setBox1Info() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10)); // Optional, to add some padding around the GridPane

        // Define Labels
        Label label1 = new Label("WO-");
        Label label2 = new Label("Model:");
        Label label3 = new Label("Serial:");
        Label label4 = new Label("Call in person:");
        Label label5 = new Label("Phone:");
        Label label6 = new Label("Email:");
        Label label7 = new Label("Case:");

        // Define TextFields
        TextField tfWO = new TextField();
        tfWO.textProperty().bindBidirectional(caseModel.getCurrentCase().workOrderProperty());
        TextField tfModel = new TextField();
        tfModel.textProperty().bindBidirectional(caseModel.getCurrentCase().modelNumberProperty());
        TextField tfSerial = new TextField();
        tfSerial.textProperty().bindBidirectional(caseModel.getCurrentCase().serialNumberProperty());
        TextField tfCallInPerson = new TextField();
        tfCallInPerson.textProperty().bindBidirectional(caseModel.getCurrentCase().callInPersonProperty());
        TextField tfPhone = new TextField();
        tfPhone.textProperty().bindBidirectional(caseModel.getCurrentCase().callInPhoneNumberProperty());
        TextField tfEmail = new TextField();
        tfEmail.textProperty().bindBidirectional(caseModel.getCurrentCase().callInEmailProperty());
        TextField tfCase = new TextField();
        tfCase.textProperty().bindBidirectional(caseModel.getCurrentCase().caseNumberProperty());

        // Add Labels to GridPane
        gridPane.add(label1, 0, 0);
        gridPane.add(label2, 0, 1);
        gridPane.add(label3, 0, 2);
        gridPane.add(label4, 0, 3);
        gridPane.add(label5, 0, 4);
        gridPane.add(label6, 0, 5);
        gridPane.add(label7, 2, 0);


        // Align Labels to the right
        GridPane.setHalignment(label1, HPos.RIGHT);
        GridPane.setHalignment(label2, HPos.RIGHT);
        GridPane.setHalignment(label3, HPos.RIGHT);
        GridPane.setHalignment(label4, HPos.RIGHT);
        GridPane.setHalignment(label5, HPos.RIGHT);
        GridPane.setHalignment(label6, HPos.RIGHT);
        GridPane.setHalignment(label7, HPos.RIGHT);

        // Add TextFields to GridPane
        gridPane.add(tfWO, 1, 0);
        gridPane.add(tfModel, 1, 1);
        gridPane.add(tfSerial, 1, 2);
        gridPane.add(tfCallInPerson, 1, 3);
        gridPane.add(tfPhone, 1, 4);
        gridPane.add(tfEmail, 1, 5);
        gridPane.add(tfCase, 3, 0);

        return gridPane;
    }

    private Node setBox2Info() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10)); // Optional, to add some padding around the GridPane

        // Define Labels and Controls
        Label lblActiveServiceContract = new Label("Entitlement:");
        ComboBox<String> cbServiceContract = new ComboBox<>();
        cbServiceContract.getItems().addAll( "Advantage Ultra","Advantage Plus","Advantage Prime", "None", "Warranty", "Warranty Extension");
        cbServiceContract.setValue("None"); // Default to "None"

        // Bind the valueProperty of the ComboBox to the activeServiceContractProperty of the CaseDTO
        cbServiceContract.valueProperty().bindBidirectional(caseModel.getCurrentCase().activeServiceContractProperty());

        // Optional: Add an action listener to observe changes
        cbServiceContract.setOnAction(e -> System.out.println("Active service contract: " + caseModel.getCurrentCase().getActiveServiceContract()));

        Label lblServiceLevel = new Label("Service Level:");
        ComboBox<String> cbServiceLevel = new ComboBox<>();
        cbServiceLevel.setPrefWidth(150);
        cbServiceLevel.getItems().addAll("4-Hour", "8-Hour", "Next Business Day");

        Label lblStatusOfUPS = new Label("Status of the UPS:");
        ComboBox<String> cbStatusOfUPS = new ComboBox<>();
        cbStatusOfUPS.setPrefWidth(150);
        cbStatusOfUPS.getItems().addAll("Online", "Bypass", "Offline");
        cbStatusOfUPS.setValue("Online"); // Default to "Online"

        cbStatusOfUPS.valueProperty().bindBidirectional(caseModel.getCurrentCase().upsStatusProperty());

        // Optional: Add an action listener to observe changes
        cbStatusOfUPS.setOnAction(e -> System.out.println("UPS Status: " + caseModel.getCurrentCase().getUpsStatus()));

        Label lblLoadSupported = new Label("Load Supported:");
        RadioButton rbLoadYes = new RadioButton("Yes");
        RadioButton rbLoadNo = new RadioButton("No");
        ToggleGroup tgLoadSupported = new ToggleGroup();
        rbLoadYes.setToggleGroup(tgLoadSupported);
        rbLoadNo.setToggleGroup(tgLoadSupported);
        rbLoadYes.setSelected(true); // Default to "Yes"

        caseModel.getCurrentCase().loadSupportedProperty().bindBidirectional(rbLoadYes.selectedProperty());

        rbLoadYes.setOnAction(e -> System.out.println("load supported set to " + caseModel.getCurrentCase().loadSupportedProperty().get()));
        rbLoadNo.setOnAction(e -> System.out.println("load supported set to " + caseModel.getCurrentCase().loadSupportedProperty().get()));

        // Add Labels and Controls to GridPane
        gridPane.add(lblActiveServiceContract, 0, 0);  // Moved to row 0
        gridPane.add(cbServiceContract, 1, 0, 2, 1);  // Moved to row 0

        gridPane.add(lblServiceLevel, 0, 1);  // Moved to row 1
        gridPane.add(cbServiceLevel, 1, 1, 2, 1);  // Moved to row 1

        gridPane.add(lblStatusOfUPS, 0, 2);  // Moved to row 2
        gridPane.add(cbStatusOfUPS, 1, 2, 2, 1);  // Moved to row 2

        gridPane.add(lblLoadSupported, 0, 3);  // Moved to row 3
        gridPane.add(rbLoadYes, 1, 3);  // Moved to row 3
        gridPane.add(rbLoadNo, 2, 3);  // Moved to row 3


        // Align Labels to the right
        GridPane.setHalignment(lblActiveServiceContract, HPos.RIGHT);
        GridPane.setHalignment(lblServiceLevel, HPos.RIGHT);
        GridPane.setHalignment(lblStatusOfUPS, HPos.RIGHT);
        GridPane.setHalignment(lblLoadSupported, HPos.RIGHT);


        return gridPane;
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
}
