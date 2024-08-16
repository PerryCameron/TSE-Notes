package com.L2.mvci_main;

import com.L2.interfaces.Controller;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends Controller<MainMessage> {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    //    private ConnectController connectController;
//    private LoadingController loadingController;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


    public MainController() {
        MainModel mainModel = new MainModel();
        mainInteractor = new MainInteractor(mainModel);
        mainView = new MainView(mainModel, this::action);
//        mainInteractor.setComplete();
    }

    @Override
    public Region getView() {
        return mainView.build();
    }

    @Override
    public void action(MainMessage action) {
//        switch (action) {
//            case CLOSE_ALL_CONNECTIONS_AND_EXIT -> connectController.closeConnection();
//            case CLOSE_ALL_CONNECTIONS -> closeAllConnections();
//            case CREATE_CONNECT_CONTROLLER -> createConnectController();
//            case BACKUP_DATABASE -> backUpDatabase();
//            case SHOW_LOG -> showDebugLog();
//        }
    }
}