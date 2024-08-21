package com.L2.mvci_case;

import com.L2.dto.CaseDTO;
import com.L2.dto.EntitlementDTO;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.FakeData;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.L2.static_tools.ApplicationPaths.entitlementsFile;
import static com.L2.static_tools.ApplicationPaths.settingsDir;

public class CaseInteractor {

    private final CaseModel caseModel;
    private static final Logger logger = LoggerFactory.getLogger(CaseInteractor.class);

    public CaseInteractor(CaseModel caseModel) {
        this.caseModel = caseModel;
    }

    public void loadEntitlements() {
        try {
            // Ensure the directory and file exist
            AppFileTools.createFileIfNotExists(settingsDir);
            // Load the entitlements
            ObservableList<EntitlementDTO> entitlements = AppFileTools.getEntitlements(entitlementsFile);
            if (entitlements != null) {
                caseModel.setEntitlements(entitlements);
                logger.info("Loaded entitlements: " + entitlements.size());
            } else {
                // arrayList is already initialized so really we do nothing but warn
                logger.warn("Entitlements file is empty or could not be read. Initializing with an empty list.");
            }
        } catch (IOException e) {
            logger.error("Failed to load entitlements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setFakeTestData() {
        CaseDTO caseDTO = FakeData.createFakeCase();
        caseModel.setCurrentCase(caseDTO);
    }

    public EntitlementDTO setCurrentEntitlement() {
        EntitlementDTO entitlementDTO = caseModel.getEntitlements().stream().filter(DTO -> DTO.getName()
                .equals(caseModel.getCurrentCase().getEntitlement())).findFirst().orElse(null);
        caseModel.setCurrentEntitlement(entitlementDTO);
        logger.info("Current entitlement: " + caseModel.getCurrentCase().getEntitlement());
        return entitlementDTO;
    }
}
