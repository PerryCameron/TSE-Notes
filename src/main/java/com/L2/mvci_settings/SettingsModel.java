package com.L2.mvci_settings;

import com.L2.dto.CaseDTO;
import com.L2.dto.EntitlementDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SettingsModel {
    private ObjectProperty<EntitlementDTO> entitlements = new SimpleObjectProperty<>();

    public EntitlementDTO getEntitlements() {
        return entitlements.get();
    }

    public ObjectProperty<EntitlementDTO> entitlementsProperty() {
        return entitlements;
    }

    public void setEntitlements(EntitlementDTO entitlements) {
        this.entitlements.set(entitlements);
    }
}
