package com.L2.dto.global_spares;

public class ProductToSparesDTO {
    private int id;
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
    private String removedFromCatalogue;
    private String comments;
    private String keywords;
    private Boolean archived;
    private Boolean customAdd;
    private String lastUpdatedBy;

    // Default constructor
    public ProductToSparesDTO() {
        this.archived = false;
        this.customAdd = false;
    }

    public ProductToSparesDTO(Boolean archived, Boolean custom_add) {
        this.archived = archived;
        this.customAdd = custom_add;
    }

    public ProductToSparesDTO(int id, String pimRange, String pimProductFamily, String spareItem, String replacementItem, String standardExchangeItem, String spareDescription, String catalogueVersion, String productEndOfServiceDate, String lastUpdate, String addedToCatalogue, String removedFromCatalogue, String comments, String keywords, Boolean archived, Boolean customAdd, String lastUpdatedBy) {
        this.id = id;
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
        this.removedFromCatalogue = removedFromCatalogue;
        this.comments = comments;
        this.keywords = keywords;
        this.archived = archived;
        this.customAdd = customAdd;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void clear() {
        this.pimRange = "";
        this.pimProductFamily = "";
        this.spareItem = "";
        this.replacementItem = "";
        this.standardExchangeItem = "";
        this.spareDescription = "";
        this.catalogueVersion = "";
        this.productEndOfServiceDate = "";
        this.lastUpdate = "";
        this.addedToCatalogue = "";
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

    public Boolean isArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getArchived() {
        return archived;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean isCustomAdd() {
        return customAdd;
    }

    public void setCustomAdd(Boolean customAdd) {
        this.customAdd = customAdd;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getRemovedFromCatalogue() {
        return removedFromCatalogue;
    }

    public void setRemovedFromCatalogue(String removedFromCatalogue) {
        this.removedFromCatalogue = removedFromCatalogue;
    }

    @Override
    public String toString() {
        return "ProductToSparesDTO{" +
                "pimRange='" + pimRange + '\'' +
                ", pimProductFamily='" + pimProductFamily + '\'' +
                ", spareItem='" + spareItem + '\'' +
                ", replacementItem='" + replacementItem + '\'' +
                ", standardExchangeItem='" + standardExchangeItem + '\'' +
                ", spareDescription='" + spareDescription + '\'' +
                ", catalogueVersion='" + catalogueVersion + '\'' +
                ", productEndOfServiceDate='" + productEndOfServiceDate + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", addedToCatalogue='" + addedToCatalogue + '\'' +
                ", archived=" + archived +
                ", custom_add=" + customAdd +
                '}';
    }
}
