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
import org.fxmisc.flowless.VirtualizedScrollPane;
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

        HBox iconBox = HBoxFx.iconBox(10);
        root.getChildren().addAll(TitleBarFx.of("Issue", iconBox), getCodeAreaIssue());
        refreshFields();
        return root;
    }

    private VirtualizedScrollPane<CodeArea> getCodeAreaIssue() {
        // Create a new CodeArea instance
        CodeArea codeArea = new CodeArea();
        noteModel.issueAreaProperty().setValue(codeArea);
        // wrap in a scroll pane
        VirtualizedScrollPane<CodeArea> sp = new VirtualizedScrollPane<>(codeArea);
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

        // only blocks outer ScrollPane if inner has enough text for a ScrollPane
        codeArea.addEventFilter(ScrollEvent.SCROLL, event -> {
            ScrollPane outerScrollPane = noteModel.noteScrollPaneProperty().get();
            if (outerScrollPane == null) return; // Safety check

            // Check if CodeArea needs to scroll
            double totalContentHeight = sp.totalHeightEstimateProperty().getValue();
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

        // Return the configured CodeArea instance
        return sp;
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

        String text = noteModel.issueAreaProperty().get().getText();
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
                        noteModel.issueAreaProperty().get().replaceText(finalWordStart, finalWordEnd, suggestion);
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
            noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_ISSUE_AREA);
            noteModel.contextMenuProperty().get().hide();
        });
        noteModel.contextMenuProperty().get().getItems().add(addToDict);

        noteModel.contextMenuProperty().get().show(noteModel.issueAreaProperty().get(), event.getScreenX(), event.getScreenY());
    }
}

