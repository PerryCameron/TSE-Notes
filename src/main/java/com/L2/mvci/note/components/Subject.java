package com.L2.mvci.note.components;

import com.L2.controls.SpellCheckArea;
import com.L2.enums.AreaType;
import com.L2.interfaces.Component;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.static_tools.ImageResources;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.TitleBarFx;
import com.L2.widgetFx.ToolTipFx;
import com.L2.widgetFx.VBoxFx;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
        root.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(event -> root.setStyle("")); // Reset the style
        pause.play();
    }

    @Override
    public void refreshFields() {

    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        root.getStyleClass().add("decorative-hbox");

        Button copyButton = ButtonFx.utilityButton(() -> {
            flash();
            noteView.getAction().accept(NoteMessage.COPY_SUBJECT);
        }, ImageResources.COPY, "Copy");
        copyButton.setTooltip(ToolTipFx.of("Copy Basic Information"));

        Button[] buttons = new Button[]{copyButton};
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
        SpellCheckArea spellCheckArea = new SpellCheckArea(noteView, noteModel.boundNoteProperty().get().titleProperty(), AreaType.subject);
        HBox.setHgrow(spellCheckArea, Priority.ALWAYS);
        noteModel.subjectAreaProperty().setValue(spellCheckArea);
        // check on startup
        noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_SUBJECT_AREA);
        hBox.getChildren().add(spellCheckArea);
        return hBox;
    }
}
