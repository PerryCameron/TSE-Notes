package com.L2.mvci.note.mvci.partorderbox;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.dto.PartFx;
import com.L2.dto.PartOrderFx;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartController;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Consumer;

public class PartOrderBoxView implements Builder<Region> {
        private final PartOrderBoxModel partOrderBoxModel;
        private final NoteModel noteModel;
        private final NoteView noteView;
        Consumer<PartOrderBoxMessage> action;

    public PartOrderBoxView(PartOrderBoxModel partOrderBoxModel, NoteView noteView, Consumer<PartOrderBoxMessage> action) {
            this.partOrderBoxModel = partOrderBoxModel;
            this.noteView = noteView;
            this.noteModel = noteView.getNoteModel();
            this.action = action;
    }

    @Override
    public Region build() {
        partOrderBoxModel.setRoot(new VBox(10));
        // for each part order create a VBOX with stuff in it
        for (PartOrderFx partOrderDTO : noteModel.boundNoteProperty().get().getPartOrders()) {
            partOrderBoxModel.getRoot().getChildren().add(createPartOrderBox(partOrderDTO));
        }
        partOrderBoxModel.getRefreshFields().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                refreshFields();
            }
        });
        partOrderBoxModel.getFlash().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                flash();
            }
        });
        return partOrderBoxModel.getRoot();
    }

    public Node createPartOrderBox(PartOrderFx partOrderDTO) {
        VBox box = new VBox(10);
        partOrderBoxModel.setTableView(TableViewFx.of(PartFx.class));
        buildTable(partOrderDTO);

        box.setOnMouseEntered(event -> noteModel.selectedPartOrderProperty().set(partOrderDTO));
        box.setOnMouseExited(event -> partOrderBoxModel.getTableView().getSelectionModel().clearSelection());
        partOrderBoxModel.getPartOrderMap().put(partOrderDTO, box);
        box.getStyleClass().add("decorative-hbox");
        box.setPadding(new Insets(5, 5, 10, 5));
        HBox hBox = new HBox(5);
        hBox.setPadding(new Insets(0, 5, 0, 0));
        hBox.getChildren().addAll(menu(partOrderDTO), partOrderBoxModel.getTableView());
        box.setSpacing(5);
        box.getChildren().addAll(toolbar(partOrderDTO), hBox);
        return box;
    }

    private Node menu(PartOrderFx partOrderDTO) {
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(5, 0, 5, 5));
        // parts search dialogue
        Button searchButton = ButtonFx.utilityButton(() -> {
            partOrderBoxModel.setPartController(new PartController(noteView, partOrderBoxModel.getTableView()));
            Optional<Alert> alert = Optional.ofNullable(partOrderBoxModel.getPartController().getView());
            alert.ifPresent(Dialog::showAndWait);
            // in case we have changed the ranges in settings, we need to make sure they are fresh
            noteView.getAction().accept(NoteMessage.GET_RANGES); // why is this not working??
        }, "Search", "/images/search-16.png");
        Button addPartButton = ButtonFx.utilityButton(() -> {
            noteView.getAction().accept(NoteMessage.INSERT_PART);
            // Sort parts in reverse order
            partOrderDTO.getParts().sort(Comparator.comparing(PartFx::getId).reversed());
            // Refresh the table view layout and focus
            partOrderBoxModel.getTableView().layout();
            partOrderBoxModel.getTableView().requestFocus();
            // Select row 0 and focus the first column
            partOrderBoxModel.getTableView().getSelectionModel().select(0);
            partOrderBoxModel.getTableView().getFocusModel().focus(0, partOrderBoxModel.getTableView().getColumns().getFirst());  // Focus the first column (index 0)
            // Edit the first cell in the first row
            partOrderBoxModel.getTableView().edit(0, partOrderBoxModel.getTableView().getColumns().getFirst());  // Edit row 0, first column
        }, "Add Part", "/images/create-16.png");
        Button deleteButton = ButtonFx.utilityButton(() -> noteView.getAction().accept(NoteMessage.DELETE_PART), "Delete Part", "/images/delete-16.png");
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
    private HBox lineTypeToggle(PartOrderFx partOrderDTO) {
        HBox vBox = new HBox(5);
        vBox.setPadding(new Insets(15, 5, 15, 5));
        Label label = new Label("Show Type");
        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.setSelected(partOrderDTO.showType());
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> partOrderDTO.showTypeProperty().set(observable.getValue()));
        vBox.getChildren().addAll(label, toggleSwitch);
        return vBox;
    }

    private TextField createPartOrderText(PartOrderFx partOrderDTO) {
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

    private Node toolbar(PartOrderFx partOrderDTO) {
        HBox outerBox = new HBox();
        HBox hBox = new HBox(5);
        TextField partNameTextField = createPartOrderText(partOrderDTO);
        outerBox.getChildren().addAll(hBox, createButtons(partOrderDTO));
        outerBox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label("Part Order");
        label.setPadding(new Insets(0, 0, 2, 5));
        if (partOrderDTO.getOrderNumber().isEmpty() || partOrderDTO.getOrderNumber() == null) {
            hBox.getChildren().add(partNameTextField);
        } else {
            label.setText("Part Order: " + partOrderDTO.getOrderNumber());
            hBox.getChildren().add(label);
        }
        label.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
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

    private Node createButtons(PartOrderFx partOrderDTO) {
        HBox iconBox = HBoxFx.iconBox(10);
        Button deleteButton = ButtonFx.utilityButton(() -> {
            noteView.getAction().accept(NoteMessage.DELETE_PART_ORDER);
            noteModel.boundNoteProperty().get().getPartOrders().remove(partOrderDTO);
            partOrderBoxModel.getRoot().getChildren().remove(partOrderBoxModel.getPartOrderMap().get(partOrderDTO));
        }, "Delete PO", "/images/delete-16.png");

        Button copyButton = ButtonFx.utilityButton(() -> {
            noteView.getAction().accept(NoteMessage.COPY_PART_ORDER);
            VBox vBox = partOrderBoxModel.getPartOrderMap().get(partOrderDTO);
            vBox.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.setStyle("")); // Reset the style
            pause.play();
        }, "Copy", "/images/copy-16.png");
        iconBox.getChildren().addAll(copyButton, deleteButton);
        return iconBox;
    }

    public TableView<PartFx> buildTable(PartOrderFx partOrderDTO) {
        TableView<PartFx> tableView = partOrderBoxModel.getTableView();
        Map<String, TableColumn<PartFx, String>> map = new HashMap<>();
        tableView.setItems(partOrderDTO.getParts()); // Set the ObservableList here
        tableView.setEditable(true);
        map.put("part-number", col1());
        map.put("line-type", col2());
        map.put("description", col3());
        map.put("quantity", col4());
        map.put("action", col5()); // Add the new button column
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
        TableView.TableViewSelectionModel<PartFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
               // System.out.println("setting selected part property with new part");
                noteModel.selectedPartProperty().set(newSelection);
            }
        });
        return tableView;
    }

    private static void lineTypeIsShown(Map<String, TableColumn<PartFx, String>> map, boolean showType, TableView<PartFx> tableView) {
        TableColumn<PartFx, String> col1 = map.get("part-number");
        TableColumn<PartFx, String> col2 = map.get("line-type");
        TableColumn<PartFx, String> col3 = map.get("description");
        TableColumn<PartFx, String> col4 = map.get("quantity");
        TableColumn<PartFx, String> col5 = map.get("action"); // Include the button column
        if (showType) {
            tableView.getColumns().addAll(Arrays.asList(col1, col2, col3, col4, col5));
        } else {
            tableView.getColumns().addAll(Arrays.asList(col1, col3, col4, col5));
        }
    }

    private TableColumn<PartFx, String> col1() {
        TableColumn<PartFx, String> col = TableColumnFx.editableStringTableColumn(PartFx::partNumberProperty, "Part Number");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteModel.selectedPartProperty().get().setPartNumber(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
        });
        // Bind width to 20% of TableView width
        col.prefWidthProperty().bind(partOrderBoxModel.getTableView().widthProperty().multiply(0.20));
        return col;
    }

    private TableColumn<PartFx, String> col2() {
        // Define options and default value
        List<String> lineTypeOptions = Arrays.asList("Advanced Exchange", "Ship Only", "Return Only");
        String defaultLineType = "Advanced Exchange";
        TableColumn<PartFx, String> col = TableColumnFx.comboBoxTableColumn(
                PartFx::lineTypeProperty,
                "Line Type",
                lineTypeOptions,
                defaultLineType
        );
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteModel.selectedPartProperty().get().setLineType(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
        });
        col.prefWidthProperty().bind(partOrderBoxModel.getTableView().widthProperty().multiply(0.20));
        return col;
    }

    private TableColumn<PartFx, String> col3() {
        TableColumn<PartFx, String> col = TableColumnFx.editableStringTableColumn(PartFx::partDescriptionProperty, "Part Description");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteModel.selectedPartProperty().get().setPartDescription(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
        });
        col.prefWidthProperty().bind(partOrderBoxModel.getTableView().widthProperty().multiply(0.45));
        return col;
    }

    private TableColumn<PartFx, String> col4() {
        TableColumn<PartFx, String> col = TableColumnFx.editableStringTableColumn(PartFx::partQuantityProperty, "Qty");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteModel.selectedPartProperty().get().setPartQuantity(event.getNewValue());
            noteView.getAction().accept(NoteMessage.UPDATE_PART);
        });
        col.prefWidthProperty().bind(partOrderBoxModel.getTableView().widthProperty().multiply(0.10));
        return col;
    }

    private TableColumn<PartFx, String> col5() {
        TableColumn<PartFx, String> col = new TableColumn<>("View");
        col.setStyle("-fx-alignment: center");

        // Set a cell factory to render a button in each cell
        col.setCellFactory(param -> new TableCell<PartFx, String>() {
            Image copyIcon = new Image(Objects.requireNonNull(ButtonFx.class.getResourceAsStream("/images/view-16.png")));
            ImageView imageViewCopy = new ImageView(copyIcon);
            Button button = ButtonFx.of(imageViewCopy, "invisible-button");
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // PartFx part = getTableView().getItems().get(getIndex());
                    button.setOnAction(event -> {
                        // make sure row is selected so we don't get a null on next step
                        getTableView().getSelectionModel().select(getIndex());
                        action.accept(PartOrderBoxMessage.VIEW_PART_AS_SPARE);
                    });
                    setGraphic(button);
                }
            }
        });
        col.prefWidthProperty().bind(partOrderBoxModel.getTableView().widthProperty().multiply(0.05));
        return col;
    }

    public void flash() {
        for (Map.Entry<PartOrderFx, VBox> entry : partOrderBoxModel.getPartOrderMap().entrySet()) {
            VBox vBox = entry.getValue();
            vBox.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.setStyle("")); // Reset the style
            pause.play();
        }
    }

    public void refreshFields() {
        partOrderBoxModel.getRoot().getChildren().clear();
        noteModel.boundNoteProperty().get().getPartOrders().forEach((partOrderDTO) -> partOrderBoxModel.getRoot().getChildren().add(createPartOrderBox(partOrderDTO)));
    }


}
