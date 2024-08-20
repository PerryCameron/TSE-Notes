package com.L2.mvci_settings.menus;

import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.function.Consumer;

public class EntitlementsMenu implements Builder<Region> {


    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public EntitlementsMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
    }

    @Override
    public Region build() {
            VBox vBox = new VBox(5);
            TextField tf1 = new TextField();
            tf1.setPromptText("Entitlement Name");
            tf1.textProperty().bindBidirectional(settingsModel.getCurrentEntitlement().nameProperty());
            TextField tf2 = new TextField();
            tf2.setPromptText("Includes");
            tf2.textProperty().bindBidirectional(settingsModel.getCurrentEntitlement().includesProperty());
            TextField tf3 = new TextField();
            tf3.setPromptText("Does Not Include");
            tf3.textProperty().bindBidirectional(settingsModel.getCurrentEntitlement().notIncludesProperty());
            Button btn1 = new Button("Save");
            btn1.setOnAction(event -> action.accept(SettingsMessage.SAVE_ENTITLEMENTS));
            Button btn2 = new Button("Print all entitlements");
            btn2.setOnAction(event -> action.accept(SettingsMessage.PRINT_ENTITLEMENTS));
            vBox.getChildren().addAll(tf1, tf2, tf3, btn1, btn2);
        return vBox;
    }
}
