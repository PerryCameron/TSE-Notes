package com.L2.widgetFx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;

import java.util.function.Function;

public class TableColumnFx {
    public static <T> TableColumn<T, String> editableStringTableColumn(Function<T, StringProperty> property, String label) {
        TableColumn<T, String> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        col.setCellFactory(column -> EditCellFx.createStringEditCell());
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
}
