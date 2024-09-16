package com.L2.mvci_notelist;

import com.L2.interfaces.Controller;
import com.L2.mvci_main.MainInteractor;
import com.L2.mvci_main.MainMessage;
import com.L2.mvci_main.MainModel;
import com.L2.mvci_main.MainView;
import com.L2.mvci_note.NoteController;
import com.L2.mvci_note.NoteMessage;
import com.L2.mvci_settings.SettingsController;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoteListController extends Controller<NoteListMessage> {

    private final NoteListInteractor noteListInteractor;
    private final NoteListView noteListView;
    private static final Logger logger = LoggerFactory.getLogger(NoteListController.class);
    private NoteController noteController = null;


    public NoteListController() {
        NoteListModel noteListModel = new NoteListModel();
        noteListInteractor = new NoteListInteractor(noteListModel);
        noteListView = new NoteListView(noteListModel, this::action);
    }

    @Override
    public Region getView() {
        return noteListView.build();
    }

    @Override
    public void action(NoteListMessage action) {
        switch (action) {
        }
    }
}