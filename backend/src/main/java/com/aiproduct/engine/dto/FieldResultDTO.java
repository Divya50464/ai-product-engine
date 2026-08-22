package com.aiproduct.engine.dto;

public class FieldResultDTO {

    private String fieldName;
    private String value;
    private Double confidence;
    private Integer authorityTier;
    private String source;
    private String rejectedValue;
    private String rejectedSource;
    private String status;

    public FieldResultDTO() {
    }

    public FieldResultDTO(
            String fieldName,
            String value,
            Double confidence,
            Integer authorityTier,
            String source,
            String rejectedValue,
            String rejectedSource,
            String status) {

        this.fieldName = fieldName;
        this.value = value;
        this.confidence = confidence;
        this.authorityTier = authorityTier;
        this.source = source;
        this.rejectedValue = rejectedValue;
        this.rejectedSource = rejectedSource;
        this.status = status;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Integer getAuthorityTier() {
        return authorityTier;
    }

    public void setAuthorityTier(Integer authorityTier) {
        this.authorityTier = authorityTier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getRejectedSource() {
        return rejectedSource;
    }

    public void setRejectedSource(String rejectedSource) {
        this.rejectedSource = rejectedSource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}