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
    private final VBox root;
    private final TextField[] aTextFields;
    private final TextField[] cTextFields;
    private final TextArea streetTextArea;

    public ShippingInformation(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.aTextFields = new TextField[5];
        this.cTextFields = new TextField[3];
        this.streetTextArea = TextAreaFx.of(true, 70, 16, 2);
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
        cTextFields[1] = TextFieldFx.of(250, "Contact Phone");
        cTextFields[2] = TextFieldFx.of(250, "Contact Email");
        Button clearButton = ButtonFx.utilityButton(() -> {
            noteModel.getBoundNote().clearContact();
            for(TextField textField : cTextFields) textField.setText("");
        }, "Clear", "/images/clear-16.png");
        clearButton.setTooltip(ToolTipFx.of("Clear Shipping Contact"));

        Button pasteButton = ButtonFx.utilityButton(() -> {
            String[] contactInfo = CopyPastaParser.extractContactInfo();
            noteModel.getBoundNote().setContactName(contactInfo[0]);
            noteModel.getBoundNote().setContactPhoneNumber(contactInfo[1]);
            noteModel.getBoundNote().setContactEmail(contactInfo[2]);
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
        streetTextArea.setPrefWidth(400);
        streetTextArea.setPromptText("Street");
        aTextFields[1] = TextFieldFx.of(250, "City");
        aTextFields[2] = TextFieldFx.of(50, "State/Province");
        aTextFields[3] = TextFieldFx.of(100, "zip Code");
        aTextFields[4] = TextFieldFx.of(200, "Country");
        Button clearButton = ButtonFx.utilityButton(() -> {
            noteModel.getBoundNote().clearAddress();
            for (TextField textField : aTextFields)
                textField.clear();
            streetTextArea.textProperty().set("");

        }, "Clear", "/images/clear-16.png");
        clearButton.setTooltip(ToolTipFx.of("Clear Shipping Address"));

        Button pasteButton = ButtonFx.utilityButton(() -> {
            String[] addressInfo = CopyPastaParser.parseAddress();
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

    // resets all the text fields
    @Override
    public void refreshFields() {
        refreshBindings();
    }

    public void refreshBindings() {
        unbindTextFields();  // Unbind previous bindings
        bindTextFields();    // Bind the TextFields to the latest properties
    }

    public void unbindTextFields() {
        aTextFields[0].textProperty().unbindBidirectional(noteModel.getBoundNote().installedAtProperty());
        streetTextArea.textProperty().unbindBidirectional(noteModel.getBoundNote().streetProperty());
        aTextFields[1].textProperty().unbindBidirectional(noteModel.getBoundNote().cityProperty());
        aTextFields[2].textProperty().unbindBidirectional(noteModel.getBoundNote().stateProperty());
        aTextFields[3].textProperty().unbindBidirectional(noteModel.getBoundNote().zipProperty());
        aTextFields[4].textProperty().unbindBidirectional(noteModel.getBoundNote().countryProperty());
        cTextFields[0].textProperty().unbindBidirectional(noteModel.getBoundNote().contactNameProperty());
        cTextFields[1].textProperty().unbindBidirectional(noteModel.getBoundNote().contactPhoneNumberProperty());
        cTextFields[2].textProperty().unbindBidirectional(noteModel.getBoundNote().contactEmailProperty());
    }

    public void bindTextFields() {
        // Bind each TextField to the corresponding property in the noteModel
        aTextFields[0].textProperty().bindBidirectional(noteModel.getBoundNote().installedAtProperty());
        streetTextArea.textProperty().bindBidirectional(noteModel.getBoundNote().streetProperty());
        aTextFields[1].textProperty().bindBidirectional(noteModel.getBoundNote().cityProperty());
        aTextFields[2].textProperty().bindBidirectional(noteModel.getBoundNote().stateProperty());
        aTextFields[3].textProperty().bindBidirectional(noteModel.getBoundNote().zipProperty());
        aTextFields[4].textProperty().bindBidirectional(noteModel.getBoundNote().countryProperty());
        cTextFields[0].textProperty().bindBidirectional(noteModel.getBoundNote().contactNameProperty());
        cTextFields[1].textProperty().bindBidirectional(noteModel.getBoundNote().contactPhoneNumberProperty());
        cTextFields[2].textProperty().bindBidirectional(noteModel.getBoundNote().contactEmailProperty());
    }
}


