package com.L2.mvci_settings.menus;

import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.function.Consumer;

public class UserMenu implements Builder<Region> {

    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public UserMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
    }

    @Override
    public Region build() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().add(new Label("User Menu"));
        return vbox;
    }
}
