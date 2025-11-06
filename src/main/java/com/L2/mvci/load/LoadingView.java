package com.L2.mvci.load;

import com.L2.BaseApplication;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Builder;

public class LoadingView implements Builder<Region> {

    private final LoadingModel loadingModel;

    public LoadingView(LoadingModel loadingModel) {
        this.loadingModel = loadingModel;
    }

    @Override
    public Region build() {
        setUpStage();
        VBox loadingVBox = new VBox(createProcessIndicator());
        loadingVBox.setPrefSize(200,200);
        loadingVBox.setAlignment(Pos.CENTER);
        loadingVBox.setBackground(Background.EMPTY);
        loadingVBox.setPadding(Insets.EMPTY);
        return loadingVBox;
    }

    private Control createProcessIndicator() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
//        progressIndicator.getStyleClass().add("progress-indicator-style"); // Add the custom style class
        progressIndicator.setStyle("-fx-progress-color: #1658e7;"); // TODO move this to css
        // Set the scale transformation to resize the spinner
        double scaleFactor = 2.0; // Adjust this value to change the spinner's size
        Scale scale = new Scale(scaleFactor, scaleFactor);
        progressIndicator.getTransforms().add(scale);        progressIndicator.setBackground(Background.EMPTY); // Add this line
        return progressIndicator;
    }

    private void setUpStage() {
        loadingModel.getLoadingStage().initOwner(BaseApplication.primaryStage);
        loadingModel.getLoadingStage().initModality(Modality.APPLICATION_MODAL);
        loadingModel.getLoadingStage().initStyle(StageStyle.TRANSPARENT);
        loadingModel.primaryXPropertyProperty().bind(BaseApplication.primaryStage.xProperty());
        loadingModel.primaryYPropertyProperty().bind(BaseApplication.primaryStage.yProperty());
        loadingModel.setOffsets(50.0, 50.0);
        updateSpinnerLocation();
        setOffsetListener();
        monitorPropertyChange(loadingModel.primaryXPropertyProperty());
        monitorPropertyChange(loadingModel.primaryYPropertyProperty());
    }

    private void setOffsetListener() {
        loadingModel.offsetXProperty().addListener(observable -> {
            updateSpinnerLocation();
        });
    }

    public void monitorPropertyChange(DoubleProperty property) {
        property.addListener((observable, oldValue, newValue) -> {
            updateSpinnerLocation();
        });
    }

    private void updateSpinnerLocation() {
        double centerXPosition = BaseApplication.primaryStage.getX() + BaseApplication.primaryStage.getWidth() / 2d;
        double centerYPosition = BaseApplication.primaryStage.getY() + BaseApplication.primaryStage.getHeight() / 2d;
        loadingModel.getLoadingStage().setOnShown(windowEvent -> {
            loadingModel.getLoadingStage().setX(centerXPosition -
                    (loadingModel.getLoadingStage().getWidth() + loadingModel.getOffsetX()) / 2d);
            loadingModel.getLoadingStage().setY(centerYPosition -
                    (loadingModel.getLoadingStage().getHeight() + loadingModel.getOffsetY()) / 2d);
        });
    }
}
