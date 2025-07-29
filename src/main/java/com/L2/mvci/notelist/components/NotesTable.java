package com.L2.mvci.notelist.components;

import com.L2.dto.NoteFx;
import com.L2.interfaces.Component;
import com.L2.mvci.notelist.NoteListMessage;
import com.L2.mvci.notelist.NoteListView;
import com.L2.static_tools.ImageResources;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.util.Arrays;

public class NotesTable implements Component<Region> {

    private final NoteListView noteListView;
    private TableView<NoteFx> tableView;

    public NotesTable(NoteListView noteListView) {
        this.noteListView = noteListView;
    }

    @Override
    public TableView<NoteFx> build() {
        this.tableView = TableViewFx.of(NoteFx.class);
        noteListView.getNoteListModel().setNoteTable(tableView);
        tableView.setItems(noteListView.getNoteListModel().getNotes()); // Set the ObservableList here
        tableView.setEditable(true);
        tableView.getColumns().addAll(Arrays.asList(col0(), mail(), col1(), col3(), col2()));
        tableView.setPlaceholder(new Label(""));

        // Highlight rows where getRelatedCaseNumber is not empty
//        tableView.setRowFactory(tv -> {
//            TableRow<NoteFx> row = new TableRow<>();
//            row.itemProperty().addListener((obs, oldItem, newItem) -> {
//                if (newItem != null && newItem.getRelatedCaseNumber() != null && !newItem.getRelatedCaseNumber().isEmpty()) {
//                    row.setStyle("-fx-background-color: #fdfdd1;"); // Light yellow highlight
//                } else {
//                    row.setStyle(""); // Reset to default style
//                }
//            });
//            return row;
//        });

//        tableView.setRowFactory(tv -> {
//            TableRow<NoteFx> row = new TableRow<>();
//            row.itemProperty().addListener((obs, oldItem, newItem) -> {
//                row.getStyleClass().remove("highlighted-row"); // Remove the class first
//                if (newItem != null && newItem.getRelatedCaseNumber() != null && !newItem.getRelatedCaseNumber().isEmpty()) {
//                    row.getStyleClass().add("highlighted-row"); // Add highlight class
//                }
//            });
//            return row;
//        });

        // do not delete this you will be sorry
        TableView.TableViewSelectionModel<NoteFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                noteListView.getNoteListModel().setSelectedNote(newSelection);
                noteListView.getAction().accept(NoteListMessage.UPDATE_BOUND_NOTE);
            }
        });

        addScrollListener(tableView);
        return tableView;
    }

    private void addScrollListener(TableView<?> tableView) {
        tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                // Delay the lookup slightly to ensure the ScrollBar is available
                Platform.runLater(() -> {
                    ScrollBar verticalScrollBar = (ScrollBar) tableView.lookup(".scroll-bar:vertical");
                    if (verticalScrollBar != null) {
                        setupListeners(tableView, verticalScrollBar);
                    }
                });
            }
        });
    }

    private boolean actionInProgress = false;

    private void setupListeners(TableView<?> tableView, ScrollBar verticalScrollBar) {
        tableView.setOnScroll(event -> {
            // don't do anything if there is an active search
            if (!actionInProgress && !noteListView.getNoteListModel().isActiveSearch()) {
                // if we have an active search do not do this
                if (event.getDeltaY() > 0 && verticalScrollBar.getValue() == 0.0) {
                    actionInProgress = true;
                    noteListView.getAction().accept(NoteListMessage.ADD_TO_TOP_OF_LIST);
                    actionInProgress = false;
                } else if (event.getDeltaY() < 0 && verticalScrollBar.getValue() == 1.0) {
                    actionInProgress = true;
                    noteListView.getAction().accept(NoteListMessage.ADD_TO_BOTTOM_OF_LIST);
                    actionInProgress = false;
                }
            } else noteListView.getAction().accept(NoteListMessage.NO_ACTION_TAKEN_FOR_SCROLL);
        });

        tableView.setOnKeyPressed(event -> {
            // don't do anything if there is an active search
            if (!actionInProgress && !noteListView.getNoteListModel().isActiveSearch()) {
                switch (event.getCode()) {
                    case UP:
                        if (verticalScrollBar.getValue() == 0.0) {
                            actionInProgress = true;
                            noteListView.getAction().accept(NoteListMessage.ADD_TO_TOP_OF_LIST);
                            actionInProgress = false;
                        }
                        break;
                    case DOWN:
                        if (verticalScrollBar.getValue() == 1.0) {
                            actionInProgress = true;
                            noteListView.getAction().accept(NoteListMessage.ADD_TO_BOTTOM_OF_LIST);
                            actionInProgress = false;
                        }
                        break;
                    default:
                        break;
                }
            } else noteListView.getAction().accept(NoteListMessage.NO_ACTION_TAKEN_FOR_KEY_PRESS);
        });
    }

    private TableColumn<NoteFx, String> col0() {
        TableColumn<NoteFx, String> col = TableColumnFx.stringTableColumn(NoteFx::formattedTimestampProperty, "Date/Time");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(125);
        col.setMinWidth(125);
        col.setMaxWidth(125);
        return col;
    }

    private TableColumn<NoteFx, String> col1() {
        TableColumn<NoteFx, String> col = TableColumnFx.stringTableColumn(NoteFx::callInPersonProperty, "Caller");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
        return col;
    }

    private TableColumn<NoteFx, String> col3() {
        TableColumn<NoteFx, String> col = TableColumnFx.stringTableColumn(NoteFx::modelNumberProperty, "Model");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
        return col;
    }

    private TableColumn<NoteFx, String> col2() {
        TableColumn<NoteFx, String> col = TableColumnFx.editableStringTableColumn(NoteFx::titleProperty, "Problem");
        col.setStyle("-fx-alignment: center-left");
        col.setSortable(false);
        col.setOnEditCommit(event -> {
            if (event.getNewValue() != null) {
                noteListView.getNoteListModel().getBoundNote().setTitle(event.getNewValue());
                noteListView.getAction().accept(NoteListMessage.SAVE_OR_UPDATE_NOTE);
            }
        });
        return col;
    }

    private TableColumn<NoteFx, Boolean> mail() {
        TableColumn<NoteFx, Boolean> emailCol = new TableColumn<>("");
        emailCol.setPrefWidth(30);
        emailCol.setMaxWidth(30);
        emailCol.setMinWidth(30);

        // Define the cell factory
        emailCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<NoteFx, Boolean> call(TableColumn<NoteFx, Boolean> param) {
                return new TableCell<>() {
                    private final ImageView emailImageView = new ImageView(ImageResources.MAIL);
                    private final ImageView caseImageView = new ImageView(ImageResources.OWN); // Replace with your case icon

                    @Override
                    protected void updateItem(Boolean isEmail, boolean empty) {
                        super.updateItem(isEmail, empty);
                        NoteFx note = getTableRow().getItem(); // Get the current NoteFx item
                        if (empty || note == null) {
                            setGraphic(null); // No icon if empty
                        } else if (isEmail != null && isEmail) {
                            emailImageView.setFitWidth(16);
                            emailImageView.setFitHeight(16);
                            setGraphic(emailImageView); // Display email icon
                        } else if (note.getRelatedCaseNumber() != null && !note.getRelatedCaseNumber().isEmpty()) {
                            caseImageView.setFitWidth(16);
                            caseImageView.setFitHeight(16);
                            setGraphic(caseImageView); // Display case icon
                        } else {
                            setGraphic(null); // No icon if neither condition is met
                        }
                    }
                };
            }
        });
        // Bind the isEmail property to the column's value
        emailCol.setCellValueFactory(cellData -> cellData.getValue().isEmailProperty());
        return emailCol;
    }

    @Override
    public void flash() {

    }

    @Override
    public void refreshFields() {
        tableView.refresh();
    }
}
