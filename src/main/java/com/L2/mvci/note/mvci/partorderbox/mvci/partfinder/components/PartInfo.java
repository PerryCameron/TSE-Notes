package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components;

import atlantafx.base.controls.ToggleSwitch;
import com.L2.dto.UpdatedByDTO;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderMessage;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderView;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.List;


public class PartInfo implements Builder<Pane> {

    private final PartFinderView partView;
    private HBox hBox;
    private VBox vBox;
    private ToggleSwitch toggleSwitch = new ToggleSwitch("Part in catalogue");

    public PartInfo(PartFinderView partView) {
        this.partView = partView;
    }

    @Override
    public Pane build() {
        this.vBox = new VBox();
        this.hBox = new HBox(20);
//        vBox.getChildren().add(new Label("Part is in catalogue: " + partView.getPartModel().selectedSpareProperty().get().getArchived()));
        // System.out.println(partView.getPartFinderModel().selectedSpareProperty().get());
        addEditHistory();
        partView.getPartFinderModel().refreshPartInfoProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) refresh();
        });
        hBox.getChildren().addAll(vBox, addToggleSwitch());
        return hBox;
    }

    public Control addToggleSwitch() {

        toggleSwitch.setSelected(!partView.getPartFinderModel().selectedSpareProperty().get().getArchived());
        toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!partView.getPartFinderModel().automaticToggleSwitchChangeProperty().get()) {
                System.out.println("Triggered manual change");
                partView.getPartFinderModel().selectedSpareProperty().get().setArchived(newValue);
                partView.getAction().accept(PartFinderMessage.UPDATE_IN_CATELOGUE);
            }
            partView.getPartFinderModel().automaticToggleSwitchChangeProperty().set(false);
        });
        return toggleSwitch;
    }

    public void addEditHistory() {
        List<UpdatedByDTO> dtoList = partView.getPartFinderModel().getUpdatedByDTOs();
        if(!partView.getPartFinderModel().getUpdatedByDTOs().isEmpty()) {
            for (UpdatedByDTO dto : dtoList) {
                String changes = dto.getChangeMade() == null ? "" : " Changes: " + dto.getChangeMade();
                vBox.getChildren().add(new Label(dto.getUpdatedBy() + " " + dto.getUpdatedDateTime() + changes));
            }
        } else {
            vBox.getChildren().add(new Label("No updated by"));
        }
    }

    public void refresh() {
        this.vBox.getChildren().clear();
        addEditHistory();
        toggleSwitch.setSelected(!partView.getPartFinderModel().selectedSpareProperty().get().getArchived());
    }
}
