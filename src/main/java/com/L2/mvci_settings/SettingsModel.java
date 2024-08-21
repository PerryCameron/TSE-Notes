package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.util.ArrayList;

public class SettingsModel {
    // The one we wish to change
    private ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<Region> currentMenu = new SimpleObjectProperty<>();
    private ObjectProperty<TableView> entitlementsTableView = new SimpleObjectProperty<>();
    private ObjectProperty<TextField> tFEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<TextField> tFInclude = new SimpleObjectProperty<>();
    private ObjectProperty<TextField> tFIncludeNot = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = null;


    public TextField gettFEntitlement() {
        return tFEntitlement.get();
    }

    public ObjectProperty<TextField> tFEntitlementProperty() {
        return tFEntitlement;
    }

    public void settFEntitlement(TextField tFEntitlement) {
        this.tFEntitlement.set(tFEntitlement);
    }

    public TextField gettFInclude() {
        return tFInclude.get();
    }

    public ObjectProperty<TextField> tFIncludeProperty() {
        return tFInclude;
    }

    public void settFInclude(TextField tFInclude) {
        this.tFInclude.set(tFInclude);
    }

    public TextField gettFIncludeNot() {
        return tFIncludeNot.get();
    }

    public ObjectProperty<TextField> tFIncludeNotProperty() {
        return tFIncludeNot;
    }

    public void settFIncludeNot(TextField tFIncludeNot) {
        this.tFIncludeNot.set(tFIncludeNot);
    }

    public TableView<EntitlementDTO> getEntitlementsTableView() {
        return entitlementsTableView.get();
    }

    public ObjectProperty<TableView> entitlementsTableViewProperty() {
        return entitlementsTableView;
    }

    public void setEntitlementsTableView(TableView entitlementsTableView) {
        this.entitlementsTableView.set(entitlementsTableView);
    }

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

    public ObservableList<EntitlementDTO> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(ObservableList<EntitlementDTO> entitlements) {
        this.entitlements = entitlements;
    }
}
