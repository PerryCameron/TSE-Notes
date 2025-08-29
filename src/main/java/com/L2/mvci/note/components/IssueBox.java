package com.L2.mvci.note.components;

import com.L2.controls.SpellCheckArea;
import com.L2.dto.NoteFx;
import com.L2.enums.AreaType;
import com.L2.interfaces.Component;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.static_tools.NoteDTOProcessor;
import com.L2.widgetFx.HBoxFx;
import com.L2.widgetFx.TitleBarFx;
import com.L2.widgetFx.VBoxFx;
import javafx.animation.PauseTransition;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueBox implements Component<Region> {
    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox root;
    private static final Logger logger = LoggerFactory.getLogger(IssueBox.class);


    public IssueBox(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(5.0, new Insets(5, 10, 10, 10));
        root.getStyleClass().add("decorative-hbox");
        HBox iconBox = HBoxFx.iconBox(10);
        root.getChildren().addAll(TitleBarFx.of("Issue", iconBox), getTextArea());
        refreshFields();
        return root;
    }

    // the text area that you write in
    private VirtualizedScrollPane<CodeArea> getTextArea() {
        // Create a new CodeArea instance
        SpellCheckArea spellCheckArea = new SpellCheckArea(noteView, noteModel.boundNoteProperty().get().issueProperty(), AreaType.issue);
        noteModel.issueAreaProperty().setValue(spellCheckArea);
        // check on startup
        VirtualizedScrollPane<CodeArea> scrollWrapper = new VirtualizedScrollPane<>(spellCheckArea);

        noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA);
        // wrap in a scroll pane
        // Retain focus listener logic
        CodeArea codeArea = spellCheckArea.getCodeArea();
        codeArea.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                // Check if the bound note is not an email
                if (!noteModel.boundNoteProperty().get().isEmail()) {
                    // Process the text as an email if it matches email format
                    if (NoteDTOProcessor.isEmail(codeArea.getText())) {
                        NoteFx noteDTO = NoteDTOProcessor.processEmail(
                                codeArea.getText(),
                                noteModel.boundNoteProperty().get().getId()
                        );
                        // Update the bound note with the processed email data
                        noteModel.boundNoteProperty().get().copyFrom(noteDTO);
                        logger.info("Processed an email and updated the note model.");
                        // Set the CodeArea to read-only
                        codeArea.setEditable(false);
                        noteView.getAction().accept(NoteMessage.CLEAR_HIGHLIGHTS_ISSUE);
                    }
                    // trim the text to get rid of all returns etc
                    noteView.getAction().accept(NoteMessage.TRIM_ISSUE);
                    // Trigger save or update note action
                    noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
                }
            }
        });
        return scrollWrapper;
    }

    @Override
    public void flash() {
        root.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(0.2));
        pause.setOnFinished(event -> root.setStyle(""));
        pause.play();
    }

    @Override
    public void refreshFields() {
        noteModel.issueAreaProperty().get().setEditable(!noteModel.boundNoteProperty().get().isEmail());
    }
}

