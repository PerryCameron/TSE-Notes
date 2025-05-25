package com.L2.mvci_settings.components;

import com.L2.dto.global_spares.RangesFx;
import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.scene.control.*;
import javafx.util.Builder;

import java.util.function.Consumer;


public class RangesListView implements Builder<ListView<RangesFx>> {

    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public RangesListView(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
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
                // copy the values from newSelection to the bound object
                settingsModel.boundRangeFxProperty().get().copyFromSelectedRange(newSelection);
                // copy the reference from newSelection to selection object
                settingsModel.selectedRangeProperty().set(newSelection);
                action.accept(SettingsMessage.UPDATE_NUMBER_OF_SPARES);
            }
        });

        return listView;
    }
}
