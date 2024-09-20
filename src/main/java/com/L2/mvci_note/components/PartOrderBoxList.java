package com.L2.mvci_note.components;

import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.interfaces.Component;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PartOrderBoxList implements Component<Region> {

    private final NoteModel noteModel;
    private final NoteView noteView;
    private VBox root;
    private TableView<PartDTO> tableView;
    private final Map<PartOrderDTO, VBox> partOrderMap = new HashMap<>();
    private boolean currentRowLocked = false;

    public PartOrderBoxList(NoteView noteView) {
        this.noteModel = noteView.getNoteModel();
        this.noteView = noteView;
    }

    @Override
    public Region build() {
        this.root = new VBox(10);
        // for each part order create a VBOX with stuff in it
        for(PartOrderDTO partOrderDTO: noteModel.getBoundNote().getPartOrders()) {
            root.getChildren().add(createPartOrderBox(partOrderDTO));
        }
        return root;
    }

    public Node createPartOrderBox(PartOrderDTO partOrderDTO) {
        VBox box = new VBox(10);
        partOrderMap.put(partOrderDTO, box);
        box.getStyleClass().add("decorative-hbox");
        box.setPadding(new Insets(5, 5, 10, 5));
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(buildTable(partOrderDTO), menu(partOrderDTO));
        box.setSpacing(5);
        box.getChildren().addAll(toolbar(partOrderDTO), hBox);
        return box;
    }

    private Node menu(PartOrderDTO partOrderDTO) {
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.setPrefWidth(300);
        Button addPartButton = ButtonFx.utilityButton(() -> {
            noteModel.setSelectedPartOrder(partOrderDTO);
            noteView.getAction().accept(NoteMessage.INSERT_PART);

            // Sort parts in reverse order
            partOrderDTO.getParts().sort(Comparator.comparing(PartDTO::getId).reversed());

            // Refresh the table view layout and focus
            tableView.layout();
            tableView.requestFocus();

            // Select row 0 and focus the first column
            tableView.getSelectionModel().select(0);
            tableView.getFocusModel().focus(0, tableView.getColumns().get(0));  // Focus the first column (index 0)

            // Edit the first cell in the first row
            tableView.edit(0, tableView.getColumns().get(0));  // Edit row 0, first column
        }, "Add Part", "/images/create-16.png");


        Button deleteButton = ButtonFx.utilityButton( () -> {
            noteModel.setSelectedPartOrder(partOrderDTO);
//            partOrderDTO.getParts().remove(partOrderDTO.getSelectedPart());
            partOrderDTO.getParts().remove(noteModel.getSelectedPart());
            noteView.getAction().accept(NoteMessage.DELETE_PART);
        }, "Delete", "/images/delete-16.png");
        TextField textField = TextFieldFx.of(250, "Search");
        vBox.getChildren().addAll(textField, addPartButton, deleteButton);
        return vBox;
    }

    private TextField createPartOrderText(PartOrderDTO partOrderDTO) {
        TextField textField = TextFieldFx.of(250, "Part Order Number");
        textField.textProperty().set(partOrderDTO.getOrderNumber());
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                noteModel.setSelectedPartOrder(partOrderDTO);
                partOrderDTO.setOrderNumber(textField.getText());
                noteView.getAction().accept(NoteMessage.UPDATE_PART_ORDER);
            }
        });
        return textField;
    }

    private Node toolbar(PartOrderDTO partOrderDTO) {
        HBox outerBox = new HBox();
        HBox hBox = new HBox(5);
        TextField partNameTextField = createPartOrderText(partOrderDTO);
        outerBox.getChildren().addAll(hBox, createButtons(partOrderDTO));
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
                noteView.getAction().accept(NoteMessage.UPDATE_PART_ORDER);
                hBox.getChildren().remove(partNameTextField);
                hBox.getChildren().add(label);
            }
        });
        return outerBox;
    }

    private Node createButtons(PartOrderDTO partOrderDTO) {
        HBox iconBox = HBoxFx.iconBox();
        Button deleteButton = ButtonFx.utilityButton( () -> {
            noteModel.setSelectedPartOrder(partOrderDTO);
            noteView.getAction().accept(NoteMessage.DELETE_PART_ORDER);
            noteModel.getBoundNote().getPartOrders().remove(partOrderDTO);
                root.getChildren().remove(partOrderMap.get(partOrderDTO));
        }, "Delete PO", "/images/delete-16.png");

        Button copyButton = ButtonFx.utilityButton( () -> {
            noteModel.setSelectedPartOrder(partOrderDTO);
            noteView.getAction().accept(NoteMessage.COPY_PART_ORDER);
            VBox vBox = partOrderMap.get(partOrderDTO);
            vBox.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.setStyle("")); // Reset the style
            pause.play();
        }, "Copy", "/images/copy-16.png");
        iconBox.getChildren().addAll(copyButton, deleteButton);
        return iconBox;
    }

    public TableView<PartDTO> buildTable(PartOrderDTO partOrderDTO) {
        this.tableView = TableViewFx.of(PartDTO.class);
        tableView.setItems(partOrderDTO.getParts()); // Set the ObservableList here
        tableView.setEditable(true);
        tableView.getColumns().addAll(Arrays.asList(col1(),col2(),col3()));
        tableView.setPlaceholder(new Label(""));
        tableView.setPrefHeight(160);

        // auto selector
        TableView.TableViewSelectionModel<PartDTO> selectionModel = tableView.getSelectionModel();

        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(!currentRowLocked) {
                if (newSelection != null)
                noteModel.setSelectedPart(newSelection);
                // below is too complex since you can only select one part anyway
                // partOrderDTO.setSelectedPart(newSelection);
                System.out.println("selected part: " + noteModel.getSelectedPart());
//                noteModel.getBoundNote().setSelectedPartOrder(partOrderDTO);
                noteModel.setSelectedPartOrder(partOrderDTO);
                System.out.println("Selected partOrder: " + noteModel.getSelectedPartOrder());
//                noteModel.getBoundNote().getSelectedPartOrder().setSelectedPart(newSelection);
            }
        });
        return tableView;
    }

    private TableColumn<PartDTO, String> col1() {
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partNumberProperty,"Part Number");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditStart(event -> {
            currentRowLocked = true;  // Lock the row
        });
        col.setOnEditCommit(event -> {
            noteModel.getSelectedPart().setPartNumber(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
            currentRowLocked = false;
        });
        // When the editing is canceled (e.g., pressing Escape)
        col.setOnEditCancel(event -> {
            currentRowLocked = false;  // Unlock the row
        });
        col.setMaxWidth(150);
        col.setPrefWidth(150);
        return col;
    }

    private TableColumn<PartDTO, String> col2() {
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partDescriptionProperty,"Part Description");
        col.setStyle("-fx-alignment: center-left");
        // When the cell starts being edited
        col.setOnEditStart(event -> {
            currentRowLocked = true;  // Lock the row
        });
        col.setOnEditCommit(event -> {
            noteModel.getSelectedPart().setPartDescription(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
            currentRowLocked = false;
        });
        // When the editing is canceled (e.g., pressing Escape)
        col.setOnEditCancel(event -> {
            currentRowLocked = false;  // Unlock the row
        });
        return col;
    }

    private TableColumn<PartDTO, String> col3() {
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partQuantityProperty,"Qty");
        col.setStyle("-fx-alignment: center-left");
        // When the cell starts being edited
        col.setOnEditStart(event -> {
            currentRowLocked = true;  // Lock the row
        });
        col.setOnEditCommit(event -> {
            noteModel.getSelectedPart().setPartQuantity(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
            currentRowLocked = false;
        });
        // When the editing is canceled (e.g., pressing Escape)
        col.setOnEditCancel(event -> {
            currentRowLocked = false;  // Unlock the row
        });
        col.setMaxWidth(70.0);
        return col;
    }

    @Override
    public void flash() {
        for(Map.Entry<PartOrderDTO, VBox> entry : partOrderMap.entrySet()) {
            VBox vBox = entry.getValue();
            vBox.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.setStyle("")); // Reset the style
            pause.play();
        }
    }

    @Override
    public void refreshFields() {
        root.getChildren().clear();
        noteModel.getBoundNote().getPartOrders().forEach((partOrderDTO) -> root.getChildren().add(createPartOrderBox(partOrderDTO)));
    }
}
