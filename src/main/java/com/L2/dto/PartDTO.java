package com.L2.dto;

import javafx.beans.property.*;


public class PartDTO {

    private final IntegerProperty id =  new SimpleIntegerProperty();
    private final IntegerProperty partOrderId =  new SimpleIntegerProperty();
    private final StringProperty partNumber =  new SimpleStringProperty();
    private final StringProperty partDescription =  new SimpleStringProperty();
    private final StringProperty partQuantity =  new SimpleStringProperty();
    private final StringProperty serialReplaced =  new SimpleStringProperty();
    private final BooleanProperty partEditable =  new SimpleBooleanProperty();

    public PartDTO(Integer id, Integer partOrderId, String partNumber, String partDescription, String partQuantity, String serialReplaced, boolean partEditable) {
        this.id.set(id);
        this.partOrderId.set(partOrderId);
        this.partNumber.set(partNumber);
        this.partDescription.set(partDescription);
        this.partQuantity.set(partQuantity);
        this.serialReplaced.set(serialReplaced);
        this.partEditable.set(partEditable);
    }

    public PartDTO(int partOrderId) {
        this.id.set(0);
        this.partOrderId.set(partOrderId);
        this.partNumber.set("");
        this.partDescription.set("");
        this.partQuantity.set("");
        this.serialReplaced.set("");
        this.partEditable.set(false);
    }

    public int getPartOrderId() {
        return partOrderId.get();
    }

    public IntegerProperty partOrderIdProperty() {
        return partOrderId;
    }

    public void setPartOrderId(int partOrderId) {
        this.partOrderId.set(partOrderId);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Getters and setters for partNumber
    public StringProperty partNumberProperty() {
        return partNumber;
    }


    public String getPartNumber() {
        return partNumber.get();
    }


    public void setPartNumber(String partNumber) {
        this.partNumber.set(partNumber);
    }


    // Getters and setters for partDescription
    public StringProperty partDescriptionProperty() {
        return partDescription;
    }


    public String getPartDescription() {
        return partDescription.get();
    }


    public void setPartDescription(String partDescription) {
        this.partDescription.set(partDescription);
    }


    public String getPartQuantity() {
        return partQuantity.get();
    }

    public StringProperty partQuantityProperty() {
        return partQuantity;
    }

    public void setPartQuantity(String partQuantity) {
        this.partQuantity.set(partQuantity);
    }

    // Getters and setters for serialReplaced
    public StringProperty serialReplacedProperty() {
        return serialReplaced;
    }


    public String getSerialReplaced() {
        return serialReplaced.get();
    }


    public void setSerialReplaced(String serialReplaced) {
        this.serialReplaced.set(serialReplaced);
    }


    // Getters and setters for partEditable
    public BooleanProperty partEditableProperty() {
        return partEditable;
    }


    public boolean isPartEditable() {
        return partEditable.get();
    }


    public void setPartEditable(boolean partEditable) {
        this.partEditable.set(partEditable);
    }
}





