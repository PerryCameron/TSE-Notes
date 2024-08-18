package com.L2.mvci_case;

import com.L2.dto.CaseDTO;
import com.L2.static_tools.FakeData;

public class CaseInteractor {

    private final CaseModel caseModel;

    public CaseInteractor(CaseModel caseModel) {
        this.caseModel = caseModel;
    }

    public void setComplete() {
        CaseDTO caseDTO = FakeData.createFakeCase();
        caseModel.setCurrentCase(caseDTO);
        System.out.println(caseModel.getCurrentCase());
    }
}
