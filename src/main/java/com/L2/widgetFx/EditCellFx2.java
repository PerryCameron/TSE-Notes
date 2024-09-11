package com.L2.widgetFx;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;


public class EditCellFx2 {


    // Create an editable cell for String values
    public static <T> TableCell<T, String> createStringEditCell() {
        return new TableCell<T, String>() {
            private TextField textField;


            @Override
            public void startEdit() {
                super.startEdit();
                if (textField == null) {
                    createTextField();
                }
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                textField.requestFocus();
            }


            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }


            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(getString());
                        }
                        setGraphic(textField);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    } else {
                        setText(getString());
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                }
            }


            private void createTextField() {
                textField = new TextField(getString());
                textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                textField.setOnAction(e -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitEdit(textField.getText());
                    }
                });
            }


            private String getString() {
                return getItem() == null ? "" : getItem();
            }
        };
    }
}



