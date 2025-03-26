package com.L2.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;


public class NoteDTO {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> timestamp = new SimpleObjectProperty<>(); // this is the field not updating
    private final StringProperty workOrder = new SimpleStringProperty();
    private final StringProperty caseNumber = new SimpleStringProperty();
    private final StringProperty serialNumber = new SimpleStringProperty();
    private final StringProperty modelNumber = new SimpleStringProperty();
    private final StringProperty callInPerson = new SimpleStringProperty();
    private final StringProperty callInPhoneNumber = new SimpleStringProperty();
    private final StringProperty callInEmail = new SimpleStringProperty();
    private final BooleanProperty underWarranty = new SimpleBooleanProperty();
    private final StringProperty activeServiceContract = new SimpleStringProperty();
    private final StringProperty serviceLevel = new SimpleStringProperty();
    private final StringProperty schedulingTerms = new SimpleStringProperty();
    private final StringProperty upsStatus = new SimpleStringProperty();
    private final BooleanProperty loadSupported = new SimpleBooleanProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty issue = new SimpleStringProperty();
    private final StringProperty contactName = new SimpleStringProperty();
    private final StringProperty contactPhoneNumber = new SimpleStringProperty();
    private final StringProperty contactEmail = new SimpleStringProperty();
    private final StringProperty street = new SimpleStringProperty();
    private final StringProperty installedAt = new SimpleStringProperty();
    private final StringProperty city = new SimpleStringProperty();
    private final StringProperty state = new SimpleStringProperty();
    private final StringProperty zip = new SimpleStringProperty();
    private final StringProperty country = new SimpleStringProperty();
    private final StringProperty createdWorkOrder = new SimpleStringProperty();
    private final StringProperty tex = new SimpleStringProperty();
    private final IntegerProperty partsOrder = new SimpleIntegerProperty();
    private final BooleanProperty completed = new SimpleBooleanProperty();
    private final BooleanProperty isEmail = new SimpleBooleanProperty();
    private final StringProperty additionalCorrectiveActionText = new SimpleStringProperty();
    private final StringProperty relatedCaseNumber = new SimpleStringProperty();
    private final StringProperty tAndM = new SimpleStringProperty();
    private final ListProperty<PartOrderDTO> partOrders = new SimpleListProperty<>(FXCollections.observableArrayList());

    public NoteDTO() {
    }

    public NoteDTO(int id, boolean isEmail) {
        // Initialize properties with passed values
        this.id.set(id);
        this.timestamp.set(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
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
        this.title.set("");
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
        this.createdWorkOrder.set("");
        this.tex.set("");
        this.partsOrder.set(0);
        this.completed.set(false);
        this.isEmail.set(isEmail);
        this.additionalCorrectiveActionText.set("");
        this.relatedCaseNumber.set("");
        this.tAndM.set("");
    }

    public NoteDTO(Integer id, LocalDateTime timestamp, String workOrder, String caseNumber, String serialNumber,
                   String modelNumber, String callInPerson, String callInPhoneNumber, String callInEmail,
                   Boolean underWarranty, String activeServiceContract, String serviceLevel, String schedulingTerms,
                   String upsStatus, Boolean loadSupported, String title, String issue, String contactName,
                   String contactPhoneNumber, String contactEmail, String street, String installedAt,
                   String city, String state, String zip, String country,
                   String createdWorkOrder, String tex, Integer partsOrder,
                   Boolean completed, Boolean isEmail, String additionalCorrectiveActionText,
                   String relatedCaseNumber, String tAndM) {
        this.id.set(id);
        this.timestamp.set(timestamp);
        this.workOrder.set(workOrder);
        this.caseNumber.set(caseNumber);
        this.serialNumber.set(serialNumber);
        this.modelNumber.set(modelNumber);
        this.callInPerson.set(callInPerson);
        this.callInPhoneNumber.set(callInPhoneNumber);
        this.callInEmail.set(callInEmail);
        this.underWarranty.set(underWarranty);
        this.activeServiceContract.set(activeServiceContract);
        this.serviceLevel.set(serviceLevel);
        this.schedulingTerms.set(schedulingTerms);
        this.upsStatus.set(upsStatus);
        this.loadSupported.set(loadSupported);
        this.issue.set(issue);
        this.title.set(title);
        this.contactName.set(contactName);
        this.contactPhoneNumber.set(contactPhoneNumber);
        this.contactEmail.set(contactEmail);
        this.street.set(street);
        this.installedAt.set(installedAt);
        this.city.set(city);
        this.state.set(state);
        this.zip.set(zip);
        this.country.set(country);
        this.createdWorkOrder.set(createdWorkOrder);
        this.tex.set(tex);
        this.partsOrder.set(partsOrder);
        this.completed.set(completed);
        this.isEmail.set(isEmail);
        this.additionalCorrectiveActionText.set(additionalCorrectiveActionText);
        this.relatedCaseNumber.set(relatedCaseNumber);
        this.tAndM.set(tAndM);
    }

    public void copyFrom(NoteDTO noteDTO) {
        timestamp.set(noteDTO.getTimestamp());
        workOrder.set(noteDTO.getWorkOrder());
        caseNumber.set(noteDTO.getCaseNumber());
        serialNumber.set(noteDTO.getSerialNumber());
        modelNumber.set(noteDTO.getModelNumber());
        callInPerson.set(noteDTO.getCallInPerson());
        callInPhoneNumber.set(noteDTO.getCallInPhoneNumber());
        callInEmail.set(noteDTO.getCallInEmail());
        underWarranty.set(noteDTO.isUnderWarranty());
        activeServiceContract.set(noteDTO.getActiveServiceContract());
        serviceLevel.set(noteDTO.getServiceLevel());
        schedulingTerms.set(noteDTO.getSchedulingTerms());
        upsStatus.set(noteDTO.getUpsStatus());
        loadSupported.set(noteDTO.isLoadSupported());
        issue.set(noteDTO.getIssue());
        title.set(noteDTO.getTitle());
        contactName.set(noteDTO.getContactName());
        contactPhoneNumber.set(noteDTO.getContactPhoneNumber());
        contactEmail.set(noteDTO.getContactEmail());
        street.set(noteDTO.getStreet());
        installedAt.set(noteDTO.getInstalledAt());
        city.set(noteDTO.getCity());
        state.set(noteDTO.getState());
        zip.set(noteDTO.getZip());
        country.set(noteDTO.getCountry());
        partOrders.set(noteDTO.getPartOrders());
        createdWorkOrder.set(noteDTO.getCreatedWorkOrder());
        tex.set(noteDTO.getTex());
        partsOrder.set(noteDTO.getPartsOrder());
        completed.set(noteDTO.isCompleted());
        isEmail.set(noteDTO.isEmail());
        additionalCorrectiveActionText.set(noteDTO.getAdditionalCorrectiveActionText());
        relatedCaseNumber.set(noteDTO.getRelatedCaseNumber());
        tAndM.set(noteDTO.gettAndM());
        id.set(noteDTO.getId()); // since this is listened to, it must copy last
//        System.out.println("NoteDTO copied, timeStamp: " + this.timestamp.get() + " id: " + this.id.get());
//        System.out.println();
    }

    public void clear() {
        this.timestamp.set(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
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
        this.title.set("");
        this.contactName.set("");
        this.contactPhoneNumber.set("");
        this.contactEmail.set("");
        this.street.set("");
        this.installedAt.set("");
        this.city.set("");
        this.state.set("");
        this.zip.set("");
        this.country.set("");
        this.partOrders.set(FXCollections.observableArrayList());
        this.createdWorkOrder.set("");
        this.tex.set("");
        this.partsOrder.set(0);
        this.completed.set(false);
        this.isEmail.set(false);
        this.additionalCorrectiveActionText.set("");
        this.relatedCaseNumber.set("");
        this.tAndM.set("");
    }

    /**
     * This provides a nice formatted date found on things that are copied, and also the status bar at the bottom of
     * the application
     * @return formatted date
     */

    public String formattedDate() {
        LocalDateTime dateTime = timestampProperty().get();
        if (dateTime == null) {
            return "";
        }
        // Use a formatter with a fixed locale (e.g., US) for consistent AM/PM formatting
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a z", Locale.US);

        // Assuming you want to format it with the system's default time zone
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateTime.atZone(zone);

        // Format the date and time with the short time zone name
        return zonedDateTime.format(formatter);
    }


    public String prettyDate() {
        LocalDateTime dateTime = timestampProperty().get();
        if (dateTime == null) {
            return "";
        }
        // Use a formatter that formats the date in a pretty style
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        // Assuming you want to format it with the system's default time zone
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateTime.atZone(zone);
        // Format the date and time
        return zonedDateTime.format(formatter);
    }

    /////////////////////////////////////////////////////////////
    // this is how it is displayed in noteListTableView
    public StringProperty formattedTimestampProperty() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        return new SimpleStringProperty(timestamp.get().format(formatter));
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getRelatedCaseNumber() {
        return relatedCaseNumber.get();
    }

    public StringProperty relatedCaseNumberProperty() {
        return relatedCaseNumber;
    }

    public void setRelatedCaseNumber(String relatedCaseNumber) {
        this.relatedCaseNumber.set(relatedCaseNumber);
    }

    public boolean isEmail() {
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

    private transient ListProperty<PartOrderDTO> parts = new SimpleListProperty<>(FXCollections.observableArrayList());

    public List<PartOrderDTO> getPartsList() {
        return parts.get();
    }


    public void setPartsList(List<PartOrderDTO> partsList) {
        this.parts.set(FXCollections.observableArrayList(partsList));
    }

    public String gettAndM() {
        return tAndM.get();
    }

    public StringProperty tAndMProperty() {
        return tAndM;
    }

    public void settAndM(String tAndM) {
        this.tAndM.set(tAndM);
    }

    public String toTest() {
        return "NoteDTO{" +
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
                ", schedulingTerms=" + schedulingTerms +
                ", upsStatus=" + upsStatus +
                ", loadSupported=" + loadSupported +
                ", title=" + title +
                ", issue=" + issue +
                ", contactName=" + contactName +
                ", contactPhoneNumber=" + contactPhoneNumber +
                ", contactEmail=" + contactEmail +
                ", street=" + street +
                ", installedAt=" + installedAt +
                ", city=" + city +
                ", state=" + state +
                ", zip=" + zip +
                ", country=" + country +
                ", createdWorkOrder=" + createdWorkOrder +
                ", tex=" + tex +
                ", partsOrder=" + partsOrder +
                ", completed=" + completed +
                ", isEmail=" + isEmail +
                ", additionalCorrectiveActionText=" + additionalCorrectiveActionText +
                ", relatedCaseNumber=" + relatedCaseNumber +
                ", partOrders=" + partOrders +
                ", parts=" + parts +
                ", t&m=" + tAndM +
                '}';
    }
}
