package com.L2.mvci_note;

import com.L2.dto.CaseDTO;
import com.L2.dto.EntitlementDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.dto.UserDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class NoteModel {
    private ObjectProperty<CaseDTO> currentNote = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = FXCollections.observableArrayList();
    private ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<PartOrderDTO> currentPartOrder = new SimpleObjectProperty<>();
    private ObjectProperty<VBox> PlanDetailsBox = new SimpleObjectProperty<>();
    private StringProperty statusLabel = new SimpleStringProperty();
    private ObjectProperty<UserDTO> user = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> dateTimeProperty = new SimpleObjectProperty<>();


    public String formattedDate() {
        LocalDateTime dateTime = dateTimeProperty.get();
        if (dateTime == null) {
            return "";
        }
        // Use a formatter that includes the short time zone name
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a z");
        // Assuming you want to format it with the system's default time zone
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateTime.atZone(zone);
        // Format the date and time with the short time zone name
        return zonedDateTime.format(formatter);
    }



    public LocalDateTime getDateTimeProperty() {
        return dateTimeProperty.get();
    }

    public ObjectProperty<LocalDateTime> dateTimePropertyProperty() {
        return dateTimeProperty;
    }

    public void setDateTimeProperty(LocalDateTime dateTimeProperty) {
        this.dateTimeProperty.set(dateTimeProperty);
    }

    public PartOrderDTO getCurrentPartOrder() {
        return currentPartOrder.get();
    }

    public ObjectProperty<PartOrderDTO> currentPartOrderProperty() {
        return currentPartOrder;
    }

    public void setCurrentPartOrder(PartOrderDTO currentPartOrder) {
        this.currentPartOrder.set(currentPartOrder);
    }

    public UserDTO getUser() {
        return user.get();
    }

    public ObjectProperty<UserDTO> userProperty() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user.set(user);
    }

    public String getStatusLabel() {
        return statusLabel.get();
    }

    public StringProperty statusLabelProperty() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel.set(statusLabel);
    }

    public VBox getPlanDetailsBox() {
        return PlanDetailsBox.get();
    }

    public ObjectProperty<VBox> planDetailsBoxProperty() {
        return PlanDetailsBox;
    }

    public void setPlanDetailsBox(VBox planDetailsBox) {
        this.PlanDetailsBox.set(planDetailsBox);
    }

    public ObservableList<EntitlementDTO> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(ObservableList<EntitlementDTO> entitlements) {
        this.entitlements = entitlements;
    }

    public CaseDTO getCurrentNote() {
        return currentNote.get();
    }

    public ObjectProperty<CaseDTO> currentNoteProperty() {
        return currentNote;
    }

    public void setCurrentNote(CaseDTO currentNote) {
        this.currentNote.set(currentNote);
    }

    public EntitlementDTO getCurrentEntitlement() {
        return currentEntitlement.get();
    }

    public ObjectProperty<EntitlementDTO> currentEntitlementProperty() {
        return currentEntitlement;
    }

    public void setCurrentEntitlement(EntitlementDTO currentEntitlement) {
        this.currentEntitlement.set(currentEntitlement);
    }
}
