package com.L2.mvci_note.components;

import com.L2.dto.PartDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Builder;

public class PartTableView implements Builder<TableView<PartDTO>> {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private TableView<PartDTO> tableView;

    public PartTableView(NoteView noteView) {
        this.noteView =  noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    @SuppressWarnings("unchecked")
    public TableView<PartDTO> build() {
        this.tableView = TableViewFx.of(PartDTO.class);
        tableView.setItems(noteModel.getCurrentNote().getSelectedPartOrder().getParts()); // Set the ObservableList here
        tableView.getColumns().addAll(col1(),col2(),col3());
        tableView.setPlaceholder(new Label(""));
        tableView.setPrefHeight(160);
        // auto selector
        TableView.TableViewSelectionModel<PartDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) noteModel.getCurrentNote().getSelectedPartOrder().setSelectedPart(newSelection);
        });
        return tableView;
    }

    private TableColumn<PartDTO, String> col1() {
        TableColumn<PartDTO, String> col = TableColumnFx.stringTableColumn(PartDTO::partNumberProperty,"Part Number");
        col.setStyle("-fx-alignment: center-left");
        return col;
    }

    private TableColumn<PartDTO, String> col2() {
        TableColumn<PartDTO, String> col = TableColumnFx.stringTableColumn(PartDTO::partDescriptionProperty,"Part Description");
        col.setStyle("-fx-alignment: center-left");
        return col;
    }

    private TableColumn<PartDTO, Integer> col3() {
        TableColumn<PartDTO, Integer> col = TableColumnFx.integerTableColumn(PartDTO::partQuantityProperty,"Qty");
        col.setStyle("-fx-alignment: center-right");
        return col;
    }
}
