package com.L2.mvci_note.components;

import com.L2.dto.EntitlementDTO;
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
    public TableView<PartDTO> build() {
        this.tableView = TableViewFx.of(PartDTO.class);
        tableView.setItems(noteModel.getCurrentNote().getParts()); // Set the ObservableList here
        tableView.getColumns().add(col1());
        tableView.setPlaceholder(new Label(""));
        // auto selector
        TableView.TableViewSelectionModel<PartDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) noteModel.getCurrentNote().setSelectedPart(newSelection);
//            settingsModel.gettFEntitlement().setText(newSelection == null ? "" : newSelection.getName());
//            settingsModel.gettFInclude().setText(newSelection == null ? "" : newSelection.getIncludes());
//            settingsModel.gettFIncludeNot().setText(newSelection == null ? "" : newSelection.getNotIncludes());
        });


        return tableView;
    }

    private TableColumn<PartDTO, String> col1() {
        TableColumn<PartDTO, String> col = TableColumnFx.stringTableColumn(PartDTO::partNumberProperty,"Part Number");
        col.setStyle("-fx-alignment: center");
        return col;
    }
}
