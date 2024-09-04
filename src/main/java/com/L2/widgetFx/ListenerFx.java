package com.L2.widgetFx;

import com.L2.dto.ResultDTO;
import com.L2.static_tools.StringChecker;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListenerFx {

    private static final Logger logger = LoggerFactory.getLogger(ListenerFx.class);

    public static void addFocusListener(TextArea textArea, String controlName, StringProperty currentObject, StringProperty statusLabel) {
        logger.info("Setting focus listener for issue text area");
        textArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                Platform.runLater(() -> {
                    textArea.selectAll();
                    statusLabel.set(controlName + " field is being updated");
                });
            } else if (wasFocused) {
                Platform.runLater(() -> {
                    currentObject.set(textArea.getText());
                    textArea.deselect();
                    statusLabel.set(controlName + " field successfully updated.");
                });
            }
        });
    }

    public static void addFocusListener(TextField textField, String controlName, StringProperty currentObject, StringProperty statusLabel) {
        logger.info("Setting focus listener for issue textField {}", controlName);
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            ResultDTO resultDTO = StringChecker.checkString(controlName, textField.getText());
            textField.setText(resultDTO.getFieldName());
            if (isNowFocused) {
                Platform.runLater(() -> {
                    textField.selectAll();
                    statusLabel.set(controlName + " field is being edited");
                });
            }
            if (wasFocused) {
                Platform.runLater(() -> {
                    textField.deselect();
                    currentObject.set(textField.textProperty().get());
                    if (resultDTO.isSuccess()) statusLabel.set(controlName + " field successfully saved.");
                    else statusLabel.set(controlName + " field has incorrect value.");
                });
            }
        });
    }
}