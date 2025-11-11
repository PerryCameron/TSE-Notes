package com.L2.mvci.bom;

import com.L2.mvci.bom.components.BomTreeTableView;
import com.L2.mvci.bom.components.ComponentTableView;
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
        root.getStyleClass().add("base-vbox");
        root.setPadding(new Insets(10, 10, 0, 10));
        root.getChildren().addAll(navigation(), new BomTreeTableView(bomModel).build());
        return root;
    }

    private Node navigation() {
        HBox hBox = new HBox(10);
        bomModel.setStackPane(new StackPane());
        Node buttonStack = buttonStack(bomModel.getStackPane(),
                bomFindBox(),
                bomSearchBox(),
                infoBox());
        hBox.getChildren().addAll(buttonStack, bomModel.getStackPane());
        HBox.setHgrow(bomModel.getStackPane(), Priority.ALWAYS);  // Ensure the StackPane grows to fill the HBox
        return hBox;
    }

    private Node infoBox() {
        return new HBox(10);
    }

    private Node bomFindBox() {
        HBox hBox = HBoxFx.of(Pos.CENTER, new Insets(10,10,10,10));
        hBox.getStyleClass().add("decorative-hbox");
        hBox.getChildren().addAll(bomTextField(), new LevelPieChart(bomModel).build());
        return hBox;
    }

    private Node bomTextField() {
        HBox hBox = new HBox(10);
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

    private Node bomSearchBox() {
        VBox vBox = new VBox(10);
        bomModel.setComponentTable(new ComponentTableView(bomModel).build());
        vBox.getChildren().addAll(searchBomTextField(), bomModel.getComponentTable());
        return vBox;
    }

    private Node searchBomTextField() {
        HBox hBox = new HBox(10);
        hBox.getStyleClass().add("decorative-hbox");
        TextField textField = TextFieldFx.of(200, "");
        textField.textProperty().bindBidirectional(bomModel.searchInBomProperty());
        Button button = ButtonFx.of("Search", 100, "app-button");
        button.setOnAction(event -> {
            if (!bomModel.searchInBomProperty().get().isEmpty())
                action.accept(BomMessage.SEARCH_CURRENT);
        });
        hBox.getChildren().addAll(textField, button);
        return hBox;
    }

    private Node buttonStack(StackPane stackPane, Node bomBox, Node searchBox, Node infoBox) {
        VBox buttonStack = new VBox(2);
        buttonStack.setMinWidth(150);
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton bomButton = ButtonFx.toggleof("BOM", 150, toggleGroup);
        ToggleButton searchButton = ButtonFx.toggleof("Search BOM", 150, toggleGroup);
        ToggleButton infoButton = ButtonFx.toggleof("Part Info", 150, toggleGroup);
        // Add all buttons to the VBox first
        buttonStack.getChildren().addAll(bomButton, searchButton, infoButton);
        // Set default content in the StackPane
        // Assume familyPane, notePane, keywordsPane, infoPane, photoPane are already created
        stackPane.getChildren().addAll(bomBox, searchBox);
        bomBox.setVisible(true);
        searchBox.setVisible(false);
        // Set the default selected button AFTER adding to the scene graph
        bomButton.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                toggleGroup.selectToggle(oldToggle);  // Prevent deselecting by re-selecting the old toggle
                return;
            }
            // Hide all panes
            bomBox.setVisible(false);
            searchBox.setVisible(false);
            // Show the selected pane
            if (newToggle == null || newToggle == bomButton) {
                bomBox.setVisible(true);
            } else if (newToggle == searchButton) {
                searchBox.setVisible(true);
            }
        });
        return buttonStack;
    }
}
