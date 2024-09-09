package com.L2.mvci_note;

import com.L2.dto.CaseDTO;
import com.L2.dto.EntitlementDTO;
import com.L2.dto.UserDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

public class NoteModel {
    private ObservableList<CaseDTO> notes = FXCollections.observableArrayList();
    private ObjectProperty<CaseDTO> boundNote = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = FXCollections.observableArrayList();
    private ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<VBox> PlanDetailsBox = new SimpleObjectProperty<>();
    private StringProperty statusLabel = new SimpleStringProperty();
    private ObjectProperty<UserDTO> user = new SimpleObjectProperty<>();



    public ObservableList<CaseDTO> getNotes() {
        return notes;
    }

    public void setNotes(ObservableList<CaseDTO> notes) {
        this.notes = notes;
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

    public CaseDTO getBoundNote() {
        return boundNote.get();
    }

    public ObjectProperty<CaseDTO> boundNoteProperty() {
        return boundNote;
    }

    public void setBoundNote(CaseDTO boundNote) {
        this.boundNote.set(boundNote);
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
