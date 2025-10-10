package com.L2.mvci.settings.components;

import com.L2.dto.EntitlementFx;
import com.L2.mvci.settings.SettingsMessage;
import com.L2.mvci.settings.SettingsModel;
import com.L2.mvci.settings.SettingsView;
import com.L2.static_tools.ImageResources;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.HeaderFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Insets;
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
            settingsModel.currentEntitlementProperty().set(new EntitlementFx());
        } else settingsModel.currentEntitlementProperty().set(new EntitlementFx(settingsModel.getEntitlements().getFirst()));
    }

    @Override
    public Region build() {
        VBox root = VBoxFx.of(new Insets(0, 10, 10, 10));
        root.getStyleClass().add("decorative-hbox");
        HBox inputGroup = new HBox(); // extends hbox
        inputGroup.setSpacing(10);
        inputGroup.getChildren().addAll(new EntitlementsTableView(view).build(), CreateEditFields());
        root.getChildren().addAll(HeaderFx.withTitle("Entitlements"), inputGroup);
        return root;
    }

    private Node CreateEditFields() {
        VBox vBox = new VBox(5);
        vBox.setPrefWidth(500);
        TextField tf1 = new TextField();
        settingsModel.entitlementTextFieldProperty().set(tf1);
        tf1.setPromptText("Entitlement Name");
        TextArea tf2 = new TextArea();
        settingsModel.includeTextAreaProperty().set(tf2);
        TextArea tf3 = new TextArea();
        tf3.setPromptText("Does Not Include");
        settingsModel.includeNotTextAreaProperty().set(tf3);
        vBox.getChildren().addAll(new Label("Plan"), tf1, new Label("Includes"), tf2, new Label("Does not include"), tf3, createButtonRow());
        return vBox;
    }

    private Node createButtonRow() {
        HBox hBox = new HBox(5);
        Button saveButton = ButtonFx.utilityButton( () -> {
            action.accept(SettingsMessage.SAVE_ENTITLEMENTS);
            action.accept(SettingsMessage.REFRESH_ENTITLEMENT_COMBO_BOX);
        }, ImageResources.SAVE, "Save");
        Button deleteButton = ButtonFx.utilityButton( () -> action.accept(SettingsMessage.DELETE_ENTITLEMENT), ImageResources.DELETE, "Delete");
        Button newButton = ButtonFx.utilityButton( () -> action.accept(SettingsMessage.NEW_ENTITLEMENT), ImageResources.NEW, "New Entitlement");
        hBox.getChildren().addAll(saveButton, deleteButton, newButton);
        return hBox;
    }
}
