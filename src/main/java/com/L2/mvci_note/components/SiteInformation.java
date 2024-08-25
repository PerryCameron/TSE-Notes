package com.L2.mvci_note.components;

import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.ListenerFx;
import com.L2.widgetFx.TextAreaFx;
import com.L2.widgetFx.TextFieldFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class SiteInformation implements Builder<Region> {

    private final NoteView noteView;
    private final NoteModel noteModel;

    public SiteInformation(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {

        VBox vBox = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        Label label = new Label("Site Information");
        label.setPadding(new Insets(0, 0, 0, 5));
        vBox.getStyleClass().add("decorative-hbox");
        HBox hBox = new HBox(5);
        TextField tf1 = TextFieldFx.of(200, "Related Account / Installed at");
        tf1.textProperty().set(noteModel.getCurrentNote().getInstalledAt());
        ListenerFx.addFocusListener(tf1, "Related Account", noteModel.getCurrentNote().installedAtProperty(), noteModel.statusLabelProperty());

        TextArea ta1 = TextAreaFx.of(true, 70, 16, 2);
        ta1.setPrefWidth(400);
        ta1.setPromptText("Street");
        ta1.textProperty().set(noteModel.getCurrentNote().getStreet());

        TextField tf2 = TextFieldFx.of(250, "City");
        tf2.textProperty().set(noteModel.getCurrentNote().getCity());
        ListenerFx.addFocusListener(tf2, "City", noteModel.getCurrentNote().cityProperty(), noteModel.statusLabelProperty());

        TextField tf3 = TextFieldFx.of(50, "State/Province");
        tf3.textProperty().set(noteModel.getCurrentNote().getState());
        ListenerFx.addFocusListener(tf3, "State/Province", noteModel.getCurrentNote().stateProperty(), noteModel.statusLabelProperty());

        TextField tf4 = TextFieldFx.of(100, "zip Code");
        tf4.textProperty().set(noteModel.getCurrentNote().getZip());
        ListenerFx.addFocusListener(tf4, "zip Code", noteModel.getCurrentNote().zipProperty(), noteModel.statusLabelProperty());

        TextField tf5 = TextFieldFx.of(200, "Country");
        tf5.textProperty().set(noteModel.getCurrentNote().getCountry());
        ListenerFx.addFocusListener(tf5, "Country", noteModel.getCurrentNote().countryProperty(), noteModel.statusLabelProperty());

        hBox.getChildren().addAll(tf2, tf3, tf4);
        vBox.getChildren().addAll(label, tf1, ta1, hBox, tf5);

        return vBox;
    }
}

