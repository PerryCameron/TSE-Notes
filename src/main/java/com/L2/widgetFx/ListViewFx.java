package com.L2.widgetFx;

import com.L2.dto.global_spares.ProductToSparesDTO;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class ListViewFx {

    public static ListView<ProductToSparesDTO> partListView(ObservableList<ProductToSparesDTO> parts) {
        ListView<ProductToSparesDTO> listView = new ListView<>();

        // Set ListView to grow vertically and limit its height
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setPrefHeight(200); // Set a reasonable initial height
        listView.setMaxHeight(300); // Optional: limit max height to avoid excessive growth
        // Set the items to the ListView
        listView.setItems(parts);

        // Customize the cell factory to display partNumber and partDescription
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ProductToSparesDTO part, boolean empty) {
                super.updateItem(part, empty);
                if (empty || part == null) {
                    setText(null);
                } else {
                    setText(part.getSpareItem() + " - " + part.getSpareDescription());
                }
            }
        });

        return listView;
    }

}
