package com.L2.mvci.bom;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class BomView implements Builder<Region> {
    private final Consumer<BomMessage> action;
    private final BomModel bomModel;

    public BomView(BomModel bomModel, Consumer<BomMessage> action) {
        this.bomModel = bomModel;
        this.action = action;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("base-vbox");
        borderPane.setLeft(new Label("Left"));
        borderPane.setCenter(new Label("Center"));
        borderPane.setRight(new Label("Right"));
        borderPane.setBottom(new Label("Bottom"));
        borderPane.setTop(new Label("Top"));
        return borderPane;
    }
}
