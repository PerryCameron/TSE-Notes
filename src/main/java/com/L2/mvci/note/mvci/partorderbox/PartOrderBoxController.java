package com.L2.mvci.note.mvci.partorderbox;

import com.L2.interfaces.Controller;
import com.L2.mvci.note.NoteView;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PartOrderBoxController extends Controller<PartOrderBoxMessage> {
    private static final Logger logger = LoggerFactory.getLogger(PartOrderBoxController.class);
    private final PartOrderBoxModel partOrderBoxModel;
    private final PartOrderBoxView partOrderBoxView;
    private final PartOrderBoxInteractor partOrderBoxInteractor;

    public PartOrderBoxController(NoteView noteView) {
        this.partOrderBoxModel = new PartOrderBoxModel();
        this.partOrderBoxInteractor = new PartOrderBoxInteractor(partOrderBoxModel);
        this.partOrderBoxView = new PartOrderBoxView(partOrderBoxModel, noteView, this::action);
    }

    @Override
    public Region getView() {
        return partOrderBoxView.build();
    }

    @Override
    public void action(PartOrderBoxMessage actionEnum) {
        switch (actionEnum) {
            case FLASH -> partOrderBoxInteractor.flash();
        }
    }

    public void flash() {
        partOrderBoxInteractor.flash();
    }

    public void refreshFields() {
        partOrderBoxInteractor.refreshFields();
    }

    public void printPartsTableView() {
        partOrderBoxInteractor.printPartsTableView();
    }
}
