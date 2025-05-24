package com.L2.widgetFx;

import com.L2.dto.PartFx;
import javafx.application.Platform;
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


    public static void focusOnLastItem(TableView<PartFx> tableView) {
        tableView.layout();
        Platform.runLater(() -> {
            try {
                // Check if the TableView has items and columns
                if (!tableView.getItems().isEmpty() && !tableView.getColumns().isEmpty()) {
                    // Select the last row
                    int lastRowIndex = tableView.getItems().size() - 1;
                    tableView.getSelectionModel().clearAndSelect(lastRowIndex);

                    // Focus the last row and the last column
                    TableColumn<PartFx, ?> lastColumn = tableView.getColumns().get(tableView.getColumns().size() - 1);
                    tableView.getFocusModel().focus(lastRowIndex, lastColumn);

                    // Scroll to the last row to ensure it's visible
                    tableView.scrollTo(lastRowIndex);

                    // Request focus on the TableView to ensure it receives keyboard input
                    tableView.requestFocus();

                    // Debug output
                    System.out.println("Focused row: " + lastRowIndex + ", column: " + lastColumn.getText());
                } else {
                    System.out.println("Table view is empty or has no columns");
                }
            } catch (Exception e) {
                System.err.println("Error focusing last item: " + e.getMessage());
            }
        });
    }


}
