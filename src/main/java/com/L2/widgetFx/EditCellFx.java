package com.L2.widgetFx;

import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.util.StringConverter;


public class EditCellFx<S, T> extends TableCell<S, T> {

    // Text field for editing
    // TODO: allow this to be a plugable control.
    private final TextField textField = new TextField();

    // Converter for converting the text in the text field to the user type, and vice-versa:
    private final StringConverter<T> converter ;

    public EditCellFx(StringConverter<T> converter) {
        this.converter = converter ;

        itemProperty().addListener((obx, oldItem, newItem) -> {
            if (newItem == null) {
                setText(null);
            } else {
                setText(converter.toString(newItem));
            }
        });
        setGraphic(textField);
        setContentDisplay(ContentDisplay.TEXT_ONLY);

        textField.setOnAction(evt -> commitEdit(this.converter.fromString(textField.getText())));
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                commitEdit(this.converter.fromString(textField.getText()));
            }
        });
    }

    /**
     * Convenience converter that does nothing (converts Strings to themselves and vice-versa...).
     */
    public static final StringConverter<String> IDENTITY_CONVERTER = new StringConverter<>() {

        @Override
        public String toString(String object) {
            return object;
        }

        @Override
        public String fromString(String string) {
            return string;
        }

    };

    /**
     * Convenience method for creating an EditCell for a String value.
     * @return an editable cell
     */
    public static <S> EditCellFx<S, String> createStringEditCell() {
        return new EditCellFx<>(IDENTITY_CONVERTER);
    }


    // set the text of the text field and display the graphic
    @Override
    public void startEdit() {
        super.startEdit();
        textField.setText(converter.toString(getItem()));
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.requestFocus();
    }

    // revert to text display
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(T item) {
        TableView<S> table = getTableView();
        if (table != null) {
            TableColumn<S, T> column = getTableColumn();
            int editingRow = getIndex();  // Get the current row being edited
            @SuppressWarnings("unchecked")
            TablePosition<S, T> currentPosition = table.getFocusModel().getFocusedCell();  // Get the current row with focus

            // Ensure the edit is committed to the correct row
            if (currentPosition.getRow() == editingRow) {
                CellEditEvent<S, T> event = new CellEditEvent<>(table,
                        new TablePosition<>(table, editingRow, column),
                        TableColumn.editCommitEvent(), item);
                Event.fireEvent(column, event);
            }
        }
        super.commitEdit(item);
        setContentDisplay(ContentDisplay.TEXT_ONLY);  // Revert back to text display
    }
}