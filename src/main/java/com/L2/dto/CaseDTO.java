package com.L2.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class CaseDTO implements Serializable {
    private static final long serialVersionUID = 13897650298347L;

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
    private StringProperty schedulingTerms = new SimpleStringProperty();
    private StringProperty upsStatus = new SimpleStringProperty();
    private BooleanProperty loadSupported = new SimpleBooleanProperty();
    private StringProperty issue = new SimpleStringProperty();
    private StringProperty contactName = new SimpleStringProperty();
    private StringProperty contactPhoneNumber = new SimpleStringProperty();
    private StringProperty contactEmail = new SimpleStringProperty();
    private StringProperty street = new SimpleStringProperty();
    private StringProperty installedAt = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty state = new SimpleStringProperty();
    private StringProperty zip = new SimpleStringProperty();
    private StringProperty country = new SimpleStringProperty();
    private ListProperty<PartOrderDTO> partOrders = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<PartOrderDTO> selectedPartOrder = new SimpleObjectProperty<>();
    private StringProperty createdWorkOrder = new SimpleStringProperty();
    private StringProperty tex = new SimpleStringProperty();
    private IntegerProperty partsOrder = new SimpleIntegerProperty();
    private StringProperty entitlement = new SimpleStringProperty();
    private BooleanProperty completed = new SimpleBooleanProperty();
    private BooleanProperty isEmail = new SimpleBooleanProperty();
    private StringProperty additionalCorrectiveActionText = new SimpleStringProperty();

    public CaseDTO() {
    }

    public CaseDTO(int id, boolean isEmail) {
        // Initialize properties with passed values
        this.id.set(id);
        this.timestamp.set(LocalDateTime.now());
        this.workOrder.set("");
        this.caseNumber.set("");
        this.serialNumber.set("");
        this.modelNumber.set("");
        this.callInPerson.set("");
        this.callInPhoneNumber.set("");
        this.callInEmail.set("");
        this.underWarranty.set(false);
        this.activeServiceContract.set("None");
        this.serviceLevel.set("");
        this.schedulingTerms.set("");
        this.upsStatus.set("");
        this.loadSupported.set(true);
        this.issue.set("");
        this.contactName.set("");
        this.contactPhoneNumber.set("");
        this.contactEmail.set("");
        this.street.set("");
        this.installedAt.set("");
        this.city.set("");
        this.state.set("");
        this.zip.set("");
        this.country.set("");
        this.partOrders.set(FXCollections.observableArrayList(partOrders));
        this.selectedPartOrder.set(null);
        this.createdWorkOrder.set("");
        this.tex.set("");
        this.partsOrder.set(0);
        this.entitlement.set("");
        this.completed.set(false);
        this.isEmail.set(isEmail);
        this.additionalCorrectiveActionText.set("");
    }

    public void clearAddress() {
        installedAt.set("");
        street.set("");
        city.set("");
        state.set("");
        zip.set("");
        country.set("");
    }

    public void clearContact() {
        contactName.set("");
        contactPhoneNumber.set("");
        contactEmail.set("");
    }

    /////////////////////////////////////////////////////////////

    public boolean isIsEmail() {
        return isEmail.get();
    }

    public BooleanProperty isEmailProperty() {
        return isEmail;
    }

    public void setIsEmail(boolean isEmail) {
        this.isEmail.set(isEmail);
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public String getAdditionalCorrectiveActionText() {
        return additionalCorrectiveActionText.get();
    }

    public StringProperty additionalCorrectiveActionTextProperty() {
        return additionalCorrectiveActionText;
    }

    public void setAdditionalCorrectiveActionText(String additionalCorrectiveActionText) {
        this.additionalCorrectiveActionText.set(additionalCorrectiveActionText);
    }

    public String getTex() {
        return tex.get();
    }

    public StringProperty texProperty() {
        return tex;
    }

    public void setTex(String tex) {
        this.tex.set(tex);
    }

    public String getSchedulingTerms() {
        return schedulingTerms.get();
    }

    public StringProperty schedulingTermsProperty() {
        return schedulingTerms;
    }

    public void setSchedulingTerms(String schedulingTerms) {
        this.schedulingTerms.set(schedulingTerms);
    }

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

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getInstalledAt() {
        return installedAt.get();
    }

    public StringProperty installedAtProperty() {
        return installedAt;
    }

    public void setInstalledAt(String installedAt) {
        this.installedAt.set(installedAt);
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

    public String getCreatedWorkOrder() {
        return createdWorkOrder.get();
    }

    public StringProperty createdWorkOrderProperty() {
        return createdWorkOrder;
    }

    public void setCreatedWorkOrder(String createdWorkOrder) {
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

    public ObservableList<PartOrderDTO> getPartOrders() {
        return partOrders.get();
    }

    public ListProperty<PartOrderDTO> partOrdersProperty() {
        return partOrders;
    }

    public void setPartOrders(ObservableList<PartOrderDTO> partOrders) {
        this.partOrders.set(partOrders);
    }

    public PartOrderDTO getSelectedPartOrder() {
        return selectedPartOrder.get();
    }

    public ObjectProperty<PartOrderDTO> selectedPartOrderProperty() {
        return selectedPartOrder;
    }

    public void setSelectedPartOrder(PartOrderDTO selectedPartOrder) {
        this.selectedPartOrder.set(selectedPartOrder);
    }

    private transient ListProperty<PartOrderDTO> parts = new SimpleListProperty<>(FXCollections.observableArrayList());


    public List<PartOrderDTO> getPartsList() {
        return parts.get();
    }


    public void setPartsList(List<PartOrderDTO> partsList) {
        this.parts.set(FXCollections.observableArrayList(partsList));
    }


    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(new ArrayList<>(parts.get()));
    }


    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        List<PartOrderDTO> savedParts = (List<PartOrderDTO>) in.readObject();
        this.parts = new SimpleListProperty<>(FXCollections.observableArrayList(savedParts));
    }
}
