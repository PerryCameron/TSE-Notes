package com.L2.dto.global_spares;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductFamilyDTO {

    @JsonProperty("range")
    private String range;

    @JsonProperty("product_families")
    private List<String> productFamilies;

    // Default constructor for Jackson
    public ProductFamilyDTO() {
    }

    // Constructor for convenience (optional, not required by Jackson)
    public ProductFamilyDTO(String range, List<String> productFamilies) {
        this.range = range;
        this.productFamilies = productFamilies;
    }

    // Getters and setters
    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public List<String> getProductFamilies() {
        return productFamilies;
    }

    public void setProductFamilies(List<String> productFamilies) {
        this.productFamilies = productFamilies;
    }

    public String testString() {
        return "Range: " + (range != null ? range : "null") + ", ProductFamilies: " + (productFamilies != null ? productFamilies : "null");
    }

    @Override
    public String toString() {
        return range != null ? range : "Unnamed Range";
    }

    public void toFullString() {
       System.out.println("ProductFamilyDTO: range='" + range + '\'' + " Instance: " +System.identityHashCode(this));
       productFamilies.forEach(family -> System.out.println(family + " instance: " + System.identityHashCode(family)));
    }
}