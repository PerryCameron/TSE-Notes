package com.L2.mvci_settings;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.dto.EntitlementFx;
import com.L2.dto.UserDTO;
import com.L2.dto.global_spares.RangesDTO;
import com.L2.dto.global_spares.RangesFx;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.nio.file.Path;

public class SettingsModel {
    // The one we wish to change
    private ObjectProperty<EntitlementFx> currentEntitlement = new SimpleObjectProperty<>();
    private ObjectProperty<Region> currentMenu = new SimpleObjectProperty<>();
    private ObjectProperty<TableView<EntitlementFx>> entitlementsTableView = new SimpleObjectProperty<>();
    private ObjectProperty<TextField> entitlementTextField = new SimpleObjectProperty<>();
    private ObjectProperty<TextArea> includeTextArea = new SimpleObjectProperty<>();
    private ObjectProperty<TextArea> includeNotTextArea = new SimpleObjectProperty<>();
    private ObservableList<EntitlementFx> entitlements = null;
    private ObjectProperty<UserDTO> user = new SimpleObjectProperty<>(null);
    private ObjectProperty<ToggleSwitch> isSpellCheck = new SimpleObjectProperty<>(new ToggleSwitch("Enable Spell Checking"));
    private StringProperty filePath = new SimpleStringProperty();
    private BooleanProperty partsDBAvailable = new SimpleBooleanProperty(false);
    private ObjectProperty<Path> droppedFile = new SimpleObjectProperty<>();
    private ObservableList<RangesFx> ranges = null;
    // text fields are bound to this object
    public ObjectProperty<RangesFx> boundRangeFxProperty = new SimpleObjectProperty<>(new RangesFx());
    private ObjectProperty<RangesFx> selectedRange = new SimpleObjectProperty<>();
    private ObjectProperty<TextArea> modelsTextArea = new SimpleObjectProperty<>();
    private ObjectProperty<TextField> typeTextField = new SimpleObjectProperty<>();

    public void updateRangeInList() {
        if(ranges != null) {
            RangesFx rangesFx = ranges.stream().filter(range -> range.getId() == selectedRange.get().getId()).findFirst().get();
            if(rangesFx != null) {
                rangesFx.copyFrom(boundRangeFxProperty.get());
            }
        }
    }
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
    public ObjectProperty<TableView<EntitlementFx>> entitlementsTableViewProperty() {
        return entitlementsTableView;
    }
    public ObjectProperty<Region> currentMenuProperty() {
        return currentMenu;
    }
    public ObjectProperty<EntitlementFx> currentEntitlementProperty() {
        return currentEntitlement;
    }
    public ObservableList<EntitlementFx> getEntitlements() {
        return entitlements;
    }
    public void setEntitlements(ObservableList<EntitlementFx> entitlements) {
        this.entitlements = entitlements;
    }
    public ObjectProperty<ToggleSwitch> isSpellCheckProperty() { return isSpellCheck; }
    public StringProperty filePathProperty() { return filePath; }
    public BooleanProperty partsDBAvailableProperty() { return partsDBAvailable; }
    public void togglePartsDbAvailable(boolean state) {
        partsDBAvailable.set(!state);
        partsDBAvailable.set(state);
    }
    public ObjectProperty<Path> droppedFileProperty() { return droppedFile; }
    public ObservableList<RangesFx> getRanges() {
        return ranges;
    }
    public void setRanges(ObservableList<RangesFx> ranges) {
        this.ranges = ranges;
    }
    public ObjectProperty<RangesFx> selectedRangeProperty() {
        return selectedRange;
    }

    public ObjectProperty<TextArea> modelsTextAreaProperty() {
        return modelsTextArea;
    }
    public ObjectProperty<TextField> typeTextFieldProperty() {
        return typeTextField;
    }
    public ObjectProperty<RangesFx> boundRangeFxProperty() {
        return boundRangeFxProperty;
    }
}
