package com.L2.dto.global_spares;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RangesFx {
    IntegerProperty id;
    StringProperty range;
    StringProperty productFamily;
    StringProperty rangeType;
    StringProperty lastUpdate;
    StringProperty lastUpdatedBy;

    public RangesFx(Integer id, String range, String productFamily, String rangeType, String lastUpdate, String lastUpdatedBy) {
        this.id = new SimpleIntegerProperty(id);
        this.range = new SimpleStringProperty(range);
        this.productFamily = new SimpleStringProperty(productFamily);
        this.rangeType = new SimpleStringProperty(rangeType);
        this.lastUpdate = new SimpleStringProperty(lastUpdate);
        this.lastUpdatedBy = new SimpleStringProperty(lastUpdatedBy);
    }

    public RangesFx() {
        this.id = new SimpleIntegerProperty(0);
        this.range = new SimpleStringProperty("");
        this.productFamily = new SimpleStringProperty("");
        this.rangeType = new SimpleStringProperty("");
        this.lastUpdate = new SimpleStringProperty("");
        this.lastUpdatedBy = new SimpleStringProperty("");
    }

    // copy from list, (selected Range) to bound note
    public void copyFromSelectedRange(RangesFx rangesFx) {
        this.id.set(rangesFx.id.get());
        this.range.set(rangesFx.range.get());
        this.productFamily.set(filterForDisplay(rangesFx.productFamily.get()));
        this.rangeType.set(rangesFx.rangeType.get());
        this.lastUpdate.set(rangesFx.lastUpdate.get());
        this.lastUpdatedBy.set(rangesFx.lastUpdatedBy.get());
        printRange(rangesFx);
    }

    public boolean copyFromBoundRange(RangesFx rangesFx) {
        if(rangesFx.getId() == this.id.get()) {
            this.range.set(rangesFx.range.get());
            this.productFamily.set(filterForDatabase(rangesFx.productFamily.get()));
            this.rangeType.set(rangesFx.rangeType.get());
            this.lastUpdate.set(rangesFx.lastUpdate.get());
            this.lastUpdatedBy.set(rangesFx.lastUpdatedBy.get());
            return true;
        } else return false;
    }

    private String filterForDisplay(String input) {
        String trimmedInput = input.trim();
        String converted = trimmedInput.replaceAll(",+", "\n");
        return converted;
    }

    private String filterForDatabase(String input) {
        String trimmedInput = input.trim();
        String converted = trimmedInput.replaceAll("\n+", ",");
        return converted;
    }

    private void printRange(RangesFx rangesFx) {
        System.out.println("-------------------------------------------------");
        System.out.println("boundRangeFx: " + this);
        System.out.println("Updated with RangeFx: " + rangesFx);
        System.out.println("this.id=" + rangesFx.getId());
        System.out.println("this.range=" + rangesFx.getRange());
        System.out.println("this.rangeAdditional=" + rangesFx.getProductFamily());
        System.out.println("this.rangeType=" + rangesFx.getRangeType());
        System.out.println("this.lastUpdate=" + rangesFx.getLastUpdate());
        System.out.println("this.lastUpdatedBy=" + rangesFx.getLastUpdatedBy());
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

    public String getRange() {
        return range.get();
    }

    public StringProperty rangeProperty() {
        return range;
    }

    public void setRange(String range) {
        this.range.set(range);
    }

    public String getProductFamily() {
        return productFamily.get();
    }

    public StringProperty productFamilyProperty() {
        return productFamily;
    }

    public void setProductFamily(String productFamily) {
        this.productFamily.set(productFamily);
    }

    public String getRangeType() {
        return rangeType.get();
    }

    public StringProperty rangeTypeProperty() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType.set(rangeType);
    }

    public String getLastUpdate() {
        return lastUpdate.get();
    }

    public StringProperty lastUpdateProperty() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate.set(lastUpdate);
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy.get();
    }

    public StringProperty lastUpdatedByProperty() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy.set(lastUpdatedBy);
    }

    public void printRange() {
        System.out.println(
        "RangesFx{" +
                "id=" + id +
                ", range=" + range +
                ", productFamily=" + productFamily +
                ", rangeType=" + rangeType +
                ", lastUpdate=" + lastUpdate +
                ", lastUpdatedBy=" + lastUpdatedBy +
                '}');
    }
}
