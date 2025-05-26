package com.L2.mvci.settings.components;

import com.L2.mvci.settings.SettingsMessage;
import com.L2.mvci.settings.SettingsModel;
import com.L2.mvci.settings.SettingsView;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.function.Consumer;

public class TemplateMenu implements Builder<Region> {

    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public TemplateMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
    }

    @Override
    public Region build() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().add(new Label("Template Menu"));
        return vbox;
    }
}
