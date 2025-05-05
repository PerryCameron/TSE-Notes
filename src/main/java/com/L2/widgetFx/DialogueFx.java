package com.L2.widgetFx;

import com.L2.BaseApplication;
import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.dto.global_spares.ProductToSparesDTO;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class DialogueFx {

    private static final Logger logger = LoggerFactory.getLogger(DialogueFx.class);

    public static Alert aboutDialogue(String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header); // I would like the header to be a larger font
        alert.setContentText(message);

        alert.setTitle("");

        Image image = new Image(DialogueFx.class.getResourceAsStream("/images/TSELogo-64.png"));
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

        Image image = new Image(DialogueFx.class.getResourceAsStream("/images/TSELogo-64.png"));
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

    public static Alert searchAlert(NoteView noteView, TableView<PartDTO> tableView, PartOrderDTO partOrderDTO) {
        NoteModel noteModel = noteView.getNoteModel();
        // Create a custom Alert with no default buttons
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Search..."); // Set custom title bar text
        // Create a DialogPane
        DialogPane dialogPane = new DialogPane();
        dialogPane.getStylesheets().add("css/light.css");
        dialogPane.getStyleClass().add("search-dialogue");

        // Create layout for content
        VBox content = new VBox(10);
//        content.getStyleClass().add("decorative-header-box");
        content.setPadding(new Insets(10, 10, 10, 10));
        content.setPrefWidth(600);
        Label messageLabel = new Label("Part Search");
        TextField searchField = new TextField();
        searchField.setPromptText("Search Part Number or description...");

        // Create buttons
        Button searchButton = new Button("Search");
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(10, searchButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        VBox partContainer = new VBox(10);

        // Add components to content
        content.getChildren().addAll(messageLabel, searchField, buttonBox, partContainer);
        dialogPane.setContent(content);

        // Set DialogPane to Alert
        alert.setDialogPane(dialogPane);

        // Handle Search button
        searchButton.setOnAction(e -> {
            noteModel.searchWordProperty().set(searchField.getText().trim());
            if (!noteModel.searchWordProperty().get().isEmpty()) {
                noteView.getAction().accept(NoteMessage.SEARCH_PARTS);
                partContainer.getChildren().clear();
                Button addToPartOrderButton = new Button("Add to Part Order");
                ListView<SparesDTO> listView = ListViewFx.partListView(noteModel.getSearchedParts());
                partContainer.getChildren().add(listView); // I want it to expand vertically to make room for this
                partContainer.getChildren().add(addToPartOrderButton);
                // Force dialog to re-layout and resize to fit new content
                addToPartOrderButton.setOnAction(add -> {
                    SparesDTO sparesDTO = listView.getSelectionModel().getSelectedItem();
                    noteView.getAction().accept(NoteMessage.INSERT_PART);
                    PartDTO partDTO = noteModel.selectedPartProperty().get();
                    partDTO.setPartNumber(sparesDTO.getSpareItem());
                    partDTO.setPartDescription(sparesDTO.getSpareDescription());
                    // no need to put in part into FX UI here as it is being done elseware
                    noteView.getAction().accept(NoteMessage.UPDATE_PART);

                    // Refresh the table view layout and focus
                    tableView.layout();
                    tableView.requestFocus();

                    // Select row 0 and focus the first column
                    tableView.getSelectionModel().select(0);
                    tableView.getFocusModel().focus(0, tableView.getColumns().getFirst());  // Focus the first column (index 0)
                });
                dialogPane.requestLayout();
                alert.getDialogPane().getScene().getWindow().sizeToScene();
            }
        });

        // Handle Cancel button
        cancelButton.setOnAction(e -> {
            System.out.println("Cancel button clicked");
            noteModel.searchWordProperty().set("");
            noteModel.getSearchedParts().clear();
            alert.setResult(ButtonType.CANCEL);
            alert.hide();
        });

        // put our icon in titlebar
        getTitleIcon(dialogPane);

        // Tie alert to stage and calculates where to start dialogue location
        tieAlertToStage(alert, 600, 400);

        return alert;
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
        alertStage.setOnShown(e -> {
            alertStage.removeEventHandler(WindowEvent.WINDOW_SHOWING, positionHandler);
        });
    }

}
