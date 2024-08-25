package com.L2.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PartOrderDTO {

    private StringProperty orderNumber = new SimpleStringProperty();
    private ListProperty<PartDTO> parts = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<PartDTO> selectedPart = new SimpleObjectProperty<>();

    public PartOrderDTO(String orderNumber) {
        this.orderNumber = new SimpleStringProperty(orderNumber);
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
