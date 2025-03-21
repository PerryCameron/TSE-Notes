package com.L2.mvci_settings.components;

import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.scene.layout.Region;
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

        // so I have this stubbed out code,
        return null;
    }

}
