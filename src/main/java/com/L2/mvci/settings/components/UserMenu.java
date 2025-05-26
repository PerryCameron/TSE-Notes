package com.L2.mvci.settings.components;

import com.L2.mvci.settings.SettingsMessage;
import com.L2.mvci.settings.SettingsModel;
import com.L2.mvci.settings.SettingsView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.HeaderFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
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
        if(settingsModel.userProperty().get() == null) {
            action.accept(SettingsMessage.MAKE_REFERENCE_TO_USER);
        }
        VBox vBox = VBoxFx.of(5.0, new Insets(0,10,10,10));
        vBox.getStyleClass().add("decorative-hbox");
        TextField tf1 = new TextField();
        tf1.setText(settingsModel.userProperty().get().getFirstName());
        tf1.setPrefSize(200,20);
        tf1.setPromptText("First Name");
        tf1.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                settingsModel.userProperty().get().setFirstName(tf1.getText());
            }
        });

        TextField tf2 = new TextField();
        tf2.setText(settingsModel.userProperty().get().getLastName());
        tf2.setPromptText("Last Name");
        tf2.setPrefSize(200,20);
        tf2.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                settingsModel.userProperty().get().setLastName(tf2.getText());
            }
        });

        TextField ef3 = new TextField();
        ef3.setText(settingsModel.userProperty().get().getEmail());
        ef3.setPromptText("Email");
        ef3.setPrefSize(200,20);
        ef3.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                settingsModel.userProperty().get().setEmail(ef3.getText());
            }
        });

        TextField tf3 = new TextField();
        tf3.setText(settingsModel.userProperty().get().getSesa());
        tf3.setPromptText("SESA #");
        tf3.setPrefSize(200,20);
        tf3.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                settingsModel.userProperty().get().setSesa(tf3.getText());
            }
        });

        TextField tf4 = new TextField();
        tf4.setText(settingsModel.userProperty().get().getProfileLink());
        tf4.setPromptText("URL");
        tf4.setPrefSize(200,20);
        tf4.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                settingsModel.userProperty().get().setProfileLink(tf4.getText());
            }
        });

        Button saveButton = ButtonFx.utilityButton( () -> action.accept(SettingsMessage.SAVE_USER), "Save", "/images/save-16.png");
        vBox.getChildren().addAll(HeaderFx.withTitle("User Information"), tf1, tf2, ef3, tf3, tf4, saveButton);
        return vBox;
    }
}
