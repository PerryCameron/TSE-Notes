package com.L2.mvci_note.components;

import com.L2.dto.NoteDTO;
import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.static_tools.NoteDTOProcessor;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueBox implements Component<Region> {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox root;
    private TextArea textAreaIssue;
    private static final Logger logger = LoggerFactory.getLogger(IssueBox.class);

    public IssueBox(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.textAreaIssue = TextAreaFx.of(true, 200, 16, 2);
    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        root.getStyleClass().add("decorative-hbox");
        textAreaIssue.setPrefWidth(900);
        textAreaIssue.textProperty().bindBidirectional(noteModel.getBoundNote().issueProperty());
//        Button copyButton = ButtonFx.utilityButton(() -> {
//            flash();
//            noteView.getAction().accept(NoteMessage.COPY_ISSUE);
//        }, "Copy", "/images/copy-16.png");
//        copyButton.setTooltip(ToolTipFx.of("Copy Issue"));
        Button[] buttons = new Button[]{};
        root.getChildren().addAll(TitleBarFx.of("Issue", buttons), textAreaIssue);
        refreshFields();
        textAreaIssue.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) ;
            else {  // focus removed
                // is this an email?
                if(!noteModel.getBoundNote().isEmail()) {
                    if (NoteDTOProcessor.isEmail(textAreaIssue.getText())) {
                        // process that into a note dto
                        NoteDTO noteDTO = NoteDTOProcessor.processEmail(textAreaIssue.getText(), noteModel.getBoundNote().getId());
                        noteModel.getBoundNote().copyFrom(noteDTO);
                        logger.info("Processing an email...");
                        textAreaIssue.setEditable(false);
                    }
                    noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
                }
            }
        });
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
        if(noteModel.getBoundNote().isEmail()) textAreaIssue.setEditable(false);
        else textAreaIssue.setEditable(true);
    }
}
