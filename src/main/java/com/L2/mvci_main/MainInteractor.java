package com.L2.mvci_main;

import com.L2.static_tools.ApplicationPaths;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainInteractor implements ApplicationPaths {

    private static final Logger logger = LoggerFactory.getLogger(MainInteractor.class);
    private final MainModel mainModel;

    public MainInteractor(MainModel mainModel) {
        this.mainModel = mainModel;

    }

//    public boolean tabIsNotOpen(int msId) {  // find if tab is open
//        if (PaneFx.tabIsOpen(msId, mainModel.getMainTabPane())) {
//            Platform.runLater(() -> {
//                mainModel.setMsId(msId);
//                mainModel.setReturnMessage(MainMessage.SELECT_TAB);
//            });
//            return false;
//        }
//        return true;
//    }
}
