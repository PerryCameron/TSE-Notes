package com.L2.mvci_main;

import com.L2.static_tools.ApplicationPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainInteractor implements ApplicationPaths {

    private static final Logger logger = LoggerFactory.getLogger(MainInteractor.class);
    private final MainModel mainModel;

    public MainInteractor(MainModel mainModel) {
        this.mainModel = mainModel;

    }
}
