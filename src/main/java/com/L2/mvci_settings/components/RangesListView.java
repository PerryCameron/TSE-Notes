package com.L2.mvci_settings.components;

import com.L2.dto.global_spares.RangesDTO;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Builder;


public class RangesListView implements Builder<ListView<RangesDTO>> {

    private final SettingsModel settingsModel;
//    private final Consumer<SettingsMessage> action;

    public RangesListView(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
//        this.action = view.getAction();
    }

    @Override
    public ListView<RangesDTO> build() {
        ListView<RangesDTO> listView = new ListView<>(settingsModel.getRanges());

        listView.setCellFactory(param -> new ListCell<RangesDTO>() {
            @Override
            protected void updateItem(RangesDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getRange());
                }
            }
        });

        // Handle selection
        ObservableList<RangesDTO> selectedRanges = FXCollections.observableArrayList();
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Add to selected list if not already present
                if (!selectedRanges.contains(newSelection)) {
                    selectedRanges.add(newSelection);
                }
                settingsModel.selectedRangeProperty().setValue(newSelection);
                System.out.println("Selected Range: " + newSelection.getRange());
                System.out.println("Current Selected List: " + selectedRanges);
            }
        });
        return listView;
    }


}
