package com.L2.mvci_settings.components;

import com.L2.dto.EntitlementDTO;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import javafx.scene.control.*;
import javafx.util.Builder;


public class EntitlementsTableView implements Builder<TableView<EntitlementDTO>> {

    private final SettingsModel settingsModel;
//    private final Consumer<SettingsMessage> action;

    public EntitlementsTableView(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
//        this.action = view.getAction();
    }

    @Override
    public TableView<EntitlementDTO> build() {
        TableView<EntitlementDTO> tableView = TableViewFx.of(EntitlementDTO.class);
        settingsModel.entitlementsTableViewProperty().set(tableView);
        tableView.setItems(settingsModel.getEntitlements()); // Set the ObservableList here
        tableView.getColumns().add(col1());
        tableView.setPlaceholder(new Label(""));
        // auto selector
        TableView.TableViewSelectionModel<EntitlementDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) settingsModel.currentEntitlementProperty().set(newSelection);
            settingsModel.entitlementTextFieldProperty().get().setText(newSelection == null ? "" : newSelection.getName());
            settingsModel.includeTextAreaProperty().get().setText(newSelection == null ? "" : newSelection.getIncludes());
            settingsModel.includeNotTextAreaProperty().get().setText(newSelection == null ? "" : newSelection.getNotIncludes());
        });
        return tableView;
    }

    private TableColumn<EntitlementDTO, String> col1() {
        TableColumn<EntitlementDTO, String> col = TableColumnFx.stringTableColumn(EntitlementDTO::nameProperty,"Entitlements");
        col.setStyle("-fx-alignment: center");
        return col;
    }
}
