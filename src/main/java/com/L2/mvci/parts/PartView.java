package com.L2.mvci.parts;

import java.util.function.Consumer;

public class PartView {

    private final Consumer<PartMessage> action;

    public PartView(PartModel partModel, Consumer<PartMessage> message) {
        this.action = message;
    }


}
