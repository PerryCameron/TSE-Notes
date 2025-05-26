package com.L2.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductFamilyFx {
    private StringProperty range  = new SimpleStringProperty();
    private ObservableList<String> productFamilies = FXCollections.observableArrayList();

    public ProductFamilyFx(String range, List<String> productFamilies) {
        this.range = new SimpleStringProperty(range);
        this.productFamilies = FXCollections.observableList(productFamilies);
    }

    // used for jackson
    public ProductFamilyFx() {
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

    public ObservableList<String> getProductFamilies() {
        return productFamilies;
    }

    @JsonProperty("product_families") // Map JSON "product_families" to this setter
    public void setProductFamilies(List<String> productFamilies) {
        this.productFamilies.setAll(productFamilies != null ? productFamilies : List.of());
    }
}
