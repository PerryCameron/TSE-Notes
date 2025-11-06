package com.L2.mvci.bom;

import com.L2.mvci.bom.components.BomTreeTableView;
import com.L2.mvci.notelist.NoteListMessage;
import com.L2.widgetFx.TextFieldFx;
import com.L2.widgetFx.TitleBarFx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
        VBox vBox = new VBox();
        HBox hBox = new HBox(10);
        vBox.getStyleClass().add("decorative-hbox");
        Button[] buttons = new Button[]{};
        vBox.getChildren().addAll(TitleBarFx.of("Part/Model number", buttons), hBox);
        hBox.setPadding(new Insets(5, 0, 5, 5));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(searchBox(), searchButton());
        return vBox;
    }

    private Node searchBox() {
        TextField textField = TextFieldFx.of(200, "Find BOM");
        textField.textProperty().bindBidirectional(bomModel.searchComponentProperty());
        return textField;
    }

    private Node searchButton() {
        Button button = new Button("Search");
        button.setOnAction(event -> {
            if(!bomModel.searchComponentProperty().get().isEmpty())
                action.accept(BomMessage.SEARCH);
        });
        return button;
    }
}
