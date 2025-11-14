package com.L2.mvci.bom;

import com.L2.mvci.bom.components.BomTreeTableView;
import com.L2.mvci.bom.components.ComponentTableView;
import com.L2.mvci.bom.components.LevelPieChart;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.HBoxFx;
import com.L2.widgetFx.LabelFx;
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
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 5, 5, 5));
        hBox.getStyleClass().add("decorative-hbox2");
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
        HBox hBox = HBoxFx.of(Pos.CENTER, new Insets(10,10,10,10));
        hBox.setSpacing(30);
        hBox.getStyleClass().add("horizontal-tab-body-hbox");

        hBox.getChildren().add(bomPartInfoBox());
        return hBox;
    }

    private Node bomPartInfoBox() {
        VBox vBox = new VBox(5.0);
        Node item = LabelFx.boundLabel("Part Number", bomModel.selectedComponent.itemProperty());
        Node itemId = LabelFx.boundLabel("Part ID", bomModel.selectedComponent.itemIdProperty());
        Node level = LabelFx.boundLabel("Level", bomModel.selectedComponent.levelProperty());
        Node description = LabelFx.boundLabel("Description", bomModel.selectedComponent.descriptionProperty());
        Node revision = LabelFx.boundLabel("Revision", bomModel.selectedComponent.revisionProperty());
        Node uom = LabelFx.boundLabel("Unit Of Measurement", bomModel.selectedComponent.uomProperty());
        Node qty = LabelFx.boundLabel("Quantity", bomModel.selectedComponent.quantityProperty());
        Node type = LabelFx.boundLabel("Item Type", bomModel.selectedComponent.itemTypeProperty());
        Node referenceDesignator = LabelFx.boundLabel("Refrence Designator", bomModel.selectedComponent.refDesProperty());
        vBox.getChildren().addAll(item, itemId, level, description, revision, uom, qty, type, referenceDesignator);
        return vBox;
    }

    private Node bomFindBox() {
        HBox hBox = HBoxFx.of(Pos.CENTER, new Insets(10,10,10,10));
        hBox.setSpacing(30);
        hBox.getStyleClass().add("horizontal-tab-body-hbox");
        hBox.getChildren().addAll(bomTextField(), new LevelPieChart(bomModel).build());
        return hBox;
    }

    private Node bomTextField() {
        HBox hBox = new HBox(5.0);
        hBox.setAlignment(Pos.CENTER);
        TextField textField = TextFieldFx.of(200, "Part Number");
        textField.textProperty().bindBidirectional(bomModel.searchComponentProperty());
        Button button = ButtonFx.of("Find Bom", 100, "app-button");
        button.setOnAction(event -> {
            if (!bomModel.searchComponentProperty().get().isEmpty() || bomModel.searchComponentProperty().get() != null)
                action.accept(BomMessage.SEARCH);
        });
        hBox.getChildren().addAll(textField, button);
        return hBox;
    }

    private Node bomSearchBox() {
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.getStyleClass().add("horizontal-tab-body-hbox");
        bomModel.setComponentTable(new ComponentTableView(bomModel).build());
        vBox.getChildren().addAll(searchBomTextField(), bomModel.getComponentTable());
        return vBox;
    }

    private Node searchBomTextField() {
        HBox hBox = HBoxFx.of(new Insets(5,5,5,5), 10.0);
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
        ToggleButton bomTab = ButtonFx.TabOf("BOM", 150, toggleGroup);
        ToggleButton searchTab = ButtonFx.TabOf("Search BOM", 150, toggleGroup);
        ToggleButton infoTab = ButtonFx.TabOf("Part Info", 150, toggleGroup);
        buttonStack.getChildren().addAll(bomTab, searchTab, infoTab);
        stackPane.getChildren().addAll(bomBox, searchBox, infoBox);
        bomBox.setVisible(true);
        searchBox.setVisible(false);
        infoBox.setVisible(false);
        // Set the default selected button AFTER adding to the scene graph
        bomTab.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                toggleGroup.selectToggle(oldToggle);  // Prevent deselecting by re-selecting the old toggle
                return;
            }
            // Hide all panes
            bomBox.setVisible(false);
            searchBox.setVisible(false);
            infoBox.setVisible(false);
            // Show the selected pane
            if (newToggle == null || newToggle == bomTab) {
                bomBox.setVisible(true);
            } else if (newToggle == searchTab) {
                searchBox.setVisible(true);
            } else if (newToggle == infoTab) {
                infoBox.setVisible(true);
            }
        });
        return buttonStack;
    }
}
