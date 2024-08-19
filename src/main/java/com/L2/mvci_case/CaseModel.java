package com.L2.mvci_case;

import com.L2.dto.CaseDTO;
import com.L2.dto.EntitlementDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CaseModel {
    private ObjectProperty<CaseDTO> currentCase = new SimpleObjectProperty<>();
    private ArrayList<EntitlementDTO> entitlements = new ArrayList<>();
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

    public ArrayList<EntitlementDTO> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(ArrayList<EntitlementDTO> entitlements) {
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
