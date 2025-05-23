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
        // I have two objects, RangeDTO which is a POJO and RangeFx with is the same as Range but a JavaFX property object
        // both objects have copy functions ie..POJO.copyFrom(FX) and FX.copyFrom(POJO) for easy conversion
        // this is a list of RangeDTO POJOS
        ObservableList<RangesDTO> selectedRanges = FXCollections.observableArrayList();
        // when I make a new selection I want to save the changes from oldSelection to proper object in list and copy the new seletion to the bound FX object
        // the bound object is ObjectProperty<RangeFx>, it is bidirectionally bound to a textField and a textArea
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Add to selectedRanges if not already present
                if (!selectedRanges.contains(newSelection)) {
                    selectedRanges.add(newSelection);
                }
                // Update the selectedRangeProperty to track the current selection
                settingsModel.selectedRangeProperty().set(newSelection);
                if (oldSelection != null) {
                    // Save changes from the bound RangesFx to the old RangesDTO in rangesList
                    int oldIndex = selectedRanges.indexOf(oldSelection);
                    if (oldIndex != -1) {
                        selectedRanges.get(oldIndex).copyFx(settingsModel.boundRangeFxProperty().get());
                    }
                }
                // Copy the new selection to the bound RangesFx object
                settingsModel.boundRangeFxProperty().get().copyFrom(newSelection);
                System.out.println("Selected Range: " + newSelection);
                System.out.println("Old Selection: " + oldSelection);
            }
        });
        return listView;
    }

    private String colonToNewline(String rangeAdditional) {
        if (rangeAdditional == null) {
            return "";
        }
        String[] parts = rangeAdditional.split(":");
        return String.join("\n", parts);
    }

}
