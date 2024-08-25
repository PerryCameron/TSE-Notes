package com.L2.mvci_note.components;

import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.ListenerFx;
import com.L2.widgetFx.TextFieldFx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class WorkOrderBox implements Builder<Region> {
    private final NoteView noteView;
    private final NoteModel noteModel;

    public WorkOrderBox(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("decorative-hbox");
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setSpacing(110);
        hBox.getChildren().add(followUpWorkOrderTextField());
        hBox.getChildren().add(relatedCase());
        hBox.getChildren().add(tex());
        hBox.getChildren().add(newPartOrder());
        return hBox;
    }

    private Node tex() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 2, 2));
        Label label = new Label("TEX");
        TextField tf = TextFieldFx.of(120,  "TEX-");
        tf.textProperty().set(String.valueOf(noteModel.getCurrentNote().getSelectedPartOrder().getOrderNumber()));
        vbox.getChildren().addAll(label, tf);
        return vbox;
    }


    private Node relatedCase() {
        VBox vbox = new VBox();
        vbox.setSpacing(2);
        vbox.setPadding(new Insets(2, 0, 2, 2));
        Label label = new Label("Related Case");
        TextField tf = TextFieldFx.of(120,  "Related Case");
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
        TextField tf = TextFieldFx.of(130,  "WO-");
        tf.textProperty().set(noteModel.getCurrentNote().getWorkOrder());
        ListenerFx.addFocusListener(tf, "Work Order", noteModel.getCurrentNote().createdWorkOrderProperty(), noteModel.statusLabelProperty());
        vbox.getChildren().addAll(label, tf);
        return vbox;
    }

    private Node newPartOrder() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20, 0, 0, 60));
        Button button = new Button("New Part Order");
        button.setOnAction(event -> {
            System.out.println("copy to clipboard");
            TestCopy.copyHTML();
        });
        vbox.getChildren().add(button);
        return vbox;
    }
}

