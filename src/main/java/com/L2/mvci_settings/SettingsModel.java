package com.L2.mvci_settings;

import com.L2.dto.EntitlementDTO;
import com.L2.dto.UserDTO;
import javafx.beans.property.ObjectProperty;
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
    private ObjectProperty<TableView> entitlementsTableView = new SimpleObjectProperty<>();
    private ObjectProperty<TextField> entitlementTextField = new SimpleObjectProperty<>();
    private ObjectProperty<TextArea> includeTextArea = new SimpleObjectProperty<>();
    private ObjectProperty<TextArea> includeNotTextArea = new SimpleObjectProperty<>();
    private ObservableList<EntitlementDTO> entitlements = null;
    private ObjectProperty<UserDTO> user = new SimpleObjectProperty<>(null);

    public UserDTO getUser() {
        return user.get();
    }

    public ObjectProperty<UserDTO> userProperty() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user.set(user);
    }

    public TextField getEntitlementTextField() {
        return entitlementTextField.get();
    }

    public ObjectProperty<TextField> entitlementTextFieldProperty() {
        return entitlementTextField;
    }

    public void setEntitlementTextField(TextField entitlementTextField) {
        this.entitlementTextField.set(entitlementTextField);
    }

    public TextArea getIncludeTextArea() {
        return includeTextArea.get();
    }

    public ObjectProperty<TextArea> includeTextAreaProperty() {
        return includeTextArea;
    }

    public void setIncludeTextArea(TextArea includeTextArea) {
        this.includeTextArea.set(includeTextArea);
    }

    public TextArea getIncludeNotTextArea() {
        return includeNotTextArea.get();
    }

    public ObjectProperty<TextArea> includeNotTextAreaProperty() {
        return includeNotTextArea;
    }

    public void setIncludeNotTextArea(TextArea includeNotTextArea) {
        this.includeNotTextArea.set(includeNotTextArea);
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
