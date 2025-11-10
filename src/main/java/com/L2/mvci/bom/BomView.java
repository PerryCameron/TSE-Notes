package com.L2.mvci.bom;

import com.L2.mvci.bom.components.BomTreeTableView;
import com.L2.mvci.bom.components.LevelPieChart;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.HBoxFx;
import com.L2.widgetFx.TextFieldFx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class BomView implements Builder<Region> {
    private final Consumer<BomMessage> action;
    private final BomModel bomModel;
    private static final Logger logger = LoggerFactory.getLogger(BomView.class);

    public BomView(BomModel bomModel, Consumer<BomMessage> action) {
        this.bomModel = bomModel;
        this.action = action;
    }

    @Override
    public Region build() {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: red");
        root.getStyleClass().add("base-vbox");
        root.setPadding(new Insets(10, 10, 0, 10));
        root.getChildren().addAll(navigation(), new BomTreeTableView(bomModel).build());
        return root;
    }

    private Node navigation() {
        HBox hBox = new HBox(10);
        hBox.setStyle("-fx-background-color: #3498db;");//        hBox.getStyleClass().add("decorative-hbox");
        bomModel.setStackPane(new StackPane());
        Node buttonStack = buttonStack(bomModel.getStackPane(),
                new Pane(bomFindBox()),
                new Pane(new Label("Pane 2")));
        hBox.getChildren().addAll(buttonStack, bomModel.getStackPane());  // Key fix: Add the StackPane to the HBox
        return hBox;
    }

    private Node bomFindBox() {
        HBox hBox = HBoxFx.of(Pos.CENTER, new Insets(10,10,10,10));
        hBox.setStyle("-fx-background-color: purple;");
        hBox.getStyleClass().add("decorative-hbox");
        hBox.getChildren().addAll(bomTextField(), new LevelPieChart(bomModel).build());
        return hBox;
    }

    private Node bomTextField() {
        HBox hBox = new HBox(10);
        hBox.setStyle("-fx-background-color: green;");
        hBox.setAlignment(Pos.CENTER_LEFT);
        TextField textField = TextFieldFx.of(200, "Part Number");
        textField.textProperty().bindBidirectional(bomModel.searchComponentProperty());
        Button button = ButtonFx.of("Find Bom", 100, "app-button");
        button.setOnAction(event -> {
            if (!bomModel.searchComponentProperty().get().isEmpty())
                action.accept(BomMessage.SEARCH);
        });
        hBox.getChildren().addAll(textField, button);
        return hBox;
    }

    private Node searchBomTextField() {
        HBox hBox = new HBox(10);
        TextField textField = TextFieldFx.of(200, "");
        textField.textProperty().bindBidirectional(bomModel.searchInBomProperty());
        Button button = new Button("Search");
        button.setOnAction(event -> {
            if (!bomModel.searchInBomProperty().get().isEmpty())
                action.accept(BomMessage.SEARCH_CURRENT);
        });
        hBox.getChildren().addAll(textField, button);
        return hBox;
    }

    private Node buttonStack(StackPane stackPane, Node bomPane, Node searchPane) {
        VBox buttonStack = new VBox(2);
        buttonStack.setStyle("-fx-background-color: yellow;");
        buttonStack.setMinWidth(150);
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton bomButton = ButtonFx.toggleof("BOM", 150, toggleGroup); // If I click on a button it selects the correct pane, if I click on the button when it is selected it goes away. Nothing should happen when I click on a selected button.
        ToggleButton searchButton = ButtonFx.toggleof("Search", 150, toggleGroup);
        // Add all buttons to the VBox first
        buttonStack.getChildren().addAll(bomButton, searchButton);
        // Set default content in the StackPane
        // Assume familyPane, notePane, keywordsPane, infoPane, photoPane are already created
        stackPane.getChildren().addAll(bomPane, searchPane);
        bomPane.setVisible(true);
        searchPane.setVisible(false);
        // Set the default selected button AFTER adding to the scene graph
        bomButton.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            System.out.println("newToggle = " + newToggle + " oldToggle = " + oldToggle);
            if (newToggle == null) {
                toggleGroup.selectToggle(oldToggle);  // Prevent deselecting by re-selecting the old toggle
                return;
            }
            // Hide all panes
            bomPane.setVisible(false);
            searchPane.setVisible(false);
            // Show the selected pane
            if (newToggle == null || newToggle == bomButton) {
                bomPane.setVisible(true);
            } else if (newToggle == searchButton) {
                searchPane.setVisible(true);
            }
        });
        return buttonStack;
    }
}
