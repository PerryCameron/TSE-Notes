package com.L2.controls;

import com.L2.dto.NoteDTO;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.static_tools.NoteDTOProcessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
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

public class SpellCheckArea extends VirtualizedScrollPane<CodeArea>   {
    // Create a new CodeArea instance
    private final CodeArea codeArea;
    private final NoteView noteView;
    private final NoteModel noteModel;
    private static final Logger logger = LoggerFactory.getLogger(SpellCheckArea.class);
    private final ContextMenu contextMenu;

    public SpellCheckArea(NoteView noteView, double height) {  //There is no parameterless constructor available in 'org. fxmisc. flowless. VirtualizedScrollPane'
        super(new CodeArea());
        this.codeArea = getContent();
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.contextMenu = new ContextMenu();
        this.contextMenu.setStyle("-fx-font-family: '" + Font.getDefault().getFamily() + "';");
        // Enable text wrapping in the CodeArea
        codeArea.setWrapText(true);
        // Adjust based on your needs
        codeArea.setPrefHeight(height);
        // Set the mouse hover event handler
        codeArea.setOnMouseMoved(this::handleMouseHover);
        // Set the font style for the CodeArea
        codeArea.setStyle("-fx-font-family: '" + Font.getDefault().getFamily() + "';");
        // Apply the "code-area" style class to the CodeArea
        codeArea.getStyleClass().add("code-area"); // Apply the style class
        // Create a bridge property to sync CodeArea text with the model
        StringProperty bridgeProperty = new SimpleStringProperty();
        // Bind the bridge property bidirectionally to the issue property of the model
        bridgeProperty.bindBidirectional(noteModel.getBoundNote().issueProperty());
        // Sync the initial text of the CodeArea with the bridge property
        codeArea.replaceText(bridgeProperty.getValue());
        // Update the bridge property when the text in the CodeArea changes
        codeArea.textProperty().addListener((obs, oldVal, newVal) -> bridgeProperty.set(newVal));
        // Update the CodeArea text when the bridge property changes
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
                // Check if the bound note is not an email
                if (!noteModel.getBoundNote().isEmail()) {
                    // Process the text as an email if it matches email format
                    if (NoteDTOProcessor.isEmail(codeArea.getText())) {
                        NoteDTO noteDTO = NoteDTOProcessor.processEmail(
                                codeArea.getText(),
                                noteModel.getBoundNote().getId()
                        );
                        // Update the bound note with the processed email data
                        noteModel.getBoundNote().copyFrom(noteDTO);
                        logger.info("Processed an email and updated the note model.");
                        // Set the CodeArea to read-only
                        codeArea.setEditable(false);
                    }
                    // Trigger save or update note action
                    noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
                }
            }
        });

        // only blocks outer ScrollPane if inner has enough text for a ScrollPane
        codeArea.addEventFilter(ScrollEvent.SCROLL, event -> {
            ScrollPane outerScrollPane = noteModel.noteScrollPaneProperty().get();
            if (outerScrollPane == null) return; // Safety check

            // Check if CodeArea needs to scroll
            double totalContentHeight = this.totalHeightEstimateProperty().getValue();
            double visibleHeight = codeArea.getHeight();
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

    public CodeArea getCodeArea() {
        return codeArea;
    }

    private void handleMouseHover(MouseEvent event) {
        if (noteModel.hunspellProperty().get() == null) return;

        OptionalInt charIndexOpt = noteModel.issueAreaProperty().get().hit(event.getX(), event.getY()).getCharacterIndex();
        if (!charIndexOpt.isPresent()) {
            this.contextMenu.hide();
            return;
        }
        int charIndex = charIndexOpt.getAsInt();
        if (charIndex < 0 || charIndex >= noteModel.issueAreaProperty().get().getText().length()) {
            this.contextMenu.hide();
            return;
        }

        String text = noteModel.issueAreaProperty().get().getText();
        int wordStart = text.lastIndexOf(" ", charIndex) + 1;
        int wordEnd = text.indexOf(" ", charIndex);
        if (wordEnd == -1) wordEnd = text.length();
        if (wordStart >= wordEnd) {
            this.contextMenu.hide();
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
            this.contextMenu.hide();
            return;
        }

        boolean isSpelledCorrectly = noteModel.hunspellProperty().get().spell(cleanWord);
        logger.debug("Word '{}': spelled correctly = {}, suggestions = {}", cleanWord, isSpelledCorrectly, noteModel.hunspellProperty().get().suggest(cleanWord));

        if (isSpelledCorrectly) {
            this.contextMenu.hide();
            return;
        }

        this.contextMenu.getItems().clear();
        List<String> suggestions = noteModel.hunspellProperty().get().suggest(cleanWord);
        if (!suggestions.isEmpty()) {
            for (String suggestion : suggestions) {
                if (!suggestion.equals(cleanWord)) {
                    MenuItem item = new MenuItem(suggestion);
                    final int finalWordStart = wordStart;
                    final int finalWordEnd = actualWordEnd;
                    item.setOnAction(e -> {
                        noteModel.issueAreaProperty().get().replaceText(finalWordStart, finalWordEnd, suggestion);
                        this.contextMenu.hide();
                    });
                    this.contextMenu.getItems().add(item);
                }
            }
        }

        MenuItem addToDict = new MenuItem("Add to Dictionary");
        addToDict.setOnAction(e -> {
            noteModel.hunspellProperty().get().add(cleanWord); // Add "S/N" or "TCP/IP" with slashes
            noteModel.newWordProperty().set(cleanWord);
            noteView.getAction().accept(NoteMessage.ADD_WORD_TO_DICT);
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA);
            this.contextMenu.hide();
        });
        this.contextMenu.getItems().add(addToDict);

        this.contextMenu.show(noteModel.issueAreaProperty().get(), event.getScreenX(), event.getScreenY());
    }
}
