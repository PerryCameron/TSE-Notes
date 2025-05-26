package com.L2.mvci.parts;

import com.L2.interfaces.Controller;
import com.L2.mvci.note.NoteController;
import javafx.scene.layout.Region;

public class PartController extends Controller<PartMessage> {
    private final NoteController noteController;
    private final PartInteractor partInteractor;
    private final PartView partView;

    public PartController(NoteController noteController) {
        this.noteController = noteController;
        PartModel partModel = new PartModel();
        this.partInteractor = new PartInteractor(partModel);
        this.partView = new PartView(partModel, this::action);
    }

    @Override
    public Region getView() {
        return null;
    }

    @Override
    public void action(PartMessage partMessage) {
    }


}
