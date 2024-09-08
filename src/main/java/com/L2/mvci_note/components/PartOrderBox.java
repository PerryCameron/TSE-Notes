package com.L2.mvci_note.components;

import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.mvci_note.NoteMessage;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PartOrderBox extends VBox {

    private final PartOrderDTO partOrderDTO;
    private final TextField partNameTextField;
    private final NoteModel noteModel;
    private final NoteView noteView;

    public PartOrderBox(PartOrderDTO partOrderDTO, NoteView noteView) {
        this.partOrderDTO = partOrderDTO;
        this.noteModel = noteView.getNoteModel();
        this.noteView = noteView;
        this.partNameTextField = createPartOrderText();
        this.getStyleClass().add("decorative-hbox");
        this.setPadding(new Insets(5, 5, 10, 5));
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(buildTable(), menu());
        this.setSpacing(5);
        this.getChildren().addAll(toolbar(), hBox);
    }

    private Node menu() {
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.setPrefWidth(300);

        Button addPartButton = ButtonFx.utilityButton( () -> {
            partOrderDTO.getParts().add(new PartDTO());
        }, "Add Part", "/images/create-16.png");

        Button deleteButton = ButtonFx.utilityButton( () -> {
            partOrderDTO.getParts().remove(partOrderDTO.getSelectedPart());
        }, "Delete", "/images/delete-16.png");
        TextField textField = TextFieldFx.of(250, "Search");
        vBox.getChildren().addAll(textField, addPartButton, deleteButton);
        return vBox;
    }

    private TextField createPartOrderText() {
        TextField textField = TextFieldFx.of(250, "Part Order Number");
        textField.textProperty().set(partOrderDTO.getOrderNumber());
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                partOrderDTO.setOrderNumber(textField.getText());
                noteView.getAction().accept(NoteMessage.LOG_ORDER_NUMBER_CHANGE);
            }
        });
        return textField;
    }

    private Node toolbar() {
        HBox outerBox = new HBox();
        HBox hBox = new HBox(5);
        outerBox.getChildren().addAll(hBox, createTrashButton());
        outerBox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label("Part Order");
        label.setPadding(new Insets(0, 0, 2, 5));
        if(partOrderDTO.getOrderNumber().isEmpty() || partOrderDTO.getOrderNumber() == null) {
            hBox.getChildren().add(partNameTextField);
        } else {
            label.setText("Part Order: " + partOrderDTO.getOrderNumber());
            hBox.getChildren().add(label);
        }
        label.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                hBox.getChildren().remove(label);
                hBox.getChildren().add(partNameTextField);
            }
        });
        partOrderDTO.orderNumberProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                label.setText("Part Order: " + newValue);
                hBox.getChildren().remove(partNameTextField);
                hBox.getChildren().add(label);
            }
        });
        return outerBox;
    }

    private Node createTrashButton() {
        HBox iconBox = HBoxFx.iconBox();
        Button deleteButton = ButtonFx.utilityButton( () -> {
            noteModel.getCurrentNote().getPartOrders().remove(partOrderDTO);
            if (this.getParent() instanceof VBox parent) {
                parent.getChildren().remove(this);
            }
        }, "Delete PO", "/images/delete-16.png");

        Button copyButton = ButtonFx.utilityButton( () -> {
            noteModel.getCurrentNote().setSelectedPartOrder(partOrderDTO);
            noteView.getAction().accept(NoteMessage.COPY_PART_ORDER);
            flashBorder();
        }, "Copy", "/images/copy-16.png");
        iconBox.getChildren().addAll(copyButton, deleteButton);
        return iconBox;
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
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partNumberProperty,"Part Number");
        col.setStyle("-fx-alignment: center-left");
        return col;
    }

    private TableColumn<PartDTO, String> col2() {
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partDescriptionProperty,"Part Description");
        col.setStyle("-fx-alignment: center-left");
        return col;
    }

    private TableColumn<PartDTO, String> col3() {
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partQuantityProperty,"Qty");
        col.setStyle("-fx-alignment: center-left");
        col.setMaxWidth(70.0);
        return col;
    }

    public void flashBorder() {
        this.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(event -> this.setStyle("")); // Reset the style
        pause.play();
    }
}
