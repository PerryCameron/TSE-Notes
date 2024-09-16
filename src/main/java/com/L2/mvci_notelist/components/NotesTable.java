package com.L2.mvci_notelist.components;

import com.L2.dto.NoteDTO;
import com.L2.interfaces.Component;
import com.L2.mvci_notelist.NoteListMessage;
import com.L2.mvci_notelist.NoteListView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;

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
        tableView.getColumns().addAll(Arrays.asList(col0(), col1(),col3(),col2()));
        tableView.setPlaceholder(new Label(""));

        // auto selector
        TableView.TableViewSelectionModel<NoteDTO> selectionModel = tableView.getSelectionModel();

        // Set row factory to conditionally change row color
        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(NoteDTO note, boolean empty) {
                super.updateItem(note, empty);
                if (note == null || empty) {
                    setStyle(""); // Reset style for empty rows
                } else {
                    if (note.isEmail()) {
                        setStyle("-fx-background-color: #f6d39f;");
                    } else {
                        setStyle(""); // Reset to default if not applicable
                    }
                }
            }
        });

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
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
//        col.setOnEditCommit(event -> {
//            noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart().setPartNumber(event.getNewValue());
//            noteView.getAction().accept(NoteMessage.UPDATE_PART);
//        });
        return col;
    }

    private TableColumn<NoteDTO, String> col1() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::callInPersonProperty,"Caller");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
//        col.setOnEditCommit(event -> {
//            noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart().setPartNumber(event.getNewValue());
//            noteView.getAction().accept(NoteMessage.UPDATE_PART);
//        });
        return col;
    }

    private TableColumn<NoteDTO, String> col3() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::modelNumberProperty,"Model");
        col.setStyle("-fx-alignment: center-left");
        col.setPrefWidth(150);
        col.setMinWidth(150);
        col.setMaxWidth(150);
//        col.setOnEditCommit(event -> {
//            noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart().setPartQuantity(event.getNewValue());
//            noteView.getAction().accept(NoteMessage.UPDATE_PART);
//        });
        return col;
    }

    private TableColumn<NoteDTO, String> col2() {
        TableColumn<NoteDTO, String> col = TableColumnFx.editableStringTableColumn(NoteDTO::titleProperty,"Problem");
        col.setStyle("-fx-alignment: center-left");
        col.setOnEditCommit(event -> {
            System.out.println("saving title");
//            noteListView.getNoteListModel().getBoundNote().setTitle(event.getNewValue());
//            noteListView.getAction().accept(NoteListMessage.UPDATE_NOTE);
        });
        return col;
    }

    @Override
    public void flash() {

    }

    @Override
    public void refreshFields() {

    }
}
