package com.L2.mvci_settings;

import com.L2.mvci_settings.components.EntitlementsMenu;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.util.function.Consumer;

public class SettingsView implements Builder<Region> {
    private final SettingsModel settingsModel;
    Consumer<SettingsMessage> action;

    public SettingsView(SettingsModel settingsModel, Consumer<SettingsMessage> m) {
        this.settingsModel = settingsModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(setMenu());
        borderPane.setCenter(setCenter());
        return borderPane;
    }

    private Node setMenu() {
        VBox vBox = VBoxFx.of(150.0, 10.0, new Insets(30, 0, 0, 10));
        Button userButton = ButtonFx.utilityButton(() -> action.accept(SettingsMessage.SHOW_USER), "User", "/images/person-16.png");
        Button entitlementsButton = ButtonFx.utilityButton(() -> action.accept(SettingsMessage.SHOW_ENTITLEMENTS), "Entitlements", "/images/help-16.png");
        vBox.getChildren().addAll(userButton, entitlementsButton);
        return vBox;
    }

    private Node setCenter() {
        VBox vBox = new VBox();
        vBox.setPrefSize(1024, 768);
        vBox.setPadding(new Insets(10, 20, 0, 10));
        vBox.setSpacing(10);
        settingsModel.currentMenuProperty().addListener((obs, oldMenu, newMenu) -> {
            vBox.getChildren().clear(); // Clear old content
            if (newMenu != null) {
                vBox.getChildren().add(newMenu); // Add the new menu
            }
        });
        // default menu
        settingsModel.setCurrentMenu(new EntitlementsMenu(this).build());
        return vBox;
    }

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    public Consumer<SettingsMessage> getAction() {
        return action;
    }
}
