package com.L2.mvci_notelist.components;

import com.L2.dto.NoteDTO;
import com.L2.dto.NoteDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_notelist.NoteListModel;
import com.L2.mvci_notelist.NoteListView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

import java.util.Arrays;

public class NotesTable implements Component<Region> {

    private final NoteListView noteListView;
    private TableView<NoteDTO> tableView;

    public NotesTable(NoteListView noteListView) {
        this.noteListView = noteListView;
    }



    @Override
    public TableView<NoteDTO> build() {
        this.tableView = TableViewFx.of(NoteDTO.class);
        tableView.setItems(noteListView.getNoteListModel().getNotes()); // Set the ObservableList here
//        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.setEditable(true);
        tableView.getColumns().addAll(Arrays.asList(col1(),col2(),col3()));
        tableView.setPlaceholder(new Label(""));
        tableView.setPrefHeight(160);
        // Key event for Tab navigation
        // Handle key events for the TableView, only when focused


        // auto selector
        TableView.TableViewSelectionModel<NoteDTO> selectionModel = tableView.getSelectionModel();

        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) noteListView.getNoteListModel()(newSelection);
//            noteModel.getBoundNote().setSelectedPartOrder(partOrderDTO);
//            noteModel.getBoundNote().getSelectedPartOrder().setSelectedPart(newSelection);
        });
        return tableView;
    }

    private TableColumn<NoteDTO, String> col1() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::callInPersonProperty,"Caller");
        col.setStyle("-fx-alignment: center-left");
//        col.setOnEditCommit(event -> {
//            noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart().setPartNumber(event.getNewValue());
//            noteView.getAction().accept(NoteMessage.UPDATE_PART);
//        });
        return col;
    }

    private TableColumn<NoteDTO, String> col2() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::serialNumberProperty,"Serial");
        col.setStyle("-fx-alignment: center-left");
//        col.setOnEditCommit(event -> {
//            noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart().setPartDescription(event.getNewValue());
//            noteView.getAction().accept(NoteMessage.UPDATE_PART);
//        });
        return col;
    }

    private TableColumn<NoteDTO, String> col3() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::modelNumberProperty,"Model");
        col.setStyle("-fx-alignment: center-left");
//        col.setOnEditCommit(event -> {
//            noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart().setPartQuantity(event.getNewValue());
//            noteView.getAction().accept(NoteMessage.UPDATE_PART);
//        });
        col.setMaxWidth(70.0);
        return col;
    }

    @Override
    public void flash() {

    }

    @Override
    public void refreshFields() {

    }
}
