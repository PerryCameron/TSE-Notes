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
    private String productEndOfServiceDate;
    private String lastUpdate;
    private String addedToCatalogue;
    private Boolean archived;
    private Boolean custom_add;

    // Default constructor
    public ProductToSpares() {
    }

    public ProductToSpares(String pimRange, String pimProductFamily, String spareItem, String replacementItem, String standardExchangeItem, String spareDescription, String catalogueVersion, String productEndOfServiceDate, String lastUpdate, String addedToCatalogue, Boolean archived, Boolean custom_add) {
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
        this.archived = archived;
        this.custom_add = custom_add;
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

    public String getProductEndOfServiceDate() {
        return productEndOfServiceDate;
    }

    public void setProductEndOfServiceDate(String productEndOfServiceDate) {
        this.productEndOfServiceDate = productEndOfServiceDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getAddedToCatalogue() {
        return addedToCatalogue;
    }

    public void setAddedToCatalogue(String addedToCatalogue) {
        this.addedToCatalogue = addedToCatalogue;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getCustom_add() {
        return custom_add;
    }

    public void setCustom_add(Boolean custom_add) {
        this.custom_add = custom_add;
    }
}
