package com.L2.mvci_note.components;

import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.ListenerFx;
import com.L2.widgetFx.TextFieldFx;
import com.L2.widgetFx.TitleBarFx;
import com.L2.widgetFx.VBoxFx;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Duration;

public class WorkOrderBox implements Builder<Region> {
    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox root;

    public WorkOrderBox(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(true,5.0, new Insets(3, 5, 3, 5));
        root.getStyleClass().add("decorative-hbox");
        root.setAlignment(Pos.TOP_CENTER);
        Button[] buttons = new Button[] {  };
        root.getChildren().addAll(TitleBarFx.of("Related", buttons));
        root.getChildren().add(followUpWorkOrderTextField());
        root.getChildren().add(relatedCase());
        root.getChildren().add(tex());
        return root;
    }

    private Node tex() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 2, 2));
        Label label = new Label("TEX");
        label.setPadding(new Insets(0,0,0,5));
        TextField tf = TextFieldFx.of(200,  "TEX-");
        tf.textProperty().set(String.valueOf(noteModel.getCurrentNote().getTex()));
        vbox.getChildren().addAll(label, tf);
        return vbox;
    }

    private Node relatedCase() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 2, 2));
        Label label = new Label("Related Case");
        label.setPadding(new Insets(0,0,0,5));
        TextField tf = TextFieldFx.of(200,  "Related Case");
        tf.textProperty().set(String.valueOf(noteModel.getCurrentNote().getCaseNumber()));
        tf.setPromptText("Case-");
        vbox.getChildren().addAll(label, tf);
        return vbox;
    }

    private Node followUpWorkOrderTextField() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 5, 5));
        Label label = new Label("Follow Up Work Order");
        label.setPadding(new Insets(0,0,0,5));
        TextField tf = TextFieldFx.of(200,  "WO-");
        tf.textProperty().set(noteModel.getCurrentNote().getCreatedWorkOrder());
        ListenerFx.addFocusListener(tf, "Work Order", noteModel.getCurrentNote().createdWorkOrderProperty(), noteModel.statusLabelProperty());
        vbox.getChildren().addAll(label, tf);
        return vbox;
    }

    public void flashBorder() {
        root.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(event -> root.setStyle("")); // Reset the style
        pause.play();
    }
}

