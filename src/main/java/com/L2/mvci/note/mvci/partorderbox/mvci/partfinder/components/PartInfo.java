package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components;

import org.controlsfx.control.ToggleSwitch;
import com.L2.dto.UpdatedByDTO;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderMessage;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderView;
import javafx.scene.Node;
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

    public PartInfo(PartFinderView partView) {
        this.partView = partView;
    }

    @Override
    public Pane build() {
        this.vBox = new VBox();
        this.hBox = new HBox(20);
        addEditHistory();
        partView.getPartFinderModel().refreshPartInfoProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) refresh();
        });
        hBox.getChildren().addAll(vBox, addToggleSwitch());
        return hBox;
    }

    public Node addToggleSwitch() {
        // procure our toggle switch
        ToggleSwitch toggleSwitch = partView.getPartFinderModel().inSparesToggleProperty().get();
        // bind to our model
        toggleSwitch.selectedProperty().bindBidirectional(partView.getPartFinderModel().inSparesTogSwitchProperty());
        // find if selected spare is archived (we take the opposite)
        boolean inSpares = !partView.getPartFinderModel().selectedSpareProperty().get().getArchived();
        // match toggle switch to status of spare part we are viewing
        partView.getPartFinderModel().inSparesTogSwitchProperty().set(inSpares);
        // lets us know if we are using part in catalogue toggle button (prevents conflict when changing spares we are viewing)
        toggleSwitch.hoverProperty().addListener((observable, wasHovering, isHovering) -> {
            if (isHovering) {
                partView.getPartFinderModel().blockToggleSwitchProperty().set(true);
            } else {
                partView.getPartFinderModel().blockToggleSwitchProperty().set(false);
            }
        });
        // listener
        partView.getPartFinderModel().inSparesTogSwitchProperty().addListener((observable, oldValue, newValue) -> {
            // this is to prevent this from happening when we select a new part
            if(partView.getPartFinderModel().blockToggleSwitchProperty().get()) {
                // new value must have not(!) in front because archived is reverse to our use.
                partView.getPartFinderModel().selectedSpareProperty().get().setArchived(!newValue);
                partView.getAction().accept(PartFinderMessage.UPDATE_IN_CATELOGUE);
            }
        });
        return toggleSwitch;
    }

    public void addEditHistory() {
        List<UpdatedByDTO> dtoList = partView.getPartFinderModel().getUpdatedByDTOs();
        if (!partView.getPartFinderModel().getUpdatedByDTOs().isEmpty()) {
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
        ToggleSwitch toggleSwitch = partView.getPartFinderModel().inSparesToggleProperty().get();
//        toggleSwitch.setSelected(!partView.getPartFinderModel().selectedSpareProperty().get().getArchived());
    }
}
