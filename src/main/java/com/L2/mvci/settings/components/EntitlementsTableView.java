package com.L2.mvci.settings.components;

import com.L2.dto.EntitlementFx;
import com.L2.mvci.settings.SettingsModel;
import com.L2.mvci.settings.SettingsView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import javafx.scene.control.*;
import javafx.util.Builder;


public class EntitlementsTableView implements Builder<TableView<EntitlementFx>> {

    private final SettingsModel settingsModel;
//    private final Consumer<SettingsMessage> action;

    public EntitlementsTableView(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
//        this.action = view.getAction();
    }

    @Override
    public TableView<EntitlementFx> build() {
        TableView<EntitlementFx> tableView = TableViewFx.of(EntitlementFx.class);
        settingsModel.entitlementsTableViewProperty().set(tableView);
        tableView.setItems(settingsModel.getEntitlements()); // Set the ObservableList here
        tableView.getColumns().add(col1());
        tableView.setPlaceholder(new Label(""));
        // auto selector
        TableView.TableViewSelectionModel<EntitlementFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) settingsModel.currentEntitlementProperty().set(newSelection);
            settingsModel.entitlementTextFieldProperty().get().setText(newSelection == null ? "" : newSelection.getName());
            settingsModel.includeTextAreaProperty().get().setText(newSelection == null ? "" : newSelection.getIncludes());
            settingsModel.includeNotTextAreaProperty().get().setText(newSelection == null ? "" : newSelection.getNotIncludes());
        });
        return tableView;
    }

    private TableColumn<EntitlementFx, String> col1() {
        TableColumn<EntitlementFx, String> col = TableColumnFx.stringTableColumn(EntitlementFx::nameProperty,"Entitlements");
        col.setStyle("-fx-alignment: center");
        return col;
    }
}
