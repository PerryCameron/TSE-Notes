package com.L2.widgetFx;

import com.L2.BaseApplication;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.image.Image;

import java.util.Optional;

public class DialogueFx {
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
        Label headerLabel = (Label) dialogPane.lookup(".dialog-pane .header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        }
        tieAlertToStage(alert);
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
        Label headerLabel = (Label) dialogPane.lookup(".dialog-pane .header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        }
        tieAlertToStage(alert);
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
        tieAlertToStage(alert);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/light.css");
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
        return alert;
    }

    public static void customAlertWithShow(String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(message);
        tieAlertToStage(alert);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/light.css");
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
    }

    public static void tieAlertToStage(Alert alert) {
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        // Add a window showing listener to set the position of the dialog
        alertStage.addEventHandler(WindowEvent.WINDOW_SHOWN, setStageLocation(alertStage));
    }

    public static boolean verifyAction(String[] string, Object o) {
        if(o != null) {
            Alert alert = DialogueFx.aboutDialogue(string[0], string[1], Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        } else {
            Alert alert = DialogueFx.aboutDialogue(string[2],string[3], Alert.AlertType.INFORMATION);
            alert.showAndWait();
        }
        return false;
    }

    public static EventHandler<WindowEvent> setStageLocation(Stage stage) {
        return e -> {
            // Position the dialog at the center of the main stage
            stage.setX(BaseApplication.primaryStage.getX() + (BaseApplication.primaryStage.getWidth() / 2) - (stage.getWidth() / 2));
            stage.setY(BaseApplication.primaryStage.getY() + (BaseApplication.primaryStage.getHeight() / 2) - (stage.getHeight() / 2));
        };
    }
}
