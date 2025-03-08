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
import com.nikialeksey.hunspell.Hunspell;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueBox implements Component<Region> {
    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox root;
    private final CodeArea codeAreaIssue; // Replacing TextArea with CodeArea
    private static final Logger logger = LoggerFactory.getLogger(IssueBox.class);


    public IssueBox(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.codeAreaIssue = getCodeAreaIssue();
    }

    private CodeArea getCodeAreaIssue() {
        CodeArea codeArea = new CodeArea();
        codeArea.setWrapText(true);
        HBox.setHgrow(codeArea, Priority.ALWAYS);
        codeArea.setPrefHeight(200); // Adjust based on your needs
        codeArea.setOnMouseMoved(this::handleMouseHover);
        codeArea.setStyle("-fx-font-family: '" + Font.getDefault().getFamily() + "';");
        codeArea.getStyleClass().add("code-area"); // Apply the style class
        return codeArea;
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
        noteView.getAction().accept(NoteMessage.INITALIZE_DICTIONARY);
        // this is our bridge, since CodeArea does not have native FX binding support
        StringProperty bridgeProperty = new SimpleStringProperty();

        // Bind bridge to model
        bridgeProperty.bindBidirectional(noteModel.getBoundNote().issueProperty());
        // Sync bridge with CodeArea
        codeAreaIssue.replaceText(bridgeProperty.getValue());
        codeAreaIssue.textProperty().addListener((obs, oldVal, newVal) -> bridgeProperty.set(newVal));
        bridgeProperty.addListener((obs, oldVal, newVal) -> {
            if (!newVal.equals(codeAreaIssue.getText())) {
                codeAreaIssue.replaceText(newVal);
            }
        });

        // Setup spell-checking with debounce
        if (noteModel.hunspellProperty().get() != null) {
            noteModel.spellCheckSubscriptionProperty().setValue(codeAreaIssue.multiPlainChanges()
                    .successionEnds(java.time.Duration.ofMillis(500)) // Debounce 500ms
                    .subscribe(ignore -> computeHighlighting()));
        }

        HBox iconBox = HBoxFx.iconBox(10);
        root.getChildren().addAll(TitleBarFx.of("Issue", iconBox), codeAreaIssue);
        refreshFields();

        // Retain focus listener logic
        codeAreaIssue.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                if (!noteModel.getBoundNote().isEmail()) {
                    if (NoteDTOProcessor.isEmail(codeAreaIssue.getText())) {
                        NoteDTO noteDTO = NoteDTOProcessor.processEmail(
                                codeAreaIssue.getText(),
                                noteModel.getBoundNote().getId()
                        );
                        noteModel.getBoundNote().copyFrom(noteDTO);
                        logger.info("Processed an email and updated the note model.");
                        // Set to read-only
                        codeAreaIssue.setEditable(false);
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
        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(0.2));
        pause.setOnFinished(event -> root.setStyle(""));
        pause.play();
    }

    @Override
    public void refreshFields() {
        codeAreaIssue.setEditable(!noteModel.getBoundNote().isEmail());
    }

    private void handleMouseHover(MouseEvent event) {
        if (noteModel.hunspellProperty().get() == null) return;
        // Get character index under mouse
        OptionalInt charIndexOpt = codeAreaIssue.hit(event.getX(), event.getY()).getCharacterIndex();
        if (!charIndexOpt.isPresent()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }
        int charIndex = charIndexOpt.getAsInt();
        if (charIndex < 0 || charIndex >= codeAreaIssue.getText().length()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Get word at charIndex
        String text = codeAreaIssue.getText();
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
                    codeAreaIssue.replaceText(finalWordStart, finalWordEnd, suggestion);
                    noteModel.contextMenuProperty().get().hide();
                });
                noteModel.contextMenuProperty().get().getItems().add(item);
            }
        }

        // Add "Add to Dictionary" option
        MenuItem addToDict = new MenuItem("Add to Dictionary");
        addToDict.setOnAction(e -> {
            noteModel.hunspellProperty().get().add(word);
            computeHighlighting(); // Re-run spell-check
            noteModel.contextMenuProperty().get().hide();
        });
        noteModel.contextMenuProperty().get().getItems().add(addToDict);

        // Show context menu at mouse position
        noteModel.contextMenuProperty().get().show(codeAreaIssue, event.getScreenX(), event.getScreenY());
    }

    private void computeHighlighting() {
        if (noteModel.hunspellProperty().get() == null) return;

        String text = codeAreaIssue.getText();
        if (text.isEmpty()) {
            codeAreaIssue.setStyleSpans(0, new StyleSpansBuilder<Collection<String>>().create());
            return;
        }

        new Thread(() -> {
            StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
            int totalLength = 0;
            int i = 0;

            while (i < text.length()) {
                // Skip whitespace
                int start = i;
                while (i < text.length() && Character.isWhitespace(text.charAt(i))) {
                    i++;
                }
                if (i > start) {
                    spansBuilder.add(Collections.emptyList(), i - start);
                    totalLength += i - start;
                }

                // Find word boundaries (including trailing punctuation)
                start = i;
                while (i < text.length() && !Character.isWhitespace(text.charAt(i))) {
                    i++;
                }

                if (start < i) { // We have a word (possibly with punctuation)
                    String rawWord = text.substring(start, i); // e.g., "fox,"
                    // Strip punctuation for spell-checking
                    String cleanWord = rawWord.replaceAll("[^\\p{L}\\p{N}]", ""); // Keep only letters and numbers
                    if (!cleanWord.isEmpty() && !noteModel.hunspellProperty().get().spell(cleanWord)) {
                        // Highlight the full raw word (including punctuation)
                        spansBuilder.add(Collections.singleton("misspelled"), rawWord.length());
                        totalLength += rawWord.length();
                    } else {
                        spansBuilder.add(Collections.emptyList(), rawWord.length());
                        totalLength += rawWord.length();
                    }
                }
            }

            // Add any trailing whitespace
            if (totalLength < text.length()) {
                spansBuilder.add(Collections.emptyList(), text.length() - totalLength);
                totalLength = text.length();
            }

            StyleSpans<Collection<String>> spans = spansBuilder.create();
            Platform.runLater(() -> codeAreaIssue.setStyleSpans(0, spans));
        }).start();
    }


    // Cleanup (optional, if IssueBox is reused or disposed)
    public void dispose() {
        if (noteModel.spellCheckSubscriptionProperty().get() != null) {
            noteModel.spellCheckSubscriptionProperty().get().unsubscribe();
        }
        if (noteModel.hunspellProperty().get() != null) {
            noteModel.hunspellProperty().get().close();
        }
    }
}

