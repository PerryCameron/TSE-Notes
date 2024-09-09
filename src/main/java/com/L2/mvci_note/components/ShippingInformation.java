package com.L2.mvci_note.components;

import com.L2.interfaces.Component;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.static_tools.CopyPastaParser;
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
    private VBox root;
    private final TextField[] aTextFields;
    private final TextField[] cTextFields;
    private final TextArea streetTextArea;

    public ShippingInformation(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.aTextFields = new TextField[5];
        this.cTextFields = new TextField[3];
        this.streetTextArea = TextAreaFx.of(true, 70, 16, 2);
    }

    @Override
    public Region build() {
        this.root = VBoxFx.of(5.0, new Insets(5, 5, 5, 5));
        HBox hBox = HBoxFx.of(new Insets(5, 5, 5, 5), 5.0);
        root.getStyleClass().add("decorative-hbox");
        hBox.getChildren().addAll(contact(), address());
        Button copyButton = ButtonFx.utilityButton(() -> {
            flash();
            noteView.getAction().accept(NoteMessage.SHIPPING_INFORMATION);
        }, "Copy", "/images/copy-16.png");
        copyButton.setTooltip(ToolTipFx.of("Copy Shipping Information"));
        Button[] buttons = new Button[]{copyButton};
        root.getChildren().addAll(TitleBarFx.of("Shipping Information", buttons), hBox);
        refreshFields();
        return root;
    }

    private Node contact() {
        VBox vBox = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        vBox.getStyleClass().add("inner-decorative-hbox");
        cTextFields[0] = TextFieldFx.of(250, "Contact Name");
        ListenerFx.addFocusListener(cTextFields[0], "Contact Name", noteModel.getCurrentNote().contactNameProperty(), noteModel.statusLabelProperty());

        cTextFields[1] = TextFieldFx.of(250, "Contact Phone");
        ListenerFx.addFocusListener(cTextFields[1], "Contact Phone", noteModel.getCurrentNote().contactPhoneNumberProperty(), noteModel.statusLabelProperty());

        cTextFields[2] = TextFieldFx.of(250, "Contact Email");
        ListenerFx.addFocusListener(cTextFields[2], "Contact Email", noteModel.getCurrentNote().contactEmailProperty(), noteModel.statusLabelProperty());

        Button clearButton = ButtonFx.utilityButton(() -> {
            noteModel.getCurrentNote().clearContact();
            for(TextField textField : cTextFields) textField.setText("");
        }, "Clear", "/images/clear-16.png");
        clearButton.setTooltip(ToolTipFx.of("Clear Shipping Contact"));

        Button pasteButton = ButtonFx.utilityButton(() -> {
            String[] contactInfo = CopyPastaParser.extractContactInfo();
            noteModel.getCurrentNote().setContactName(contactInfo[0]);
            cTextFields[0].textProperty().set(contactInfo[0]);
            noteModel.getCurrentNote().setContactPhoneNumber(contactInfo[1]);
            cTextFields[1].textProperty().set(contactInfo[1]);
            noteModel.getCurrentNote().setContactEmail(contactInfo[2]);
            cTextFields[2].textProperty().set(contactInfo[2]);
        }, "Paste", "/images/paste-16.png");
        pasteButton.setTooltip(ToolTipFx.of("Paste Shipping Contact Information"));

        Button[] buttons = new Button[]{clearButton, pasteButton};
        vBox.getChildren().addAll(TitleBarFx.of("Contact", buttons), cTextFields[0], cTextFields[1], cTextFields[2]);
        return vBox;
    }

    private Node address() {
        VBox vBox = VBoxFx.of(5.0, new Insets(5, 5, 10, 5));
        vBox.getStyleClass().add("inner-decorative-hbox");
        HBox hBox = new HBox(5);

        aTextFields[0] = TextFieldFx.of(200, "Related Account / Installed at");
        ListenerFx.addFocusListener(aTextFields[0], "Related Account", noteModel.getCurrentNote().installedAtProperty(), noteModel.statusLabelProperty());


        streetTextArea.setPrefWidth(400);
        streetTextArea.setPromptText("Street");
        ListenerFx.addFocusListener(streetTextArea, "Street", noteModel.getCurrentNote().streetProperty(), noteModel.statusLabelProperty());

        aTextFields[1] = TextFieldFx.of(250, "City");
        ListenerFx.addFocusListener(aTextFields[1], "City", noteModel.getCurrentNote().cityProperty(), noteModel.statusLabelProperty());

        aTextFields[2] = TextFieldFx.of(50, "State/Province");
        ListenerFx.addFocusListener(aTextFields[2], "State/Province", noteModel.getCurrentNote().stateProperty(), noteModel.statusLabelProperty());

        aTextFields[3] = TextFieldFx.of(100, "zip Code");
        ListenerFx.addFocusListener(aTextFields[3], "zip Code", noteModel.getCurrentNote().zipProperty(), noteModel.statusLabelProperty());

        aTextFields[4] = TextFieldFx.of(200, "Country");
        ListenerFx.addFocusListener(aTextFields[4], "Country", noteModel.getCurrentNote().countryProperty(), noteModel.statusLabelProperty());

        Button clearButton = ButtonFx.utilityButton(() -> {
            noteModel.getCurrentNote().clearAddress();
            for (TextField textField : aTextFields)
                textField.clear();
            streetTextArea.textProperty().set("");

        }, "Clear", "/images/clear-16.png");
        clearButton.setTooltip(ToolTipFx.of("Clear Shipping Address"));

        Button pasteButton = ButtonFx.utilityButton(() -> {
            String[] addressInfo = CopyPastaParser.parseAddress();
            noteModel.getCurrentNote().setInstalledAt(addressInfo[0]);
            noteModel.getCurrentNote().setStreet(addressInfo[1]);
            noteModel.getCurrentNote().setCity(addressInfo[2]);
            noteModel.getCurrentNote().setState(addressInfo[3]);
            noteModel.getCurrentNote().setZip(addressInfo[4]);
            noteModel.getCurrentNote().setCountry(addressInfo[5]);
            aTextFields[0].textProperty().set(addressInfo[0]);
            streetTextArea.textProperty().set(addressInfo[1]);
            aTextFields[1].textProperty().set(addressInfo[2]);
            aTextFields[2].textProperty().set(addressInfo[3]);
            aTextFields[3].textProperty().set(addressInfo[4]);
            aTextFields[4].textProperty().set(addressInfo[5]);
        }, "Paste", "/images/paste-16.png");
        pasteButton.setTooltip(ToolTipFx.of("Paste Shipping Address Information"));
        Button[] buttons = new Button[]{clearButton, pasteButton};
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
        cTextFields[0].textProperty().set(noteModel.getCurrentNote().getContactName());
        cTextFields[1].textProperty().set(noteModel.getCurrentNote().getContactPhoneNumber());
        cTextFields[2].textProperty().set(noteModel.getCurrentNote().getContactEmail());
        streetTextArea.textProperty().set(noteModel.getCurrentNote().getStreet());
        aTextFields[0].textProperty().set(noteModel.getCurrentNote().getInstalledAt());
        aTextFields[1].textProperty().set(noteModel.getCurrentNote().getCity());
        aTextFields[2].textProperty().set(noteModel.getCurrentNote().getState());
        aTextFields[3].textProperty().set(noteModel.getCurrentNote().getZip());
        aTextFields[4].textProperty().set(noteModel.getCurrentNote().getCountry());
    }
}

