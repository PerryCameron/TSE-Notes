package com.L2.mvci.load;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingInteractor {
    private static final Logger logger = LoggerFactory.getLogger(LoadingInteractor.class);
    private final LoadingModel loadingModel;

    public LoadingInteractor(LoadingModel loadingModel) {
        this.loadingModel = loadingModel;
    }

    public void showLoadSpinner(boolean show) {
        Platform.runLater(() -> {
            logger.info("Loading Spinner set: " + show);
            if(show) loadingModel.getLoadingStage().show();
            else loadingModel.loadingStage.hide();
        });
    }

    public void logInfo(String log) {
        logger.info(log);
    }

    public Stage getStage() {
        return loadingModel.getLoadingStage();
    }

    public void setOffSet(double x, double y) {
        Platform.runLater(() -> {
            loadingModel.setOffsets(x, y);
        });
    }
}
