package com.L2.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdatedByDTO {
    @JsonProperty("updated_by") // Map to JSON field name if different
    private String updatedBy;

    @JsonProperty("updated_date_time")
    private String updatedDateTime;

    @JsonProperty("change_made")
    @JsonInclude(JsonInclude.Include.NON_NULL) // Ignore if null or not present
    private String changeMade;

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

    public String getChangeMade() {
        return changeMade;
    }

    public void setChangeMade(String changeMade) {
        this.changeMade = changeMade;
    }

    @Override
    public String toString() {
        return "UpdatedByDTO{" +
                "updatedBy='" + updatedBy + '\'' +
                ", updatedDateTime='" + updatedDateTime + '\'' +
                ", changeMade=" + changeMade +
                '}';
    }
}