package com.L2.controls;

import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleLineSpellCheckArea extends CodeArea {
    private final NoteView noteView;
    private final NoteModel noteModel;
    private static final Logger logger = LoggerFactory.getLogger(SingleLineSpellCheckArea.class);
    private NoteMessage computeHighlight = null;
    private ObjectProperty<CodeArea> areaObjectProperty;

    public SingleLineSpellCheckArea(NoteView noteView, ObjectProperty<CodeArea> areaObjectProperty, StringProperty stringProperty) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.areaObjectProperty = areaObjectProperty;
        areaObjectProperty.setValue(this);

        // Disable wrapping and enforce single-line behavior
        this.setWrapText(false);
        this.setPrefHeight(40);
        this.setMaxHeight(40);
        this.setMinHeight(40);

        // Attempt to disable scrolling
        this.setStyle("-fx-font-family: '" + Font.getDefault().getFamily() + "'; -fx-padding: 6;");
        this.getStyleClass().add("code-area");

        // Block Enter key and newlines more aggressively
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                event.consume();
            }
        });
        this.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.contains("\n") || newVal.contains("\r")) {
                this.replaceText(newVal.replaceAll("[\n\r]", ""));
            }
        });

        // Set mouse hover for spell-check context menu
        this.setOnMouseMoved(this::handleMouseHover);

        // Sync with stringProperty
        StringProperty bridgeProperty = new SimpleStringProperty();
        bridgeProperty.bindBidirectional(stringProperty);
        this.replaceText(bridgeProperty.getValue());
        this.textProperty().addListener((obs, oldVal, newVal) -> bridgeProperty.set(newVal));
        bridgeProperty.addListener((obs, oldVal, newVal) -> {
            if (!newVal.equals(this.getText())) {
                this.replaceText(newVal);
            }
        });

        // Spell-checking with highlighting
        if (noteModel.hunspellProperty().get() != null) {
            noteModel.spellCheckSubscriptionProperty().setValue(this.multiPlainChanges()
                    .successionEnds(java.time.Duration.ofMillis(500))
                    .subscribe(ignore -> highlightMisspelledWords()));
        }
    }

    public void setComputeHighlight(NoteMessage message) {
        this.computeHighlight = message;
    }

    // Highlight misspelled words
    private void highlightMisspelledWords() {
        if (noteModel.hunspellProperty().get() == null) return;

        String text = this.getText();
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        Pattern wordPattern = Pattern.compile("[\\p{L}\\p{N}'-/]+");
        Matcher matcher = wordPattern.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastEnd); // Non-word segment
            }
            String word = matcher.group();
            boolean isCorrect = noteModel.hunspellProperty().get().spell(word);
            if (!isCorrect) {
                spansBuilder.add(Collections.singleton("misspelled"), word.length()); // Style misspelled words
            } else {
                spansBuilder.add(Collections.emptyList(), word.length()); // No style for correct words
            }
            lastEnd = matcher.end();
        }
        if (lastEnd < text.length()) {
            spansBuilder.add(Collections.emptyList(), text.length() - lastEnd); // Remaining text
        }

        StyleSpans<Collection<String>> spans = spansBuilder.create();
        this.setStyleSpans(0, spans);
    }

    private void handleMouseHover(MouseEvent event) {
        if (noteModel.hunspellProperty().get() == null) return;

        OptionalInt charIndexOpt = areaObjectProperty.get().hit(event.getX(), event.getY()).getCharacterIndex();
        if (!charIndexOpt.isPresent()) {
//            this.contextMenu.hide();

            return;
        }
        int charIndex = charIndexOpt.getAsInt();
        if (charIndex < 0 || charIndex >= areaObjectProperty.get().getText().length()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        String text = areaObjectProperty.get().getText();
        int wordStart = text.lastIndexOf(" ", charIndex) + 1;
        int wordEnd = text.indexOf(" ", charIndex);
        if (wordEnd == -1) wordEnd = text.length();
        if (wordStart >= wordEnd) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        int actualWordEnd = wordStart;
        while (actualWordEnd < wordEnd && (Character.isLetterOrDigit(text.charAt(actualWordEnd)) ||
                text.charAt(actualWordEnd) == '\'' ||
                text.charAt(actualWordEnd) == '-' ||
                text.charAt(actualWordEnd) == '/')) {
            actualWordEnd++;
        }
        String word = text.substring(wordStart, actualWordEnd);
        String cleanWord = word.replaceAll("[^\\p{L}\\p{N}'-/]", ""); // Keep slashes

        if (cleanWord.isEmpty()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        boolean isSpelledCorrectly = noteModel.hunspellProperty().get().spell(cleanWord);
        logger.debug("Word '{}': spelled correctly = {}, suggestions = {}", cleanWord, isSpelledCorrectly, noteModel.hunspellProperty().get().suggest(cleanWord));

        if (isSpelledCorrectly) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        noteModel.contextMenuProperty().get().getItems().clear();
        List<String> suggestions = noteModel.hunspellProperty().get().suggest(cleanWord);
        if (!suggestions.isEmpty()) {
            for (String suggestion : suggestions) {
                if (!suggestion.equals(cleanWord)) {
                    MenuItem item = new MenuItem(suggestion);
                    final int finalWordStart = wordStart;
                    final int finalWordEnd = actualWordEnd;
                    item.setOnAction(e -> {
                        areaObjectProperty.get().replaceText(finalWordStart, finalWordEnd, suggestion);
                        noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
                        noteModel.contextMenuProperty().get().hide();
                    });
                    noteModel.contextMenuProperty().get().getItems().add(item);
                }
            }
        }

        MenuItem addToDict = new MenuItem("Add to Dictionary");
        addToDict.setOnAction(e -> {
            noteModel.hunspellProperty().get().add(cleanWord); // Add "S/N" or "TCP/IP" with slashes
            noteModel.newWordProperty().set(cleanWord);
            noteView.getAction().accept(NoteMessage.ADD_WORD_TO_DICT);
            noteView.getAction().accept(computeHighlight);
            noteModel.contextMenuProperty().get().hide();
        });
        noteModel.contextMenuProperty().get().getItems().add(addToDict);

        noteModel.contextMenuProperty().get().show(areaObjectProperty.get(), event.getScreenX(), event.getScreenY());
    }
}