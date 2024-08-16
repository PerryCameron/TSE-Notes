package com.L2.mvci_main;

import com.L2.BaseApplication;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class MainView implements Builder<Region> {
    private final MainModel mainModel;
    Consumer<MainMessage> action;

    public MainView(MainModel mainModel, Consumer<MainMessage> m) {
        this.mainModel = mainModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1028,830);
        BaseApplication.primaryStage.setOnHiding(event -> action.accept(MainMessage.CLOSE_ALL_CONNECTIONS_AND_EXIT));
        BaseApplication.primaryStage.setTitle("Global Spares");
        return borderPane;
    }
}