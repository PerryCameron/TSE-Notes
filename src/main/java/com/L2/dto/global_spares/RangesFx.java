package com.L2.dto.global_spares;

import javafx.application.Platform;
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
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> copyFrom(rangesDTO));
            return;
        }
        // Create a defensive copy of rangesDTO to avoid modifying the original
        RangesDTO dtoCopy = new RangesDTO(
                rangesDTO.getId(),
                rangesDTO.getRange(),
                rangesDTO.getRangeAdditional(),
                rangesDTO.getRangeType(),
                rangesDTO.getLastUpdate(),
                rangesDTO.getLastUpdatedBy()
        );
        String rangeTypeValue = dtoCopy.getRangeType();
        this.id.set(dtoCopy.getId());
        this.range.set(dtoCopy.getRange());
        this.rangeAdditional.set(updateAdditionalRange(dtoCopy));
        if (this.rangeType.isBound()) {
            this.rangeType.unbind();
        }
        this.rangeType.set(rangeTypeValue != null ? rangeTypeValue : "");
        this.lastUpdate.set(dtoCopy.getLastUpdate());
        this.lastUpdatedBy.set(dtoCopy.getLastUpdatedBy());
    }

    // this method converts the commas back to returns.
    private String updateAdditionalRange(RangesDTO rangesDTO) {
        String trimmed = rangesDTO.getRangeAdditional().trim();
        String converted = trimmed.replaceAll(",+", "\n");
        return converted;
    }

    public RangesFx() {
        this.id = new SimpleIntegerProperty(0);
        this.range = new SimpleStringProperty("");
        this.rangeAdditional = new SimpleStringProperty("");
        this.rangeType = new SimpleStringProperty("");
        this.lastUpdate = new SimpleStringProperty("");
        this.lastUpdatedBy = new SimpleStringProperty("");
    }

    public void copyFrom(RangesFx rangesFx) {
        this.id = new SimpleIntegerProperty(rangesFx.id.get());
        this.range = new SimpleStringProperty(rangesFx.range.get());
        this.rangeAdditional = new SimpleStringProperty(rangesFx.rangeAdditional.get());
        this.rangeType = new SimpleStringProperty(rangesFx.rangeType.get());
        this.lastUpdate = new SimpleStringProperty(rangesFx.lastUpdate.get());
        this.lastUpdatedBy = new SimpleStringProperty(rangesFx.lastUpdatedBy.get());
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
