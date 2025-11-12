package com.L2.mvci.bom.components;

import com.L2.dto.bom.ComponentDTO;
import com.L2.mvci.bom.BomModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
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

        TableView.TableViewSelectionModel<ComponentDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                long targetId = newSelection.itemIdProperty().get();
                TreeItem<ComponentDTO> root = bomModel.getRoot();
                collapseAll(root);
                TreeItem<ComponentDTO> targetItem = findTreeItem(root, targetId);
                if (targetItem != null) {
                    expandTo(targetItem);
                    // Optional: Select and scroll to the item in the TreeTableView
                    bomModel.getTreeTable().getSelectionModel().select(targetItem);
                    bomModel.getTreeTable().scrollTo(bomModel.getTreeTable().getRow(targetItem));
                }
            }
        });

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

    // Add these methods to the ComponentTableView class

    private void collapseAll(TreeItem<ComponentDTO> item) {
        if (item == null) return;
        item.setExpanded(false);
        for (TreeItem<ComponentDTO> child : item.getChildren()) {
            collapseAll(child);
        }
    }

    private TreeItem<ComponentDTO> findTreeItem(TreeItem<ComponentDTO> item, long targetId) {
        if (item.getValue().itemIdProperty().get() == targetId) {
            return item;
        }
        for (TreeItem<ComponentDTO> child : item.getChildren()) {
            TreeItem<ComponentDTO> found = findTreeItem(child, targetId);
            if (found != null) return found;
        }
        return null;
    }

    private void expandTo(TreeItem<ComponentDTO> item) {
        if (item == null) return;
        TreeItem<ComponentDTO> current = item.getParent(); // Start from parent, as the item itself doesn't need expansion unless it has children
        while (current != null) {
            current.setExpanded(true);
            current = current.getParent();
        }
    }
}
