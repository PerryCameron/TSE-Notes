package com.L2.mvci_settings;

import com.L2.widgetFx.VBoxFx;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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
        borderPane.setCenter(setCenter());
        return borderPane;
    }

    private Node setCenter() {
        VBox vBox = VBoxFx.of(1024,768,true, true);
        vBox.setPadding(new Insets(10,0,0,10));
        vBox.setSpacing(10);
        vBox.getChildren().add(setEntitlements());
        return vBox;
    }

    private Node setEntitlements() {
        VBox vBox = new VBox();
        Label entitlementName = new Label("Entitlement Name");
        TextField tf = new TextField();


        return vBox;
    }


}
