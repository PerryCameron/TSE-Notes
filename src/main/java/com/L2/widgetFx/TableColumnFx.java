package com.L2.widgetFx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.List;
import java.util.function.Function;

public class TableColumnFx {
    public static <T> TableColumn<T, String> editableStringTableColumn(Function<T, StringProperty> property, String label) {
        TableColumn<T, String> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        col.setCellFactory(column -> EditCellFx.createStringEditCell());  // EditCellStandardFx
        return col;
    }

    public static <T> TableColumn<T, String> stringTableColumn(Function<T, StringProperty> property, String label) {
        TableColumn<T, String> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        return col;
    }

    public static <T> TableColumn<T, Integer> integerTableColumn(Function<T, IntegerProperty> property, String label) {
        TableColumn<T, Integer> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()).asObject());
        return col;
    }



    public static <T> TableColumn<T, String> comboBoxTableColumn(
            Function<T, StringProperty> property,
            String label,
            List<String> options,
            String defaultValue) {

        TableColumn<T, String> col = new TableColumn<>(label);

        // Set the cell value factory to display the property value
        col.setCellValueFactory(cellData -> {
            StringProperty prop = property.apply(cellData.getValue());
            return Bindings.createStringBinding(() ->
                    prop.get() != null && !prop.get().isEmpty() ? prop.get() : defaultValue, prop);
        });

        // Set the cell factory to use a ComboBox for editing
        col.setCellFactory(column -> new TableCell<T, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll(options);
                comboBox.setValue(defaultValue); // Set initial value

                // Update model only when editing
                comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
                    if (isEditing() && getTableRow() != null && getTableRow().getItem() != null) {
                        T item = getTableRow().getItem();
                        property.apply(item).set(newValue != null ? newValue : defaultValue);
                    }
                });

                setGraphic(comboBox);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                comboBox.setVisible(false); // Hidden until editing
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(item != null && !item.isEmpty() ? item : defaultValue);
                    setGraphic(comboBox);
                    comboBox.setVisible(isEditing());
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                comboBox.setVisible(true);
                comboBox.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                comboBox.setVisible(false);
            }
        });

        col.setEditable(true);
        return col;
    }
}
