package com.L2.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PartOrderDTO {

    private IntegerProperty orderNumber = new SimpleIntegerProperty();
    private ListProperty<PartDTO> parts = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<PartDTO> selectedPart = new SimpleObjectProperty<>();

    public PartOrderDTO(Integer orderNumber) {
        this.orderNumber = new SimpleIntegerProperty(orderNumber);
    }

    public int getOrderNumber() {
        return orderNumber.get();
    }

    public IntegerProperty orderNumberProperty() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
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

    public PartDTO getSelectedPart() {
        return selectedPart.get();
    }

    public ObjectProperty<PartDTO> selectedPartProperty() {
        return selectedPart;
    }

    public void setSelectedPart(PartDTO selectedPart) {
        this.selectedPart.set(selectedPart);
    }
}
