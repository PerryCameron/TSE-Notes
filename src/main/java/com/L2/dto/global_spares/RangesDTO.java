package com.L2.dto.global_spares;

import java.util.Arrays;

public class RangesDTO {
    int id;
    String range;
    String rangeAdditional;
    String rangeType;
    String lastUpdate;
    String lastUpdatedBy;

    public RangesDTO(int id, String range, String rangeAdditional, String rangeType, String lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.range = range;
        this.rangeAdditional = rangeAdditional;
        this.rangeType = rangeType;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public RangesDTO() {
    }

    // for updating object in list with bound object
    public void copyFx(RangesFx rangesFx) {
        this.id = rangesFx.getId();
        this.range = rangesFx.getRange();
        this.rangeAdditional = updateAdditionalRange(rangesFx); // convert returns to commas
        this.rangeType = rangesFx.getRangeType();
        this.lastUpdate = rangesFx.getLastUpdate();
        this.lastUpdatedBy = rangesFx.getLastUpdatedBy();
    }

    // helper method to make DTO correct
    private String updateAdditionalRange(RangesFx rangesFx) {
        String trimmed = rangesFx.getRangeAdditional().trim();
        String converted = trimmed.replaceAll("\n+", ","); // Normalize multiple newlines to one
        return converted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRangeAdditional() {
        return rangeAdditional;
    }

    public void setRangeAdditional(String rangeAdditional) {
        this.rangeAdditional = rangeAdditional;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void printRanges() {
        String[] subRanges = rangeAdditional.split(",");
        Arrays.stream(subRanges).forEach(subRange -> {
            System.out.print(subRange + " ");
        });
        System.out.println();
    }

    @Override
    public String toString() {
        return "RangesDTO{" +
                "id=" + id +
                ", range='" + range + '\'' +
                ", rangeAdditional='" + rangeAdditional + '\'' +
                ", rangeType='" + rangeType + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
