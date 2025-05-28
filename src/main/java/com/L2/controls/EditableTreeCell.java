package com.L2.controls;

import com.L2.dto.global_spares.ProductFamilyDTO;
import com.L2.mvci.parts.PartInteractor;
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
        if (depth == 2) {
            super.commitEdit(newText); // Update TreeItem value for product families (String)
        } else {
            super.commitEdit(getTreeItem().getValue()); // Preserve ProductFamilyDTO for ranges
        }
        updateModel(newText);
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

    private void updateModel(String newText) {
        TreeItem<Object> treeItem = getTreeItem();
        if (treeItem == null) {
            logger.warn("No tree item selected for update.");
            return;
        }

        int depth = getTreeItemDepth(treeItem);
        Object value = treeItem.getValue();
        logger.debug("Updating TreeItem at depth {} with value: {}", depth, value);

        if (depth == 1) {
            if (!(value instanceof ProductFamilyDTO)) {
                logger.error("Expected ProductFamilyDTO at depth 1, but got: {}", value != null ? value.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid item type for range node.").showAndWait();
                return;
            }
            ProductFamilyDTO pf = (ProductFamilyDTO) value;
            pf.setRange(newText);
            // No need to set treeItem.setValue(pf), as commitEdit preserves it
        } else if (depth == 2) {
            if (!(value instanceof String)) {
                logger.error("Expected String at depth 2, but got: {}", value != null ? value.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid item type for product family node.").showAndWait();
                return;
            }
            String oldValue = (String) value;
            Object parentValue = treeItem.getParent().getValue();
            if (!(parentValue instanceof ProductFamilyDTO)) {
                logger.error("Expected ProductFamilyDTO parent, but got: {}", parentValue != null ? parentValue.getClass().getName() : "null");
                new Alert(Alert.AlertType.ERROR, "Invalid parent type for product family node.").showAndWait();
                return;
            }
            ProductFamilyDTO pf = (ProductFamilyDTO) parentValue;
            int index = pf.getProductFamilies().indexOf(oldValue);
            if (index >= 0) {
                pf.getProductFamilies().set(index, newText);
                // TreeItem value already set to newText in commitEdit
            } else {
                logger.warn("Product family '{}' not found in parent.", oldValue);
            }
        } else {
            logger.warn("Unexpected depth {} for editing.", depth);
        }
    }
}

//// Placeholder methods (unchanged)
//private void addNewFamily() {
//    // Implement as needed
//}
//
//private void addNewProduct() {
//    // Implement as needed
//}
//
//private void saveToJson() {
//    // Implement as needed
//}
//
//private void setTreeTop() {
//    // Implement as needed
//}
//
//private void setButtonFamily() {
//    // Implement as needed
//}
//
//private void setButtonProduct() {
//    // Implement as needed
//}
//
//private void setNonSelected() {
//    // Implement as needed
//}

