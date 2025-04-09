package com.L2.dto.global_spares;

import java.util.Date;

public class ProductToSpares {
    private String pimRange;
    private String pimProductFamily;
    private String spareItem;
    private String replacementItem;
    private String standardExchangeItem;
    private String spareDescription;
    private String catalogueVersion;
    private Date productEndOfServiceDate;
    private Date lastUpdate;
    private Date addedToCatalogue;
    private String comments;
    private Double productGDP;
    private String productLineHeliosCode;

    // Default constructor
    public ProductToSpares() {
    }

    // Parameterized constructor
    public ProductToSpares(String pimRange, String pimProductFamily, String spareItem,
                   String replacementItem, String standardExchangeItem, String spareDescription,
                   String catalogueVersion, Date productEndOfServiceDate, Date lastUpdate,
                   Date addedToCatalogue, String comments, Double productGDP,
                   String productLineHeliosCode) {
        this.pimRange = pimRange;
        this.pimProductFamily = pimProductFamily;
        this.spareItem = spareItem;
        this.replacementItem = replacementItem;
        this.standardExchangeItem = standardExchangeItem;
        this.spareDescription = spareDescription;
        this.catalogueVersion = catalogueVersion;
        this.productEndOfServiceDate = productEndOfServiceDate;
        this.lastUpdate = lastUpdate;
        this.addedToCatalogue = addedToCatalogue;
        this.comments = comments;
        this.productGDP = productGDP;
        this.productLineHeliosCode = productLineHeliosCode;
    }

    // Getters and Setters
    public String getPimRange() {
        return pimRange;
    }

    public void setPimRange(String pimRange) {
        this.pimRange = pimRange;
    }

    public String getPimProductFamily() {
        return pimProductFamily;
    }

    public void setPimProductFamily(String pimProductFamily) {
        this.pimProductFamily = pimProductFamily;
    }

    public String getSpareItem() {
        return spareItem;
    }

    public void setSpareItem(String spareItem) {
        this.spareItem = spareItem;
    }

    public String getReplacementItem() {
        return replacementItem;
    }

    public void setReplacementItem(String replacementItem) {
        this.replacementItem = replacementItem;
    }

    public String getStandardExchangeItem() {
        return standardExchangeItem;
    }

    public void setStandardExchangeItem(String standardExchangeItem) {
        this.standardExchangeItem = standardExchangeItem;
    }

    public String getSpareDescription() {
        return spareDescription;
    }

    public void setSpareDescription(String spareDescription) {
        this.spareDescription = spareDescription;
    }

    public String getCatalogueVersion() {
        return catalogueVersion;
    }

    public void setCatalogueVersion(String catalogueVersion) {
        this.catalogueVersion = catalogueVersion;
    }

    public Date getProductEndOfServiceDate() {
        return productEndOfServiceDate;
    }

    public void setProductEndOfServiceDate(Date productEndOfServiceDate) {
        this.productEndOfServiceDate = productEndOfServiceDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getAddedToCatalogue() {
        return addedToCatalogue;
    }

    public void setAddedToCatalogue(Date addedToCatalogue) {
        this.addedToCatalogue = addedToCatalogue;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Double getProductGDP() {
        return productGDP;
    }

    public void setProductGDP(Double productGDP) {
        this.productGDP = productGDP;
    }

    public String getProductLineHeliosCode() {
        return productLineHeliosCode;
    }

    public void setProductLineHeliosCode(String productLineHeliosCode) {
        this.productLineHeliosCode = productLineHeliosCode;
    }

    // Optional: toString method for easy printing
    @Override
    public String toString() {
        return "Product{" +
                "pimRange='" + pimRange + '\'' +
                ", pimProductFamily='" + pimProductFamily + '\'' +
                ", spareItem='" + spareItem + '\'' +
                ", replacementItem='" + replacementItem + '\'' +
                ", standardExchangeItem='" + standardExchangeItem + '\'' +
                ", spareDescription='" + spareDescription + '\'' +
                ", catalogueVersion='" + catalogueVersion + '\'' +
                ", productEndOfServiceDate=" + productEndOfServiceDate +
                ", lastUpdate=" + lastUpdate +
                ", addedToCatalogue=" + addedToCatalogue +
                ", comments='" + comments + '\'' +
                ", productGDP=" + productGDP +
                ", productLineHeliosCode='" + productLineHeliosCode + '\'' +
                '}';
    }
}
