package com.L2.dto;

public class ResultDTO {
    String fieldName;
    boolean success;

    public ResultDTO(String fieldName, boolean success) {
        this.fieldName = fieldName;
        this.success = success;
    }

    public ResultDTO() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
