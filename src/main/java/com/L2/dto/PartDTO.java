package com.L2.dto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;


import java.io.Serializable;


public class PartDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private final StringProperty partNumber;
    private final StringProperty partDescription;
    private final IntegerProperty partQuantity;
    private final StringProperty serialReplaced;
    private final BooleanProperty partEditable;


    public PartDTO(String partNumber, String partDescription, int partQuantity, String serialReplaced, boolean partEditable) {
        this.partNumber = new SimpleStringProperty(partNumber);
        this.partDescription = new SimpleStringProperty(partDescription);
        this.partQuantity = new SimpleIntegerProperty(partQuantity);
        this.serialReplaced = new SimpleStringProperty(serialReplaced);
        this.partEditable = new SimpleBooleanProperty(partEditable);
    }

    public PartDTO() {
        this.partNumber = new SimpleStringProperty("");
        this.partDescription = new SimpleStringProperty("");
        this.partQuantity = new SimpleIntegerProperty(0);
        this.serialReplaced = new SimpleStringProperty("");
        this.partEditable = new SimpleBooleanProperty(false);
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


    // Getters and setters for partQuantity
    public IntegerProperty partQuantityProperty() {
        return partQuantity;
    }


    public int getPartQuantity() {
        return partQuantity.get();
    }


    public void setPartQuantity(int partQuantity) {
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





