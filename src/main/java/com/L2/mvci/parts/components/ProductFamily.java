package com.L2.mvci.parts.components;

import com.L2.controls.EditableTreeCell;
import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.mvci.parts.PartModel;
import com.L2.mvci.parts.PartView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.List;

public class ProductFamily implements Builder<Pane> {

    private final PartView partView;
    private final PartModel partModel;
    private Button addFamily;
    private Button addProduct;
    private Button editItem;
    private Button saveButton;

    public ProductFamily(PartView partView) {
        this.partView = partView;
        this.partModel = partView.getPartModel();
    }

    @Override
    public Pane build() {
        HBox hBox = new HBox();
        VBox vBox = VBoxFx.of(10.0, Pos.TOP_LEFT, 150.0);

        partModel.setTreeView(createProductFamilyTreeView());
        partModel.getTreeView().setPrefHeight(200);
        hBox.setPrefHeight(200);
        hBox.getChildren().add(partModel.getTreeView());
        partModel.getTreeView().setPrefWidth(500);
        partModel.getTreeView().setEditable(true);

        // Explicitly set the cell factory to use EditableTreeCell
        partModel.getTreeView().setCellFactory(param -> new EditableTreeCell());

        this.addFamily = ButtonFx.utilityButton("/images/new-16.png", "Add Family", 150);
        this.addProduct = ButtonFx.utilityButton("/images/new-16.png", "Add Product", 150);
        this.editItem = ButtonFx.utilityButton("/images/modify-16.png", "Edit Item ", 150);
        this.saveButton = ButtonFx.utilityButton("/images/save-16.png", "Edit Item ", 150);

        editItem.setOnAction(event -> {
            TreeItem<Object> selected = partModel.getTreeView().getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "No item selected.").showAndWait();
            } else if (getTreeItemDepth(selected) == 0) {
                new Alert(Alert.AlertType.WARNING, "Cannot edit the root node.").showAndWait();
            } else {
                partModel.getTreeView().edit(selected);
            }
        });

        // Other button actions (placeholders)
        addFamily.setOnAction(event -> addNewFamily());
        addProduct.setOnAction(event -> addNewProduct());
        saveButton.setOnAction(event -> saveToJson());

        partModel.getTreeView().getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                switch (getTreeItemDepth(newItem)) {
                    case 0 -> setTreeTop();
                    case 1 -> setButtonFamily();
                    case 2 -> setButtonProduct();
                }
                System.out.println("Selected node: " + newItem.getValue() + " Depth: " + getTreeItemDepth(newItem));
            }
        });
        setNonSelected();
        hBox.getChildren().add(vBox);
        vBox.getChildren().addAll(addFamily, addProduct, editItem);
        return hBox;
    }

    private void saveToJson() {
    }

    private void addNewProduct() {
    }

    private void addNewFamily() {
    }

    // Updated method to create TreeView<Object>
    private TreeView<Object> createProductFamilyTreeView() {
        TreeItem<Object> rootItem = createTreeItemRoot(partModel.getProductFamilies());
        TreeView<Object> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(true);
        return treeView;
    }

    // Updated static method to create TreeItem<Object> root
    public static TreeItem<Object> createTreeItemRoot(List<ProductFamilyDTO> productFamilies) {
        TreeItem<Object> rootItem = new TreeItem<>("Product Families");
        rootItem.setExpanded(true);
        for (ProductFamilyDTO pf : productFamilies) {
            TreeItem<Object> rangeItem = new TreeItem<>(pf);
            rangeItem.setExpanded(true);
            for (String productFamily : pf.getProductFamilies()) {
                rangeItem.getChildren().add(new TreeItem<>(productFamily));
            }
            rootItem.getChildren().add(rangeItem);
        }
        return rootItem;
    }

    public static String getDisplayText(TreeItem<Object> item) {
        if (item == null) return "";
        Object value = item.getValue();
        if (value instanceof ProductFamilyDTO pf) {
            return pf.getRange() != null ? pf.getRange() : "";
        } else if (value instanceof String s) {
            return s;
        }
        return value != null ? value.toString() : "";
    }


    private void setNonSelected() {
        addFamily.setVisible(false);
        addFamily.setManaged(false);
        addProduct.setVisible(false);
        addProduct.setManaged(false);
        saveButton.setVisible(false);
        saveButton.setManaged(false);
        editItem.setVisible(false);
        editItem.setManaged(false);
    }

    private void setTreeTop() {
        addFamily.setVisible(true);
        addFamily.setManaged(true);
        addProduct.setVisible(false);
        addProduct.setManaged(false);
        saveButton.setVisible(false);
        saveButton.setManaged(false);
        editItem.setVisible(true);
        editItem.setManaged(true);
    }

    private void setButtonFamily() {
        addFamily.setVisible(false);
        addFamily.setManaged(false);
        addProduct.setVisible(true);
        addProduct.setManaged(true);
        saveButton.setVisible(false);
        saveButton.setManaged(false);
        editItem.setVisible(true);
        editItem.setManaged(true);
    }

    private void setButtonProduct() {
        addFamily.setVisible(false);
        addFamily.setManaged(false);
        addProduct.setVisible(false);
        addProduct.setManaged(false);
        saveButton.setVisible(false);
        saveButton.setManaged(false);
        editItem.setVisible(true);
        editItem.setManaged(true);
    }

    // Calculate the depth of a TreeItem in the TreeView
    public static int getTreeItemDepth(TreeItem<?> item) {
        int depth = 0;
        TreeItem<?> parent = item.getParent();
        while (parent != null) {
            depth++;
            parent = parent.getParent();
        }
        return depth;
    }
}

