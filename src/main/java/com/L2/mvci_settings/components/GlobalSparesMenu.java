package com.L2.mvci_settings.components;

import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_settings.SettingsMessage;
import com.L2.mvci_settings.SettingsModel;
import com.L2.mvci_settings.SettingsView;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Consumer;

public class GlobalSparesMenu implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(GlobalSparesMenu.class);
    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;

    public GlobalSparesMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.action = view.getAction();
    }

    @Override
    public Region build() {
        VBox vbox = new VBox();
        setPartsAvailabilityListener(vbox);
        action.accept(SettingsMessage.VERIFY_PARTS_DATABASE);
        return vbox;
    }

    private void setPartsAvailabilityListener(VBox vbox) {
        settingsModel.partsDBAvailableProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                vbox.getChildren().clear();
                vbox.getChildren().add(dbStatus());
                action.accept(SettingsMessage.GET_RANGES);
            } else {
                vbox.getChildren().clear();
                vbox.getChildren().addAll(dbStatus(), createDropRegion());
            }
        });
    }

    public Node dbStatus() {
        HBox hbox = new HBox(7);
        hbox.setAlignment(Pos.CENTER); // Center the circle in the HBox
        hbox.setPadding(new Insets(5)); // Optional: Add padding for spacing
        Label label = new Label();

        Circle circle = new Circle();
        circle.setRadius(10.0);

        // Define the radial gradient: lighter green center to darker green edge
        RadialGradient gradient = new RadialGradient(
                0, // focusAngle
                0.1, // focusDistance
                0.5, // centerX (relative to circle)
                0.5, // centerY (relative to circle)
                1.0, // radius (relative to circle)
                true, // proportional (coordinates are relative to circle size)
                CycleMethod.NO_CYCLE, // No repeating gradient
                new Stop(0.0, Color.LIGHTGREEN), // Center: Light green
                new Stop(1.0, Color.DARKGREEN) // Edge: Dark green
        );

        // Bind circle fill to database status
        circle.fillProperty().bind(Bindings.createObjectBinding(() -> {
            if (settingsModel.partsDBAvailableProperty().get()) {
                label.setText("Parts Database available");
                return gradient; // Green gradient for available
            } else {
                label.setText("Parts Database not available");
                return Color.RED; // Red for unavailable
            }
        }, settingsModel.partsDBAvailableProperty()));

        hbox.getChildren().addAll(circle, label);
        return hbox;
    }

    public Pane createDropRegion() {
        // Create a Region (using Pane for simplicity)
        Pane dropRegion = new Pane();
        dropRegion.setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 2;");
        dropRegion.setPrefSize(400, 300);

        // Add a text node to display instructions or results
        Text dropText = new Text("Drag and drop global-spares.db file here");
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

        // Handle drag-dropped event to process the file
        dropRegion.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                // Get the first file from the dragboard
                Path droppedFile = dragboard.getFiles().get(0).toPath();
                String fileName = droppedFile.getFileName().toString();
                settingsModel.droppedFileProperty().setValue(droppedFile);
                // Check if the dropped file is named global-spares.db
                if (fileName.equals("global-spares.db")) {
                        action.accept(SettingsMessage.INSTALL_PART_DATABASE);
                } else {
                    dropText.setText("Invalid file: Please drop global-spares.db");
                }
            } else {
                dropText.setText("No file dropped");
            }
            // Let the system know whether the drop was successful
            event.setDropCompleted(success);
            event.consume();
        });
        return dropRegion;
    }
}
