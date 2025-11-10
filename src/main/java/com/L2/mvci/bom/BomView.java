package com.L2.mvci.bom;

import com.L2.mvci.bom.components.BomTreeTableView;
import com.L2.mvci.bom.components.LevelPieChart;
import com.L2.widgetFx.TextFieldFx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
        hBox.getStyleClass().add("decorative-hbox");
        hBox.setPadding(new Insets(5, 0, 5, 5));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(searchBox(), new LevelPieChart(bomModel).build());
        return hBox;
    }

    private Node searchBox() {
        VBox vBox = new VBox(10);
        vBox.getStyleClass().add("search-box");
        vBox.getChildren().addAll(bomTextField(), searchBomTextField());
        return vBox;
    }

    private Node bomTextField() {
        HBox hBox = new HBox(10);
        TextField textField = TextFieldFx.of(200, "Part Number");
        textField.textProperty().bindBidirectional(bomModel.searchComponentProperty());
        Button button = new Button("Find BOM");
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


}
