package com.L2.controls;

import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderModel;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components.ProductFamily;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components.ProductFamily.getDisplayText;
import static com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components.ProductFamily.getTreeItemDepth;

public class EditableTreeCell extends TreeCell<Object> {
    private static final Logger logger = LoggerFactory.getLogger(EditableTreeCell.class);
    private final TextField textField;
    private final ProductFamily productFamily;
    private final PartFinderModel partFinderModel; // Add PartModel field

    public EditableTreeCell(ProductFamily productFamily, PartFinderModel partFinderModel) {
        this.productFamily = productFamily;
        this.partFinderModel = partFinderModel;
        textField = new TextField();
        textField.setOnAction(event -> {
            logger.debug("Enter pressed for TreeItem at depth {}, text: {}",
                    getTreeItem() != null ? getTreeItemDepth(getTreeItem()) : -1, textField.getText());
            commitEdit(textField.getText());
        });
    }

    @Override
    public void startEdit() {
        if (getTreeItem() == null || getTreeItemDepth(getTreeItem()) == 0) {
            logger.warn("Cannot edit: TreeItem is null or root (depth 0)");
            cancelEdit();
            return;
        }
        super.startEdit();
        textField.setText(getDisplayText(getTreeItem()));
        setText(null);
        setGraphic(textField);
        textField.requestFocus();
        logger.debug("Started editing TreeItem at depth {}, value: {}",
                getTreeItemDepth(getTreeItem()), getTreeItem().getValue());
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDisplayText(getTreeItem()));
        setGraphic(null);
        logger.debug("Cancelled edit for TreeItem at depth {}, value: {}",
                getTreeItem() != null ? getTreeItemDepth(getTreeItem()) : -1,
                getTreeItem() != null ? getTreeItem().getValue() : null);
    }


    @Override
    public void commitEdit(Object newValue) {
        System.out.println("commitEdit called and newValue: " + newValue + " (type: " + (newValue != null ? newValue.getClass().getName() : "null") + ")");
        if (!(newValue instanceof String newText) || newText.trim().isEmpty()) {
            logger.warn("Commit failed: New value is empty or not a String: {}", newValue);
            cancelEdit();
            new Alert(Alert.AlertType.WARNING, "Value cannot be empty.").showAndWait();
            return;
        }

        TreeItem<Object> treeItem = getTreeItem();
        if (treeItem == null) {
            logger.error("Commit failed: TreeItem is null");
            cancelEdit();
            return;
        }

        int depth = getTreeItemDepth(treeItem);
        Object oldValue = treeItem.getValue();
        logger.debug("Committing edit at depth {}, oldValue: {} (instance: {}), newText: {}",
                depth, oldValue, System.identityHashCode(oldValue), newText);

        // Update the model and get the updated ProductFamilyDTO (if applicable)
        Object updatedValue = updateModel(newText, oldValue);
        if (updatedValue == null) {
            logger.error("Model update failed for TreeItem at depth {}, reverting to old value", depth);
            cancelEdit();
            new Alert(Alert.AlertType.ERROR, "Failed to update model. Changes not saved.").showAndWait();
            return;
        }

        // Update TreeItem value
        System.out.println("NewText: " + newText);
        super.commitEdit(updatedValue); // Use updatedValue (either ProductFamilyDTO or String)

        System.out.println("oldValue: " + oldValue);
        setText(newText);
        setGraphic(null);
        logger.debug("Committed edit successfully for TreeItem at depth {}, newText: {}", depth, newText);
    }

    //    @Override
//    protected void updateItem(Object item, boolean empty) {
//        super.updateItem(item, empty);
//        if (empty || item == null) {
//            setText(null);
//            setGraphic(null);
//            setStyle(null);
//        } else {
//            setText(ProductFamily.getDisplayText(getTreeItem()));
//            setGraphic(isEditing() ? textField : null);
//            TreeItem<Object> treeItem = getTreeItem();
//            if (productFamily.isMarkedForDeletion(treeItem)) {
//                setStyle("-fx-text-fill: red;");
//            } else {
//                setStyle(null);
//            }
//        }
//    }
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            getStyleClass().remove("deleted-item"); // Remove the class if present
        } else {
            setText(ProductFamily.getDisplayText(getTreeItem()));
            setGraphic(isEditing() ? textField : null);
            TreeItem<Object> treeItem = getTreeItem();
            if (productFamily.isMarkedForDeletion(treeItem)) {
                if (!getStyleClass().contains("deleted-item")) {
                    getStyleClass().add("deleted-item"); // Add the class if not already present
                }
            } else {
                getStyleClass().remove("deleted-item"); // Remove the class if present
            }
        }
    }

    private Object updateModel(String newText, Object oldValue) {
        TreeItem<Object> treeItem = getTreeItem();
        if (treeItem == null) {
            logger.error("UpdateModel failed: TreeItem is null");
            return null;
        }

        int depth = getTreeItemDepth(treeItem);
        logger.debug("Updating model at depth {}, oldValue: {} (instance: {}), newText: {}",
                depth, oldValue, System.identityHashCode(oldValue), newText);

        List<ProductFamilyDTO> modelFamilies = partFinderModel.getProductFamilies();
        logger.debug("partModel ProductFamilies before update: {}",
                modelFamilies.stream()
                        .map(pf -> pf.getRange() + " -> " + pf.getProductFamilies() + " (instance: " + System.identityHashCode(pf) + ")")
                        .collect(Collectors.toList()));

        if (depth == 1) {
            if (!(oldValue instanceof ProductFamilyDTO pf)) {
                logger.error("Expected ProductFamilyDTO at depth 1, but got: {}",
                        oldValue != null ? oldValue.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid item type.").showAndWait();
                System.out.println("Not an instance of ProductFamilyDTO: " + oldValue + " (type: " + (oldValue != null ? oldValue.getClass().getName() : "null") + ")");
                return null;
            }
            logger.debug("Before update: ProductFamilyDTO range: {} (instance: {})",
                    pf.getRange(), System.identityHashCode(pf));
            // Find matching DTO in model by range
            ProductFamilyDTO modelPf = modelFamilies.stream()
                    .filter(dto -> dto.getRange().equals(pf.getRange()))
                    .findFirst()
                    .orElse(null);
            if (modelPf == null) {
                logger.warn("ProductFamilyDTO with range '{}' not found in partModel, adding new", pf.getRange());
                modelFamilies.add(pf);
                modelPf = pf;
            }
            modelPf.setRange(newText);
            logger.debug("After update: ProductFamilyDTO range: {} (instance: {})",
                    modelPf.getRange(), System.identityHashCode(modelPf));
            if (!newText.equals(modelPf.getRange())) {
                logger.error("Failed to update ProductFamilyDTO range: expected {}, got {}",
                        newText, modelPf.getRange());
                return null;
            }
            return modelPf; // Return the updated ProductFamilyDTO
        } else if (depth == 2) {
            if (!(oldValue instanceof String oldString)) {
                logger.error("Expected String at depth 2, but got: {}",
                        oldValue != null ? oldValue.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid item type.").showAndWait();
                return null;
            }
            Object parentValue = treeItem.getParent().getValue();
            if (!(parentValue instanceof ProductFamilyDTO parentDTO)) {
                logger.error("Expected ProductFamilyDTO parent, but got: {}",
                        parentValue != null ? parentValue.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid parent type.").showAndWait();
                return null;
            }
            logger.debug("Before update: ProductFamilyDTO productFamilies: {} (instance: {})",
                    parentDTO.getProductFamilies(), System.identityHashCode(parentDTO));

            // Find matching DTO in model by range
            ProductFamilyDTO modelPf = modelFamilies.stream()
                    .filter(dto -> dto.getRange().equals(parentDTO.getRange()))
                    .findFirst()
                    .orElse(null);
            if (modelPf == null) {
                logger.warn("Parent ProductFamilyDTO with range '{}' not found in partModel, adding new", parentDTO.getRange());
                modelFamilies.add(parentDTO);
                modelPf = parentDTO;
            }
            logger.debug("Updating modelPf: {} (instance: {})",
                    modelPf.getRange(), System.identityHashCode(modelPf));
            int index = modelPf.getProductFamilies().indexOf(oldString);
            if (index >= 0) {
                modelPf.getProductFamilies().set(index, newText);
                logger.debug("After update: ProductFamilyDTO productFamilies: {} (instance: {})",
                        modelPf.getProductFamilies(), System.identityHashCode(modelPf));
                if (!modelPf.getProductFamilies().contains(newText)) {
                    logger.error("Failed to update product family: expected {} not found in {}",
                            newText, modelPf.getProductFamilies());
                    return null;
                }
            } else {
                logger.warn("Product family '{}' not found in parent: {}, adding new entry",
                        oldString, modelPf.getRange());
                modelPf.getProductFamilies().add(newText);
                if (!modelPf.getProductFamilies().contains(newText)) {
                    logger.error("Failed to add product family: {}", newText);
                    return null;
                }
            }
            return newText; // Return the new String for depth 2
        } else {
            logger.warn("Unexpected depth {} for editing", depth);
            return null;
        }
    }
}