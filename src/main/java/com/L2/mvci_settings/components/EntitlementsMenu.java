package com.L2.mvci_settings.components;

import atlantafx.base.layout.InputGroup;
import com.L2.dto.EntitlementDTO;
import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import com.L2.widgetFx.ButtonFx;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class EntitlementsMenu implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(EntitlementsMenu.class);
    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;
    private final SettingsView view;

    public EntitlementsMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
        this.view = view;
        setCurrentEntitlement();
    }

    private void setCurrentEntitlement() {
        // start with first if they already exist, otherwise start with new one
        if (settingsModel.getEntitlements().isEmpty()) {
            logger.info("No entitlements exist, creating a new one");
            settingsModel.setCurrentEntitlement(new EntitlementDTO());
        } else settingsModel.setCurrentEntitlement(new EntitlementDTO(settingsModel.getEntitlements().get(0)));
    }

    @Override
    public Region build() {
        InputGroup inputGroup = new InputGroup(); // extends hbox
        inputGroup.setSpacing(10);
        inputGroup.getChildren().addAll(new EntitlementsTableView(view).build(), CreateEditFields());
        return inputGroup;
    }

    private Node CreateEditFields() {
        VBox vBox = new VBox(5);
        vBox.setPrefWidth(500);
        TextField tf1 = new TextField();
        settingsModel.setEntitlementTextField(tf1);
        tf1.setPromptText("Entitlement Name");
        TextArea tf2 = new TextArea();
        settingsModel.setIncludeTextArea(tf2);
        TextArea tf3 = new TextArea();
        tf3.setPromptText("Does Not Include");
        settingsModel.setIncludeNotTextArea(tf3);
        vBox.getChildren().addAll(new Label("Plan"), tf1, new Label("Includes"), tf2, new Label("Does not include"), tf3, createButtonRow());
        return vBox;
    }

    private Node createButtonRow() {
        HBox hBox = new HBox(5);
        Button saveButton = ButtonFx.utilityButton( () -> action.accept(SettingsMessage.SAVE_ENTITLEMENTS), "Save", "/images/save-16.png");
        Button deleteButton = ButtonFx.utilityButton( () -> action.accept(SettingsMessage.DELETE_ENTITLEMENT), "Delete", "/images/delete-16.png");
        Button newButton = ButtonFx.utilityButton( () -> action.accept(SettingsMessage.NEW_ENTITLEMENT), "New Entitlement", "/images/create-16.png");
        hBox.getChildren().addAll(saveButton, deleteButton, newButton);
        return hBox;
    }
}
