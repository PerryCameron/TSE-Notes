package com.L2.mvci.note.mvci.partorderbox.mvci.partviewer.components;

import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderMessage;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderView;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.HBoxFx;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class Photo implements Builder<Pane> {

    private final PartFinderView partView;
    public Photo(PartFinderView partView) {
        this.partView = partView;
    }

    @Override
    public Pane build() {
        // Create HBox for layout
        HBox hBox = HBoxFx.of(265, 10);
        VBox imageContainer = new VBox();
        VBox buttonContainer = new VBox();

        imageContainer.setPrefWidth(400);
        imageContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPrefWidth(200);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        // ImageView to display the spare's image
        partView.getPartModel().setImageView(new ImageView());
        partView.getPartModel().getImageView().setFitWidth(357); // Match target width
        partView.getPartModel().getImageView().setFitHeight(265); // Match target height
        partView.getPartModel().getImageView().setPreserveRatio(true);
        // Button to save clipboard image

        Button saveButton = ButtonFx.utilityButton("/images/paste-16.png", "Paste Clipboard Image", 200);
        saveButton.setOnAction(event -> saveClipboardImage());
        // Load initial image for the selected spare
        loadImageForSelectedSpare();
        // Listen for changes to the selected spare
        partView.getPartModel().selectedSpareProperty().addListener((obs, oldSpare, newSpare) -> loadImageForSelectedSpare());
        imageContainer.getChildren().addAll(partView.getPartModel().getImageView());
        buttonContainer.getChildren().add(saveButton);
        // Add components to HBox
        hBox.getChildren().addAll(imageContainer, buttonContainer);
        return hBox;
    }

    private void saveClipboardImage() {
            partView.getAction().accept(PartFinderMessage.SAVE_IMAGE_TO_DATABASE);
    }

    private void loadImageForSelectedSpare() {
        partView.getAction().accept(PartFinderMessage.LOAD_IMAGE);
    }

}