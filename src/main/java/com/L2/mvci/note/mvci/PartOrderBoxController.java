package com.L2.mvci.note.mvci;

import com.L2.interfaces.Controller;
import com.L2.mvci.main.MainMessage;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PartOrderBoxController extends Controller<PartOrderBoxMessage> {
    private static final Logger logger = LoggerFactory.getLogger(PartOrderBoxController.class);
    private final PartOrderBoxModel partOrderBoxModel;
    private final PartOrderBoxView partOrderBoxView;
    private final PartOrderBoxInteractor partOrderBoxInteractor;

    public PartOrderBoxController(Consumer<MainMessage> PartOrderBoxModel) {
        this.partOrderBoxModel = new PartOrderBoxModel();
        this.partOrderBoxInteractor = new PartOrderBoxInteractor(partOrderBoxModel);
        this.partOrderBoxView = new PartOrderBoxView(partOrderBoxModel, this::action);
    }


    @Override
    public Region getView() {
        return partOrderBoxView.build();
    }

    @Override
    public void action(PartOrderBoxMessage actionEnum) {

    }
}
