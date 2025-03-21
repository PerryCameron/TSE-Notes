package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.dto.UserDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class SettingsModel {
    // The one we wish to change
    private ObjectProperty<EntitlementDTO> currentEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<Region> currentMenu = new SimpleObjectProperty<>();
    private ObjectProperty<TableView<EntitlementDTO>> entitlementsTableView = new SimpleObjectProperty<>();
    private ObjectProperty<TextField> entitlementTextField = new SimpleObjectProperty<>();
    private ObjectProperty<TextArea> includeTextArea = new SimpleObjectProperty<>();
    private ObjectProperty<TextArea> includeNotTextArea = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = null;
    private ObjectProperty<UserDTO> user = new SimpleObjectProperty<>(null);
    private BooleanProperty isSpellChecked;



    public ObjectProperty<UserDTO> userProperty() {
        return user;
    }
    public ObjectProperty<TextField> entitlementTextFieldProperty() {
        return entitlementTextField;
    }
    public ObjectProperty<TextArea> includeTextAreaProperty() {
        return includeTextArea;
    }
    public ObjectProperty<TextArea> includeNotTextAreaProperty() {
        return includeNotTextArea;
    }
    public ObjectProperty<TableView<EntitlementDTO>> entitlementsTableViewProperty() {
        return entitlementsTableView;
    }
    public ObjectProperty<Region> currentMenuProperty() {
        return currentMenu;
    }
    public ObjectProperty<EntitlementDTO> currentEntitlementProperty() {
        return currentEntitlement;
    }
    public ObservableList<EntitlementDTO> getEntitlements() {
        return entitlements;
    }
    public void setEntitlements(ObservableList<EntitlementDTO> entitlements) {
        this.entitlements = entitlements;
    }
}
