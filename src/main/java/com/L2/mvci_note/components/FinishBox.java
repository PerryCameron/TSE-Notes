package com.L2.mvci_note.components;

import com.L2.controls.SpellCheckArea;
import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.TextAreaFx;
import com.L2.widgetFx.TitleBarFx;
import com.L2.widgetFx.VBoxFx;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

public class FinishBox implements Component<Region> {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox root;
    private TextArea textArea;

    public FinishBox(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        root.getStyleClass().add("decorative-hbox");
        HBox hBox = new HBox(); // box to hold basic info and service plan
        hBox.setPadding(new Insets(0, 5, 5, 5));
        Button[] buttons = new Button[]{};
        hBox.getChildren().addAll(correctiveText(), buttonBox());
        root.getChildren().addAll(TitleBarFx.of("Answer To Customer Notes", buttons), hBox);
        refreshFields();
        root.setOnMouseExited(event -> {
            noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
        });
        return root;
    }

    private Node correctiveText() {
        // Create a new CodeArea instance
        SpellCheckArea spellCheckArea = new SpellCheckArea(noteView, 100, noteModel.getBoundNote().additionalCorrectiveActionTextProperty());
        spellCheckArea.setComputeHighlight(NoteMessage.COMPUTE_HIGHLIGHTING_FINISH_AREA);
        noteModel.finishAreaProperty().setValue(spellCheckArea);
        VirtualizedScrollPane<CodeArea> scrollWrapper = new VirtualizedScrollPane<>(spellCheckArea);
        HBox.setHgrow(scrollWrapper, Priority.ALWAYS);
        // check on startup
        noteView.getAction().accept(NoteMessage.COMPUTE_HIGHLIGHTING_FINISH_AREA);
        // wrap in a scroll pane
        return scrollWrapper;
    }

    private Node buttonBox() {
        VBox vBox = new VBox(10);
        vBox.setPrefWidth(200);
        vBox.setPadding(new Insets(0, 0, 0, 10));
        Button customerRequestButton = ButtonFx.utilityButton(() -> {
            noteView.flashGroupA();
            noteView.getAction().accept(NoteMessage.COPY_CUSTOMER_REQUEST);
        }, "Customer Request", "/images/question-16.png");

        Button correctiveActionButton = ButtonFx.utilityButton(() -> {
            noteView.flashGroupB();
            noteView.getAction().accept(NoteMessage.COPY_ANSWER_TO_CUSTOMER);
        }, "Answer to Customer", "/images/smile-16.png");

        Button logCallActionButton = ButtonFx.utilityButton(() -> {
            noteView.flashGroupC();
            noteView.getAction().accept(NoteMessage.COPY_LOGGED_CALL);
        }, "Log Call", "/images/call-16.png");

        vBox.getChildren().addAll(customerRequestButton, correctiveActionButton, logCallActionButton);
        return vBox;
    }

    @Override
    public void flash() {
        root.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(event -> root.setStyle("")); // Reset the style
        pause.play();
    }

    @Override
    public void refreshFields() {

    }
}
