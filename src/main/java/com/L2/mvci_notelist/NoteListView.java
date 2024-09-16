package com.L2.mvci_notelist;

import com.L2.mvci_main.MainMessage;
import com.L2.mvci_main.MainModel;
import com.L2.mvci_main.components.TitleBar;
import com.L2.mvci_notelist.components.NotesTable;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.MenuFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        root.getChildren().add(notesTable.build());
        root.setStyle("-fx-border-color: #878484; -fx-border-width: 1;");
        return root;
    }

    public NoteListModel getNoteListModel() {
        return noteListModel;
    }

    public Consumer<NoteListMessage> getAction() {
        return action;
    }
}