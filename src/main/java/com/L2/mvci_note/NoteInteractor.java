package com.L2.mvci_note;

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

public class NoteInteractor {

    private final NoteModel noteModel;
    private static final Logger logger = LoggerFactory.getLogger(NoteInteractor.class);

    public NoteInteractor(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public void loadEntitlements() {
        try {
            // Ensure the directory and file exist
            AppFileTools.createFileIfNotExists(settingsDir);
            // Load the entitlements
            ObservableList<EntitlementDTO> entitlements = AppFileTools.getEntitlements(entitlementsFile);
            if (entitlements != null) {
                noteModel.setEntitlements(entitlements);
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
        noteModel.setCurrentNote(caseDTO);
    }

    public EntitlementDTO setCurrentEntitlement() {
        EntitlementDTO entitlementDTO = noteModel.getEntitlements().stream().filter(DTO -> DTO.getName()
                .equals(noteModel.getCurrentNote().getEntitlement())).findFirst().orElse(null);
        noteModel.setCurrentEntitlement(entitlementDTO);
        System.out.println("Entitlement DTO: " + entitlementDTO);
        return entitlementDTO;
    }

    public String getStatus() {
        return noteModel.statusLabelProperty().get();
    }
}
