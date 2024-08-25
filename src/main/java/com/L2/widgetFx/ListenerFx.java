package com.L2.widgetFx;

import com.L2.dto.ResultDTO;
import com.L2.static_tools.StringChecker;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ListenerFx {

    public static void addFocusListener(TextArea textArea, String controlName, StringProperty currentObject, StringProperty statusLabel) {
        textArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            Platform.runLater(() -> {
                if (isNowFocused) {
                    textArea.selectAll();
                    statusLabel.set(controlName + " field is being updated");
                }
                if (wasFocused) {
                    textArea.deselect();
                    currentObject.set(textArea.textProperty().get());
                    statusLabel.set(controlName + " field successfully updated.");
                }
            });
        });
    }

    public static void addFocusListener(TextField textField, String controlName, StringProperty currentObject, StringProperty statusLabel) {
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            ResultDTO resultDTO = StringChecker.checkString(controlName, textField.getText());
            textField.setText(resultDTO.getFieldName());
            Platform.runLater(() -> {
                if (isNowFocused) {
                    textField.selectAll();
                    statusLabel.set(controlName + " field is being edited");
                }
                if (wasFocused) {
                    textField.deselect();
                    currentObject.set(textField.textProperty().get());
                    if(resultDTO.isSuccess()) statusLabel.set(controlName + " field successfully saved.");
                    else statusLabel.set(controlName + " field has incorrect value.");
                }
            });
        });
    }
}