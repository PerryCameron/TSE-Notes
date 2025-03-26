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
import javafx.scene.input.*;
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

//        this.setOnMouseMoved(this::handleMouseHover);
        this.setOnMouseClicked(event -> {
            // Check if the event is a right-click (secondary button)
            if (event.getButton() == MouseButton.SECONDARY) {
                handleRightClick(event); // Call the handler for right-click events
            }
            else if (event.getButton() == MouseButton.PRIMARY) {
                handleLeftClick(event); // Call the handler for left-click events
            }
        });
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

    // Handles right-click events to show a context menu for misspelled words
    private void handleRightClick(MouseEvent event) {
        ContextMenu contextMenu = noteModel.contextMenuProperty().get();
        // Check if the Hunspell spell-check engine is initialized
        // If not, log a debug message and exit early
        if (noteModel.hunspellProperty().get() == null) {
            logger.debug("Hunspell not initialized");
            return;
        }

        // Get the character index under the mouse cursor using the hit() method
        // Returns an OptionalInt that may or may not contain a valid index
        OptionalInt charIndexOpt = this.hit(event.getX(), event.getY()).getCharacterIndex();

        // If no character index is present (e.g., clicked on an empty area), log it and exit
        if (charIndexOpt.isEmpty()) {
            logger.debug("No character index at ({}, {})", event.getX(), event.getY());
            contextMenu.hide(); // Hide any existing menu
            return;
        }

        // Extract the character index from the OptionalInt
        int charIndex = charIndexOpt.getAsInt();

        // Validate the character index to ensure it’s within the text bounds
        // If invalid, log it, hide the menu, and exit
        if (charIndex < 0 || charIndex >= this.getText().length()) {
            logger.debug("Invalid charIndex: {}", charIndex);
            contextMenu.hide();
            return;
        }

        // Fetch the latest StyleSpans based on the area type (subject, issue, or finish)
        // StyleSpans contains styling info, including which words are misspelled
        StyleSpans<Collection<String>> spans = switch (areaType) {
            case subject -> noteModel.subjectSpansProperty().get(); // Spans for subject area
            case issue -> noteModel.issueSpansProperty().get();     // Spans for issue area
            case finish -> noteModel.finishSpansProperty().get();   // Spans for finish area
        };

        // If no spans are available (e.g., text hasn’t been analyzed), log it, hide menu, and exit
        if (spans == null) {
            logger.debug("No spans available for {}", areaType);
            contextMenu.hide();
            return;
        }

        // Variables to track the position and boundaries of a misspelled word
        int position = 0;         // Current position in the text as we iterate through spans
        int wordStart = -1;       // Start index of the misspelled word
        int wordEnd = -1;         // End index of the misspelled word
        String cleanWord = null;  // The cleaned-up misspelled word for spell-check
        boolean isMisspelled = false; // Flag indicating if the clicked character is misspelled

        // Iterate through the StyleSpans to find the span under the cursor
        for (StyleSpan<Collection<String>> span : spans) {
            int spanEnd = position + span.getLength(); // End position of the current span

            // Check if the character index falls within this span
            if (charIndex >= position && charIndex < spanEnd) {
                // If the span is marked as misspelled, extract the word details
                if (span.getStyle().contains("misspelled")) {
                    wordStart = position;
                    wordEnd = spanEnd;
                    // Extract and clean the word, removing non-letter/number characters except ' - /
                    cleanWord = this.getText().substring(wordStart, wordEnd)
                            .replaceAll("[^\\p{L}\\p{N}'-/]", "");
                    isMisspelled = true;
                }
                break; // Exit loop once the relevant span is found
            }
            position += span.getLength(); // Advance position for the next span
        }

        // If the clicked character isn’t in a misspelled word, hide the menu and exit
        if (!isMisspelled) {
            contextMenu.hide();
            return;
        }

        // Clear the context menu to prepare for fresh suggestions
        contextMenu.getItems().clear();

        // Get spelling suggestions for the misspelled word from Hunspell
        List<String> suggestions = noteModel.hunspellProperty().get().suggest(cleanWord);
        logger.debug("Word '{}': suggestions={}", cleanWord, suggestions);

        // Populate the context menu with suggestions if any exist
        if (!suggestions.isEmpty()) {
            for (String suggestion : suggestions) {
                // Skip if the suggestion matches the original word
                if (!suggestion.equals(cleanWord)) {
                    MenuItem item = new MenuItem(suggestion); // Create a menu item for the suggestion
                    final int finalWordStart = wordStart;     // Final for use in lambda
                    final int finalWordEnd = wordEnd;         // Final for use in lambda

                    // Define action: replace the word and save the note
                    item.setOnAction(e -> {
                        this.replaceText(finalWordStart, finalWordEnd, suggestion);
                        noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
                        contextMenu.hide(); // Hide the menu after selection
                    });
                    contextMenu.getItems().add(item); // Add the suggestion to the menu
                }
            }
        }
        // Add an "Add to Dictionary" option to the menu
        MenuItem addToDict = getMenuItem(cleanWord);
        contextMenu.getItems().add(addToDict);

        // Log the number of items prepared for the menu
        logger.debug("Showing context menu with {} items", contextMenu.getItems().size());

        // Show the context menu immediately at the click location
        contextMenu.show(this, event.getScreenX(), event.getScreenY());
    }

    private MenuItem getMenuItem(String cleanWord) {
        MenuItem addToDict = new MenuItem("Add to Dictionary");

        // Set the text color to green using an inline style
        addToDict.setStyle("-fx-text-fill: green;");

        addToDict.setOnAction(e -> {
            noteModel.hunspellProperty().get().add(cleanWord);
            noteModel.newWordProperty().set(cleanWord);
            noteView.getAction().accept(NoteMessage.ADD_WORD_TO_DICT);
            // re-span all areas
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_SUBJECT_AREA);
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA);
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_FINISH_AREA);
            noteModel.contextMenuProperty().get().hide();
        });
        return addToDict;
    }

    // Handles left-click events to hide the context menu if clicked outside it
    private void handleLeftClick(MouseEvent event) {
        ContextMenu contextMenu = noteModel.contextMenuProperty().get();
        // If the context menu isn’t showing, no action is needed
        if (!contextMenu.isShowing()) {
            return;
        }

        // Get the screen coordinates of the left-click
        double clickX = event.getScreenX();
        double clickY = event.getScreenY();

        // Get the bounds of the context menu in screen coordinates
        double menuX = contextMenu.getX();
        double menuY = contextMenu.getY();
        double menuWidth = contextMenu.getWidth();
        double menuHeight = contextMenu.getHeight();

        // Check if the click is outside the context menu’s bounds
        boolean isOutsideMenu = clickX < menuX || clickX > (menuX + menuWidth) ||
                clickY < menuY || clickY > (menuY + menuHeight);

        // If the click is outside, hide the context menu
        if (isOutsideMenu) {
            contextMenu.hide();
            logger.debug("Left-click outside context menu at ({}, {}), hiding menu", clickX, clickY);
        }
    }
}




// This method works on a delay, it works beautiful but then I realized that a right click was better
// but it was too nice to delete, may use it again some day.
//    // A PauseTransition to delay showing the context menu by 200 milliseconds
//    private final PauseTransition contextMenuDelay = new PauseTransition(javafx.util.Duration.millis(200));
//    // Flag to track whether the context menu is currently visible
//    private boolean contextMenuVisible = false;
//
//    // Handles mouse hover events to show a context menu for misspelled words
//    private void handleMouseHover(MouseEvent event) {
//        // Stop any ongoing delay to reset the timer if the mouse moves
//        contextMenuDelay.stop();
//        // Reference to the ContextMenu, stored as a field for reuse across hover events
//        ContextMenu contextMenu = noteModel.contextMenuProperty().get();
//        // Check if the Hunspell spell-check engine is initialized
//        // If not, log a debug message and exit early
//        if (noteModel.hunspellProperty().get() == null) {
//            logger.debug("Hunspell not initialized");
//            return;
//        }
//        // Get the character index under the mouse cursor using the hit() method
//        // Returns an OptionalInt that may or may not contain a valid index
//        OptionalInt charIndexOpt = this.hit(event.getX(), event.getY()).getCharacterIndex();
//        if (!charIndexOpt.isPresent()) {
//            logger.debug("No character index at ({}, {})", event.getX(), event.getY());
//            if (contextMenuVisible) {
//                contextMenu.hide();
//                contextMenuVisible = false;
//            }
//            return;
//        }
//        // Extract the character index from the OptionalInt
//        int charIndex = charIndexOpt.getAsInt();
//        // Validate the character index to ensure it’s within the text bounds
//        // If invalid, log it, hide the menu if visible, and exit
//        if (charIndex < 0 || charIndex >= this.getText().length()) {
//            logger.debug("Invalid charIndex: {}", charIndex);
//            if (contextMenuVisible) {
//                contextMenu.hide();
//                contextMenuVisible = false;
//            }
//            return;
//        }
//        // Fetch the latest StyleSpans based on the area type (subject, issue, or finish)
//        // StyleSpans contains styling info, including which words are misspelled
//        StyleSpans<Collection<String>> spans = switch (areaType) {
//            case subject -> noteModel.subjectSpansProperty().get();
//            case issue -> noteModel.issueSpansProperty().get();
//            case finish -> noteModel.finishSpansProperty().get();
//        };
//        // If no spans are available (e.g., text hasn’t been analyzed), log it, hide menu, and exit
//        if (spans == null) {
//            logger.debug("No spans available for {}", areaType);
//            if (contextMenuVisible) {
//                contextMenu.hide();
//                contextMenuVisible = false;
//            }
//            return;
//        }
//        // Variables to track the position and boundaries of a misspelled word
//        int position = 0;         // Current position in the text as we iterate through spans
//        int wordStart = -1;       // Start index of the misspelled word
//        int wordEnd = -1;         // End index of the misspelled word
//        String cleanWord = null;  // The cleaned-up misspelled word for spell-check
//        boolean isMisspelled = false; // Flag indicating if the hovered character is misspelled
//        // Iterate through the StyleSpans to find the span under the cursor
//        for (StyleSpan<Collection<String>> span : spans) {
//            int spanEnd = position + span.getLength();
//            // Check if the character index falls within this span
//            if (charIndex >= position && charIndex < spanEnd) {
//                // If the span is marked as misspelled, extract the word details
//                if (span.getStyle().contains("misspelled")) {
//                    wordStart = position;
//                    wordEnd = spanEnd;
//                    // Extract and clean the word, removing non-letter/number characters except ' - /
//                    cleanWord = this.getText().substring(wordStart, wordEnd)
//                            .replaceAll("[^\\p{L}\\p{N}'-/]", "");
//                    isMisspelled = true;
//                }
//                break; // Exit loop once the relevant span is found
//            }
//            position += span.getLength(); // Advance position for the next span
//        }
//        // If the character isn’t in a misspelled word, hide the menu if visible and exit
//        if (!isMisspelled) {
//            if (contextMenuVisible) {
//                contextMenu.hide();
//                contextMenuVisible = false;
//            }
//            return;
//        }
//        // Clear the context menu to prepare for fresh suggestions
//        contextMenu.getItems().clear();
//        // Get spelling suggestions for the misspelled word from Hunspell
//        List<String> suggestions = noteModel.hunspellProperty().get().suggest(cleanWord);
//        logger.debug("Word '{}': suggestions={}", cleanWord, suggestions);
//        // Populate the context menu with suggestions if any exist
//        if (!suggestions.isEmpty()) {
//            for (String suggestion : suggestions) {
//                // Skip if the suggestion matches the original word
//                if (!suggestion.equals(cleanWord)) {
//                    MenuItem item = new MenuItem(suggestion); // Create a menu item for the suggestion
//                    final int finalWordStart = wordStart;     // Final for use in lambda
//                    final int finalWordEnd = wordEnd;         // Final for use in lambda
//                    // Define action: replace the word and save the note
//                    item.setOnAction(e -> {
//                        this.replaceText(finalWordStart, finalWordEnd, suggestion);
//                        noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
//                        contextMenu.hide();
//                        contextMenuVisible = false;
//                    });
//                    contextMenu.getItems().add(item); // Add the suggestion to the menu
//                }
//            }
//        }
//        // Add an "Add to Dictionary" option to the menu
//        MenuItem addToDict = getMenuItem(cleanWord);
//        contextMenu.getItems().add(addToDict);
//        // Log the number of items prepared for the menu
//        logger.debug("Prepared context menu with {} items", contextMenu.getItems().size());
//        // Delay showing the context menu
//        int finalWordStart1 = wordStart;
//        int finalWordEnd1 = wordEnd;
//        // Set up the delay to show the context menu after 300ms
//        contextMenuDelay.setOnFinished(e -> {
//            // Only show the menu if it’s not already visible and the mouse is still over the word
//            if (!contextMenuVisible && isMouseOverWord(event.getX(), event.getY(), finalWordStart1, finalWordEnd1)) {
//                contextMenu.show(this, event.getScreenX(), event.getScreenY());
//                contextMenuVisible = true;
//            }
//        });
//        contextMenuDelay.playFromStart();  // Start the delay timer
//        // Update visibility flag when the menu is shown
//        contextMenu.setOnShowing(e -> contextMenuVisible = true);
//        // Update visibility flag when the menu is hidden
//        contextMenu.setOnHidden(e -> contextMenuVisible = false);
//    }
//
//    // Helper method to check if mouse is still over the word
//    private boolean isMouseOverWord(double x, double y, int wordStart, int wordEnd) {
//        // Get the current character index under the mouse
//        OptionalInt currentCharIndexOpt = this.hit(x, y).getCharacterIndex();
//        // Return true if the index is present and within the word’s bounds
//        return currentCharIndexOpt.isPresent() &&
//                currentCharIndexOpt.getAsInt() >= wordStart &&
//                currentCharIndexOpt.getAsInt() < wordEnd;
//    }