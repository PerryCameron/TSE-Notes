package com.L2.controls;

import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.OptionalInt;

public class SpellCheckArea extends CodeArea   {
    // Create a new CodeArea instance
    private final NoteView noteView;
    private final NoteModel noteModel;
    private static final Logger logger = LoggerFactory.getLogger(SpellCheckArea.class);
    private NoteMessage computeHighlight = null;
    private ObjectProperty<CodeArea> areaObjectProperty;

    public SpellCheckArea(NoteView noteView, double height, ObjectProperty<CodeArea> areaObjectProperty, StringProperty stringProperty) {  //There is no parameterless constructor available in 'org. fxmisc. flowless. VirtualizedScrollPane'
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.areaObjectProperty = areaObjectProperty;
        areaObjectProperty.setValue(this);
        // Enable text wrapping in the CodeArea
        this.setWrapText(true);
        // Adjust based on your needs
        this.setPrefHeight(height);
        // Set the mouse hover event handler
        this.setOnMouseMoved(this::handleMouseHover);
        // Set the font style for the CodeArea
        this.setStyle("-fx-font-family: '" + Font.getDefault().getFamily() + "';");
        // Apply the "code-area" style class to the CodeArea
        this.getStyleClass().add("code-area"); // Apply the style class
        // Create a bridge property to sync CodeArea text with the model
        StringProperty bridgeProperty = new SimpleStringProperty();
        // Bind the bridge property bidirectionally to the issue property of the model
        bridgeProperty.bindBidirectional(stringProperty);  // this must change
        // Sync the initial text of the CodeArea with the bridge property
        this.replaceText(bridgeProperty.getValue());
        // Update the bridge property when the text in the CodeArea changes
        this.textProperty().addListener((obs, oldVal, newVal) -> bridgeProperty.set(newVal));
        // Update the CodeArea text when the bridge property changes
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

        // only blocks outer ScrollPane if inner has enough text for a ScrollPane
        this.addEventFilter(ScrollEvent.SCROLL, event -> {
            ScrollPane outerScrollPane = noteModel.noteScrollPaneProperty().get();
            if (outerScrollPane == null) return; // Safety check

            // Check if CodeArea needs to scroll
            double totalContentHeight = this.totalHeightEstimateProperty().getValue();
            double visibleHeight = this.getHeight();
            boolean codeAreaScrollable = totalContentHeight > visibleHeight;

            if (!codeAreaScrollable) {
                // Pass scroll event to outer ScrollPane
                double deltaY = event.getDeltaY();
                double currentVvalue = outerScrollPane.getVvalue();
                double vMax = outerScrollPane.getVmax();
                double vMin = outerScrollPane.getVmin();

                double scrollAmount = deltaY * 0.005; // Adjustable multiplier
                double newVvalue = currentVvalue - scrollAmount;
                newVvalue = Math.max(vMin, Math.min(vMax, newVvalue));

                outerScrollPane.setVvalue(newVvalue);
                event.consume(); // Prevent VirtualizedScrollPane from handling it
            }
            // If codeAreaScrollable is true, let VirtualizedScrollPane handle it
        });
    }

    public void setComputeHighlight(NoteMessage message) {
        this.computeHighlight = message;
    }

    public CodeArea getCodeArea() {
        return this;
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
                Character.isLetterOrDigit(text.charAt(actualWordEnd)))) {                     // Slashes (e.g., TCP/IP)
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
