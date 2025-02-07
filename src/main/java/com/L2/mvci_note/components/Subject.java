package com.L2.mvci_note.components;

import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.TitleBarFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Subject implements Component<Region> {
    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox root;

    public Subject(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public void flash() {

    }

    @Override
    public void refreshFields() {

    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        root.getStyleClass().add("decorative-hbox");

        Button[] buttons = new Button[]{};
//        hBox.getChildren().addAll(correctiveText(), buttonBox());
        root.getChildren().addAll(TitleBarFx.of("Subject", buttons), createSubjectField());
        refreshFields();
        root.setOnMouseExited(event -> {
            noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
        });
        return root;
    }

    private Node createSubjectField() {
        HBox hBox = new HBox(); // box to hold basic info and service plan
        hBox.setPadding(new Insets(0, 5, 5, 5));
        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(noteModel.getBoundNote().titleProperty());
        HBox.setHgrow(textField, Priority.ALWAYS);
        hBox.getChildren().add(textField);
        return hBox;
    }
}
