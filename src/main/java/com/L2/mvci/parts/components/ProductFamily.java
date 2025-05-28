package com.L2.mvci.parts.components;

import com.L2.dto.ProductFamilyFx;
import com.L2.mvci.parts.PartModel;
import com.L2.mvci.parts.PartView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
        this.addFamily = ButtonFx.utilityButton("/images/new-16.png", "Add Family", 150);
        this.addProduct = ButtonFx.utilityButton("/images/new-16.png", "Add Product", 150);
        this.editItem = ButtonFx.utilityButton("/images/modify-16.png", "Edit Item ", 150);
        this.saveButton = ButtonFx.utilityButton("/images/save-16.png", "Edit Item ", 150);
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

    private TreeView<String> createProductFamilyTreeView() {
        TreeItem<String> rootItem = createTreeItemRoot(partModel.getProductFamilies());
        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(true);
        return treeView;
    }

    // static so that it can be used in PartView as well
    public static TreeItem<String> createTreeItemRoot(List<ProductFamilyFx> productFamilies) {
        TreeItem<String> rootItem = new TreeItem<>("Product Families");
        rootItem.setExpanded(true);
        for (ProductFamilyFx pf : productFamilies) {
            TreeItem<String> rangeItem = new TreeItem<>(pf.getRange());
            rangeItem.setExpanded(true);
            for (String productFamily : pf.getProductFamilies()) {
                rangeItem.getChildren().add(new TreeItem<>(productFamily));
            }
            rootItem.getChildren().add(rangeItem);
        }
        return rootItem;
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
    private int getTreeItemDepth(TreeItem<?> item) {
        int depth = 0;
        TreeItem<?> parent = item.getParent();
        while (parent != null) {
            depth++;
            parent = parent.getParent();
        }
        return depth;
    }
}
