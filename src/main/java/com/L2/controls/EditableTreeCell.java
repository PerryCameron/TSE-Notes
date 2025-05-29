// com.L2.controls/EditableTreeCell.java
package com.L2.controls;

import com.L2.dto.global_spares.ProductFamilyDTO;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.L2.mvci.parts.components.ProductFamily.getDisplayText;
import static com.L2.mvci.parts.components.ProductFamily.getTreeItemDepth;

public class EditableTreeCell extends TreeCell<Object> {
    private static final Logger logger = LoggerFactory.getLogger(EditableTreeCell.class);
    private TextField textField;

    public EditableTreeCell() {
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
        } else {
            setText(getDisplayText(getTreeItem()));
            setGraphic(isEditing() ? textField : null);
        }
    }

    private void updateModel(String newText, Object oldValue) {
        TreeItem<Object> treeItem = getTreeItem();
        if (treeItem == null) {
            logger.warn("No tree item selected for update.");
            return;
        }

        int depth = getTreeItemDepth(treeItem);
        logger.debug("Updating TreeItem at depth {} with old value: {} (instance: {}), new text: {}",
                depth, oldValue, System.identityHashCode(oldValue), newText);

        if (depth == 1) {
            if (!(oldValue instanceof ProductFamilyDTO)) {
                logger.error("Expected ProductFamilyDTO at depth 1, but got: {}", oldValue != null ? oldValue.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid item type for range node.").showAndWait();
                return;
            }
            ProductFamilyDTO pf = (ProductFamilyDTO) oldValue;
            logger.debug("Updating ProductFamilyDTO range from '{}' to '{}' (instance: {})",
                    pf.getRange(), newText, System.identityHashCode(pf));
            pf.setRange(newText);
        } else if (depth == 2) {
            if (!(oldValue instanceof String)) {
                logger.error("Expected String at depth 2, but got: {}", oldValue != null ? oldValue.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid item type for product family node.").showAndWait();
                return;
            }
            String oldString = (String) oldValue;
            Object parentValue = treeItem.getParent().getValue();
            if (!(parentValue instanceof ProductFamilyDTO)) {
                logger.error("Expected ProductFamilyDTO parent, but got: {}", parentValue != null ? parentValue.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid parent type for product family node.").showAndWait();
                return;
            }
            ProductFamilyDTO pf = (ProductFamilyDTO) parentValue;
            logger.debug("Updating ProductFamilyDTO product family from '{}' to '{}' (instance: {})",
                    oldString, newText, System.identityHashCode(pf));
            int index = pf.getProductFamilies().indexOf(oldString);
            if (index >= 0) {
                pf.getProductFamilies().set(index, newText);
            } else {
                logger.warn("Product family '{}' not found in parent ProductFamilyDTO: {}", oldString, pf.getRange());
            }
        } else {
            logger.warn("Unexpected depth {} for editing.", depth);
        }
    }
}