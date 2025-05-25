package com.L2.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ProductFamilyFx {
    private StringProperty range;
    private ObservableList<String> productFamilies;

    public ProductFamilyFx(String range, List<String> productFamilies) {
        this.range = new SimpleStringProperty(range);
        this.productFamilies = FXCollections.observableList(productFamilies);
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

    public void setProductFamilies(ObservableList<String> productFamilies) {
        this.productFamilies = productFamilies;
    }
}
