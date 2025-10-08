package com.L2.mvci.settings.components;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.BaseApplication;
import com.L2.mvci.settings.SettingsMessage;
import com.L2.mvci.settings.SettingsModel;
import com.L2.mvci.settings.SettingsView;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.ThemeChanger;
import com.L2.widgetFx.HBoxFx;
import com.L2.widgetFx.VBoxFx;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
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
        VBox vbox = VBoxFx.of(false, 10, Pos.CENTER_LEFT);
        vbox.getStyleClass().add("decorative-hbox");
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(spellCheckOption(), themeSelection());
        return vbox;
    }

    private Node themeSelection() {
        HBox hbox = HBoxFx.of(10, Pos.CENTER_LEFT);
        List<String> themes = AppFileTools.getCssFileNames();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(themes);
        comboBox.getSelectionModel().select(BaseApplication.theme);
        // Add listener to update BaseApplication.theme and apply the selected theme
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                BaseApplication.theme = newValue; // Update the theme field
                ThemeChanger.applyTheme();
                action.accept(SettingsMessage.PERSIST_THEME);
                // Persist the theme to the database
                //setTheme(newValue);
            }
        });
        hbox.getChildren().addAll(new Label("Theme:"), comboBox);
        return hbox;
    }

    private Control spellCheckOption() {
        ObjectProperty<ToggleSwitch> toggleSwitch = settingsModel.isSpellCheckProperty();
        settingsModel.isSpellCheckProperty().get().selectedProperty().addListener((obs, oldVal, newVal) -> {
            logger.debug("ToggleSwitch changed from {} to {}", oldVal, newVal);
            view.getAction().accept(SettingsMessage.TOGGLE_SPELLCHECK);
        });
        return toggleSwitch.get();
    }

}
