package com.L2.mvci.parts.components;

import com.L2.controls.EditableTreeCell;
import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.mvci.parts.PartModel;
import com.L2.mvci.parts.PartView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.VBoxFx;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductFamily implements Builder<Pane> {
    private static final Logger logger = LoggerFactory.getLogger(ProductFamily.class);
    private final PartView partView;
    private final PartModel partModel;
    private Button addFamily;
    private Button addProduct;
    private Button editItem;
    private Button saveButton;
    private Button deleteButton;
    private BooleanProperty editMade = new SimpleBooleanProperty(false);
    private Button testButton;

    public ProductFamily(PartView partView) {
        this.partView = partView;
        this.partModel = partView.getPartModel();
    }

    @Override
    public Pane build() {
        HBox hBox = new HBox();
        VBox vBox = VBoxFx.of(10.0, Pos.TOP_LEFT, 150.0);

        logger.debug("Building TreeView with ProductFamilies: {}", partModel.getProductFamilies());

        partModel.setTreeView(createProductFamilyTreeView());
        partModel.getTreeView().setPrefHeight(200);
        hBox.setPrefHeight(200);
        hBox.getChildren().add(partModel.getTreeView());
        partModel.getTreeView().setPrefWidth(500);
        partModel.getTreeView().setEditable(true);

        // Explicitly set the cell factory to use EditableTreeCell
        partModel.getTreeView().setCellFactory(param -> new EditableTreeCell());

        this.addFamily = ButtonFx.utilityButton("/images/create-16.png", "Add Range", 150);
        this.addProduct = ButtonFx.utilityButton("/images/create-16.png", "Add Product", 150);
        this.editItem = ButtonFx.utilityButton("/images/modify-16.png", "Edit Item", 150);
        this.saveButton = ButtonFx.utilityButton("/images/save-16.png", "Save Changes", 150);
        this.deleteButton = ButtonFx.utilityButton("/images/delete-16.png", "Delete Item", 150);

        this.testButton = ButtonFx.utilityButton("/images/test-16.png", "Test Products", 150);

        editItem.setOnAction(event -> {
            TreeItem<Object> selected = partModel.getTreeView().getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "No item selected.").showAndWait();
            } else if (getTreeItemDepth(selected) == 0) {
                new Alert(Alert.AlertType.WARNING, "Cannot edit the root node.").showAndWait();
            } else {
                partModel.getTreeView().edit(selected);
                editMade.set(true);
            }
        });

        saveButton.setOnAction(event -> {
            editMade.set(false);
        });

        testButton.setOnAction(event -> {
            if (partModel.getProductFamilies() == null) {
                System.out.println("Product families is null");
            } else {
                System.out.println("There are " + partModel.getProductFamilies().size() + " product families.");
                partModel.getProductFamilies().forEach(productFamilyDTO ->
                        System.out.println(productFamilyDTO.testString() + " (instance: " + System.identityHashCode(productFamilyDTO) + ")")
                );
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
                String displayText = getDisplayText(newItem);
                System.out.println("Selected node: " + displayText + " Depth: " + getTreeItemDepth(newItem));
            }
        });

        editMade.addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                if(newItem) {
                    saveButton.setVisible(true);
                    saveButton.setManaged(true);
                } else {
                    saveButton.setVisible(false);
                    saveButton.setManaged(false);
                }
            }
        });
        editMade.set(false);
        setNonSelected();
        hBox.getChildren().add(vBox);
        vBox.getChildren().addAll(addFamily, addProduct, editItem, saveButton, deleteButton, testButton);
        return hBox;
    }

    private void saveToJson() {
    }

    private void addNewProduct() {
        TreeItem<Object> selected = partModel.getTreeView().getSelectionModel().getSelectedItem();
        if (selected == null || getTreeItemDepth(selected) != 1) {
            new Alert(Alert.AlertType.WARNING, "Select a range to add a product.").showAndWait();
            return;
        }

        ProductFamilyDTO pf = (ProductFamilyDTO) selected.getValue();
        TreeItem<Object> rangeItem = selected;

        String newProduct = "New Product";
        pf.getProductFamilies().add(newProduct);
        logger.debug("Added product '{}' to ProductFamilyDTO: {} (instance: {})", newProduct, pf.getRange(), System.identityHashCode(pf));

        TreeItem<Object> newProductItem = new TreeItem<>(newProduct);
        rangeItem.getChildren().add(newProductItem);
        partModel.getTreeView().getSelectionModel().select(newProductItem);
    }

    private void addNewFamily() {
        ProductFamilyDTO newFamily = new ProductFamilyDTO("New Range", new ArrayList<>());
        partModel.getProductFamilies().add(newFamily);
        logger.debug("Added new ProductFamilyDTO: {} (instance: {})", newFamily.getRange(), System.identityHashCode(newFamily));

        TreeItem<Object> rootItem = partModel.getTreeView().getRoot();
        TreeItem<Object> newRangeItem = new TreeItem<>(newFamily);
        newRangeItem.setExpanded(true);
        rootItem.getChildren().add(newRangeItem);
        partModel.getTreeView().getSelectionModel().select(newRangeItem);
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
            logger.debug("Creating range node for ProductFamilyDTO: {} (instance: {})", pf.getRange(), System.identityHashCode(pf));
            TreeItem<Object> rangeItem = new TreeItem<>(pf);
            rangeItem.setExpanded(true);
            for (Object productFamily : pf.getProductFamilies() != null ? pf.getProductFamilies() : List.of()) {
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
        editItem.setVisible(false);
        editItem.setManaged(false);
        deleteButton.setVisible(false);
        deleteButton.setManaged(false);
        saveButton.setVisible(false);
        saveButton.setManaged(false);
    }

    private void setTreeTop() {
        addFamily.setVisible(true);
        addFamily.setManaged(true);
        addProduct.setVisible(false);
        addProduct.setManaged(false);
        editItem.setVisible(false);
        editItem.setManaged(false);
        deleteButton.setVisible(false);
        deleteButton.setManaged(false);
    }

    private void setButtonFamily() {
        addFamily.setVisible(false);
        addFamily.setManaged(false);
        addProduct.setVisible(true);
        addProduct.setManaged(true);
        editItem.setVisible(true);
        editItem.setManaged(true);
        deleteButton.setVisible(false);
        deleteButton.setManaged(false);
    }

    private void setButtonProduct() {
        addFamily.setVisible(false);
        addFamily.setManaged(false);
        addProduct.setVisible(false);
        addProduct.setManaged(false);
        editItem.setVisible(true);
        editItem.setManaged(true);
        deleteButton.setVisible(false);
        deleteButton.setManaged(false);
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

