package com.L2.mvci.parts.components;

import com.L2.controls.EditableTreeCell;
import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.mvci.parts.PartMessage;
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
    private final BooleanProperty editCompleted = new SimpleBooleanProperty(false);
    private final BooleanProperty editMode = new SimpleBooleanProperty(false);
    private final PartView partView;
    private final PartModel partModel;
    private Button addRange;
    private Button addProduct;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;

//    private Button testButton;

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

        this.addRange = ButtonFx.utilityButton("/images/create-16.png", "Add Range", 150);
        this.addProduct = ButtonFx.utilityButton("/images/create-16.png", "Add Product", 150);
        this.editButton = ButtonFx.utilityButton("/images/modify-16.png", "Edit", 150);
        this.saveButton = ButtonFx.utilityButton("/images/save-16.png", "Save", 150);
        this.deleteButton = ButtonFx.utilityButton("/images/delete-16.png", "Delete Item", 150);
//        this.testButton = ButtonFx.utilityButton("/images/test-16.png", "Test Products", 150);

        editButton.setOnAction(event -> {
            editMode.set(true);

        });

        // Other button actions (placeholders)
        addRange.setOnAction(event -> addNewRange());

        addProduct.setOnAction(event -> addNewProduct());

        saveButton.setOnAction(event -> {
//            editMode.set(false);
            partView.getAction().accept(PartMessage.SAVE_PIM_TO_JSON);
        });

        editMode.addListener((observable, oldValue, newValue) -> {
            // make button disappear
            buttonVisible(editButton, oldValue);
            // save button appears
            buttonVisible(saveButton, newValue);
            logger.info("Now in Edit mode{}: ", editMode.get());
            partModel.getTreeView().setEditable(editMode.get());
        });

        partModel.getTreeView().getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                if (editMode.get())
                    switch (getTreeItemDepth(newItem)) {
                        case 0 -> setTreeTop();
                        case 1 -> setButtonRange();
                        case 2 -> setButtonProduct();
                    }
                String displayText = getDisplayText(newItem);
                System.out.println("Selected node: " + displayText + " Depth: " + getTreeItemDepth(newItem));
            }
        });

        partModel.updatedRangeProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Range property is " + newValue);
            if (newValue != null) {
                if (newValue) {
                    editMode.set(false);
                    partModel.updatedRangeProperty().set(false);
                }
            }
        });

        setNonSelected();
        partModel.getTreeView().setEditable(editMode.get());
        buttonVisible(saveButton,false); // I should not need this
        hBox.getChildren().add(vBox);
        vBox.getChildren().addAll(addRange, addProduct, editButton, saveButton, deleteButton);
        return hBox;
    }

    private void addNewProduct() {
        TreeItem<Object> selected = partModel.getTreeView().getSelectionModel().getSelectedItem();
        if (selected == null || getTreeItemDepth(selected) != 1) {
            new Alert(Alert.AlertType.WARNING, "Select a range to add a product.").showAndWait();
            return;
        }
        ProductFamilyDTO pf = (ProductFamilyDTO) selected.getValue();
        String newProduct = "New Product";
        pf.getProductFamilies().add(newProduct);
        logger.debug("Added product '{}' to ProductFamilyDTO: {} (instance: {})", newProduct, pf.getRange(), System.identityHashCode(pf));
        TreeItem<Object> newProductItem = new TreeItem<>(newProduct);
        selected.getChildren().add(newProductItem);
        partModel.getTreeView().getSelectionModel().select(newProductItem);
    }

    private void addNewRange() {
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

    private static void buttonVisible(Button button, boolean value) {
        button.setVisible(value);
        button.setManaged(value);
    }

    private void setNonSelected() {
        buttonVisible(addRange, false);
        buttonVisible(addProduct, false);
        buttonVisible(deleteButton, false);
    }

    private void setTreeTop() {
        buttonVisible(addRange, true);
        buttonVisible(addProduct, false);
        buttonVisible(deleteButton, false);
    }

    private void setButtonRange() {
        buttonVisible(addRange, false);
        buttonVisible(addProduct, true);
        buttonVisible(deleteButton, false);
    }

    private void setButtonProduct() {
        buttonVisible(addRange, false);
        buttonVisible(addProduct, false);
        buttonVisible(deleteButton, false);
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

/////////////////////////////
//            editItem.setOnAction(event -> {
//            editMode.set(true);
//            //make button disappear
//            button(editItem, false);
//            //save button appears
//            button(saveButton, true);
//            logger.info("Now in Edit mode");
//            TreeItem<Object> selected = partModel.getTreeView().getSelectionModel().getSelectedItem();
//            if (selected == null) {
//                new Alert(Alert.AlertType.WARNING, "No item selected.").showAndWait();
//            } else if (getTreeItemDepth(selected) == 0) {
//                new Alert(Alert.AlertType.WARNING, "Cannot edit the root node.").showAndWait();
//            } else {
//                partModel.getTreeView().edit(selected);
//                editMade.set(true);
//                System.out.println("Edit Mode: " + editMade.get());
//            }
//        });

//        testButton.setOnAction(event -> {
//            if (partModel.getProductFamilies() == null) {
//                System.out.println("Product families is null");
//            } else {
//                System.out.println("There are " + partModel.getProductFamilies().size() + " product families.");
//                partModel.getProductFamilies().forEach(productFamilyDTO ->
//                        System.out.println(productFamilyDTO.testString() + " (instance: " + System.identityHashCode(productFamilyDTO) + ")")
//                );
//            }
//        });

//        editCompleted.addListener((obs, oldItem, newItem) -> {
//            if (newItem != null) {
//                if(newItem) {
//                    saveButton.setVisible(true);
//                    saveButton.setManaged(true);
//                } else {
//                    saveButton.setVisible(false);
//                    saveButton.setManaged(false);
//                }
//            }
//        });
//        editCompleted.set(false);