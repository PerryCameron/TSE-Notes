package com.L2.controls;

import com.L2.enums.AreaType;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;

public class SpellCheckArea extends CodeArea {
    // Create a new CodeArea instance
    private final NoteView noteView;
    private final NoteModel noteModel;
    private final AreaType areaType;
    private static final Logger logger = LoggerFactory.getLogger(SpellCheckArea.class);
    private NoteMessage computeHighlight = null;

    // this is our textArea instance
    public SpellCheckArea(NoteView noteView, StringProperty stringProperty, AreaType areaType) {  //There is no parameterless constructor available in 'org. fxmisc. flowless. VirtualizedScrollPane'
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.areaType = areaType;
        double height = 0;
        this.setStyle("-fx-font-family: '" + Font.getDefault().getFamily() + "';");
        switch (areaType) {
            case subject -> {
                height = 40;
                this.setWrapText(false);
                this.getStyleClass().add("code-area-single");
                removeReturns();
                this.computeHighlight = NoteMessage.COMPUTE_HIGHLIGHTING_SUBJECT_AREA;
            }
            case issue -> {
                height = 200;
                setUpArea();
                this.computeHighlight = NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA;
            }
            case finish -> {
                height = 100;
                setUpArea();
                this.computeHighlight = NoteMessage.COMPUTE_HIGHLIGHTING_FINISH_AREA;
            }
        }
        this.setPrefHeight(height);
        this.setMaxHeight(height);
        this.setMinHeight(height);
        this.setOnMouseMoved(this::handleMouseHover);
        setBridgeToBind(stringProperty);
        setUpSpellCheckingWithDebounce();
    }

    private void setUpArea() {
        this.setWrapText(true);
        this.getStyleClass().add("code-area");
        applyScrollingRules();
    }

    private void removeReturns() {
        // Block Enter key
        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });
        // Removes hard returns
        this.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.contains("\n") || newVal.contains("\r")) {
                this.replaceText(newVal.replaceAll("[\n\r]", ""));
            }
        });
    }

    private void setBridgeToBind(StringProperty stringProperty) {
        StringProperty bridgeProperty = new SimpleStringProperty();
        // Bind the bridge property bidirectionally to the issue property of the model
        bridgeProperty.bindBidirectional(stringProperty);  // this must change
        // Sync the initial text of the CodeArea with the bridge property
        // Sync the initial text of the CodeArea with the bridge property, defaulting to "" if null
        String initialText = bridgeProperty.getValue();
        this.replaceText(initialText != null ? initialText : "");  // Line 95 fixed
        // Update the bridge property when the text in the CodeArea changes
        this.textProperty().addListener((obs, oldVal, newVal) -> bridgeProperty.set(newVal));
        // Update the CodeArea text when the bridge property changes
        bridgeProperty.addListener((obs, oldVal, newVal) -> {
            if (!newVal.equals(this.getText())) {
                this.replaceText(newVal);
            }
        });
    }

    private void setUpSpellCheckingWithDebounce() {
        if (noteModel.hunspellProperty().get() != null) {
            noteModel.spellCheckSubscriptionProperty().setValue(this.multiPlainChanges()
                    .successionEnds(Duration.ofMillis(500)) // Debounce 500ms
                    .subscribe(ignore -> noteView.getAction().accept(computeHighlight)));
        }
    }

    private void applyScrollingRules() {
        // Configures scroll event handling to manage interaction between inner CodeArea
        // and outer ScrollPane, only blocking outer scroll when inner content needs it
        this.addEventFilter(ScrollEvent.SCROLL, event -> {
            // Get reference to the outer ScrollPane from noteModel
            ScrollPane outerScrollPane = noteModel.noteScrollPaneProperty().get();

            // Safety check: exit if outer ScrollPane isn't available
            if (outerScrollPane == null) return;

            // Calculate if CodeArea has enough content to require scrolling
            double totalContentHeight = this.totalHeightEstimateProperty().getValue(); // Estimated height of all content
            double visibleHeight = this.getHeight(); // Visible area of CodeArea
            boolean codeAreaScrollable = totalContentHeight > visibleHeight; // True if content exceeds visible area

            // If CodeArea doesn't need scrolling, delegate scroll to outer ScrollPane
            if (!codeAreaScrollable) {
                // Get scroll wheel movement direction and magnitude
                double deltaY = event.getDeltaY();

                // Get current scroll position and bounds of outer ScrollPane
                double currentVvalue = outerScrollPane.getVvalue(); // Current vertical scroll position (0.0 to 1.0)
                double vMax = outerScrollPane.getVmax(); // Maximum scroll position
                double vMin = outerScrollPane.getVmin(); // Minimum scroll position (usually 0.0)

                // Calculate new scroll position based on wheel movement
                double scrollAmount = deltaY * 0.005; // Multiplier to adjust scroll sensitivity
                double newVvalue = currentVvalue - scrollAmount; // Proposed new scroll position

                // Clamp the new scroll position to valid range [vMin, vMax]
                newVvalue = Math.max(vMin, Math.min(vMax, newVvalue));

                // Apply the calculated scroll position to outer ScrollPane
                outerScrollPane.setVvalue(newVvalue);

                // Consume the event to prevent inner VirtualizedScrollPane from processing it
                event.consume();
            }
            // If codeAreaScrollable is true, event passes through to VirtualizedScrollPane
            // for default inner scrolling behavior
        });
    }

    public CodeArea getCodeArea() {
        return this;
    }

    private void handleMouseHover(MouseEvent event) {
        // Check if spell-check is enabled first
        if (noteModel.hunspellProperty().get() == null) {
            logger.debug("Hunspell not initialized");
            return;
        }

        OptionalInt charIndexOpt = this.hit(event.getX(), event.getY()).getCharacterIndex();
        if (!charIndexOpt.isPresent()) {
            logger.debug("No character index at ({}, {})", event.getX(), event.getY());
            return;
        }

        int charIndex = charIndexOpt.getAsInt();
        if (charIndex < 0 || charIndex >= this.getText().length()) {
            logger.debug("Invalid charIndex: {}", charIndex);
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        StyleSpans<Collection<String>> spans = switch (areaType) {
            case subject -> noteModel.subjectSpansProperty().get();
            case issue -> noteModel.issueSpansProperty().get();
            case finish -> noteModel.finishSpansProperty().get();
        };

        if (spans == null) {
            logger.debug("No spans available for {}", areaType);
            return;
        }

        int position = 0;
        int wordStart = -1;
        int wordEnd = -1;
        String cleanWord = null;
        boolean isMisspelled = false;

        for (StyleSpan<Collection<String>> span : spans) {
            int spanEnd = position + span.getLength();
            if (charIndex >= position && charIndex < spanEnd) {
                if (span.getStyle().contains("misspelled")) {
                    wordStart = position;
                    wordEnd = spanEnd;
                    cleanWord = this.getText().substring(wordStart, wordEnd)
                            .replaceAll("[^\\p{L}\\p{N}'-/]", "");
                    isMisspelled = true;
                }
                break;
            }
            position += span.getLength();
        }

        if (!isMisspelled) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        ContextMenu contextMenu = noteModel.contextMenuProperty().get();
        contextMenu.getItems().clear();
        List<String> suggestions = noteModel.hunspellProperty().get().suggest(cleanWord);
        logger.debug("Word '{}': suggestions={}", cleanWord, suggestions);

        if (!suggestions.isEmpty()) {
            for (String suggestion : suggestions) {
                if (!suggestion.equals(cleanWord)) {
                    MenuItem item = new MenuItem(suggestion);
                    final int finalWordStart = wordStart;
                    final int finalWordEnd = wordEnd;
                    item.setOnAction(e -> {
                        this.replaceText(finalWordStart, finalWordEnd, suggestion);
                        noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
                        contextMenu.hide();
                    });
                    contextMenu.getItems().add(item);
                }
            }
        }

        // Add the "Add to Dictionary" menu item using the provided method
        MenuItem addToDict = getMenuItem(cleanWord);
        contextMenu.getItems().add(addToDict);

        logger.debug("Showing context menu with {} items", noteModel.contextMenuProperty().get().getItems().size());

        // Show the context menu
        contextMenu.show(this, event.getScreenX(), event.getScreenY());
    }

    private MenuItem getMenuItem(String cleanWord) {
        MenuItem addToDict = new MenuItem("Add to Dictionary");

        // Set the text color to green using an inline style
        addToDict.setStyle("-fx-text-fill: green;");

        String finalCleanWord = cleanWord;
        addToDict.setOnAction(e -> {
            noteModel.hunspellProperty().get().add(finalCleanWord);
            noteModel.newWordProperty().set(finalCleanWord);
            noteView.getAction().accept(NoteMessage.ADD_WORD_TO_DICT);
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_SUBJECT_AREA);
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA);
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_FINISH_AREA);
            noteModel.contextMenuProperty().get().hide();
        });
        return addToDict;
    }
}
