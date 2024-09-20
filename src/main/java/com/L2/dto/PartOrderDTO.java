package com.L2.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PartOrderDTO {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty noteId = new SimpleIntegerProperty();
    private final StringProperty orderNumber = new SimpleStringProperty();
    private final ListProperty<PartDTO> parts = new SimpleListProperty<>(FXCollections.observableArrayList());
//    private ObjectProperty<PartDTO> selectedPart = new SimpleObjectProperty<>();

    public PartOrderDTO(Integer id, Integer noteId, String orderNumber) {
        this.id.set(id);
        this.noteId.set(noteId);
        this.orderNumber.set(orderNumber);
    }

    public String getOrderNumber() {
        return orderNumber.get();
    }

    public StringProperty orderNumberProperty() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber.set(orderNumber);
    }

    public ObservableList<PartDTO> getParts() {
        return parts.get();
    }

    public ListProperty<PartDTO> partsProperty() {
        return parts;
    }

    public void setParts(ObservableList<PartDTO> parts) {
        this.parts.set(parts);
    }

//    public PartDTO getSelectedPart() {
//        return selectedPart.get();
//    }
//
//    public ObjectProperty<PartDTO> selectedPartProperty() {
//        return selectedPart;
//    }
//
//    public void setSelectedPart(PartDTO selectedPart) {
//        this.selectedPart.set(selectedPart);
//    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getNoteId() {
        return noteId.get();
    }

    public IntegerProperty noteIdProperty() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId.set(noteId);
    }
}
