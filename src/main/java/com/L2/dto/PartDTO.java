package com.L2.dto;

import javafx.beans.property.*;


import java.io.Serializable;


public class PartDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private IntegerProperty id;
    private StringProperty partNumber;
    private StringProperty partDescription;
    private StringProperty partQuantity;
    private StringProperty serialReplaced;
    private BooleanProperty partEditable;

    public PartDTO(Integer id, String partNumber, String partDescription, String partQuantity, String serialReplaced, boolean partEditable) {
        this.id = new SimpleIntegerProperty(id);
        this.partNumber = new SimpleStringProperty(partNumber);
        this.partDescription = new SimpleStringProperty(partDescription);
        this.partQuantity = new SimpleStringProperty(partQuantity);
        this.serialReplaced = new SimpleStringProperty(serialReplaced);
        this.partEditable = new SimpleBooleanProperty(partEditable);
    }

    public PartDTO(IntegerProperty id, String partNumber, String partDescription, String partQuantity) {
        this.id = new SimpleIntegerProperty(0);
        this.partNumber = new SimpleStringProperty(partNumber);
        this.partDescription = new SimpleStringProperty(partDescription);
        this.partQuantity = new SimpleStringProperty(partQuantity);
        this.serialReplaced = new SimpleStringProperty("");
        this.partEditable = new SimpleBooleanProperty(false);
    }

    public PartDTO() {
        this.id = new SimpleIntegerProperty(0);
        this.partNumber = new SimpleStringProperty("");
        this.partDescription = new SimpleStringProperty("");
        this.partQuantity = new SimpleStringProperty("");
        this.serialReplaced = new SimpleStringProperty("");
        this.partEditable = new SimpleBooleanProperty(false);
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





