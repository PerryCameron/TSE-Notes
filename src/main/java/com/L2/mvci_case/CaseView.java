package com.L2.mvci_case;

import java.util.function.Consumer;

public class CaseView {
        private final CaseModel caseModel;
        Consumer<CaseMessage> action;

    public CaseView(CaseModel caseModel, Consumer<CaseMessage> m) {

        this.caseModel = caseModel;
        action = m;
    }
}
