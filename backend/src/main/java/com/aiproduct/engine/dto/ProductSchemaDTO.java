package com.aiproduct.engine.dto;

public class ProductSchemaDTO {

    private String name;
    private String category;
    private Double price;
    private String material;
    private String dimensions;
    private Double weight;

    public ProductSchemaDTO() {
    }

    public ProductSchemaDTO(
            String name,
            String category,
            Double price,
            String material,
            String dimensions,
            Double weight) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.material = material;
        this.dimensions = dimensions;
        this.weight = weight;
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
}