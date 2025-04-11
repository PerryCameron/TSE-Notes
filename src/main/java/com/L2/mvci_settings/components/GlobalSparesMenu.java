package com.L2.mvci_settings.components;

import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class GlobalSparesMenu implements Builder<Region> {

    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public GlobalSparesMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
    }


    @Override
    public Region build() {
        return null;
    }
}
