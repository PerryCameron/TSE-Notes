package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;

import java.util.ArrayList;

public class SettingsModel {
    private ArrayList<EntitlementDTO> entitlements = new ArrayList<>();
    private ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<Region> currentMenu = new SimpleObjectProperty<>();






    public Region getCurrentMenu() {
        return currentMenu.get();
    }

    public ObjectProperty<Region> currentMenuProperty() {
        return currentMenu;
    }

    public void setCurrentMenu(Region currentMenu) {
        this.currentMenu.set(currentMenu);
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

    public ArrayList<EntitlementDTO> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(ArrayList<EntitlementDTO> entitlements) {
        this.entitlements = entitlements;
    }
}
