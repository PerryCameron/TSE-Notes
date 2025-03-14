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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
        // Create a new CodeArea instance
        CodeArea codeArea = new CodeArea();
        // Enable text wrapping in the CodeArea
        codeArea.setWrapText(true);
        // Allow the CodeArea to grow horizontally within its parent container
        HBox.setHgrow(codeArea, Priority.ALWAYS);
        // Set the preferred height of the CodeArea (adjust as needed)
        codeArea.setPrefHeight(200); // Adjust based on your needs
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

        codeArea.addEventFilter(ScrollEvent.SCROLL, event -> {
            ScrollPane scrollPane = noteModel.noteScrollPaneProperty().get();
            double deltaY = event.getDeltaY();
            double currentVvalue = scrollPane.getVvalue();
            double vMax = scrollPane.getVmax();
            double vMin = scrollPane.getVmin();

            double scrollAmount = deltaY / codeArea.getHeight();
            double newVvalue = currentVvalue - scrollAmount;

            newVvalue = Math.max(vMin, Math.min(vMax, newVvalue));
            scrollPane.setVvalue(newVvalue);

            event.consume();
        });

        // Return the configured CodeArea instance
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
        // Check if the Hunspell spell checker is available
        if (noteModel.hunspellProperty().get() == null) return;

        // Get the character index at the mouse hover position
        OptionalInt charIndexOpt = noteModel.issueAreaProperty().get().hit(event.getX(), event.getY()).getCharacterIndex();
        if (!charIndexOpt.isPresent()) {
            // Hide the context menu if no character index is found
            noteModel.contextMenuProperty().get().hide();
            return;
        }
        int charIndex = charIndexOpt.getAsInt();
        // Check if the character index is out of bounds
        if (charIndex < 0 || charIndex >= noteModel.issueAreaProperty().get().getText().length()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Get the text from the issue area
        String text = noteModel.issueAreaProperty().get().getText();
        // Find the start and end indices of the word at the character index
        int wordStart = text.lastIndexOf(" ", charIndex) + 1;
        int wordEnd = text.indexOf(" ", charIndex);
        if (wordEnd == -1) wordEnd = text.length();
        if (wordStart >= wordEnd) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Adjust the word end to include valid characters
        int actualWordEnd = wordStart;
        while (actualWordEnd < wordEnd && (Character.isLetterOrDigit(text.charAt(actualWordEnd)) ||
                text.charAt(actualWordEnd) == '\'' ||
                text.charAt(actualWordEnd) == '-' ||
                text.charAt(actualWordEnd) == '/')) {
            actualWordEnd++;
        }
        // Extract the word and clean it
        String word = text.substring(wordStart, actualWordEnd);
        String cleanWord = word.replaceAll("[^\\p{L}\\p{N}'-/]", ""); // Match computeHighlighting

        // Hide the context menu if the cleaned word is empty
        if (cleanWord.isEmpty()) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Check if the word is spelled correctly
        boolean isSpelledCorrectly = noteModel.hunspellProperty().get().spell(cleanWord);
        logger.debug("Word '{}': spelled correctly = {}, suggestions = {}", cleanWord, isSpelledCorrectly, noteModel.hunspellProperty().get().suggest(cleanWord));

        // Hide the context menu if the word is spelled correctly
        if (isSpelledCorrectly) {
            noteModel.contextMenuProperty().get().hide();
            return;
        }

        // Clear the context menu items
        noteModel.contextMenuProperty().get().getItems().clear();
        // Get spelling suggestions for the word
        List<String> suggestions = noteModel.hunspellProperty().get().suggest(cleanWord);
        if (!suggestions.isEmpty()) {
            for (String suggestion : suggestions) {
                if (!suggestion.equals(cleanWord)) { // Skip self-suggestions
                    MenuItem item = new MenuItem(suggestion);
                    final int finalWordStart = wordStart;
                    final int finalWordEnd = actualWordEnd;
                    item.setOnAction(e -> {
                        // Replace the text with the suggestion and hide the context menu
                        noteModel.issueAreaProperty().get().replaceText(finalWordStart, finalWordEnd, suggestion);
                        noteModel.contextMenuProperty().get().hide();
                    });
                    noteModel.contextMenuProperty().get().getItems().add(item);
                }
            }
        }

        // Add an option to add the word to the dictionary
        MenuItem addToDict = new MenuItem("Add to Dictionary");
        addToDict.setOnAction(e -> {
            // Add the word to the dictionary and trigger related actions
            noteModel.hunspellProperty().get().add(cleanWord); // Use cleanWord with '
            noteModel.newWordProperty().set(cleanWord);
            noteView.getAction().accept(NoteMessage.ADD_WORD_TO_DICT);
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA);
            noteModel.contextMenuProperty().get().hide();
        });
        noteModel.contextMenuProperty().get().getItems().add(addToDict);

        // Show the context menu at the mouse hover position
        noteModel.contextMenuProperty().get().show(noteModel.issueAreaProperty().get(), event.getScreenX(), event.getScreenY());
    }
}

