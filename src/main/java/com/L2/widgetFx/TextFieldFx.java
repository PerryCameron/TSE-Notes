package com.L2.widgetFx;

import com.L2.dto.ResultDTO;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteView;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import java.util.function.Function;

public class TextFieldFx {
    public static TextField of(double width, String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefWidth(width);
        return textField;
    }


    public static TextField of(double width, double height, String prompt, StringProperty binder) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefSize(width, height);
        textField.textProperty().bindBidirectional(binder);
        return textField;
    }

    public static TextField of(double width, Property<?> property) {
        TextField textField = new TextField();
        textField.setPrefWidth(width);
        if (property instanceof StringProperty) {
            textField.textProperty().bindBidirectional((StringProperty) property);
        } else if (property instanceof IntegerProperty) {
            textField.textProperty().bindBidirectional((IntegerProperty) property, new NumberStringConverter());
        } else {
            throw new IllegalArgumentException("Unsupported property type: " + property.getClass());
        }
        return textField;
    }

    public static PasswordField passwordFieldOf(double width, String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setPrefWidth(width);
        return passwordField;
    }

    public static TextField standardTextField(double width, String prompt) { // used to have a noteView parameter
        TextField textField = TextFieldFx.of(width, prompt);
        textField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue)
                Platform.runLater(textField::selectAll);
//            else noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
        });
        return textField;
    }

    public static TextField createValidatedTextField(double width, String promptText, Function<String, ResultDTO> validationFunction, NoteView noteView) {
        TextField textField = TextFieldFx.of(width, promptText);

        textField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(textField::selectAll);
            } else {  // On focus lost
                ResultDTO resultDTO = validationFunction.apply(textField.getText());
                if (resultDTO.isSuccess()) {
                    textField.setText(resultDTO.getFieldName());
                    textField.getStyleClass().remove("text-field-error");  // Remove error class
//                    noteView.getAction().accept(NoteMessage.SAVE_OR_UPDATE_NOTE);
                } else {
                    if(textField.getText().equals(""))
                        textField.getStyleClass().remove("text-field-error");  // Remove error class
                    else if(!textField.getStyleClass().contains("text-field-error")) {
                        textField.getStyleClass().add("text-field-error");  // Add error class
                    }
                }
            }
        });
        return textField;
    }
}
