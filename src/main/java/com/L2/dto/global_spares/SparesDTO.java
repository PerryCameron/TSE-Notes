package com.L2.dto.global_spares;

public class SparesDTO {
    private int id;
    private String pim;
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

    public SparesDTO(int id, String pim, String spareItem, String replacementItem, String standardExchangeItem, String spareDescription, String catalogueVersion, String productEndOfServiceDate, String lastUpdate, String addedToCatalogue, String removedFromCatalogue, String comments, String keywords, Boolean archived, Boolean customAdd, String lastUpdatedBy) {
        this.id = id;
        this.pim = pim;
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

    public SparesDTO(String spareItem, String spareDescription) {
        this.id = 0;
        this.pim = "";
        this.spareItem = spareItem;
        this.replacementItem = "";
        this.standardExchangeItem = "";
        this.spareDescription = spareDescription;
        this.catalogueVersion = "";
        this.productEndOfServiceDate = "";
        this.lastUpdate = "";
        this.addedToCatalogue = "";
        this.removedFromCatalogue = "";
        this.comments = "";
        this.keywords = "";
        this.archived = true;
        this.customAdd = true;
        this.lastUpdatedBy = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPim() {
        return pim;
    }

    public void setPim(String pim) {
        this.pim = pim;
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

    public String getRemovedFromCatalogue() {
        return removedFromCatalogue;
    }

    public void setRemovedFromCatalogue(String removedFromCatalogue) {
        this.removedFromCatalogue = removedFromCatalogue;
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

    public String isArchived() {
        if(archived == null) return "No";
        if (archived) return "No";
        else return "Yes";
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getCustomAdd() {
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

    @Override
    public String toString() {
        return "SparesDTO{" +
                "id=" + id +
//                ", pim='" + pim + '\'' +
                ", spareItem='" + spareItem + '\'' +
//                ", replacementItem='" + replacementItem + '\'' +
//                ", standardExchangeItem='" + standardExchangeItem + '\'' +
//                ", spareDescription='" + spareDescription + '\'' +
//                ", catalogueVersion='" + catalogueVersion + '\'' +
//                ", productEndOfServiceDate='" + productEndOfServiceDate + '\'' +
//                ", lastUpdate='" + lastUpdate + '\'' +
//                ", addedToCatalogue='" + addedToCatalogue + '\'' +
//                ", removedFromCatalogue='" + removedFromCatalogue + '\'' +
//                ", comments='" + comments + '\'' +
//                ", keywords='" + keywords + '\'' +
                ", archived=" + archived +
//                ", customAdd=" + customAdd +
//                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
