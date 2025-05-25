package com.L2.widgetFx;

import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci_note.NoteModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;

public class SparesTableViewFx {
    public static TableView<SparesDTO> createTableView(NoteModel noteModel) {
        TableView<SparesDTO> tableView = TableViewFx.of(SparesDTO.class);
        tableView.setItems(noteModel.getSearchedParts());
        tableView.getColumns().addAll(Arrays.asList(col1(), col2(), col3()));
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
        col.setMinWidth(125);
        col.setPrefWidth(125);
        col.setMaxWidth(125);
        return col;
    }

    private static TableColumn<SparesDTO, String> col2() {
        // Define options and default value
        TableColumn<SparesDTO, String> col = TableColumnFx.stringTableColumnSimple(
                SparesDTO::getSpareDescription,
                "Description"
        );
        col.setStyle("-fx-alignment: center-left");
        col.setMinWidth(540);
        col.setPrefWidth(540);
        col.setMaxWidth(540);
        return col;
    }

    private static TableColumn<SparesDTO, String> col3() {
        // Define options and default value
        TableColumn<SparesDTO, String> col = TableColumnFx.stringTableColumnSimple(
                SparesDTO::isArchived,
                "In catalogue"
        );
        col.setStyle("-fx-alignment: center");
        col.setMinWidth(125);
        col.setPrefWidth(125);
        col.setMaxWidth(125);
        return col;
    }
}
