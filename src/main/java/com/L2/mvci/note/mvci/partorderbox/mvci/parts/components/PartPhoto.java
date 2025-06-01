package com.L2.mvci.note.mvci.partorderbox.mvci.parts.components;

import com.L2.mvci.note.mvci.partorderbox.mvci.parts.PartMessage;
import com.L2.mvci.note.mvci.partorderbox.mvci.parts.PartView;
import com.L2.widgetFx.HBoxFx;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Builder;

public class PartPhoto implements Builder<Pane> {

    private final PartView partView;
    public PartPhoto(PartView partView) {
        this.partView = partView;
    }

    @Override
    public Pane build() {
        // Create HBox for layout
        HBox hBox = HBoxFx.of(200, 10);
        hBox.setSpacing(10);
        // ImageView to display the spare's image
        partView.getPartModel().setImageView(new ImageView());
        partView.getPartModel().getImageView().setFitWidth(357); // Match target width
        partView.getPartModel().getImageView().setFitHeight(265); // Match target height
        partView.getPartModel().getImageView().setPreserveRatio(true);
        // Button to save clipboard image
        Button saveButton = new Button("Save Clipboard Image");
        saveButton.setOnAction(event -> saveClipboardImage());
        // Load initial image for the selected spare
        loadImageForSelectedSpare();
        // Listen for changes to the selected spare
        partView.getPartModel().selectedSpareProperty().addListener((obs, oldSpare, newSpare) -> {
            loadImageForSelectedSpare();
        });
        // Add components to HBox
        hBox.getChildren().addAll(partView.getPartModel().getImageView(), saveButton);
        return hBox;
    }

    private void saveClipboardImage() {
            partView.getAction().accept(PartMessage.SAVE_IMAGE_TO_DATABASE);
//            Image newImage = partView.getPartModel().getImage();
//            if (newImage != null) {
//                partView.getPartModel().getImageView().setImage(newImage);
//            }
    }

    private void loadImageForSelectedSpare() {
        partView.getAction().accept(PartMessage.LOAD_IMAGE);
//        if(partView.getPartModel().getImage() != null) {
//            partView.getPartModel().getImageView().setImage(partView.getPartModel().getImage());
//        }
    }
}