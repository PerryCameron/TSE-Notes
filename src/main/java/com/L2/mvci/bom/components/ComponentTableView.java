package com.L2.mvci.bom.components;

import com.L2.dto.bom.ComponentDTO;
import com.L2.mvci.bom.BomModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Arrays;

public class ComponentTableView {
    private BomModel bomModel;

    public ComponentTableView(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    public TableView<ComponentDTO> build() {
        TableView<ComponentDTO> tableView = new TableView<>();
//        tableView.setItems(bomModel.getSearchedComponents()); // Assuming searchedComponents is accessible (public field)

        // Item column
        TableColumn<ComponentDTO, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        itemColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));

        // Level column
        TableColumn<ComponentDTO, Integer> levelColumn = new TableColumn<>("Level");
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        levelColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));

        // Description column
        TableColumn<ComponentDTO, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.70));

        tableView.getColumns().addAll(Arrays.asList(itemColumn, levelColumn, descriptionColumn));
        HBox.setHgrow(tableView, Priority.ALWAYS);

        return tableView;
    }
}
