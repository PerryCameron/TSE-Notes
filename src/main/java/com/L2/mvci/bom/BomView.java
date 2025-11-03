package com.L2.mvci.bom;

import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class BomView implements Builder<Region> {
    private final Consumer<BomMessage> action;
    private final BomModel bomModel;

    public BomView(BomModel bomModel, Consumer<BomMessage> action) {
        this.bomModel = bomModel;
        this.action = action;
    }

    @Override
    public Region build() {
        return null;
    }
}
