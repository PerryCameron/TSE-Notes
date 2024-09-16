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
        BorderPane borderPane = new BorderPane();
//        borderPane.setPrefSize(1028, 830);
        borderPane.setTop(setUpTopPane());
        borderPane.setCenter(setUpCenterPane());
        borderPane.setBottom(setUpBottomPane());
        root.getChildren().add(borderPane);
        root.setStyle("-fx-border-color: #878484; -fx-border-width: 1;");
        return root;
    }

    private Node setUpTopPane() {
        return null;
    }

    private Node setUpMenuBar() {
        return null;
    }

    private Node setUpBottomPane() {
        HBox hBox = new HBox();

        return hBox;
    }

    private Node setUpCenterPane() {
        return notesTable.build();
    }

    public NoteListModel getNoteListModel() {
        return noteListModel;
    }
}