//package com.L2.controls;
//
//import javafx.scene.control.TextField;
//import javafx.scene.control.TreeCell;
//
//// Custom TreeCell for editable TreeView
//class EditableTreeCell extends TreeCell<String> {
//    private TextField textField;
//
//    public EditableTreeCell() {
//        // Initialize TextField for editing
//        textField = new TextField();
//        textField.setOnAction(event -> commitEdit(getItem())); // Commit on Enter
//    }
//
//    @Override
//    public void startEdit() {
//        super.startEdit();
//        if (getItem() == null) {
//            return;
//        }
//        // Show TextField for editing
//        textField.setText(getItem().getComments());
//        setText(null);
//        setGraphic(textField);
//        textField.requestFocus();
//    }
//
//    @Override
//    public void cancelEdit() {
//        super.cancelEdit();
//        // Restore text display
//        setText(getItem() != null ? getItem().toString() : null);
//        setGraphic(null);
//    }
//
//    @Override
//    public void commitEdit(String spare) {
//        super.commitEdit(spare);
//        // Update Spare's comments and restore display
//        if (spare != null) {
//            spare.setComments(textField.getText());
//            setText(spare.toString());
//        } else {
//            setText(null);
//        }
//        setGraphic(null);
//    }
//
//    @Override
//    protected void updateItem(Spare spare, boolean empty) {
//        super.updateItem(spare, empty);
//        if (empty || spare == null) {
//            setText(null);
//            setGraphic(null);
//        } else if (!isEditing()) {
//            setText(spare.toString());
//            setGraphic(null);
//        }
//    }
//}
//
//}