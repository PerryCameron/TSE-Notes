package com.L2.mvci.bom;

import com.L2.static_tools.bom.BOMExploderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BomInteractor {
    private static final Logger logger = LoggerFactory.getLogger(BomInteractor.class);
    private final BomModel bomModel;

    public BomInteractor(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    public void searchForComponentBom()  {
        System.out.println("Searching for component: " + bomModel.searchComponentProperty().get());
        try {
            String output = BOMExploderClient.getBOMExplosionAsString(bomModel.searchComponentProperty().get(), "BIL", "");
            System.out.println(output);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
