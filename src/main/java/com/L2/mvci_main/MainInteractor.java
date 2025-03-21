package com.L2.mvci_main;

import com.L2.dto.NoteDTO;
import com.L2.repository.implementations.SettingsRepositoryImpl;
import com.L2.repository.interfaces.SettingsRepository;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.ApplicationPaths;
import javafx.beans.property.ObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;

public class MainInteractor implements ApplicationPaths {

    private static final Logger logger = LoggerFactory.getLogger(MainInteractor.class);
    private final MainModel mainModel;
    private final SettingsRepositoryImpl settingsRepo;

    public MainInteractor(MainModel mainModel) {
        this.mainModel = mainModel;
        this.settingsRepo = new SettingsRepositoryImpl();

    }

    public void setStatusBar(String status) {
        mainModel.statusStringProperty().set(status);
    }

    public void updateNoteTabName(ObjectProperty<NoteDTO> boundNote) {
        mainModel.getNoteTab().setText("Note " + boundNote.get().getId());
    }

    public void selectNoteTab() {
        mainModel.getMainTabPane().getSelectionModel().select(mainModel.getNoteTab());
    }

    public void showLog() {
            Desktop desktop = Desktop.getDesktop(); // Gui_Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()
            // Open the document
            try {
                desktop.open(AppFileTools.outputFile);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
    }

    public void disableNextButton(boolean disable) {
        mainModel.nextButtonDisabledProperty().set(disable);
    }

    public void loadAppSettings() {
        mainModel.spellCheckProperty().set(settingsRepo.isSpellCheckEnabled());
    }
}
