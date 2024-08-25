package com.L2.mvci_note.components;

import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import com.L2.widgetFx.TextFieldFx;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class PartOrderBox extends TitledPane {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private final PartOrderDTO partOrderDTO;


    public PartOrderBox(NoteView noteView, PartOrderDTO partOrderDTO) {
        this.noteView = noteView;
        this.noteModel = new NoteModel();
        this.partOrderDTO = partOrderDTO;
        this.setText("Part Order");
        this.setCollapsible(false);
        this.getStyleClass().add("titledPane");
        TextField tf1 = TextFieldFx.of(250, "Order Number");
        tf1.textProperty().set(partOrderDTO.getOrderNumber());
        tf1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                partOrderDTO.setOrderNumber(newValue);
            }
        });
        partOrderDTO.orderNumberProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.setText("Part Order: " + newValue);
            }
        });
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(buildTable(), tf1);
        this.setContent(hBox);
    }
    
    @SuppressWarnings("unchecked")
    public TableView<PartDTO> buildTable() {
        TableView<PartDTO> tableView = TableViewFx.of(PartDTO.class);
        tableView.setItems(partOrderDTO.getParts()); // Set the ObservableList here
        tableView.getColumns().addAll(col1(),col2(),col3());
        tableView.setPlaceholder(new Label(""));
        tableView.setPrefHeight(160);
        // auto selector
        TableView.TableViewSelectionModel<PartDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) partOrderDTO.setSelectedPart(newSelection);
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
