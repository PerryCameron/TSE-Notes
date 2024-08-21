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

public class UserMenu implements Builder<Region> {

    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public UserMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
    }

    @Override
    public Region build() {
        VBox vBox = new VBox(5);
        TextField tf1 = new TextField();
        tf1.setPrefSize(200,20);
        tf1.setPromptText("First Name");
//        tf1.textProperty().bindBidirectional(settingsModel.getCurrentEntitlement().nameProperty());
        TextField tf2 = new TextField();
        tf2.setPromptText("Last Name");
        tf2.setPrefSize(200,20);
//        tf2.textProperty().bindBidirectional(settingsModel.getCurrentEntitlement().includesProperty());
        Button btn1 = new Button("Save");
        btn1.setOnAction(event -> action.accept(SettingsMessage.SAVE_USER));
        vBox.getChildren().addAll(tf1, tf2, btn1);
        return vBox;
    }
}
