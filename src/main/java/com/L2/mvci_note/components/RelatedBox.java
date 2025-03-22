package com.L2.mvci_note.components;

import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.static_tools.StringChecker;
import com.L2.widgetFx.ButtonFx;
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
import javafx.util.Duration;

public class RelatedBox implements Component<Region> {
    private final NoteView noteView;
    private final NoteModel noteModel;
    private final TextField tAndMTextField;
    private VBox root;
    TextField texTextField;
    TextField relatedCaseTextField;
    TextField createdWorkOrderTextField;


    public RelatedBox(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.root = new VBox();
        this.texTextField = TextFieldFx.standardTextField(200,  "TEX");
        this.relatedCaseTextField = TextFieldFx.standardTextField(200,  "Created Case");
        this.createdWorkOrderTextField = TextFieldFx.createValidatedTextField(200,  "WO-", StringChecker::formatWorkOrder, noteView);
        this.tAndMTextField = TextFieldFx.standardTextField(200, "T&M");
    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(true,5.0, new Insets(3, 5, 3, 5));
        root.getStyleClass().add("decorative-hbox");
        root.setAlignment(Pos.TOP_CENTER);
        Button[] buttons = new Button[] { };
        root.getChildren().addAll(TitleBarFx.of("Related", buttons));
        root.getChildren().add(followUpWorkOrderTextField());
        root.getChildren().add(createdCase());
        root.getChildren().add(tex());
        root.getChildren().add(tAndM());
        refreshFields();
        root.setOnMouseExited(event -> noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE));
        return root;
    }

    private Node tAndM() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 2, 2));
        Label label = new Label("Time and Materials");
        label.setPadding(new Insets(0,0,0,5));
        tAndMTextField.textProperty().bindBidirectional(noteModel.boundNoteProperty().get().tAndMProperty());
        vbox.getChildren().addAll(label, tAndMTextField);
        return vbox;
    }

    private Node tex() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 2, 2));
        Label label = new Label("Technical Expert Assessments");
        label.setPadding(new Insets(0,0,0,5));
        texTextField.textProperty().bindBidirectional(noteModel.boundNoteProperty().get().texProperty());
        vbox.getChildren().addAll(label, texTextField);
        return vbox;
    }

    private Node createdCase() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 2, 2));
        Label label = new Label("Created Case");
        label.setPadding(new Insets(0,0,0,5));
        relatedCaseTextField.textProperty().bindBidirectional(noteModel.boundNoteProperty().get().relatedCaseNumberProperty());
        vbox.getChildren().addAll(label, relatedCaseTextField);
        return vbox;
    }

    private Node followUpWorkOrderTextField() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 5, 5));
        Label label = new Label("Created Work Order");
        label.setPadding(new Insets(0,0,0,5));
        createdWorkOrderTextField.textProperty().bindBidirectional(noteModel.boundNoteProperty().get().createdWorkOrderProperty());
        vbox.getChildren().addAll(label, createdWorkOrderTextField);
        return vbox;
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

