package com.L2.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdatedByDTO {
    @JsonProperty("updated_by") // Map to JSON field name if different
    private String updatedBy;

    @JsonProperty("updated_date_time")
    private String updatedDateTime;

    public UpdatedByDTO() {
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    @Override
    public String toString() {
        return "UpdatedByDTO{" +
                "updatedBy='" + updatedBy + '\'' +
                ", updatedDateTime='" + updatedDateTime + '\'' +
                '}';
    }
}