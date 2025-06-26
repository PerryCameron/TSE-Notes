package com.L2.widgetFx;

import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteModel;
import com.L2.static_tools.ImageResources;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class SparesTableViewFx {
    public static TableView<SparesDTO> createTableView(NoteModel noteModel) {
        TableView<SparesDTO> tableView = TableViewFx.of(SparesDTO.class);
        tableView.setItems(noteModel.getSearchedParts());
        tableView.getColumns().addAll(Arrays.asList(col1(), col2(), catalogue()));
        tableView.setPrefHeight(200);
        return tableView;
    }

    private static TableColumn<SparesDTO, String> col1() {
        // Define options and default value
        TableColumn<SparesDTO, String> col = TableColumnFx.stringTableColumnSimple(
                SparesDTO::getSpareItem,
                "Spare Part"
        );
        col.setStyle("-fx-alignment: center-left");
        col.setMinWidth(165);
        col.setPrefWidth(165);
        col.setMaxWidth(165);
        makeTextSelectable(col);
        return col;
    }

    private static TableColumn<SparesDTO, String> col2() {
        // Define options and default value
        TableColumn<SparesDTO, String> col = TableColumnFx.stringTableColumnSimple(
                SparesDTO::getSpareDescription,
                "Description"
        );
        col.setStyle("-fx-alignment: center-left");
        col.setMinWidth(500);
        col.setPrefWidth(500);
        col.setMaxWidth(500);
        makeTextSelectable(col);
        return col;
    }

    private static TableColumn<SparesDTO, Boolean> catalogue() {
        TableColumn<SparesDTO, Boolean> catalogueCol = new TableColumn<>("In catalogue");
        catalogueCol.setPrefWidth(125);
        catalogueCol.setMaxWidth(125);
        catalogueCol.setMinWidth(125);

        // Define the cell factory
        catalogueCol.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                // Center the content in the cell
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Boolean isArchived, boolean empty) {
                super.updateItem(isArchived, empty);
                if (empty || isArchived == null) {
                    setGraphic(null); // No image if empty or null
                } else {
                    imageView.setImage(isArchived ? ImageResources.NO : ImageResources.YES);
                    imageView.setFitWidth(16);
                    imageView.setFitHeight(16);
                    setGraphic(imageView); // Display the appropriate icon
                }
            }
        });
        // Bind the archived property to the column's value
        catalogueCol.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().getArchived()));
        return catalogueCol;
    }

    // makes it so you can highlight text and still select the row
    private static void makeTextSelectable(TableColumn<SparesDTO, String> col) {
        col.setCellFactory(column -> new TableCell<>() {
            private final TextField textField = new TextField();
            {
                textField.setEditable(false);
                textField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                textField.setFocusTraversable(false);
                // Allow row selection by forwarding mouse events
                textField.setOnMouseClicked(event -> {
                    TableRow<?> row = getTableRow();
                    if (row != null && !row.isEmpty()) {
                        getTableView().getSelectionModel().select(row.getIndex());
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    textField.setText(item);
                    setGraphic(textField);
                }
            }
        });
    }
}
