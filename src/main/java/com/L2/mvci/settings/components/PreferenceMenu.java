package com.L2.mvci.settings.components;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.mvci.settings.SettingsMessage;
import com.L2.mvci.settings.SettingsModel;
import com.L2.mvci.settings.SettingsView;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.function.Consumer;

public class PreferenceMenu implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(PreferenceMenu.class);
    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;
    private final SettingsView view;

    public PreferenceMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
        this.view = view;
    }

    @Override
    public Region build() {
        VBox vbox = new VBox();
        vbox.getStyleClass().add("decorative-hbox");
        vbox.setPadding(new Insets(10, 10, 10, 10));
        ObjectProperty<ToggleSwitch> toggleSwitch = settingsModel.isSpellCheckProperty();
        settingsModel.isSpellCheckProperty().get().selectedProperty().addListener((obs, oldVal, newVal) -> {
            logger.debug("ToggleSwitch changed from {} to {}", oldVal, newVal);
            view.getAction().accept(SettingsMessage.TOGGLE_SPELLCHECK);
        });
        vbox.getChildren().add(toggleSwitch.get());
        return vbox;
    }

}
