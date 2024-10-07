package com.L2.mvci_notelist.components;

import com.L2.dto.NoteDTO;
import com.L2.interfaces.Component;
import com.L2.mvci_notelist.NoteListMessage;
import com.L2.mvci_notelist.NoteListView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.util.Callback;

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
        noteListView.getNoteListModel().setNoteTable(tableView);
        tableView.setItems(noteListView.getNoteListModel().getNotes()); // Set the ObservableList here
        tableView.setEditable(true);
        tableView.getColumns().addAll(Arrays.asList(col0(), mail(), col1(), col3(), col2()));
        tableView.setPlaceholder(new Label(""));

        tableView.setRowFactory(tv -> {
            TableRow<NoteDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                System.out.println("NoteListModel::setSelectedNote(<note clicked>)");
                noteListView.getNoteListModel().setSelectedNote(row.getItem());
                noteListView.getAction().accept(NoteListMessage.UPDATE_BOUND_NOTE);
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    noteListView.getAction().accept(NoteListMessage.SELECT_NOTE_TAB);
                }
            });
            return row;
        });
        return tableView;
    }

    private TableColumn<NoteDTO, String> col0() {
        TableColumn<NoteDTO, String> col = TableColumnFx.stringTableColumn(NoteDTO::formattedTimestampProperty, "Date/Time");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(142);
        col.setMinWidth(142);
        col.setMaxWidth(142);
        return col;
    }

    private TableColumn<NoteDTO, String> col1() {
        TableColumn<NoteDTO, String> col = TableColumnFx.stringTableColumn(NoteDTO::callInPersonProperty, "Caller");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
        return col;
    }

    private TableColumn<NoteDTO, String> col3() {
        TableColumn<NoteDTO, String> col = TableColumnFx.stringTableColumn(NoteDTO::modelNumberProperty, "Model");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
        return col;
    }

    private TableColumn<NoteDTO, String> col2() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::titleProperty, "Problem");
        col.setStyle("-fx-alignment: center-left");
        col.setSortable(false);
        col.setOnEditCommit(event -> {
            if(event.getNewValue() != null) {
                noteListView.getNoteListModel().getBoundNote().setTitle(event.getNewValue());
                noteListView.getAction().accept(NoteListMessage.SAVE_OR_UPDATE_NOTE);
            }
        });
        return col;
    }

    private TableColumn<NoteDTO, Boolean> mail() {
        TableColumn<NoteDTO, Boolean> emailCol = new TableColumn<>("");
        emailCol.setPrefWidth(28);
        emailCol.setMaxWidth(28);
        // Define the cell factory
        emailCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<NoteDTO, Boolean> call(TableColumn<NoteDTO, Boolean> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/mail-16.png")));

                    @Override
                    protected void updateItem(Boolean isEmail, boolean empty) {
                        super.updateItem(isEmail, empty);
                        if (empty || isEmail == null || !isEmail) {
                            setGraphic(null); // No image if not email
                        } else {
                            imageView.setFitWidth(16);
                            imageView.setFitHeight(16);
                            setGraphic(imageView); // Display the email icon
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
