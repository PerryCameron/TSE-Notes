package com.L2.mvci_notelist;

import com.L2.mvci_notelist.components.NotesTable;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.function.Consumer;

public class NoteListView implements Builder<Region> {
    private final NoteListModel noteListModel;
    private final NotesTable notesTable;
    Consumer<NoteListMessage> action;

    public NoteListView(NoteListModel noteListModel, Consumer<NoteListMessage> m) {
        this.noteListModel = noteListModel;
        this.notesTable = new NotesTable(this);
        action = m;
    }

    @Override
    public Region build() {
        VBox root = new VBox();
        root.setPadding(new Insets(5, 5, 5, 5));
        root.getChildren().add(notesTable.build());
        return root;
    }

    public NoteListModel getNoteListModel() {
        return noteListModel;
    }

    public Consumer<NoteListMessage> getAction() {
        return action;
    }
}