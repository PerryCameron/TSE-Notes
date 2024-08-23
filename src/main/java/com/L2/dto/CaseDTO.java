package com.L2.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDateTime;


public class CaseDTO implements Serializable {
    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<LocalDateTime> timestamp = new SimpleObjectProperty<>();
    private StringProperty workOrder = new SimpleStringProperty();
    private StringProperty caseNumber = new SimpleStringProperty();
    private StringProperty serialNumber = new SimpleStringProperty();
    private StringProperty modelNumber = new SimpleStringProperty();
    private StringProperty callInPerson = new SimpleStringProperty();
    private StringProperty callInPhoneNumber = new SimpleStringProperty();
    private StringProperty callInEmail = new SimpleStringProperty();
    private BooleanProperty underWarranty = new SimpleBooleanProperty();
    private StringProperty activeServiceContract = new SimpleStringProperty();
    private StringProperty serviceLevel = new SimpleStringProperty();
    private StringProperty upsStatus = new SimpleStringProperty();
    private BooleanProperty loadSupported = new SimpleBooleanProperty();
    private StringProperty issue = new SimpleStringProperty();
    private StringProperty contactName = new SimpleStringProperty();
    private StringProperty contactPhoneNumber = new SimpleStringProperty();
    private StringProperty contactEmail = new SimpleStringProperty();
    private StringProperty addressLine1 = new SimpleStringProperty();
    private StringProperty addressLine2 = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty state = new SimpleStringProperty();
    private StringProperty zip = new SimpleStringProperty();
    private StringProperty country = new SimpleStringProperty();
    private ListProperty<PartDTO> parts = new SimpleListProperty<>(FXCollections.observableArrayList());
    private IntegerProperty createdWorkOrder = new SimpleIntegerProperty();
    private IntegerProperty partsOrder = new SimpleIntegerProperty();
    private StringProperty entitlement = new SimpleStringProperty();




    public String getEntitlement() {
        return entitlement.get();
    }

    public StringProperty entitlementProperty() {
        return entitlement;
    }

    public void setEntitlement(String entitlement) {
        this.entitlement.set(entitlement);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public LocalDateTime getTimestamp() {
        return timestamp.get();
    }

    public ObjectProperty<LocalDateTime> timestampProperty() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp.set(timestamp);
    }

    public String getWorkOrder() {
        return workOrder.get();
    }

    public StringProperty workOrderProperty() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder.set(workOrder);
    }

    public String getCaseNumber() {
        return caseNumber.get();
    }

    public StringProperty caseNumberProperty() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber.set(caseNumber);
    }

    public String getSerialNumber() {
        return serialNumber.get();
    }

    public StringProperty serialNumberProperty() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public String getModelNumber() {
        return modelNumber.get();
    }

    public StringProperty modelNumberProperty() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber.set(modelNumber);
    }

    public String getCallInPerson() {
        return callInPerson.get();
    }

    public StringProperty callInPersonProperty() {
        return callInPerson;
    }

    public void setCallInPerson(String callInPerson) {
        this.callInPerson.set(callInPerson);
    }

    public String getCallInPhoneNumber() {
        return callInPhoneNumber.get();
    }

    public StringProperty callInPhoneNumberProperty() {
        return callInPhoneNumber;
    }

    public void setCallInPhoneNumber(String callInPhoneNumber) {
        this.callInPhoneNumber.set(callInPhoneNumber);
    }

    public String getCallInEmail() {
        return callInEmail.get();
    }

    public StringProperty callInEmailProperty() {
        return callInEmail;
    }

    public void setCallInEmail(String callInEmail) {
        this.callInEmail.set(callInEmail);
    }

    public boolean isUnderWarranty() {
        return underWarranty.get();
    }

    public BooleanProperty underWarrantyProperty() {
        return underWarranty;
    }

    public void setUnderWarranty(boolean underWarranty) {
        this.underWarranty.set(underWarranty);
    }

    public String getActiveServiceContract() {
        return activeServiceContract.get();
    }

    public StringProperty activeServiceContractProperty() {
        return activeServiceContract;
    }

    public void setActiveServiceContract(String activeServiceContract) {
        this.activeServiceContract.set(activeServiceContract);
    }

    public String getServiceLevel() {
        return serviceLevel.get();
    }

    public StringProperty serviceLevelProperty() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel.set(serviceLevel);
    }

    public String getUpsStatus() {
        return upsStatus.get();
    }

    public StringProperty upsStatusProperty() {
        return upsStatus;
    }

    public void setUpsStatus(String upsStatus) {
        this.upsStatus.set(upsStatus);
    }

    public boolean isLoadSupported() {
        return loadSupported.get();
    }

    public BooleanProperty loadSupportedProperty() {
        return loadSupported;
    }

    public void setLoadSupported(boolean loadSupported) {
        this.loadSupported.set(loadSupported);
    }

    public String getIssue() {
        return issue.get();
    }

    public StringProperty issueProperty() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue.set(issue);
    }

    public String getContactName() {
        return contactName.get();
    }

    public StringProperty contactNameProperty() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName.set(contactName);
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber.get();
    }

    public StringProperty contactPhoneNumberProperty() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber.set(contactPhoneNumber);
    }

    public String getContactEmail() {
        return contactEmail.get();
    }

    public StringProperty contactEmailProperty() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail.set(contactEmail);
    }

    public String getAddressLine1() {
        return addressLine1.get();
    }

    public StringProperty addressLine1Property() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1.set(addressLine1);
    }

    public String getAddressLine2() {
        return addressLine2.get();
    }

    public StringProperty addressLine2Property() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2.set(addressLine2);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getState() {
        return state.get();
    }

    public StringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getZip() {
        return zip.get();
    }

    public StringProperty zipProperty() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip.set(zip);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public ObservableList<PartDTO> getParts() {
        return parts.get();
    }

    public ListProperty<PartDTO> partsProperty() {
        return parts;
    }

    public void setParts(ObservableList<PartDTO> parts) {
        this.parts.set(parts);
    }

    public int getCreatedWorkOrder() {
        return createdWorkOrder.get();
    }

    public IntegerProperty createdWorkOrderProperty() {
        return createdWorkOrder;
    }

    public void setCreatedWorkOrder(int createdWorkOrder) {
        this.createdWorkOrder.set(createdWorkOrder);
    }

    public int getPartsOrder() {
        return partsOrder.get();
    }

    public IntegerProperty partsOrderProperty() {
        return partsOrder;
    }

    public void setPartsOrder(int partsOrder) {
        this.partsOrder.set(partsOrder);
    }

    @Override
    public String toString() {
        return "CaseDTO{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", workOrder=" + workOrder +
                ", caseNumber=" + caseNumber +
                ", serialNumber=" + serialNumber +
                ", modelNumber=" + modelNumber +
                ", callInPerson=" + callInPerson +
                ", callInPhoneNumber=" + callInPhoneNumber +
                ", callInEmail=" + callInEmail +
                ", underWarranty=" + underWarranty +
                ", activeServiceContract=" + activeServiceContract +
                ", serviceLevel=" + serviceLevel +
                ", upsStatus=" + upsStatus +
                ", loadSupported=" + loadSupported +
                ", issue=" + issue +
                ", contactName=" + contactName +
                ", contactPhoneNumber=" + contactPhoneNumber +
                ", contactEmail=" + contactEmail +
                ", addressLine1=" + addressLine1 +
                ", addressLine2=" + addressLine2 +
                ", city=" + city +
                ", state=" + state +
                ", zip=" + zip +
                ", country=" + country +
                ", parts=" + parts +
                ", createdWorkOrder=" + createdWorkOrder +
                ", partsOrder=" + partsOrder +
                '}';
    }

    public void clearCase(CaseDTO caseDTO) {
        caseDTO.setWorkOrder("");
        caseDTO.setCaseNumber("");
        caseDTO.setSerialNumber("");
        caseDTO.setModelNumber("");
        caseDTO.setCallInPerson("");
        caseDTO.setCallInPhoneNumber("");
        caseDTO.setCallInEmail("");
        caseDTO.setUnderWarranty(false);
        caseDTO.setActiveServiceContract("");
        caseDTO.setServiceLevel("");
        caseDTO.setUpsStatus("");
        caseDTO.setLoadSupported(false);
        caseDTO.setIssue("");
        caseDTO.setContactName("");
        caseDTO.setContactPhoneNumber("");
        caseDTO.setContactEmail("");
        caseDTO.setAddressLine1("");
        caseDTO.setAddressLine2("");
        caseDTO.setCity("");
        caseDTO.setState("");
        caseDTO.setZip("");
        caseDTO.setCountry("");
        caseDTO.getParts().clear();
        caseDTO.setCreatedWorkOrder(0);
        caseDTO.setPartsOrder(0);
        caseDTO.setTimestamp(null);
    }

}
