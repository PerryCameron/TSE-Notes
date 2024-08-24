package com.L2.mvci_note.components;

import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.ListenerFx;
import com.L2.widgetFx.TextFieldFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class BasicInformation implements Builder<Region> {

    private final NoteModel noteModel;

    public BasicInformation(NoteView noteView) {
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
        VBox vBox = VBoxFx.of(5.5, new Insets(15, 40, 0, 0));

        TextField tf1 = TextFieldFx.of(200,  "Work Order");
        tf1.textProperty().set(noteModel.getCurrentNote().getWorkOrder());
        ListenerFx.addFocusListener(tf1, "Work Order", noteModel.getCurrentNote().workOrderProperty(), noteModel.statusLabelProperty());

        TextField tf2 = TextFieldFx.of(200, "Case");
        tf2.textProperty().set(noteModel.getCurrentNote().getCaseNumber());
        ListenerFx.addFocusListener(tf2, "Case", noteModel.getCurrentNote().caseNumberProperty(), noteModel.statusLabelProperty());

        TextField tf3 = TextFieldFx.of(200, 30, "Model", noteModel.getCurrentNote().modelNumberProperty());
        tf3.textProperty().set(noteModel.getCurrentNote().getModelNumber());
        ListenerFx.addFocusListener(tf3, "Model", noteModel.getCurrentNote().modelNumberProperty(), noteModel.statusLabelProperty());

        TextField tf4 = TextFieldFx.of(200, 30, "Serial", noteModel.getCurrentNote().serialNumberProperty());
        tf4.textProperty().set(noteModel.getCurrentNote().getSerialNumber());
        ListenerFx.addFocusListener(tf4, "Serial", noteModel.getCurrentNote().serialNumberProperty(), noteModel.statusLabelProperty());

        TextField tf5 = TextFieldFx.of(200, 30, "Call-in Contact", noteModel.getCurrentNote().callInPersonProperty());
        tf5.textProperty().set(noteModel.getCurrentNote().getCallInPerson());
        ListenerFx.addFocusListener(tf5, "Call-in Contact", noteModel.getCurrentNote().callInPersonProperty(), noteModel.statusLabelProperty());

        TextField tf6 = TextFieldFx.of(200, "Call-in Phone");
        tf6.textProperty().set(noteModel.getCurrentNote().getCallInPhoneNumber());
        ListenerFx.addFocusListener(tf6, "Call-in Phone", noteModel.getCurrentNote().callInPhoneNumberProperty(), noteModel.statusLabelProperty());

        TextField tf7 = TextFieldFx.of(200, 30, "Call-in Email", noteModel.getCurrentNote().callInEmailProperty());
        tf7.textProperty().set(noteModel.getCurrentNote().getCallInEmail());
        ListenerFx.addFocusListener(tf7, "Call-in Email", noteModel.getCurrentNote().callInEmailProperty(), noteModel.statusLabelProperty());
        vBox.getChildren().addAll(tf1, tf2, tf3, tf4, tf5, tf6, tf7);
        return vBox;
    }
}
