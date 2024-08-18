package com.L2.mvci_case;

import com.L2.dto.CaseDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CaseModel {
    private ObjectProperty<CaseDTO> currentCase = new SimpleObjectProperty<>();

    public CaseDTO getCurrentCase() {
        return currentCase.get();
    }

    public ObjectProperty<CaseDTO> currentCaseProperty() {
        return currentCase;
    }

    public void setCurrentCase(CaseDTO currentCase) {
        this.currentCase.set(currentCase);
    }
}
