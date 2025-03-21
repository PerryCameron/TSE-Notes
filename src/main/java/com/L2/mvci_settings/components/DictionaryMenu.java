package com.L2.mvci_settings.components;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.function.Consumer;

public class DictionaryMenu implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(DictionaryMenu.class);
    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;
    private final SettingsView view;

    public DictionaryMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
        this.view = view;
    }

    @Override
    public Region build() {
        VBox vbox = new VBox();
        ObjectProperty<ToggleSwitch> toggleSwitch = settingsModel.isSpellCheckProperty();
        vbox.getChildren().add(toggleSwitch.get());
        return vbox;
    }

}
