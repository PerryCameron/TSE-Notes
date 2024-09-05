package com.L2.mvci_note.components;

import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.static_tools.ClipboardUtils;
import com.L2.static_tools.CopyPasteParser;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Duration;

import java.util.Objects;

public class ShippingInformation implements Builder<Region> {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox shippingBox;

    public ShippingInformation(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
        this.shippingBox = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        HBox hBox =  HBoxFx.of(new Insets(5, 5, 10, 5), 5.0);
        shippingBox.getStyleClass().add("decorative-hbox");
        hBox.getChildren().addAll(contact(), address());
        String[] boxInfo = {"Shipping Information","Copy Shipping Information"};
        shippingBox.getChildren().addAll(TitleBarFx.of(boxInfo, () -> {
            shippingBox.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> shippingBox.setStyle("")); // Reset the style
            pause.play();
            noteView.getAction().accept(NoteMessage.SITE_INFORMATION);
        }), hBox);
        return shippingBox;
    }

    private Node contact() {
        VBox vBox = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));

        TextField tf6 = TextFieldFx.of(250, "Contact Name");
        tf6.textProperty().set(noteModel.getCurrentNote().getContactName());
        ListenerFx.addFocusListener(tf6, "Contact Name", noteModel.getCurrentNote().contactNameProperty(), noteModel.statusLabelProperty());

        TextField tf7 = TextFieldFx.of(250, "Contact Phone");
        tf7.textProperty().set(noteModel.getCurrentNote().getContactPhoneNumber());
        ListenerFx.addFocusListener(tf7, "Contact Phone", noteModel.getCurrentNote().contactPhoneNumberProperty(), noteModel.statusLabelProperty());

        TextField tf8 = TextFieldFx.of(250, "Contact Email");
        tf8.textProperty().set(noteModel.getCurrentNote().getContactEmail());
        ListenerFx.addFocusListener(tf8, "Contact Email", noteModel.getCurrentNote().contactEmailProperty(), noteModel.statusLabelProperty());

        Button pasteButton = ButtonFx.utilityButton("/images/paste-16.png", () -> {
            String[] contactInfo = CopyPasteParser.extractContactInfo();
                noteModel.getCurrentNote().setContactName(contactInfo[0]);
                tf6.textProperty().set(contactInfo[0]);
                noteModel.getCurrentNote().setContactPhoneNumber(contactInfo[1]);
                tf7.textProperty().set(contactInfo[1]);
                noteModel.getCurrentNote().setContactEmail(contactInfo[2]);
                tf8.textProperty().set(contactInfo[2]);
        });

        Button[] buttons = new Button[] { pasteButton };
        vBox.getChildren().addAll(TitleBarFx.of("Contact", buttons), tf6, tf7, tf8);
        return vBox;
    }

    private Node address() {
        VBox vBox = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        HBox hBox = new HBox(5);

        TextField tf1 = TextFieldFx.of(200, "Related Account / Installed at");
        tf1.textProperty().set(noteModel.getCurrentNote().getInstalledAt());
        ListenerFx.addFocusListener(tf1, "Related Account", noteModel.getCurrentNote().installedAtProperty(), noteModel.statusLabelProperty());

        TextArea textArea = TextAreaFx.of(true, 70, 16, 2);
        textArea.setPrefWidth(400);
        textArea.setPromptText("Street");
        textArea.textProperty().set(noteModel.getCurrentNote().getStreet());
        ListenerFx.addFocusListener(textArea, "Street", noteModel.getCurrentNote().streetProperty(), noteModel.statusLabelProperty());

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

        Button pasteButton = ButtonFx.utilityButton("/images/paste-16.png", () -> {
            String[] addressInfo = CopyPasteParser.parseAddress();
            for(String info : addressInfo) {
                System.out.println(info);
            }
        });

        Button[] buttons = new Button[] { pasteButton };

        hBox.getChildren().addAll(tf2, tf3, tf4);
        vBox.getChildren().addAll(TitleBarFx.of("Contact", buttons), tf1, textArea, hBox, tf5);
        return vBox;
    }
}

