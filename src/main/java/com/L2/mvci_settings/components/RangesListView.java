package com.L2.mvci_settings.components;

import com.L2.dto.global_spares.RangesDTO;
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
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getRange());
                }
            }
        });
        // list of all our rangers
        ObservableList<RangesFx> selectedRanges = FXCollections.observableArrayList();
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (!selectedRanges.contains(newSelection)) {
                    selectedRanges.add(newSelection);
                }
                settingsModel.selectedRangeProperty().set(newSelection);
                RangesFx boundRangeFx = settingsModel.boundRangeFxProperty().get();
                boundRangeFx.copyFrom(newSelection);
                if (oldSelection != null && oldSelection != newSelection) {
                    int oldIndex = selectedRanges.indexOf(oldSelection);
                    if (oldIndex != -1) {
                        RangesFx updatedOldSelection = new RangesFx();
                        updatedOldSelection.copyFrom(boundRangeFx);
                        selectedRanges.set(oldIndex, updatedOldSelection);
                    }
                }
            }
        });
        return listView;
    }
}
