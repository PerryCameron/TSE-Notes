package com.L2.mvci_settings.menus;

import atlantafx.base.layout.InputGroup;
import com.L2.dto.EntitlementDTO;
import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
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
//        HBox hBox = new HBox(10);
        InputGroup inputGroup = new InputGroup();
        inputGroup.setSpacing(10);
        inputGroup.getChildren().addAll(new EntitlementsTableView(view).build(), CreateEditFields());
        return inputGroup;
    }

    private Node CreateEditFields() {
        VBox vBox = new VBox(5);
        vBox.setPrefWidth(500);
        TextField tf1 = new TextField();
        settingsModel.settFEntitlement(tf1);
        tf1.setPromptText("Entitlement Name");
        TextArea tf2 = new TextArea();
        settingsModel.settFInclude(tf2);
        TextArea tf3 = new TextArea();
        tf3.setPromptText("Does Not Include");
        settingsModel.settFIncludeNot(tf3);
        vBox.getChildren().addAll(new Label("Plan"), tf1, new Label("Includes"), tf2, new Label("Does not include"), tf3, createButtonRow());
        return vBox;
    }

    private Node createButtonRow() {
        HBox hBox = new HBox(5);
        Button btn1 = new Button("Save");
        btn1.getStyleClass().add("success");
        btn1.setOnAction(event -> action.accept(SettingsMessage.SAVE_ENTITLEMENTS));
        Button btn2 = new Button("Delete");
        btn2.getStyleClass().add("danger");
        btn2.setOnAction(event -> action.accept(SettingsMessage.DELETE_ENTITLEMENT));
        Button btn3 = new Button("New");
        btn3.getStyleClass().add("accent");
        btn3.setOnAction(event -> action.accept(SettingsMessage.NEW_ENTITLEMENT));
        Button btn4 = new Button("Print Entitlements");
        btn4.setOnAction(event -> action.accept(SettingsMessage.PRINT_ENTITLEMENTS));
        hBox.getChildren().addAll(btn1, btn2, btn3, btn4);
        return hBox;
    }
}
