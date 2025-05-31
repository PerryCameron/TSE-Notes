package com.L2.controls;

import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.mvci.note.mvci.partorderbox.mvci.parts.components.ProductFamily;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.L2.mvci.note.mvci.partorderbox.mvci.parts.components.ProductFamily.getDisplayText;
import static com.L2.mvci.note.mvci.partorderbox.mvci.parts.components.ProductFamily.getTreeItemDepth;

public class EditableTreeCell extends TreeCell<Object> {
    private static final Logger logger = LoggerFactory.getLogger(EditableTreeCell.class);
    private final TextField textField;
    private final ProductFamily productFamily;

    public EditableTreeCell(ProductFamily productFamily) {
        this.productFamily = productFamily;
        textField = new TextField();
        textField.setOnAction(event -> commitEdit(textField.getText()));
    }

    @Override
    public void startEdit() {
        if (getTreeItem() == null || getTreeItemDepth(getTreeItem()) == 0) {
            cancelEdit();
            return;
        }
        super.startEdit();
        textField.setText(getDisplayText(getTreeItem()));
        setText(null);
        setGraphic(textField);
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDisplayText(getTreeItem()));
        setGraphic(null);
    }

    @Override
    public void commitEdit(Object newValue) {
        if (!(newValue instanceof String newText) || newText.trim().isEmpty()) {
            cancelEdit();
            new Alert(Alert.AlertType.WARNING, "Value cannot be empty.").showAndWait();
            return;
        }
        int depth = getTreeItemDepth(getTreeItem());
        Object oldValue = getTreeItem().getValue();
        if (depth == 2) {
            super.commitEdit(newText); // Update TreeItem for product families
        } else {
            super.commitEdit(oldValue); // Preserve ProductFamilyDTO for ranges
        }
        updateModel(newText, oldValue);
        setText(newText);
        setGraphic(null);
    }

    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            setStyle(null);
        } else {
            setText(ProductFamily.getDisplayText(getTreeItem()));
            setGraphic(isEditing() ? textField : null);
            // Apply red style if marked for deletion
            TreeItem<Object> treeItem = getTreeItem();
            if (productFamily.isMarkedForDeletion(treeItem)) {
                setStyle("-fx-text-fill: red;");
            } else {
                setStyle(null);
            }
        }
    }

    private void updateModel(String text, Object value) {
        TreeItem<Object> treeItem = getTreeItem();
        if (treeItem == null) {
            logger.warn("No tree item selected for update.");
            return;
        }

        int depth = ProductFamily.getTreeItemDepth(treeItem);
        logger.debug("Updating TreeItem at depth {} with old value: {} (instance: {}), new text: {}",
                depth, value, System.identityHashCode(value), text);

        if (depth == 1) {
            if (!(value instanceof ProductFamilyDTO)) {
                logger.error("Expected ProductFamilyDTO at depth 1, but got: {}",
                        value != null ? value.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid item type.").showAndWait();
                return;
            }
            ProductFamilyDTO pf = (ProductFamilyDTO) value;
            logger.debug("Updating ProductFamilyDTO range: {} to {} (item: {})",
                    pf.getRange(), text, System.identityHashCode(pf));
            pf.setRange(text);
        } else if (depth == 2) {
            if (!(value instanceof String)) {
                logger.error("Expected String at depth 2, but got: {}",
                        value != null ? value.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid item type.").showAndWait();
                return;
            }
            String oldString = (String) value;
            Object parentValue = treeItem.getParent().getValue();
            if (!(parentValue instanceof ProductFamilyDTO)) {
                logger.error("Expected ProductFamilyDTO parent, but got: {}",
                        parentValue != null ? parentValue.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid parent type.").showAndWait();
                return;
            }
            ProductFamilyDTO parentDTO = (ProductFamilyDTO) parentValue;
            logger.debug("Updating ProductFamilyDTO product family: {} to {} (parent: {})",
                    oldString, text, System.identityHashCode(parentDTO));
            int index = parentDTO.getProductFamilies().indexOf(oldString);
            if (index >= 0) {
                parentDTO.getProductFamilies().set(index, text);
            } else {
                logger.warn("Product family '{}' not found in parent: {}", oldString, parentDTO.getRange());
            }
        } else {
            logger.warn("Unexpected depth {} for editing.", depth);
        }
    }
}