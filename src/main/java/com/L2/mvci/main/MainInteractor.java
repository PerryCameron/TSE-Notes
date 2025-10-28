package com.L2.mvci.main;

import com.L2.dto.NoteFx;
import com.L2.dto.global_spares.RangesFx;
import com.L2.repository.implementations.SettingsRepositoryImpl;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.ApplicationPaths;
import com.L2.widgetFx.DialogueFx;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

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

    public void updateNoteTabName(ObjectProperty<NoteFx> boundNote) {
        mainModel.noteTabProperty().get().setText("Note " + boundNote.get().getId());
    }

    public void selectNoteTab() {
        mainModel.mainTabPaneProperty().get().getSelectionModel().select(mainModel.noteTabProperty().get());
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

    public BooleanProperty isSpellChecked() {
        return mainModel.spellCheckProperty();
    }

    public void printRanges(ObservableList<RangesFx> ranges) {
        System.out.println("-------------------Ranges---------------------");
        System.out.println("Ranges: " + ranges.size());
        ranges.forEach(rangesFx -> rangesFx.printRange());
    }

    public ExecutorService getExecutorService() {
        logger.debug("Getting executor service...");
        return mainModel.getExecutor();
    }

    public void shutDownExecutorService() {
        logger.info("Shutting down executor service...");
        mainModel.getExecutor().shutdown();
    }

    public void openManual()  {
        try (InputStream pdfStream = getClass().getResourceAsStream("/pdf/manual.pdf")) {
            // Check if the PDF resource exists
            if (pdfStream == null) {
                System.err.println("PDF resource not found at /pdf/manual.pdf");
                return;
            }

            // Create a temporary file
            File tempFile = File.createTempFile("manual", ".pdf");
            tempFile.deleteOnExit(); // Ensure the file is deleted when the JVM exits

            // Copy the resource to the temporary file
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pdfStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // Open the temporary file with the default PDF viewer
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                logger.error("Desktop is not supported on this platform");
                DialogueFx.errorAlert("Error", "Desktop is not supported on this platform");
            }
        } catch (IOException e) {
            logger.error("Error opening PDF: {}", e.getMessage());
            DialogueFx.errorAlert("Error opening PDF", e.getMessage());
        }
    }
}
