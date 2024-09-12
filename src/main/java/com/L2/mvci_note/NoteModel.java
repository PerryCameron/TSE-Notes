package com.L2.mvci_note;

import com.L2.dto.NoteDTO;
import com.L2.dto.EntitlementDTO;
import com.L2.dto.UserDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

public class NoteModel {
    private ObservableList<NoteDTO> notes = FXCollections.observableArrayList();
    private ObjectProperty<NoteDTO> boundNote = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = FXCollections.observableArrayList();
    private ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<VBox> PlanDetailsBox = new SimpleObjectProperty<>();
    private StringProperty statusLabel = new SimpleStringProperty();
    private ObjectProperty<UserDTO> user = new SimpleObjectProperty<>();
    private BooleanProperty clearCalled  = new SimpleBooleanProperty(false);
    private BooleanProperty refreshBoundNote = new SimpleBooleanProperty(false);


    public void clearBoundNoteFields() {
        boundNote.get().getPartOrders().clear();
        boundNote.get().setSelectedPartOrder(null);
        boundNote.get().clear();
        setClearCalled(true);
        setClearCalled(false);
    }

    public boolean isRefreshBoundNote() {
        return refreshBoundNote.get();
    }

    public BooleanProperty refreshBoundNoteProperty() {
        return refreshBoundNote;
    }

    public void setRefreshBoundNote(boolean refreshBoundNote) {
        this.refreshBoundNote.set(refreshBoundNote);
    }

    public BooleanProperty clearCalledProperty() {
        return clearCalled;
    }

    public void setClearCalled(boolean clearCalled) {
        this.clearCalled.set(clearCalled);
    }

    public ObservableList<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(ObservableList<NoteDTO> notes) {
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

    public NoteDTO getBoundNote() {
        return boundNote.get();
    }

    public ObjectProperty<NoteDTO> boundNoteProperty() {
        return boundNote;
    }

    public void setBoundNote(NoteDTO boundNote) {
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
