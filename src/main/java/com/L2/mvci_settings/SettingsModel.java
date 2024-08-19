package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;

public class SettingsModel {
    private ArrayList<EntitlementDTO> entitlements = new ArrayList<>();
    private ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();

    public EntitlementDTO getCurrentEntitlement() {
        return currentEntitlement.get();
    }

    public ObjectProperty<EntitlementDTO> currentEntitlementProperty() {
        return currentEntitlement;
    }

    public void setCurrentEntitlement(EntitlementDTO currentEntitlement) {
        this.currentEntitlement.set(currentEntitlement);
    }

    public ArrayList<EntitlementDTO> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(ArrayList<EntitlementDTO> entitlements) {
        this.entitlements = entitlements;
    }
}
