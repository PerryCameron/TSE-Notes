package com.L2.mvci_settings.menus;

import com.L2.dto.EntitlementDTO;
import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import com.L2.widgetFx.TableColumnFx;
import com.L2.widgetFx.TableViewFx;
import javafx.scene.control.*;
import javafx.util.Builder;

import java.util.function.Consumer;


public class EntitlementsTableView implements Builder<TableView<EntitlementDTO>> {

    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public EntitlementsTableView(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
    }

    @Override
    public TableView build() {
        TableView<EntitlementDTO> tableView = TableViewFx.tableViewOf(EntitlementDTO.class);
        tableView.setItems(settingsModel.getEntitlements()); // Set the ObservableList here
        tableView.getColumns().add(col1());
        tableView.setPrefWidth(400);
        tableView.setPlaceholder(new Label(""));
        // auto selector
        TableView.TableViewSelectionModel<EntitlementDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) settingsModel.setCurrentEntitlement(newSelection);
            settingsModel.gettFEntitlement().setText(newSelection == null ? "" : newSelection.getName());
            settingsModel.gettFInclude().setText(newSelection == null ? "" : newSelection.getIncludes());
            settingsModel.gettFIncludeNot().setText(newSelection == null ? "" : newSelection.getNotIncludes());
            System.out.println(settingsModel.getCurrentEntitlement());
        });
        return tableView;
    }

    private TableColumn<EntitlementDTO, String> col1() {
        TableColumn<EntitlementDTO, String> col = TableColumnFx.stringTableColumn(EntitlementDTO::nameProperty,"Entitlements");
        col.setStyle("-fx-alignment: center");
//        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.15));
        return col;
    }
}
