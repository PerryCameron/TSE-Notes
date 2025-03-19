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
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.OptionalInt;

public class SingleLineSpellCheckArea extends CodeArea   {
    private final NoteView noteView;
    private final NoteModel noteModel;
    private static final Logger logger = LoggerFactory.getLogger(SingleLineSpellCheckArea.class);
    private NoteMessage computeHighlight = null;
    private ObjectProperty<CodeArea> areaObjectProperty;

    public SingleLineSpellCheckArea(NoteView noteView, ObjectProperty<CodeArea> areaObjectProperty, StringProperty stringProperty) {  //There is no parameterless constructor available in 'org. fxmisc. flowless. VirtualizedScrollPane'
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.areaObjectProperty = areaObjectProperty;
        areaObjectProperty.setValue(this);
        // Enable text wrapping in the CodeArea
        this.setWrapText(true);
        // Adjust based on your needs
        this.setPrefHeight(50);
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
        // Update the this text when the bridge property changes
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
// I am making this class, so that I can spell check the subject,  I need