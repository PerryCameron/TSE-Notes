package com.L2.widgetFx;

import com.L2.BaseApplication;
import com.L2.dto.PartDTO;
import com.L2.dto.global_spares.RangesDTO;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class DialogueFx {

    private static final Logger logger = LoggerFactory.getLogger(DialogueFx.class);

    public static Alert aboutDialogue(String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header); // I would like the header to be a larger font
        alert.setContentText(message);

        alert.setTitle("");

        Image image = new Image(Objects.requireNonNull(DialogueFx.class.getResourceAsStream("/images/TSELogo-64.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(64); // Adjust the height as needed
        imageView.setFitWidth(64);  // Adjust the width as needed
        alert.setGraphic(imageView);

        // Modify the header text programmatically
        DialogPane dialogPane = alert.getDialogPane();
        getTitleIcon(dialogPane);
        Label headerLabel = (Label) dialogPane.lookup(".dialog-pane .header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        }
        tieAlertToStage(alert, 400, 200);
        dialogPane.getStylesheets().add("css/light.css");
        return alert;
    }

    public static String showYesNoCancelDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText("Cloning Options");
        alert.setContentText("Do you want to also clone the parts?");

        Image image = new Image(Objects.requireNonNull(DialogueFx.class.getResourceAsStream("/images/TSELogo-64.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(64); // Adjust the height as needed
        imageView.setFitWidth(64);  // Adjust the width as needed
        alert.setGraphic(imageView);

        // Create custom ButtonTypes for Yes, No, and Cancel.
        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType buttonCancel = new ButtonType("Cancel clone", ButtonBar.ButtonData.CANCEL_CLOSE);
        DialogPane dialogPane = alert.getDialogPane();
        getTitleIcon(dialogPane);

        Label headerLabel = (Label) dialogPane.lookup(".dialog-pane .header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        }
        tieAlertToStage(alert, 400, 200);
        dialogPane.getStylesheets().add("css/light.css");

        // Set them as the buttons for this alert.
        alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == buttonYes) {
                return "yes";
            } else if (result.get() == buttonNo) {
                return "no";
            } else if (result.get() == buttonCancel) {
                return "cancel";
            }
        }
        return "";
    }

    public static Alert errorAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        tieAlertToStage(alert, 400, 200);
        DialogPane dialogPane = alert.getDialogPane();
        getTitleIcon(dialogPane);
        dialogPane.getStylesheets().add("css/light.css");
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
        return alert;
    }

    public static Alert searchAlert(NoteView noteView, TableView<PartDTO> partsTableView) {
        final BooleanProperty searchedBefore = new SimpleBooleanProperty(false);
        NoteModel noteModel = noteView.getNoteModel();

        // Create a custom Alert with no default buttons
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Search spares"); // Set custom title bar text

        // Create a DialogPane
        DialogPane dialogPane = new DialogPane();
        dialogPane.getStylesheets().add("css/light.css");
        dialogPane.getStyleClass().add("search-dialogue");

        // Create layout for content
        VBox content = new VBox(10);
        content.setPadding(new Insets(10, 10, 10, 10));
        content.setPrefWidth(600);
        Label messageLabel = new Label("Part Search");
        TextField searchField = new TextField();
        searchField.setPromptText("Search Part Number or description...");

        // Create buttons
        Button searchButton = new Button("Search");
        Button cancelButton = new Button("Cancel");
        Button partOrderButton = new Button("Add to Part Order");

        // Spacer Region
        HBox searchHbox = new HBox();
        searchHbox.setAlignment(Pos.CENTER_RIGHT);
        searchHbox.getChildren().add(searchButton);
        HBox.setHgrow(searchHbox, Priority.ALWAYS); // Spacer grows to push buttons right

        VBox cancelHbox = new VBox();
        cancelHbox.setAlignment(Pos.CENTER_RIGHT);
        cancelHbox.getChildren().add(cancelButton);

        HBox buttonBox = new HBox(10, rangeBox(noteModel), searchHbox, cancelHbox);

        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // contains the tableview and buttons
        VBox partContainer = new VBox(10);

        // contains label for number of results
        HBox resultsLabelHbox = new HBox();
        HBox.setHgrow(resultsLabelHbox, Priority.ALWAYS);
        resultsLabelHbox.setAlignment(Pos.CENTER_LEFT);
        resultsLabelHbox.getChildren().addAll(noteModel.resultsLabelProperty().get(), messageLabel);
        // contains buttons
        HBox partContainerButtonBox = new HBox(5);
        partContainerButtonBox.setAlignment(Pos.CENTER_RIGHT);

        // Add components to content
        content.getChildren().addAll(messageLabel, searchField, buttonBox, partContainer, partContainerButtonBox);
        dialogPane.setContent(content);

        // Set DialogPane to Alert
        alert.setDialogPane(dialogPane);

        // Handle Search button
        searchButton.setOnAction(e -> {
            noteModel.searchWordProperty().set(searchField.getText().trim());
            if (!noteModel.searchWordProperty().get().isEmpty()) { // there are search terms

                noteView.getAction().accept(NoteMessage.SEARCH_PARTS);
                // clear everything in part container in case we already made a search
                if(!searchedBefore.get()) {
                    buttonBox.getChildren().remove(cancelHbox);
                    partContainerButtonBox.getChildren().addAll(resultsLabelHbox, partOrderButton, cancelButton);
                }
                searchedBefore.set(true);
                // clears for new tableview
                partContainer.getChildren().clear();

                // make tableview with new list of parts
                TableView<SparesDTO> sparesTableView = SparesTableViewFx.createTableView(noteModel);
                // add new tableview to dialogue
                partContainer.getChildren().add(sparesTableView);
                partOrderButton.setOnAction(add -> {
                    SparesDTO sparesDTO = sparesTableView.getSelectionModel().getSelectedItem();
                    if (sparesDTO != null) {
                        noteView.getAction().accept(NoteMessage.INSERT_PART);
                        PartDTO partDTO = noteModel.selectedPartProperty().get();
                        partDTO.setPartNumber(sparesDTO.getSpareItem());
                        partDTO.setPartDescription(sparesDTO.getSpareDescription());
                        // no need to put in part into FX UI here as it is being done elsewhere
                        noteView.getAction().accept(NoteMessage.UPDATE_PART);
                        // Refresh the table view layout and focus
                        TableViewFx.focusOnLastItem(partsTableView);
                        // make sure we clear our label out so that we get no memory leaks, the label continues to exist but not the hbox
                        resultsLabelHbox.getChildren().clear();
                        cleanAlertClose(noteModel, alert);
                    }
                });
                dialogPane.requestLayout();
                alert.getDialogPane().getScene().getWindow().sizeToScene();
            }
        });

        // Handle Cancel button
        cancelButton.setOnAction(e -> cleanAlertClose(noteModel, alert));

        // put our icon in title bar
        getTitleIcon(dialogPane);

        // Tie alert to stage and calculates where to start dialogue location
        tieAlertToStage(alert, 600, 400);

        return alert;
    }

    private static Node rangeBox(NoteModel noteModel) {
        ObservableList<String> rangeItems = FXCollections.observableArrayList(
                noteModel.getRanges().stream()
                        .map(RangesDTO::getRange)
                        .collect(Collectors.toList())
        );
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        Label rangeLabel = new Label("Range");
        // Create ComboBox and set items
        ComboBox<String> rangeComboBox = new ComboBox<>();
        rangeComboBox.getSelectionModel().select("All");
        // sets the range to default so that it will search without looking
        setSelectedRange("All", noteModel);

        rangeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Assuming noteModel.getRanges() returns a list of RangeDTO objects
            setSelectedRange(newValue, noteModel);
        });

        rangeComboBox.setItems(rangeItems);
        HBox.setHgrow(rangeComboBox, Priority.ALWAYS);
        hBox.getChildren().addAll(rangeLabel, rangeComboBox);
        return hBox;
    }

    // helper method to close alert
    private static void cleanAlertClose(NoteModel noteModel, Alert alert) {
        noteModel.searchWordProperty().set("");
        noteModel.getSearchedParts().clear();
        alert.setResult(ButtonType.CANCEL);
        alert.hide();
    }

    // helper method to set range to match what is selected in the combobox
    private static void setSelectedRange(String newValue, NoteModel noteModel) {
        RangesDTO selectedRange = noteModel.getRanges().stream()
                .filter(range -> range.getRange().equals(newValue))
                .findFirst()
                .orElse(null);
        selectedRange.printRanges(); // testing
        noteModel.selectedRangeProperty().set(selectedRange);
    }

    private static void getTitleIcon(DialogPane dialogPane) {
        // Set custom icon for the title bar
        Stage alertStage = (Stage) dialogPane.getScene().getWindow();
        try {
            // Load icon from resources (adjust path as needed)
            Image icon = new Image(Objects.requireNonNull(
                    DialogueFx.class.getResourceAsStream("/images/TSELogo-16.png")));
            alertStage.getIcons().add(icon);
        } catch (Exception e) {
            logger.error("Failed to load icon: {}", e.getMessage());
        }
    }

    public static void customAlertWithShow(String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(message);
        tieAlertToStage(alert, 400, 200);
        DialogPane dialogPane = alert.getDialogPane();
        getTitleIcon(dialogPane);
        dialogPane.getStylesheets().add("css/light.css");
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
    }

    public static void tieAlertToStage(Alert alert, double stageWidth, double stageHeight) {
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        // Flag to ensure positioning runs only once
        final boolean[] hasPositioned = {false};

        // Position the dialog only once when about to show
        EventHandler<WindowEvent> positionHandler = e -> {
            if (!hasPositioned[0]) {
                if (BaseApplication.primaryStage == null) {
                    System.out.println("Warning: primaryStage is null");
                    return;
                }
                hasPositioned[0] = true;
                double primaryX = BaseApplication.primaryStage.getX();
                double primaryY = BaseApplication.primaryStage.getY();
                double primaryWidth = BaseApplication.primaryStage.getWidth();
                double primaryHeight = BaseApplication.primaryStage.getHeight();


                alertStage.setX(primaryX + (primaryWidth / 2) - (stageWidth / 2));
                alertStage.setY(primaryY + (primaryHeight / 2) - (stageHeight / 2));
            }
        };

        // Add handler and remove it after first show to prevent re-triggering
        alertStage.setOnShowing(positionHandler);
        alertStage.setOnShown(e -> alertStage.removeEventHandler(WindowEvent.WINDOW_SHOWING, positionHandler));
    }

}
