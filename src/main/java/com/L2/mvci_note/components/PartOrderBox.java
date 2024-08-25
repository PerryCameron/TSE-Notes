package com.L2.mvci_note.components;

import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import com.L2.widgetFx.TextFieldFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PartOrderBox extends VBox {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private final PartOrderDTO partOrderDTO;
    private final TextField partNameTextField;

    public PartOrderBox(NoteView noteView, PartOrderDTO partOrderDTO) {
        this.noteView = noteView;
        this.noteModel = new NoteModel();
        this.partOrderDTO = partOrderDTO;
        this.partNameTextField = createPartOrderText();
        this.getStyleClass().add("decorative-hbox");
        this.setPadding(new Insets(5, 5, 10, 5));

        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(buildTable());
        this.setSpacing(5);
        this.getChildren().addAll(setLabel(), hBox);
    }

    private TextField createPartOrderText() {
        TextField textField = TextFieldFx.of(250, "Order Number");
        textField.textProperty().set(partOrderDTO.getOrderNumber());
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                partOrderDTO.setOrderNumber(textField.getText());
            }
        });
        return textField;
    }

    private Node setLabel() {
        HBox hBox = new HBox(5);
        Label label = new Label("Part Order");
        label.setPadding(new Insets(0, 0, 2, 5));
        if(partOrderDTO.getOrderNumber() == "" || partOrderDTO.getOrderNumber() == null) {
            hBox.getChildren().add(partNameTextField);
        } else {
            label.setText("Part Order: " + partOrderDTO.getOrderNumber());
            hBox.getChildren().add(label);
        }
        label.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                hBox.getChildren().clear();
                hBox.getChildren().add(partNameTextField);
            }
        });
        partOrderDTO.orderNumberProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                label.setText("Part Order: " + newValue);
                hBox.getChildren().clear();
                hBox.getChildren().add(label);
            }
        });
        return hBox;
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
