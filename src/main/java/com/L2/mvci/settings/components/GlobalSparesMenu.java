package com.L2.mvci.settings.components;

import com.L2.dto.global_spares.RangesFx;
import com.L2.mvci.settings.SettingsMessage;
import com.L2.mvci.settings.SettingsModel;
import com.L2.mvci.settings.SettingsView;
import com.L2.static_tools.ImageResources;
import com.L2.widgetFx.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

public class GlobalSparesMenu implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(GlobalSparesMenu.class);
    private final SettingsModel settingsModel;
    private final Consumer<SettingsMessage> action;
    private final SettingsView settingsView;

    public GlobalSparesMenu(SettingsView view) {
        this.settingsModel = view.getSettingsModel();
        this.settingsView = view;
        this.action = view.getAction();
    }

    @Override
    public Region build() {
        VBox vbox = VBoxFx.of(new Insets(0, 10, 10, 10));
        vbox.getStyleClass().add("decorative-hbox");
        setPartsAvailabilityListener(vbox);
        // made sure database exists and is writable
        action.accept(SettingsMessage.VERIFY_PARTS_DATABASE);
        // settingsModel.boundRangeFxProperty().get().rangeProperty().addListener(range -> System.out.println(range));
        // settingsModel.boundRangeFxProperty().get().rangeTypeProperty().addListener(range -> System.out.println(range));
        // settingsModel.boundRangeFxProperty().get().productFamilyProperty().addListener(range -> System.out.println(range));
        return vbox;
    }

    private void setPartsAvailabilityListener(VBox vbox) {
        settingsModel.partsDBAvailableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vbox.getChildren().clear();
                vbox.getChildren().addAll(
                        HeaderFx.withTitle("Range Editor"),
                        dbStatus(),
                        description(),
                        rangeEditor());
                action.accept(SettingsMessage.GET_RANGES);
            } else {
                vbox.getChildren().clear();
                vbox.getChildren().addAll(dbStatus(), createDropRegion());
            }
        });
    }

    private Node description() {
        Label label = new Label("Your ranges allow you to quickly refine the way you search. The keywords are what will be searched for when the range is selected. You can add and delete them as you choose. Only product family and range information will be searched with this.");
        // Optional: Style the Label (e.g., font, color)
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        label.setWrapText(true);
        VBox.setMargin(label, new Insets(10, 0, 10, 0));
        return label;
    }

    private Node rangeEditor() {
        // get a reference to the list located in noteModel
        action.accept(SettingsMessage.GET_RANGES_REFERENCE);
        HBox hbox = new HBox();
        VBox.setVgrow(hbox, Priority.ALWAYS);
        hbox.getChildren().add(rangeSelector());
        hbox.getChildren().add(attributesBox());
        return hbox;
    }

    private Node attributesBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        vbox.getChildren().add(rangeBox());
        vbox.getChildren().add(numberOfSpares());
        vbox.getChildren().add(rangeTypeBox());
        vbox.getChildren().add(modelsBox());
        vbox.getChildren().add(createButtonRow());
        return vbox;
    }

    private Node numberOfSpares() {
        HBox hbox = new HBox();
        hbox.getChildren().add(settingsModel.numberOfSparesLabelProperty().get());
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getStyleClass().add("inner-decorative-hbox");
        return hbox;
    }

    private Node createButtonRow() {
        HBox hBox = new HBox(5);
        Button saveButton = ButtonFx.utilityButton(() -> {
            action.accept(SettingsMessage.SAVE_RANGES);
        }, ImageResources.SAVE, "Save");
        Button deleteButton = ButtonFx.utilityButton(() -> {
            showConfirmationAlert();
        }, ImageResources.DELETE, "Delete");
        Button newButton = ButtonFx.utilityButton(() -> {
            settingsModel.getRanges().add(new RangesFx());
            action.accept(SettingsMessage.ADD_RANGE);
        }, ImageResources.NEW, "New Range");
        hBox.getChildren().addAll(saveButton, deleteButton, newButton);
        return hBox;
    }

    private void showConfirmationAlert() {
        Optional<Alert> alertOpt = DialogueFx.conformationAlert("Delete Range","Are you sure you want to delete this range?");
        alertOpt.ifPresent(alert -> {
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    action.accept(SettingsMessage.DELETE_RANGE);
                } else {
                    logger.info("Delete Range cancelled");
                }
            });
        });
    }

    private Node modelsBox() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().add(new Label("Product Family and Range Keywords (Each on a separate line)"));
        TextArea textArea = new TextArea();
        textArea.setEditable(true);
        textArea.setWrapText(false);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                action.accept(SettingsMessage.UPDATE_RANGE_IN_LIST);
                action.accept(SettingsMessage.UPDATE_NUMBER_OF_SPARES);
            }
        });
        textArea.textProperty().bindBidirectional(settingsModel.boundRangeFxProperty().get().productFamilyProperty());
        vbox.getStyleClass().add("inner-decorative-hbox");
        vbox.getChildren().add(textArea);
        return vbox;
    }

    private Node rangeBox() {
        VBox vBox = new VBox(10);
        TextField textField = new TextField();
        textField.setMaxWidth(Double.MAX_VALUE);
        textField.textProperty().bindBidirectional(settingsModel.boundRangeFxProperty().get().rangeProperty());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) action.accept(SettingsMessage.UPDATE_RANGE_IN_LIST);
        });
        vBox.getChildren().add(new Label("Range"));
        vBox.getChildren().add(textField);
        vBox.getStyleClass().add("inner-decorative-hbox");
        vBox.setAlignment(Pos.CENTER_LEFT);
        return vBox;
    }

    private Node rangeTypeBox() {
        VBox vBox = new VBox(10);
        TextField textField = new TextField();
        textField.setMaxWidth(Double.MAX_VALUE);
        textField.textProperty().bindBidirectional(settingsModel.boundRangeFxProperty().get().rangeTypeProperty());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) action.accept(SettingsMessage.UPDATE_RANGE_IN_LIST);
        });
        vBox.getChildren().add(new Label("Range type"));
        vBox.getChildren().add(textField);
        vBox.getStyleClass().add("inner-decorative-hbox");
        vBox.setAlignment(Pos.CENTER_LEFT);
        return vBox;
    }

    public Node rangeSelector() {
        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("Range"));
        vbox.getChildren().add(new RangesListView(settingsView).build());
        return vbox;
    }

    public Node dbStatus() {
        HBox hbox = new HBox(7);
        hbox.setAlignment(Pos.CENTER); // Center the circle in the HBox
        hbox.setPadding(new Insets(5)); // Optional: Add padding for spacing
        Label label = new Label();
        Circle circle = new Circle();
        circle.setRadius(10.0);

        // Bind circle fill to database status
        circle.fillProperty().bind(Bindings.createObjectBinding(() -> {
            if (settingsModel.partsDBAvailableProperty().get()) {
                label.setText("Parts Database available");
                return GraphicFx.greenCircle(); // Green gradient for available
            } else {
                label.setText("Parts Database not available");
                return GraphicFx.redCircle(); // Red for unavailable
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
                Path droppedFile = dragboard.getFiles().getFirst().toPath();
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
