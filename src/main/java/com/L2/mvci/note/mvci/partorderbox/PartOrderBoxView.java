package com.L2.mvci.note.mvci.partorderbox;

import org.controlsfx.control.ToggleSwitch;
import com.L2.dto.PartFx;
import com.L2.dto.PartOrderFx;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.mvci.note.mvci.partorderbox.mvci.partviewer.PartViewerController;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderController;
import com.L2.static_tools.ImageResources;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
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
        setNewPartListener();
        return partOrderBoxModel.getRoot();
    }

    private void setNewPartListener() {
        partOrderBoxModel.messageProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case NEW_PART -> newPart();
                case PART_EXISTS -> launchPartViewer();
            }
            action.accept(PartOrderBoxMessage.RESET_PART_LISTENER);
        });
    }

    private void newPart() {  // I feel like newPart is a terrible name for this method
        Optional<Alert> alertOpt = DialogueFx.conformationAlert("Part not available in database ",
                "Would you like to add this part to the database?");
        alertOpt.ifPresent(alert -> {
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    if (partOrderBoxModel.getNoteModel().selectedPartProperty().get().getPartNumber().isEmpty()) {
                        DialogueFx.errorAlert("Can not view part", "There is no part number to view");
                    } else if (partOrderBoxModel.getNoteModel().selectedPartProperty().get().getPartDescription().isEmpty()) {
                        DialogueFx.errorAlert("Can not add part", "There is no part description. Please add it first.");
                    } else {
                        action.accept(PartOrderBoxMessage.ADD_PART_TO_DATABASE); // the method this message calls in the interactor will send the PART_EXISTS message back if the repo successfully adds the part
                    }
                }
            });
        });
    }

    public Node createPartOrderBox(PartOrderFx partOrderDTO) {
        VBox box = new VBox();
        box.getStyleClass().add("decorative-hbox");
        box.setPadding(new Insets(5, 5, 10, 5));
        box.setSpacing(5);
        partOrderBoxModel.setTableView(TableViewFx.of(PartFx.class));
        buildTable(partOrderDTO);
        box.setOnMouseEntered(event -> noteModel.selectedPartOrderProperty().set(partOrderDTO));
        box.setOnMouseExited(event -> partOrderBoxModel.getTableView().getSelectionModel().clearSelection());
        partOrderBoxModel.getPartOrderMap().put(partOrderDTO, box);
        HBox hBox = HBoxFx.of(new Insets(0, 5, 0, 0), 5.0);
        hBox.getChildren().addAll(menu(partOrderDTO), partOrderBoxModel.getTableView());
        box.getChildren().addAll(toolbar(partOrderDTO), hBox);
        return box;
    }

    private void launchPartViewer() {
        Optional<Alert> alert = Optional.ofNullable(new PartViewerController(this).getView());
        alert.ifPresent(Dialog::showAndWait);
    }

    private Node menu(PartOrderFx partOrderDTO) {
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(5, 0, 5, 5));
        // parts search dialogue
        Button searchButton = ButtonFx.utilityButton(() -> {
            partOrderBoxModel.setPartController(new PartFinderController(noteView, partOrderBoxModel.getTableView()));
            Optional<Alert> alert = Optional.ofNullable(partOrderBoxModel.getPartController().getView());
            alert.ifPresent(Dialog::showAndWait);
            // in case we have changed the ranges in settings, we need to make sure they are fresh
            noteView.getAction().accept(NoteMessage.GET_RANGES); // why is this not working??
        }, ImageResources.SEARCH, "Search");
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
        }, ImageResources.NEW, "Add Part");
        Button deleteButton = ButtonFx.utilityButton(() -> {
            PartFx selectedPart = partOrderBoxModel.getTableView().getSelectionModel().getSelectedItem();
            if (selectedPart == null) DialogueFx.errorAlert("Unable to delete", "You must first select a part");
            else {
                Optional<Alert> alertOpt = DialogueFx.conformationAlert("Delete Part# " + selectedPart.getPartNumber(),
                        "Are you sure you want to delete " + selectedPart.getPartDescription() + "?");
                alertOpt.ifPresent(alert -> {
                    Optional<ButtonType> result = alert.showAndWait();
                    result.ifPresent(buttonType -> {
                        if (buttonType == ButtonType.YES) {
                            noteView.getAction().accept(NoteMessage.DELETE_PART);
                        }
                    });
                });
            }
        },ImageResources.DELETE, "Delete Part");
        // Create the VBox from your method
        Control lineTypeBox = lineTypeToggle(partOrderDTO);

        // Set a top margin (e.g., 10 pixels) on lineTypeBox
        VBox.setMargin(lineTypeBox, new Insets(20, 3, 0, 3));
        vBox.getStyleClass().add("inner-decorative-hbox");
        // Now add all nodes to the parent vBox
        vBox.getChildren().addAll(searchButton, addPartButton, deleteButton, lineTypeBox);
        return vBox;
    }

    // do we want to show a line type in the table?
    private Control lineTypeToggle(PartOrderFx partOrderDTO) {
        ToggleSwitch toggleSwitch = new ToggleSwitch("Show Type");
        toggleSwitch.setSelected(partOrderDTO.showType());
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> partOrderDTO.showTypeProperty().set(observable.getValue()));
        return toggleSwitch;
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
            Optional<Alert> alertOpt = DialogueFx.conformationAlert("Delete Part Order?",
                    "Are you sure you want to delete this part order?");
            alertOpt.ifPresent(alert -> {
                Optional<ButtonType> result = alert.showAndWait();
                result.ifPresent(buttonType -> {
                    if (buttonType == ButtonType.YES) {
                        noteView.getAction().accept(NoteMessage.DELETE_PART_ORDER);
                        noteModel.boundNoteProperty().get().getPartOrders().remove(partOrderDTO);
                        partOrderBoxModel.getRoot().getChildren().remove(partOrderBoxModel.getPartOrderMap().get(partOrderDTO));
                    }
                });
            });

        }, ImageResources.DELETE, "Delete PO");
        Button copyButton = ButtonFx.utilityButton(() -> {
            noteView.getAction().accept(NoteMessage.COPY_PART_ORDER);
            VBox vBox = partOrderBoxModel.getPartOrderMap().get(partOrderDTO);
            vBox.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.setStyle("")); // Reset the style
            pause.play();
        },ImageResources.COPY, "Copy");
        iconBox.getChildren().addAll(copyButton, deleteButton);
        return iconBox;
    }

    public void buildTable(PartOrderFx partOrderDTO) {
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
        col.setCellFactory(param -> new TableCell<>() {
            final ImageView imageViewCopy = new ImageView(ImageResources.VIEW);
            final Button button = ButtonFx.of(imageViewCopy, "invisible-button");

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
            vBox.getStyleClass().add("flash");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.getStyleClass().remove("flash")); // Reset the style
            pause.play();
        }
    }

    public void refreshFields() {
        partOrderBoxModel.getRoot().getChildren().clear();
        noteModel.boundNoteProperty().get().getPartOrders().forEach((partOrderDTO) -> partOrderBoxModel.getRoot().getChildren().add(createPartOrderBox(partOrderDTO)));
    }

    public NoteView getNoteView() {
        return noteView;
    }

    public PartOrderBoxModel getPartOrderBoxModel() {
        return partOrderBoxModel;
    }
}
