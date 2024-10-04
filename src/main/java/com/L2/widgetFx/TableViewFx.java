package com.L2.widgetFx;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TableViewFx {
    public static <T> TableView<T> of(Class<T> objectClass, double prefHeight) {
        TableView<T> tableView = new TableView<>();
        HBox.setHgrow(tableView, Priority.ALWAYS);
        tableView.setPrefHeight(prefHeight);
        tableView.setFixedCellSize(30);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return tableView;
    }

    public static <T> TableView<T> of(Class<T> objectClass) {
        TableView<T> tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        HBox.setHgrow(tableView, Priority.ALWAYS);
        tableView.setFixedCellSize(30);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return tableView;
    }
}
