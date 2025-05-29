package com.L2.mvci.parts.components;

import com.L2.controls.EditableTreeCell;
import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.mvci.parts.PartMessage;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductFamily implements Builder<Pane> {
    private static final Logger logger = LoggerFactory.getLogger(ProductFamily.class);
    private final PartView partView;
    private final PartModel partModel;
    private Button addRange;
    private Button addProduct;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;
    private Button cancelButton;
    private final Set<TreeItem<Object>> markedForDeletion = new HashSet<>();

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
        partModel.getTreeView().setCellFactory(param -> new EditableTreeCell(this));

        this.addRange = ButtonFx.utilityButton("/images/create-16.png", "Add Range", 150);
        this.addProduct = ButtonFx.utilityButton("/images/create-16.png", "Add Product", 150);
        this.editButton = ButtonFx.utilityButton("/images/modify-16.png", "Edit", 150);
        this.saveButton = ButtonFx.utilityButton("/images/save-16.png", "Save Changes", 150);
        this.deleteButton = ButtonFx.utilityButton("/images/delete-16.png", "Delete Item", 150);
        this.cancelButton = ButtonFx.utilityButton("/images/cancel-16.png", "Cancel Edit", 150);

        editButton.setOnAction(event -> partModel.getTreeView().editableProperty().set(true));
        addRange.setOnAction(event -> addNewRange());
        addProduct.setOnAction(event -> addNewProduct());
        deleteButton.setOnAction(event -> markForDeletion());
        saveButton.setOnAction(event -> saveChanges());
        cancelButton.setOnAction(event -> cancelEdit());

        // we use the editable property in treeView to make an (edit mode)
        setModeListener();


        partModel.getTreeView().getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                if (partModel.getTreeView().editableProperty().get())
                    setBySelection();
                String displayText = getDisplayText(newItem);
                logger.debug("Selected node: {} Depth: {}", displayText, getTreeItemDepth(newItem));
            }
        });

        partModel.updatedRangeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue) {
                partModel.getTreeView().editableProperty().set(false);
                partModel.updatedRangeProperty().set(false);
                setBySelection();
            }
        });

        setButtonVisibility(false,false,false,false);
        partModel.getTreeView().setEditable(false);
        buttonVisible(saveButton,false); // I should not need this
        hBox.getChildren().add(vBox);
        vBox.getChildren().addAll(addRange, addProduct, editButton, saveButton, deleteButton, cancelButton);
        return hBox;
    }

    private void setModeListener() {
        partModel.getTreeView().editableProperty().addListener((observable, oldValue, editMode) -> {
            if(editMode) {
                // we are in edit mode we don't need the edit button
                buttonVisible(editButton, false);
                buttonVisible(saveButton, true);
                setBySelection();
            } else {
                // we are in normal mode
                buttonVisible(editButton, true);
                cancelEdit();
            }
        });
    }

    private void cancelEdit() {
        markedForDeletion.clear();
        partView.getAction().accept(PartMessage.REFRESH_TREEVIEW);
        partModel.getTreeView().refresh();
        partModel.getTreeView().editableProperty().set(false);
        logger.debug("Cancelled edits and refreshed TreeView");
        buttonVisible(saveButton, false);
        setButtonVisibility(false,false,false,false); // why did you remove this?
    }

    private void setBySelection() {
        if(partModel.getTreeView().getSelectionModel().selectedItemProperty().get() == null) {
            logger.warn("None of the treeView items are selected");
            //                range, product, delete, cancel
            setButtonVisibility(false,false,false,true);
            return;
        }
        int selection = getTreeItemDepth(partModel.getTreeView().getSelectionModel().getSelectedItem());
        switch (selection) {
            //                          add range, add product, delete item, cancel edit
            case 0 -> setButtonVisibility(true,false,false,true);
            case 1 -> setButtonVisibility(false,true,true,true);
            case 2 -> setButtonVisibility(false,false,true,true);
        }
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

    private void markForDeletion() {
        TreeItem<Object> selected = partModel.getTreeView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "No item selected.").showAndWait();
            return;
        }
        int depth = getTreeItemDepth(selected);
        if (depth == 0) {
            new Alert(Alert.AlertType.WARNING, "Cannot mark the root node for deletion.").showAndWait();
            return;
        }

        if (depth == 1) {
            markedForDeletion.add(selected);
            selected.getChildren().forEach(markedForDeletion::add);
            logger.debug("Marked range '{}' and its children for deletion (instance: {})",
                    getDisplayText(selected), System.identityHashCode(selected.getValue()));
        } else {
            markedForDeletion.add(selected);
            logger.debug("Marked product '{}' for deletion in range '{}'",
                    getDisplayText(selected), getDisplayText(selected.getParent()));
        }
        partModel.getTreeView().refresh();
    }

    private void saveChanges() {
        List<TreeItem<Object>> toRemove = new ArrayList<>(markedForDeletion);
        for (TreeItem<Object> item : toRemove) {
            int depth = getTreeItemDepth(item);
            TreeItem<Object> parent = item.getParent();
            if (depth == 1) {
                ProductFamilyDTO pf = (ProductFamilyDTO) item.getValue();
                if (partModel.getProductFamilies().remove(pf)) {
                    parent.getChildren().remove(item);
                    logger.debug("Removed range '{}' (instance: {})", pf.getRange(), System.identityHashCode(pf));
                } else {
                    logger.warn("Failed to remove range '{}' from ProductFamilies", pf.getRange());
                }
            } else if (depth == 2) {
                String product = (String) item.getValue();
                ProductFamilyDTO pf = (ProductFamilyDTO) parent.getValue();
                if (pf.getProductFamilies().remove(product)) {
                    parent.getChildren().remove(item);
                    logger.debug("Removed product '{}' from ProductFamilyDTO: {} (instance: {})",
                            product, pf.getRange(), System.identityHashCode(pf));
                } else {
                    logger.warn("Failed to remove product '{}' from ProductFamilyDTO: {}", product, pf.getRange());
                }
            }
        }
        markedForDeletion.clear();
        partModel.getTreeView().refresh();
        partView.getAction().accept(PartMessage.SAVE_PIM_TO_JSON);
        setButtonVisibility(false,false,false,false);
        logger.debug("Saved changes and cleared deletion marks");
    }

    public boolean isMarkedForDeletion(TreeItem<Object> item) {
        return item != null && markedForDeletion.contains(item);
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
        TreeItem<Object> rootItem = new TreeItem<>("Ranges");
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

    private void setButtonVisibility(boolean range, boolean product, boolean family, boolean cancel) {
        buttonVisible(addRange, range);
        buttonVisible(addProduct, product);
        buttonVisible(deleteButton, family);
        buttonVisible(cancelButton, cancel);
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


