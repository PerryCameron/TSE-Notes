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
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.util.Arrays;

public class NotesTable implements Component<Region> {

    private final NoteListView noteListView;
    private TableView<NoteDTO> tableView;

    public NotesTable(NoteListView noteListView) {
        this.noteListView = noteListView;
    }

    // if NoteDTO::isEmail is true I want the background color of that row to be orange
    @Override
    public TableView<NoteDTO> build() {
        this.tableView = TableViewFx.of(NoteDTO.class);
        tableView.setItems(noteListView.getNoteListModel().getNotes()); // Set the ObservableList here
        tableView.setEditable(true);
        tableView.getColumns().addAll(Arrays.asList(col0(), mail(), col1(),col3(),col2()));
        tableView.setPlaceholder(new Label(""));

        // auto selector
        TableView.TableViewSelectionModel<NoteDTO> selectionModel = tableView.getSelectionModel();

        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                noteListView.getNoteListModel().setSelectedNote(newSelection);
                noteListView.getAction().accept(NoteListMessage.UPDATE_BOUND_NOTE);
            }
        });
        return tableView;
    }

    private TableColumn<NoteDTO, String> col0() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::formattedTimestampProperty,"Date/Time");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(124);
        col.setMinWidth(124);
        col.setMaxWidth(124);
        return col;
    }

    private TableColumn<NoteDTO, String> col1() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::callInPersonProperty,"Caller");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
        return col;
    }

    private TableColumn<NoteDTO, String> col3() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::modelNumberProperty,"Model");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
        return col;
    }

    private TableColumn<NoteDTO, String> col2() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::titleProperty,"Problem");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            noteListView.getNoteListModel().getBoundNote().setTitle(event.getNewValue());
            noteListView.getAction().accept(NoteListMessage.SAVE_NOTE);
        });
        return col;
    }

    private TableColumn<NoteDTO, Boolean> mail() {
        TableColumn<NoteDTO, Boolean> emailCol = new TableColumn<>("");
        emailCol.setPrefWidth(25);
        emailCol.setMaxWidth(25);
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

    }
}
