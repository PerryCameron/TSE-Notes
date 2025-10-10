package com.L2.widgetFx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

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

    public static <T> TableColumn<T, String> stringTableColumnSimple(Function<T, String> property, String label) {
        TableColumn<T, String> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> new SimpleStringProperty(property.apply(cellData.getValue())));
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


        // Set the cell value factory to bind to the property with default fallback
        col.setCellValueFactory(cellData -> {
            StringProperty prop = property.apply(cellData.getValue());
            return Bindings.createStringBinding(() ->
                    prop.get() != null && !prop.get().isEmpty() ? prop.get() : defaultValue, prop);
        });


        // Set the cell factory
        col.setCellFactory(column -> new TableCell<>() {
            private final ComboBox<String> comboBox = new ComboBox<>();
            private final Text text = new Text();


            {
                comboBox.getItems().addAll(options);
                comboBox.setValue(defaultValue);
                text.getStyleClass().add("table-text"); // Use a unique class for table Text nodes


                // Commit edit when ComboBox value changes
                comboBox.setOnAction(event -> { // Use setOnAction instead of listener
                    if (isEditing() && getTableRow() != null && getTableRow().getItem() != null) {
                        String newValue = comboBox.getValue() != null ? comboBox.getValue() : defaultValue;
                        commitEdit(newValue);
                    }
                });


                setGraphic(text);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }


            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    String displayValue = item != null && !item.isEmpty() ? item : defaultValue;
                    if (isEditing()) {
                        comboBox.setValue(displayValue);
                        setGraphic(comboBox);
                    } else {
                        text.setText(displayValue);
                        setGraphic(text);
                    }
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                String currentValue = text.getText();
                comboBox.setValue(currentValue);
                setGraphic(comboBox);
                comboBox.show(); // Show dropdown immediately
                comboBox.requestFocus();
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue); // Pass the new value to the TableColumn
                T item = getTableRow().getItem();
                if (item != null) {
                    property.apply(item).set(newValue); // Update the model
                }
                text.setText(newValue); // Update display
                setGraphic(text); // Switch back to text
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                text.setText(getItem()); // Revert to original value
                setGraphic(text);
            }
        });

        col.setEditable(true);
        return col;
    }




}
