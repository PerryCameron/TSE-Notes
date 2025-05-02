package com.L2.mvci_note.components;

import atlantafx.base.controls.ToggleSwitch;
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

import java.util.*;

public class PartOrderBoxList implements Component<Region> {

    private final NoteModel noteModel;
    private final NoteView noteView;
    private VBox root;
    private final Map<PartOrderDTO, VBox> partOrderMap = new HashMap<>();

    public PartOrderBoxList(NoteView noteView) {
        this.noteModel = noteView.getNoteModel();
        this.noteView = noteView;
    }

    @Override
    public Region build() {
        this.root = new VBox(10);
        // for each part order create a VBOX with stuff in it
        for(PartOrderDTO partOrderDTO: noteModel.boundNoteProperty().get().getPartOrders()) {
            root.getChildren().add(createPartOrderBox(partOrderDTO));
        }
        return root;
    }

    public Node createPartOrderBox(PartOrderDTO partOrderDTO) {
        VBox box = new VBox(10);
        Map<String, TableColumn<PartDTO,String>> tableColumnMap = new HashMap<>();
        TableView<PartDTO> tableView = buildTable(partOrderDTO, tableColumnMap);
        box.setOnMouseEntered(event -> noteModel.selectedPartOrderProperty().set(partOrderDTO));
        box.setOnMouseExited(event -> tableView.getSelectionModel().clearSelection());
        partOrderMap.put(partOrderDTO, box);
        box.getStyleClass().add("decorative-hbox");
        box.setPadding(new Insets(5, 5, 10, 5));
        HBox hBox = new HBox(5);
        hBox.setPadding(new Insets(0, 5, 0, 0));
        hBox.getChildren().addAll(menu(tableView, partOrderDTO), tableView);
        box.setSpacing(5);
        box.getChildren().addAll(toolbar(partOrderDTO), hBox);
        return box;
    }

    private Node menu(TableView<PartDTO> tableView, PartOrderDTO partOrderDTO) {
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(5, 0, 5, 5));

        Button searchButton = ButtonFx.utilityButton(() -> {
            Alert alert = DialogueFx.searchAlert(noteView, tableView, partOrderDTO );
            alert.showAndWait();
                },"Search", "/images/search-16.png");

        Button addPartButton = ButtonFx.utilityButton(() -> {
            noteView.getAction().accept(NoteMessage.INSERT_PART);

            // Sort parts in reverse order
            partOrderDTO.getParts().sort(Comparator.comparing(PartDTO::getId).reversed());

            // Refresh the table view layout and focus
            tableView.layout();
            tableView.requestFocus();

            // Select row 0 and focus the first column
            tableView.getSelectionModel().select(0);
            tableView.getFocusModel().focus(0, tableView.getColumns().getFirst());  // Focus the first column (index 0)

            // Edit the first cell in the first row
            tableView.edit(0, tableView.getColumns().getFirst());  // Edit row 0, first column
        }, "Add Part", "/images/create-16.png");


        Button deleteButton = ButtonFx.utilityButton( () -> noteView.getAction().accept(NoteMessage.DELETE_PART), "Delete", "/images/delete-16.png");

        // Create the VBox from your method
        HBox lineTypeBox = lineTypeToggle(partOrderDTO);

        // Set a top margin (e.g., 10 pixels) on lineTypeBox
        VBox.setMargin(lineTypeBox, new Insets(20, 0, 0, 0));
        vBox.getStyleClass().add("inner-decorative-hbox");
        // Now add all nodes to the parent vBox
        vBox.getChildren().addAll(searchButton, addPartButton, deleteButton, lineTypeBox);
        return vBox;
    }

    // do we want to show a line type in the table?
    private HBox lineTypeToggle(PartOrderDTO partOrderDTO) {
        HBox vBox = new HBox(5);
        vBox.setPadding(new Insets(15, 5, 15, 5));
        Label label = new Label("Show Type");
        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.setSelected(partOrderDTO.showType());
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> partOrderDTO.showTypeProperty().set(observable.getValue()));
        vBox.getChildren().addAll(label, toggleSwitch);
        return vBox;
    }

    private TextField createPartOrderText(PartOrderDTO partOrderDTO) {
        TextField textField = TextFieldFx.of(250, "Part Order Number");
        textField.textProperty().set(partOrderDTO.getOrderNumber());
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
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
        HBox iconBox = HBoxFx.iconBox(10);
        Button deleteButton = ButtonFx.utilityButton( () -> {
            noteView.getAction().accept(NoteMessage.DELETE_PART_ORDER);
            noteModel.boundNoteProperty().get().getPartOrders().remove(partOrderDTO);
                root.getChildren().remove(partOrderMap.get(partOrderDTO));
        }, "Delete PO", "/images/delete-16.png");

        Button copyButton = ButtonFx.utilityButton( () -> {
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

    public TableView<PartDTO> buildTable(PartOrderDTO partOrderDTO, Map<String, TableColumn<PartDTO, String>> map) {
        TableView<PartDTO> tableView = TableViewFx.of(PartDTO.class);
        tableView.setItems(partOrderDTO.getParts()); // Set the ObservableList here
        tableView.setEditable(true);
        map.put("part-number", col1());
        map.put("line-type", col2());
        map.put("description", col3());
        map.put("quantity", col4());
        // set lineType visibility for the first time
        lineTypeIsShown(map, partOrderDTO.showTypeProperty().get(), tableView);
        // if showType changes then change visibility of lineType
        partOrderDTO.showTypeProperty().addListener((showType, oldValue, newValue) -> {
            tableView.getColumns().clear();
            lineTypeIsShown(map, showType.getValue(), tableView);
            noteView.getAction().accept(NoteMessage.UPDATE_PART_ORDER);
        });
        tableView.setPlaceholder(new Label(""));
        tableView.setPrefHeight(160);
        // auto selector
        TableView.TableViewSelectionModel<PartDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null)
                    noteModel.selectedPartProperty().set(newSelection);
        });
        return tableView;
    }

    private static void lineTypeIsShown(Map<String, TableColumn<PartDTO, String>> map, boolean showType, TableView<PartDTO> tableView) {
        TableColumn<PartDTO, String> col1 = map.get("part-number");
        TableColumn<PartDTO, String> col2 = map.get("line-type");
        TableColumn<PartDTO, String> col3 = map.get("description");
        TableColumn<PartDTO, String> col4 = map.get("quantity");
        if(showType) {
            tableView.getColumns().addAll(Arrays.asList(col1, col2, col3, col4));
        } else {
            tableView.getColumns().addAll(Arrays.asList(col1, col3, col4));
        }
    }

    private TableColumn<PartDTO, String> col1() {
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partNumberProperty,"Part Number");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteModel.selectedPartProperty().get().setPartNumber(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
        });
        col.setMinWidth(150);
        col.setPrefWidth(150);
        col.setMaxWidth(150);
        return col;
    }

    private TableColumn<PartDTO, String> col2() {
        // Define options and default value
        List<String> lineTypeOptions = Arrays.asList("Advanced Exchange", "Ship Only", "Return Only");
        String defaultLineType = "Advanced Exchange";
        TableColumn<PartDTO, String> col = TableColumnFx.comboBoxTableColumn(
                PartDTO::lineTypeProperty,
                "Line Type",
                lineTypeOptions,
                defaultLineType
        );
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteModel.selectedPartProperty().get().setLineType(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
        });
        col.setMinWidth(175);
        col.setPrefWidth(175);
        col.setMaxWidth(175);
        return col;
    }

    private TableColumn<PartDTO, String> col3() {
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partDescriptionProperty,"Part Description");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteModel.selectedPartProperty().get().setPartDescription(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
        });
        return col;
    }

    private TableColumn<PartDTO, String> col4() {
        TableColumn<PartDTO, String> col = TableColumnFx.editableStringTableColumn(PartDTO::partQuantityProperty,"Qty");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteModel.selectedPartProperty().get().setPartQuantity(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
        });
        col.setMinWidth(70.0);
        col.setPrefWidth(70.0);
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
        noteModel.boundNoteProperty().get().getPartOrders().forEach((partOrderDTO) -> root.getChildren().add(createPartOrderBox(partOrderDTO)));
    }
}
