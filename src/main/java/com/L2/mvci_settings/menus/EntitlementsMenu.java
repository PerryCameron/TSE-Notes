package com.L2.mvci_settings.menus;

import com.L2.dto.EntitlementDTO;
import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
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
        VBox vBox = new VBox(5);
        TableView<EntitlementDTO> tableView = new EntitlementsTableView(view).build();
        TextField tf1 = new TextField();
        tf1.setPromptText("Entitlement Name");
        tf1.textProperty().bindBidirectional(settingsModel.getCurrentEntitlement().nameProperty());
        TextField tf2 = new TextField();
        tf2.setPromptText("Includes");
        tf2.textProperty().bindBidirectional(settingsModel.getCurrentEntitlement().includesProperty());
        TextField tf3 = new TextField();
        tf3.setPromptText("Does Not Include");
        tf3.textProperty().bindBidirectional(settingsModel.getCurrentEntitlement().notIncludesProperty());
        vBox.getChildren().addAll(tableView,tf1, tf2, tf3, createButtonRow());
        return vBox;
    }

    private Node createButtonRow() {
        HBox hBox = new HBox(5);
        Button btn1 = new Button("Save");
        btn1.setOnAction(event -> action.accept(SettingsMessage.SAVE_ENTITLEMENTS));
        Button btn2 = new Button("<");
        btn2.setOnAction(event -> action.accept(SettingsMessage.SELECT_PREVIOUS));
        Button btn3 = new Button(">");
        btn3.setOnAction(event -> action.accept(SettingsMessage.SELECT_AFTER));
        Button btn4 = new Button("Delete");
        btn3.setOnAction(event -> action.accept(SettingsMessage.DELETE_ENTITLEMENT));
        Button btn5 = new Button("New");
        btn5.setOnAction(event -> action.accept(SettingsMessage.NEW_ENTITLEMENT));
        Button btn6 = new Button("Print Entitlements");
        btn6.setOnAction(event -> action.accept(SettingsMessage.PRINT_ENTITLEMENTS));
        hBox.getChildren().addAll(btn1, btn2, btn3, btn4, btn5, btn6);
        return hBox;
    }


}
