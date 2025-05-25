package com.L2.mvci_settings.components;

import com.L2.dto.global_spares.RangesFx;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Builder;


public class RangesListView implements Builder<ListView<RangesFx>> {

    private final SettingsModel settingsModel;
//    private final Consumer<SettingsMessage> action;

    public RangesListView(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
//        this.action = view.getAction();
    }

    @Override
    public ListView<RangesFx> build() {
        ListView<RangesFx> listView = new ListView<>(settingsModel.getRanges());
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(RangesFx item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getRange());
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Update the selected range
//                settingsModel.selectedRangeProperty().set(newSelection);
//                // Update the bound range, which is used for bindings
                System.out.println("Selected from Ranges: " + newSelection);
                settingsModel.boundRangeFxProperty().get().copyFrom(newSelection);

            } else {
                // Clear selection if no item is selected
//                settingsModel.selectedRangeProperty().set(null);
//                settingsModel.boundRangeFxProperty().set(null);
            }
        });

        return listView;
    }
}
