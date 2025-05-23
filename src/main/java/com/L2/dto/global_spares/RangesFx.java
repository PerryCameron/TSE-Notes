package com.L2.dto.global_spares;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RangesFx {
    IntegerProperty id;
    StringProperty range;
    StringProperty rangeAdditional;
    StringProperty rangeType;
    StringProperty lastUpdate;
    StringProperty lastUpdatedBy;

    public RangesFx(Integer id, String range, String rangeAdditional, String rangeType, String lastUpdate, String lastUpdatedBy) {
        this.id = new SimpleIntegerProperty(id);
        this.range = new SimpleStringProperty(range);
        this.rangeAdditional = new SimpleStringProperty(rangeAdditional);
        this.rangeType = new SimpleStringProperty(rangeType);
        this.lastUpdate = new SimpleStringProperty(lastUpdate);
        this.lastUpdatedBy = new SimpleStringProperty(lastUpdatedBy);
    }

    public void copyFrom(RangesDTO rangesDTO) {
        this.id.set(rangesDTO.getId());
        this.range.set(rangesDTO.getRange());
        this.rangeAdditional.set(rangesDTO.getRangeAdditional());
        this.rangeType.set(rangesDTO.getRangeType());
        this.lastUpdate.set(rangesDTO.getLastUpdate());
        this.lastUpdatedBy.set(rangesDTO.getLastUpdatedBy());
    }

    public RangesFx() {
        this.id = new SimpleIntegerProperty(0);
        this.range = new SimpleStringProperty("");
        this.rangeAdditional = new SimpleStringProperty("");
        this.rangeType = new SimpleStringProperty("");
        this.lastUpdate = new SimpleStringProperty("");
        this.lastUpdatedBy = new SimpleStringProperty("");
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

    public String getRangeAdditional() {
        return rangeAdditional.get();
    }

    public StringProperty rangeAdditionalProperty() {
        return rangeAdditional;
    }

    public void setRangeAdditional(String rangeAdditional) {
        this.rangeAdditional.set(rangeAdditional);
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
}
