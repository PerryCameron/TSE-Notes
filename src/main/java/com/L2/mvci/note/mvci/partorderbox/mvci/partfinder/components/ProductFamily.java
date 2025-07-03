package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components;

import com.L2.controls.EditableTreeCell;
import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderMessage;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderModel;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderView;
import com.L2.static_tools.ImageResources;
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
import java.util.stream.Collectors;

public class ProductFamily implements Builder<Pane> {
    private static final Logger logger = LoggerFactory.getLogger(ProductFamily.class);
    private final PartFinderView partView;
    private final PartFinderModel partFinderModel;
    private Button addRange;
    private Button addProduct;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;
    private Button cancelButton;
    private final Set<TreeItem<Object>> markedForDeletion = new HashSet<>();
//    private Button testButton;

    public ProductFamily(PartFinderView partView) {
        this.partView = partView;
        this.partFinderModel = partView.getPartFinderModel();
    }

    @Override
    public Pane build() {
        HBox hBox = new HBox();
        VBox vBox = VBoxFx.of(10.0, Pos.TOP_LEFT, 150.0);

        logger.debug("Building TreeView with ProductFamilies: {}", partFinderModel.getProductFamilies());
        partFinderModel.setTreeView(createProductFamilyTreeView());
        partFinderModel.getTreeView().setPrefHeight(200);

        hBox.setPrefHeight(200);
        hBox.getChildren().add(partFinderModel.getTreeView());
        partFinderModel.getTreeView().setPrefWidth(500);
        partFinderModel.getTreeView().setEditable(true);
        // Explicitly set the cell factory to use EditableTreeCell
        partFinderModel.getTreeView().setCellFactory(param -> new EditableTreeCell(this, partFinderModel));

        this.addRange = ButtonFx.utilityButton(ImageResources.NEW, "Add Range", 150);
        this.addProduct = ButtonFx.utilityButton(ImageResources.NEW, "Add Product", 150);
        this.editButton = ButtonFx.utilityButton(ImageResources.EDIT, "Edit", 150);
        this.saveButton = ButtonFx.utilityButton(ImageResources.SAVE, "Save Changes", 150);
        this.deleteButton = ButtonFx.utilityButton(ImageResources.DELETE, "Delete Item", 150);
        this.cancelButton = ButtonFx.utilityButton(ImageResources.CANCEL, "Cancel Edit", 150);

        editButton.setOnAction(event -> partFinderModel.getTreeView().editableProperty().set(true));
        addRange.setOnAction(event -> addNewRange());
        addProduct.setOnAction(event -> addNewProduct());
        deleteButton.setOnAction(event -> markForDeletion());
        saveButton.setOnAction(event -> saveChanges());
        cancelButton.setOnAction(event -> cancelEdit());

        // we use the editable property in treeView to make an (edit mode)
        setModeListener();

        partFinderModel.getTreeView().getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null && partFinderModel.getTreeView().editableProperty().get()) {
                System.out.println("selected Item property " + newItem);
                if (partFinderModel.getTreeView().editableProperty().get())
                    System.out.println("setBySelection called from selected item property");
                setBySelection();
                String displayText = getDisplayText(newItem);
                logger.debug("Selected node: {} Depth: {}", displayText, getTreeItemDepth(newItem));
            }
        });

        setButtonVisibility(false, false, false, false);
        partFinderModel.getTreeView().setEditable(false);
        ButtonFx.buttonVisible(saveButton, false); // I should not need this
        hBox.getChildren().add(vBox);
        vBox.getChildren().addAll(addRange, addProduct, editButton, saveButton, deleteButton, cancelButton);
        return hBox;
    }

    private void test() {
        partFinderModel.getProductFamilies().forEach(productFamilyDTO -> productFamilyDTO.toFullString());
    }

    private void setModeListener() {
        partFinderModel.getTreeView().editableProperty().addListener((observable, oldValue, editMode) -> {
            if (editMode) {
                ButtonFx.buttonVisible(editButton, false);
                ButtonFx.buttonVisible(saveButton, true);
                setBySelection();
            } else {
                ButtonFx.buttonVisible(editButton, true);
                cancelEdit();
            }
        });
    }

    private void cancelEdit() {
        markedForDeletion.clear();
        partView.getAction().accept(PartFinderMessage.REFRESH_TREEVIEW);
        partFinderModel.getTreeView().refresh();
        partFinderModel.getTreeView().editableProperty().set(false);
        logger.debug("Cancelled edits and refreshed TreeView");
        ButtonFx.buttonVisible(saveButton, false);
        System.out.println("visibility 4");
        setButtonVisibility(false, false, false, false); // why did you remove this?
    }

    private void setBySelection() {
        if (partFinderModel.getTreeView().getSelectionModel().selectedItemProperty().get() == null) {
            logger.warn("None of the treeView items are selected");
            //                range, product, delete, cancel
            System.out.println("visibility 2");
            setButtonVisibility(false, false, false, true);
            return;
        }
        int selection = getTreeItemDepth(partFinderModel.getTreeView().getSelectionModel().getSelectedItem());
        System.out.println("visibility 3");
        switch (selection) {
            //                          add range, add product, delete item, cancel edit
            case 0 -> setButtonVisibility(true, false, false, true);
            case 1 -> setButtonVisibility(false, true, true, true);
            case 2 -> setButtonVisibility(false, false, true, true);
        }
    }

    private void addNewRange() {
        ProductFamilyDTO newFamily = new ProductFamilyDTO("New Range", new ArrayList<>());
        partFinderModel.getProductFamilies().add(newFamily);
        logger.debug("Added new ProductFamilyDTO: {} (instance: {})", newFamily.getRange(), System.identityHashCode(newFamily));
        TreeItem<Object> rootItem = partFinderModel.getTreeView().getRoot();
        TreeItem<Object> newRangeItem = new TreeItem<>(newFamily);
        newRangeItem.setExpanded(true);
        rootItem.getChildren().add(newRangeItem);
        partFinderModel.getTreeView().getSelectionModel().select(newRangeItem);
    }

    private void addNewProduct() {
        TreeItem<Object> selected = partFinderModel.getTreeView().getSelectionModel().getSelectedItem();
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
        partFinderModel.getTreeView().getSelectionModel().select(newProductItem);
    }

    private void markForDeletion() {
        TreeItem<Object> selected = partFinderModel.getTreeView().getSelectionModel().getSelectedItem();
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
        partFinderModel.getTreeView().refresh();
    }

    private void saveChanges() {
        TreeView<Object> treeView = partFinderModel.getTreeView();
        logger.debug("Before saveChanges: ProductFamilies: {}",
                partFinderModel.getProductFamilies().stream()
                        .map(pf -> pf.getRange() + " -> " + pf.getProductFamilies())
                        .collect(Collectors.toList()));

        List<TreeItem<Object>> toRemove = new ArrayList<>(markedForDeletion);
        for (TreeItem<Object> item : toRemove) {
            int depth = getTreeItemDepth(item);
            TreeItem<Object> parent = item.getParent();
            if (depth == 1) {
                ProductFamilyDTO pf = (ProductFamilyDTO) item.getValue();
                logger.debug("Attempting to remove range: {} (instance: {})",
                        pf.getRange(), System.identityHashCode(pf));
                if (partFinderModel.getProductFamilies().remove(pf)) {
                    parent.getChildren().remove(item);
                    logger.debug("Removed range '{}' (instance: {})", pf.getRange(), System.identityHashCode(pf));
                } else {
                    logger.warn("Failed to remove range '{}' from ProductFamilies (instance: {})",
                            pf.getRange(), System.identityHashCode(pf));
                }
            } else if (depth == 2) {
                String product = (String) item.getValue();
                ProductFamilyDTO pf = (ProductFamilyDTO) parent.getValue();
                logger.debug("Attempting to remove product: {} from ProductFamilyDTO: {} (instance: {})",
                        product, pf.getRange(), System.identityHashCode(pf));
                if (pf.getProductFamilies().remove(product)) {
                    parent.getChildren().remove(item);
                    logger.debug("Removed product '{}' from ProductFamilyDTO: {} (instance: {})",
                            product, pf.getRange(), System.identityHashCode(pf));
                } else {
                    logger.warn("Failed to remove product '{}' from ProductFamilyDTO: {} (instance: {})",
                            product, pf.getRange(), System.identityHashCode(pf));
                }
            }
        }
        markedForDeletion.clear();
        treeView.refresh();
        logger.debug("After deletions: ProductFamilies: {}",
                partFinderModel.getProductFamilies().stream()
                        .map(pf -> pf.getRange() + " -> " + pf.getProductFamilies())
                        .collect(Collectors.toList()));
        partView.getAction().accept(PartFinderMessage.SAVE_PIM_TO_JSON);
        System.out.println("visibility 1");
        setButtonVisibility(false, false, false, false);
        logger.debug("After saveChanges: ProductFamilies: {}",
                partFinderModel.getProductFamilies().stream()
                        .map(pf -> pf.getRange() + " -> " + pf.getProductFamilies())
                        .collect(Collectors.toList()));
        logger.debug("Saved changes and cleared deletion marks");
        cancelEdit();  // TODO added this to see if it would work
    }


    public boolean isMarkedForDeletion(TreeItem<Object> item) {
        return item != null && markedForDeletion.contains(item);
    }

    private TreeView<Object> createProductFamilyTreeView() {
        List<ProductFamilyDTO> productFamilies = partFinderModel.getProductFamilies();
        logger.debug("Creating TreeView with ProductFamilies: {}",
                productFamilies.stream()
                        .map(pf -> pf.getRange() + " -> " + pf.getProductFamilies() + " (instance: " + System.identityHashCode(pf) + ")")
                        .collect(Collectors.toList()));
        TreeItem<Object> rootItem = createTreeItemRoot(productFamilies);
        TreeView<Object> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(true);
        return treeView;
    }

    public static TreeItem<Object> createTreeItemRoot(List<ProductFamilyDTO> productFamilies) {
        TreeItem<Object> rootItem = new TreeItem<>("Ranges");
        rootItem.setExpanded(true);
        logger.debug("Building tree with ProductFamilies: {}",
                productFamilies.stream()
                        .map(pf -> pf.getRange() + " -> " + pf.getProductFamilies() + " (instance: " + System.identityHashCode(pf) + ")")
                        .collect(Collectors.toList()));
        for (ProductFamilyDTO pf : productFamilies) {
            logger.debug("Creating range node for ProductFamilyDTO: {} (instance: {})",
                    pf.getRange(), System.identityHashCode(pf));
            TreeItem<Object> rangeItem = new TreeItem<>(pf);
            rangeItem.setExpanded(true);
            for (Object productFamily : pf.getProductFamilies() != null ? pf.getProductFamilies() : List.of()) {
                logger.debug("Creating product node: {} (instance: {})",
                        productFamily, System.identityHashCode(productFamily));
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

    private void setButtonVisibility(boolean range, boolean product, boolean family, boolean cancel) {
        logger.debug("visibility range ={}, product ={}, family={}, cancel={}", range, product, family, cancel);
        ButtonFx.buttonVisible(addRange, range);
        ButtonFx.buttonVisible(addProduct, product);
        ButtonFx.buttonVisible(deleteButton, family);
        ButtonFx.buttonVisible(cancelButton, cancel);
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


