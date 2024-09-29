package com.L2.mvci_main;

import com.L2.dto.NoteDTO;
import com.L2.static_tools.ApplicationPaths;
import javafx.beans.property.ObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainInteractor implements ApplicationPaths {

    private static final Logger logger = LoggerFactory.getLogger(MainInteractor.class);
    private final MainModel mainModel;

    public MainInteractor(MainModel mainModel) {
        this.mainModel = mainModel;

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

}
