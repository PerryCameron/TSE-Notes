package com.L2.controls;

import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

        // Set mouse hover for spell-check context menu
        this.setOnMouseMoved(this::handleMouseHover);

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

        // Setup spell-checking with debounce
        if (noteModel.hunspellProperty().get() != null) {
            noteModel.spellCheckSubscriptionProperty().setValue(this.multiPlainChanges()
                    .successionEnds(java.time.Duration.ofMillis(500)) // Debounce 500ms
                    .subscribe(ignore -> noteView.getAction().accept(computeHighlight)));
        }
    }

    public void setComputeHighlight(NoteMessage message) {
        this.computeHighlight = message;
    }

    private void handleMouseHover(MouseEvent event) {
        // Early return if spell checker (Hunspell) is not initialized
        if (noteModel.hunspellProperty().get() == null) return;

        // Get the character index where the mouse is hovering
        OptionalInt charIndexOpt = areaObjectProperty.get().hit(event.getX(), event.getY()).getCharacterIndex();

        // If no valid character index is found, exit the method
        if (!charIndexOpt.isPresent()) {
            return;
        }

        // Get the actual character index and validate it's within text bounds
        int charIndex = charIndexOpt.getAsInt();
        if (charIndex < 0 || charIndex >= areaObjectProperty.get().getText().length()) {
            noteModel.contextMenuProperty().get().hide();  // Hide context menu if index is invalid
            return;
        }

        // Get the full text content and find word boundaries around the hovered character
        String text = areaObjectProperty.get().getText();
        int wordStart = text.lastIndexOf(" ", charIndex) + 1;  // Start of word (after previous space)
        int wordEnd = text.indexOf(" ", charIndex);            // End of word (next space)
        if (wordEnd == -1) wordEnd = text.length();           // If no space found, use text end
        if (wordStart >= wordEnd) {                           // Invalid word boundaries
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Refine the word end to only include valid word characters
        int actualWordEnd = wordStart;
        while (actualWordEnd < wordEnd && (
                Character.isLetterOrDigit(text.charAt(actualWordEnd))
//                        ||  // Letters and numbers
//                        text.charAt(actualWordEnd) == '\'' ||                     // Apostrophes (e.g., don't)
//                        text.charAt(actualWordEnd) == '-' ||                      // Hyphens (e.g., well-known)
//                        text.charAt(actualWordEnd) == '/'
        )) {                     // Slashes (e.g., TCP/IP)
            actualWordEnd++;
        }

        // Extract the word and create a cleaned version
        String word = text.substring(wordStart, actualWordEnd);
        String cleanWord = word.replaceAll("[^\\p{L}\\p{N}'-/]", ""); // Remove all but letters, numbers, ', -, /

        // If the cleaned word is empty, hide menu and exit
        if (cleanWord.isEmpty()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Check if the word is spelled correctly using Hunspell
        boolean isSpelledCorrectly = noteModel.hunspellProperty().get().spell(cleanWord);
        logger.debug("Word '{}': spelled correctly = {}, suggestions = {}",
                cleanWord, isSpelledCorrectly, noteModel.hunspellProperty().get().suggest(cleanWord));

        // If spelled correctly, hide menu and exit
        if (isSpelledCorrectly) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Clear existing context menu items to prepare for new suggestions
        noteModel.contextMenuProperty().get().getItems().clear();

        // Get spelling suggestions from Hunspell
        List<String> suggestions = noteModel.hunspellProperty().get().suggest(cleanWord);
        if (!suggestions.isEmpty()) {
            // Add each suggestion as a menu item
            for (String suggestion : suggestions) {
                if (!suggestion.equals(cleanWord)) {  // Skip if suggestion matches original word
                    MenuItem item = new MenuItem(suggestion);
                    final int finalWordStart = wordStart;    // Capture word boundaries for replacement
                    final int finalWordEnd = actualWordEnd;

                    // Define action: replace word and save changes
                    item.setOnAction(e -> {
                        areaObjectProperty.get().replaceText(finalWordStart, finalWordEnd, suggestion);
                        noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);  // Trigger save
                        noteModel.contextMenuProperty().get().hide();                  // Hide menu
                    });
                    noteModel.contextMenuProperty().get().getItems().add(item);
                }
            }
        }

        // Create "Add to Dictionary" option
        MenuItem addToDict = new MenuItem("Add to Dictionary");
        addToDict.setOnAction(e -> {
            // Add word to Hunspell dictionary and update model
            noteModel.hunspellProperty().get().add(cleanWord);    // Accepts words with slashes
            noteModel.newWordProperty().set(cleanWord);          // Track new word
            noteView.getAction().accept(NoteMessage.ADD_WORD_TO_DICT);  // Notify dictionary update
            noteView.getAction().accept(computeHighlight);       // Refresh highlighting
            noteModel.contextMenuProperty().get().hide();        // Hide menu
        });
        noteModel.contextMenuProperty().get().getItems().add(addToDict);

        // Show the context menu at the mouse position
        noteModel.contextMenuProperty().get().show(areaObjectProperty.get(),
                event.getScreenX(), event.getScreenY());
    }
}