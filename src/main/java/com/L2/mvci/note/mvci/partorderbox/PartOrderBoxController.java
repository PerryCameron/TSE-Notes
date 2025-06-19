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
        this.partOrderBoxModel = new PartOrderBoxModel(noteView);
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
            case VIEW_PART_AS_SPARE -> partOrderBoxInteractor.viewPartAsSpare();
            case RESET_PART_LISTENER -> partOrderBoxInteractor.resetPartListener();
            case ADD_PART_TO_DATABASE -> partOrderBoxInteractor.addPartToDb();
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

    public void printProductFamilies() {
        partOrderBoxInteractor.printProductFamilies();
    }
}
