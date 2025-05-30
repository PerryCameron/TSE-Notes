package com.L2.mvci.note.mvci;

import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class PartOrderBoxView implements Builder<Region> {
        private final PartOrderBoxModel partOrderBoxModel;
        Consumer<PartOrderBoxMessage> action;

    public PartOrderBoxView(PartOrderBoxModel partOrderBoxModel, Consumer<PartOrderBoxMessage> action) {
            this.partOrderBoxModel = partOrderBoxModel;
            this.action = action;
    }

    @Override
    public Region build() {
        return null;
    }
}
