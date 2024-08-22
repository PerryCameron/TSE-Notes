package com.L2.mvci_note;

import com.L2.dto.CaseDTO;
import com.L2.dto.EntitlementDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

public class NoteModel {
    private ObjectProperty<CaseDTO> currentCase = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = FXCollections.observableArrayList();
    private ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<VBox> PlanDetailsBox = new SimpleObjectProperty<>();


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

    public CaseDTO getCurrentCase() {
        return currentCase.get();
    }

    public ObjectProperty<CaseDTO> currentCaseProperty() {
        return currentCase;
    }

    public void setCurrentCase(CaseDTO currentCase) {
        this.currentCase.set(currentCase);
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
