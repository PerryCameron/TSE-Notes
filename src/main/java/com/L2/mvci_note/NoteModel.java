package com.L2.mvci_note;

import com.L2.dto.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

public class NoteModel {
    private ObservableList<NoteDTO> notes = FXCollections.observableArrayList();
    // this is a NoteDTO that never moves, it just copies and pastes its values to the real notes
    private final ObjectProperty<NoteDTO> boundNote = new SimpleObjectProperty<>();
    // you can only select one part at a time, so simpler to keep here.
    private final ObjectProperty<PartDTO> selectedPart = new SimpleObjectProperty<>();
    // you can only have one part order focused at time, so simpler to keep here as well
    private final ObjectProperty<PartOrderDTO> selectedPartOrder = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = FXCollections.observableArrayList();
    private final ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private final ObjectProperty<VBox> PlanDetailsBox = new SimpleObjectProperty<>();

    // allow this many records to be displayed
    private final IntegerProperty pageSize = new SimpleIntegerProperty(50);
    // skip the first N records
    private final IntegerProperty offset = new SimpleIntegerProperty(0);

    // shouldn't this be under main controller???????
    private final StringProperty statusLabel = new SimpleStringProperty();
    private final ObjectProperty<UserDTO> user = new SimpleObjectProperty<>();
    private final BooleanProperty clearCalled  = new SimpleBooleanProperty(false);
    private final BooleanProperty refreshBoundNote = new SimpleBooleanProperty(false);
    private final BooleanProperty refreshEntitlements = new SimpleBooleanProperty(false);
    private final BooleanProperty openNoteTab = new SimpleBooleanProperty(false);


    public void clearBoundNoteFields() {
        boundNote.get().getPartOrders().clear();
        setSelectedPartOrder(null);
        boundNote.get().clear();
        refreshBoundNote();
    }

    public void refreshBoundNote() {  // to refresh fields without bindings
        setRefreshBoundNote(true);
        setRefreshBoundNote(false);
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public int getOffset() {
        return offset.get();
    }

    public IntegerProperty offsetProperty() {
        return offset;
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

    public PartDTO getSelectedPart() {
        return selectedPart.get();
    }

    public ObjectProperty<PartDTO> selectedPartProperty() {
        return selectedPart;
    }

    public void setSelectedPart(PartDTO selectedPart) {
        this.selectedPart.set(selectedPart);
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

    // gets the text in the StringProperty
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

    public BooleanProperty refreshEntitlementsProperty() {
        return refreshEntitlements;
    }

    public void refreshEntitlements() {
        refreshEntitlements.set(true);
        refreshEntitlements.set(false);
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

    public BooleanProperty openNoteTabProperty() {
        return openNoteTab;
    }

    public void openNoteTab() {
        this.openNoteTab.set(true);
        this.openNoteTab.set(false);
    }
}
