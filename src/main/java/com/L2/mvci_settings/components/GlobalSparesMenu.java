package com.L2.mvci_settings.components;

import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.Builder;

import java.util.function.Consumer;

public class GlobalSparesMenu implements Builder<Region> {

    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public GlobalSparesMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
    }

    @Override
    public Region build() {
        // Create a Region (using Pane for simplicity)
        Pane dropRegion = new Pane();
        dropRegion.setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 2;");
        dropRegion.setPrefSize(400, 300);

        // Add a text node to display instructions or results
        Text dropText = new Text("Drag and drop global spares file here");
        dropText.setLayoutX(150);
        dropText.setLayoutY(150);
        dropRegion.getChildren().add(dropText);

        // Handle drag-over event to accept file drops
        dropRegion.setOnDragOver(event -> {
            if (event.getGestureSource() != dropRegion && event.getDragboard().hasFiles()) {
                // Allow copy transfer mode for files
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // Handle drag-dropped event to get the file path
        dropRegion.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasFiles()) {
                // Get the first file from the dragboard
                String filePath = dragboard.getFiles().get(0).getAbsolutePath();

                // Update the text with the file path
                dropText.setText("File path: " + filePath);

                // You can also print to console or use the path elsewhere
                System.out.println("Dropped file path: " + filePath);

                success = true;
            }

            // Let the system know whether the drop was successful
            event.setDropCompleted(success);
            event.consume();
        });
        return dropRegion;
    }
}
