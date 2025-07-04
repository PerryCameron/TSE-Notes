package com.L2.mvci.note.components;

import com.L2.interfaces.Component;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.static_tools.CopyPastaParser;
import com.L2.static_tools.ImageResources;
import com.L2.static_tools.StringChecker;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ShippingInformation implements Component<Region> {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private final VBox root;
    private final TextField[] aTextFields;
    private final TextField[] cTextFields;
    private final TextArea streetTextArea;

    public ShippingInformation(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.aTextFields = new TextField[5];
        this.cTextFields = new TextField[3];
        this.streetTextArea = TextAreaFx.standardTextArea(true, 70, 16, 2);
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 5, 5));
    }

    @Override
    public Region build() {
        HBox hBox = HBoxFx.of(new Insets(5, 5, 5, 5), 5.0);
        root.getStyleClass().add("decorative-hbox");
        hBox.getChildren().addAll(contact(), address());
        Button copyButton = ButtonFx.utilityButton(() -> {
            flash();
            noteView.getAction().accept(NoteMessage.SHIPPING_INFORMATION);
        }, ImageResources.COPY, "Copy");
        copyButton.setTooltip(ToolTipFx.of("Copy Shipping Information"));
        Button[] buttons = new Button[]{copyButton};
        root.getChildren().addAll(TitleBarFx.of("Shipping Information", buttons), hBox);
        bindTextFields();
        root.setOnMouseExited(event -> {
            noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
        });
        return root;
    }

    private Node contact() {
        VBox vBox = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        vBox.getStyleClass().add("inner-decorative-hbox");
        cTextFields[0] = TextFieldFx.createValidatedTextField(200, "Contact Name", StringChecker::formatName, noteView);
        cTextFields[1] = TextFieldFx.createValidatedTextField(200,"Contact Phone", StringChecker::formatPhoneNumber, noteView);
        cTextFields[2] = TextFieldFx.createValidatedTextField(200,"Contact Email", StringChecker::formatEmail, noteView);

        Button pasteButton = ButtonFx.utilityButton(() -> {
            String[] contactInfo = CopyPastaParser.extractContactInfo();
            noteModel.boundNoteProperty().get().setContactName(contactInfo[0]);
            noteModel.boundNoteProperty().get().setContactPhoneNumber(contactInfo[1]);
            noteModel.boundNoteProperty().get().setContactEmail(contactInfo[2]);
        }, ImageResources.PASTE, "Paste");
        pasteButton.setTooltip(ToolTipFx.of("Paste Shipping Contact Information"));

        Button[] buttons = new Button[]{pasteButton};
        vBox.getChildren().addAll(TitleBarFx.of("Contact", buttons), cTextFields[0], cTextFields[1], cTextFields[2]);
        return vBox;
    }

    private Node address() {
        VBox vBox = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        vBox.getStyleClass().add("inner-decorative-hbox");
        HBox hBox = new HBox(5);
        aTextFields[0] = TextFieldFx.standardTextField(200, "Related Account / Installed at");
        streetTextArea.setPrefWidth(400);
        streetTextArea.setPromptText("Street");
        aTextFields[1] = TextFieldFx.standardTextField(200, "City");
        aTextFields[2] = TextFieldFx.standardTextField(100, "State/Province");
        aTextFields[3] = TextFieldFx.standardTextField(100, "zip Code");
        aTextFields[4] = TextFieldFx.standardTextField(200, "Country");

        Button pasteButton = ButtonFx.utilityButton(() -> {
            String[] addressInfo = CopyPastaParser.parseAddress();
            aTextFields[0].textProperty().set(addressInfo[0]);
            streetTextArea.textProperty().set(addressInfo[1]);
            aTextFields[1].textProperty().set(addressInfo[2]);
            aTextFields[2].textProperty().set(addressInfo[3]);
            aTextFields[3].textProperty().set(addressInfo[4]);
            aTextFields[4].textProperty().set(addressInfo[5]);
        }, ImageResources.PASTE, "Paste");
        pasteButton.setTooltip(ToolTipFx.of("Paste Shipping Address Information"));
        Button[] buttons = new Button[]{pasteButton};
        hBox.getChildren().addAll(aTextFields[1], aTextFields[2], aTextFields[3]);
        vBox.getChildren().addAll(TitleBarFx.of("Address", buttons), aTextFields[0], streetTextArea, hBox, aTextFields[4]);
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

    public void bindTextFields() {
        aTextFields[0].textProperty().bindBidirectional(noteModel.boundNoteProperty().get().installedAtProperty());
        streetTextArea.textProperty().bindBidirectional(noteModel.boundNoteProperty().get().streetProperty());
        aTextFields[1].textProperty().bindBidirectional(noteModel.boundNoteProperty().get().cityProperty());
        aTextFields[2].textProperty().bindBidirectional(noteModel.boundNoteProperty().get().stateProperty());
        aTextFields[3].textProperty().bindBidirectional(noteModel.boundNoteProperty().get().zipProperty());
        aTextFields[4].textProperty().bindBidirectional(noteModel.boundNoteProperty().get().countryProperty());
        cTextFields[0].textProperty().bindBidirectional(noteModel.boundNoteProperty().get().contactNameProperty());
        cTextFields[1].textProperty().bindBidirectional(noteModel.boundNoteProperty().get().contactPhoneNumberProperty());
        cTextFields[2].textProperty().bindBidirectional(noteModel.boundNoteProperty().get().contactEmailProperty());
    }
}


