package com.L2.mvci.parts.components;

import com.L2.mvci.parts.PartMessage;
import com.L2.mvci.parts.PartModel;
import com.L2.mvci.parts.PartView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.HBoxFx;
import com.L2.widgetFx.VBoxFx;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.function.Consumer;

public class PartKeyWords implements Builder<Pane> {

    private final PartModel partModel;
    private final Consumer<PartMessage> action;
    private Button saveButton;
    private Button cancelButton;
    private Button modifyButton;

    public PartKeyWords(PartView partView) {
        this.partModel = partView.getPartModel();
        this.action = partView.getAction();
    }

    @Override
    public Pane build() {
        HBox hBox = HBoxFx.of(200, 10);
        VBox vBox = VBoxFx.of(10.0, Pos.TOP_LEFT, 150.0);
        vBox.setPrefWidth(200.0);

        String keywords = (partModel != null && partModel.selectedSpareProperty() != null
                && partModel.selectedSpareProperty().get() != null)
                ? partModel.selectedSpareProperty().get().getKeywords() : "";
        partModel.partKeyWordsProperty().set(new TextArea(keywords != null ? keywords : ""));
//        partModel.partKeyWordsProperty().get().setPrefWidth(500);
        partModel.partKeyWordsProperty().get().setEditable(false);
        this.saveButton = ButtonFx.utilityButton("/images/save-16.png", "Save", 150);
        this.modifyButton = ButtonFx.utilityButton("/images/modify-16.png", "Edit", 150);
        this.cancelButton = ButtonFx.utilityButton("/images/cancel-16.png", "Cancel", 150);

        saveButton.setOnAction(button -> {
            partModel.selectedSpareProperty().get().setKeywords(partModel.partKeyWordsProperty().get().getText());
            action.accept(PartMessage.SAVE_PART_KEYWORDS);
        });

        modifyButton.setOnAction(button -> showSaveAndCancelButtons());

        cancelButton.setOnAction(button -> {
            action.accept(PartMessage.CANCEL_KEYWORD_UPDATE);
            partModel.partKeyWordsProperty().get().setText(partModel.selectedSpareProperty().get().getKeywords());
        });

        // Initially show only the modify button
        showEditButton();
        partModel.getUpdatedKeywordsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue) {
                    showEditButton();
                }
                partModel.getUpdatedKeywordsProperty().set(false);
            }
        });
        vBox.getChildren().addAll(modifyButton, saveButton, cancelButton);
        hBox.getChildren().addAll(partModel.partKeyWordsProperty().get(), vBox);
        return hBox;
    }

    private void showSaveAndCancelButtons() {
        partModel.partKeyWordsProperty().get().setEditable(true);
        ButtonFx.buttonVisible(saveButton, true);
        ButtonFx.buttonVisible(cancelButton, true);
        ButtonFx.buttonVisible(modifyButton, false);
    }

    private void showEditButton() {
        partModel.partKeyWordsProperty().get().setEditable(false);
        ButtonFx.buttonVisible(saveButton, false);
        ButtonFx.buttonVisible(cancelButton, false);
        ButtonFx.buttonVisible(modifyButton, true);
    }
}
