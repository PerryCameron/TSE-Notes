package com.L2.mvci_note.components;

import com.L2.dto.NoteDTO;
import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.static_tools.NoteDTOProcessor;
import com.L2.widgetFx.HBoxFx;
import com.L2.widgetFx.TitleBarFx;
import com.L2.widgetFx.VBoxFx;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import org.fxmisc.richtext.CodeArea;

import java.util.List;
import java.util.OptionalInt;

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

    private ContextMenu getContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setStyle("-fx-font-family: '" + Font.getDefault().getFamily() + "';");
        return contextMenu;
    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(5.0, new Insets(5, 10, 10, 10));
        root.getStyleClass().add("decorative-hbox");
        noteModel.contextMenuProperty().setValue(getContextMenu());
        noteView.getAction().accept(NoteMessage.INITALIZE_DICTIONARY); // This should be moved out of this class
        noteModel.issueAreaProperty().setValue(getCodeAreaIssue());
        HBox iconBox = HBoxFx.iconBox(10);
        root.getChildren().addAll(TitleBarFx.of("Issue", iconBox), noteModel.issueAreaProperty().get());
        refreshFields();
        return root;
    }

    private CodeArea getCodeAreaIssue() {
        CodeArea codeArea = new CodeArea();
        codeArea.setWrapText(true);
        HBox.setHgrow(codeArea, Priority.ALWAYS);
        codeArea.setPrefHeight(200); // Adjust based on your needs
        codeArea.setOnMouseMoved(this::handleMouseHover);
        codeArea.setStyle("-fx-font-family: '" + Font.getDefault().getFamily() + "';");
        codeArea.getStyleClass().add("code-area"); // Apply the style class
        // this is our bridge, since CodeArea does not have native FX binding support
        StringProperty bridgeProperty = new SimpleStringProperty();
        // Bind bridge to model
        bridgeProperty.bindBidirectional(noteModel.getBoundNote().issueProperty());
        // Sync bridge with CodeArea
        codeArea.replaceText(bridgeProperty.getValue());
        codeArea.textProperty().addListener((obs, oldVal, newVal) -> bridgeProperty.set(newVal));
        bridgeProperty.addListener((obs, oldVal, newVal) -> {
            if (!newVal.equals(codeArea.getText())) {
                codeArea.replaceText(newVal);
            }
        });
        // Setup spell-checking with debounce
        if (noteModel.hunspellProperty().get() != null) {
            noteModel.spellCheckSubscriptionProperty().setValue(codeArea.multiPlainChanges()
                    .successionEnds(java.time.Duration.ofMillis(500)) // Debounce 500ms
                    .subscribe(ignore -> noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA)));
        }
        // Retain focus listener logic
        codeArea.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                if (!noteModel.getBoundNote().isEmail()) {
                    if (NoteDTOProcessor.isEmail(codeArea.getText())) {
                        NoteDTO noteDTO = NoteDTOProcessor.processEmail(
                                codeArea.getText(),
                                noteModel.getBoundNote().getId()
                        );
                        noteModel.getBoundNote().copyFrom(noteDTO);
                        logger.info("Processed an email and updated the note model.");
                        // Set to read-only
                        codeArea.setEditable(false);
                    }
                    noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
                }
            }
        });
        return codeArea;
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
        noteModel.issueAreaProperty().get().setEditable(!noteModel.getBoundNote().isEmail());
    }

    private void handleMouseHover(MouseEvent event) {
        if (noteModel.hunspellProperty().get() == null) return;
        // Get character index under mouse
        OptionalInt charIndexOpt = noteModel.issueAreaProperty().get().hit(event.getX(), event.getY()).getCharacterIndex();
        if (!charIndexOpt.isPresent()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }
        int charIndex = charIndexOpt.getAsInt();
        if (charIndex < 0 || charIndex >= noteModel.issueAreaProperty().get().getText().length()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Get word at charIndex
        String text = noteModel.issueAreaProperty().get().getText();
        int wordStart = text.lastIndexOf(" ", charIndex) + 1;
        int wordEnd = text.indexOf(" ", charIndex);
        if (wordEnd == -1) wordEnd = text.length();
        if (wordStart >= wordEnd) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        String word = text.substring(wordStart, wordEnd).trim();
        if (word.isEmpty() || noteModel.hunspellProperty().get().spell(word)) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Word is misspelled, build context menu
        noteModel.contextMenuProperty().get().getItems().clear();
        List<String> suggestions = noteModel.hunspellProperty().get().suggest(word);
        if (!suggestions.isEmpty()) {
            for (String suggestion : suggestions) {
                MenuItem item = new MenuItem(suggestion);
                // Capture wordStart and wordEnd in final variables
                final int finalWordStart = wordStart;
                final int finalWordEnd = wordEnd;
                item.setOnAction(e -> {
                    noteModel.issueAreaProperty().get().replaceText(finalWordStart, finalWordEnd, suggestion);
                    noteModel.contextMenuProperty().get().hide();
                });
                noteModel.contextMenuProperty().get().getItems().add(item);
            }
        }

        // Add "Add to Dictionary" option
        MenuItem addToDict = new MenuItem("Add to Dictionary");

        addToDict.setOnAction(e -> {
            noteModel.hunspellProperty().get().add(word);
            // put new word on pedestal
            noteModel.newWordProperty().set(word);
            // save our new word
            noteView.getAction().accept(NoteMessage.ADD_WORD_TO_DICT);
            // send message for note interactor to add the word to stored file
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA); // Re-run spell-check
            noteModel.contextMenuProperty().get().hide();
        });
        noteModel.contextMenuProperty().get().getItems().add(addToDict);

        // Show context menu at mouse position
        noteModel.contextMenuProperty().get().show(noteModel.issueAreaProperty().get(), event.getScreenX(), event.getScreenY());
    }
}

