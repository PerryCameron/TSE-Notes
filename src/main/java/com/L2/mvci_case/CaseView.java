package com.L2.mvci_case;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class CaseView implements Builder<Region> {
        private final CaseModel caseModel;
        Consumer<CaseMessage> action;

    public CaseView(CaseModel caseModel, Consumer<CaseMessage> m) {

        this.caseModel = caseModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(new Label("This will be the case"));
        return borderPane;
    }
}
