package com.aiproduct.engine.dto;

import java.util.Map;



public class ProductSchemaDTO {

    // Existing core fields
    private String name;
    private String category;
    private Double price;
    private String material;
    private String dimensions;
    private Double weight;

    // Dataset / delivery fields
    private String manufacturerName;
    private String brandName;
    private String manufacturerPartNumber;
    private String shortDescription;
    private String longDescription;
    private String warranty;
    private String productImage;
    private String specificationSheet;
    private Map<String, FieldResultDTO> fieldResults;
    public Map<String, FieldResultDTO> getFieldResults() {
    return fieldResults;
}

    public void setFieldResults(
        Map<String, FieldResultDTO> fieldResults) {
    this.fieldResults = fieldResults;
   }

    public ProductSchemaDTO() {
    }

    public ProductSchemaDTO(
            String name,
            String category,
            Double price,
            String material,
            String dimensions,
            Double weight,
            String manufacturerName,
            String brandName,
            String manufacturerPartNumber,
            String shortDescription,
            String longDescription,
            String warranty,
            String productImage,
            String specificationSheet) {

        this.name = name;
        this.category = category;
        this.price = price;
        this.material = material;
        this.dimensions = dimensions;
        this.weight = weight;
        this.manufacturerName = manufacturerName;
        this.brandName = brandName;
        this.manufacturerPartNumber = manufacturerPartNumber;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.warranty = warranty;
        this.productImage = productImage;
        this.specificationSheet = specificationSheet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getSpecificationSheet() {
        return specificationSheet;
    }

    public void setSpecificationSheet(String specificationSheet) {
        this.specificationSheet = specificationSheet;
    }
}