package com.L2.dto;

public class PartDTO {
    String partNumber;
    String partDescription;

    int partQuantity;
    String serialReplaced;
    boolean partEditable;

    public PartDTO(String partNumber, String partDescription, int partQuantity, String serialReplaced, boolean partEditable) {
        this.partNumber = partNumber;
        this.partDescription = partDescription;
        this.partQuantity = partQuantity;
        this.serialReplaced = serialReplaced;
        this.partEditable = partEditable;
    }

    public PartDTO() {
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
    }

    public int getPartQuantity() {
        return partQuantity;
    }

    public void setPartQuantity(int partQuantity) {
        this.partQuantity = partQuantity;
    }

    public String getSerialReplaced() {
        return serialReplaced;
    }

    public void setSerialReplaced(String serialReplaced) {
        this.serialReplaced = serialReplaced;
    }

    public boolean isPartEditable() {
        return partEditable;
    }

    public void setPartEditable(boolean partEditable) {
        this.partEditable = partEditable;
    }

    // getters and setters below...
}
