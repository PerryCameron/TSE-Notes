package com.L2.widgetFx;

import com.L2.dto.global_spares.SparesDTO;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ListViewFx {

    public static ListView<SparesDTO> partListView(ObservableList<SparesDTO> parts) {
        ListView<SparesDTO> listView = new ListView<>();

        // Set ListView to grow vertically and limit its height
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setPrefHeight(200); // Set a reasonable initial height
        listView.setMaxHeight(300); // Optional: limit max height to avoid excessive growth
        // Set the items to the ListView
        listView.setItems(parts);

        // Customize the cell factory to display partNumber and partDescription
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(SparesDTO part, boolean empty) {
                super.updateItem(part, empty);
                if (empty || part == null) {
                    setText(null);
                } else {
                    setText(part.getSpareItem() + " - " + part.getSpareDescription() + " -  In Catalog: " + !part.getArchived());
                }
            }
        });

        return listView;
    }

}
