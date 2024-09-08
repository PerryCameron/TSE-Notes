package com.L2.mvci_note.components;

import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class IssueBox implements Component<Region> {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox root;
    private TextArea textAreaIssue;

    public IssueBox(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.textAreaIssue  = TextAreaFx.of(true, 200, 16, 5);
    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        root.getStyleClass().add("decorative-hbox");
        textAreaIssue.setPrefWidth(900);
        ListenerFx.addFocusListener(textAreaIssue, "Issue field", noteModel.getCurrentNote().issueProperty(), noteModel.statusLabelProperty());
        Button clearButton = ButtonFx.utilityButton( () -> {
        textAreaIssue.setText("");
        }, "Clear", "/images/clear-16.png");
        clearButton.setTooltip(ToolTipFx.of("Clear Issue"));
        Button copyButton = ButtonFx.utilityButton( () -> {
            flash();
            noteView.getAction().accept(NoteMessage.COPY_ISSUE);
        }, "Copy", "/images/copy-16.png");
        copyButton.setTooltip(ToolTipFx.of("Copy Issue"));
        Button[] buttons = new Button[] { clearButton, copyButton };
        root.getChildren().addAll(TitleBarFx.of("Issue", buttons), textAreaIssue);
        return root;
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
        textAreaIssue.setText(noteModel.getCurrentNote().issueProperty().get());
    }
}
