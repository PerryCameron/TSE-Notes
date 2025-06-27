package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components;

import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderMessage;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderModel;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderView;
import com.L2.static_tools.ImageResources;
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

public class PartNote implements Builder<Pane> {

    private final PartFinderModel partModel;
    private final Consumer<PartFinderMessage> action;
    private Button saveButton;
    private Button cancelButton;
    private Button modifyButton;

    public PartNote(PartFinderView partView) {
        this.partModel = partView.getPartFinderModel();
        this.action = partView.getAction();
    }

    @Override
    public Pane build() {
        HBox hBox = HBoxFx.of(200, 10);
        VBox vBox = VBoxFx.of(10.0, Pos.TOP_LEFT, 150.0);
        vBox.setPrefWidth(200.0);
        String comments = (partModel != null && partModel.selectedSpareProperty() != null
                && partModel.selectedSpareProperty().get() != null)
                ? partModel.selectedSpareProperty().get().getComments() : "";
        partModel.partNoteProperty().set(new TextArea(comments != null ? comments : ""));
        partModel.partNoteProperty().get().setEditable(false);
        partModel.partNoteProperty().get().setWrapText(true);
        this.saveButton = ButtonFx.utilityButton(ImageResources.SAVE, "Save", 150);
        this.modifyButton = ButtonFx.utilityButton(ImageResources.EDIT, "Edit", 150);
        this.cancelButton = ButtonFx.utilityButton(ImageResources.CANCEL, "Cancel", 150);

        saveButton.setOnAction(button -> {
            partModel.selectedSpareProperty().get().setComments(partModel.partNoteProperty().get().getText());
            action.accept(PartFinderMessage.SAVE_PART_NOTE);
        });

        modifyButton.setOnAction(button -> showSaveAndCancelButtons());
        cancelButton.setOnAction(button -> {
            action.accept(PartFinderMessage.CANCEL_NOTE_UPDATE);
            partModel.partNoteProperty().get().setText(partModel.selectedSpareProperty().get().getComments());
        });

        // Initially show only the modify button
        showEditButton();
        partModel.updatedNotesProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue) {
                    showEditButton();
                }
                partModel.updatedNotesProperty().set(false);
            }
        });
        vBox.getChildren().addAll(modifyButton, saveButton, cancelButton);
        hBox.getChildren().addAll(partModel.partNoteProperty().get(), vBox);
        return hBox;
    }

    private void showSaveAndCancelButtons() {
        partModel.partNoteProperty().get().setEditable(true);
        ButtonFx.buttonVisible(saveButton, true);
        ButtonFx.buttonVisible(cancelButton, true);
        ButtonFx.buttonVisible(modifyButton, false);
    }

    private void showEditButton() {
        partModel.partNoteProperty().get().setEditable(false);
        ButtonFx.buttonVisible(saveButton, false);
        ButtonFx.buttonVisible(cancelButton, false);
        ButtonFx.buttonVisible(modifyButton, true);
    }
}
