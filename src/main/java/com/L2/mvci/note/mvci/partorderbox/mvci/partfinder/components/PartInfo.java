package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components;

import com.L2.dto.UpdatedByDTO;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderView;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;



public class PartInfo implements Builder<Pane> {

    private final PartFinderView partView;
    private VBox vBox;

    public PartInfo(PartFinderView partView) {
        this.partView = partView;
    }

    @Override
    public Pane build() {
        this.vBox = new VBox();
//        vBox.getChildren().add(new Label("Part is in catalogue: " + partView.getPartModel().selectedSpareProperty().get().getArchived()));
        System.out.println(partView.getPartModel().selectedSpareProperty().get());
        addEditHistory();
        partView.getPartModel().refreshPartInfoProperty().addListener((observable, oldValue, newValue) -> {
            refresh();
        });
        return vBox;
    }

    public void addEditHistory() {
        java.util.List<UpdatedByDTO> dtoList = partView.getPartModel().getUpdatedByDTOs();
        if(!partView.getPartModel().getUpdatedByDTOs().isEmpty()) {
            System.out.println("not empty");
            for (UpdatedByDTO dto : dtoList) {
                vBox.getChildren().add(new Label(dto.getUpdatedBy() + " " + dto.getUpdatedDateTime()));
            }
        } else {
            System.out.println("empty");
        }
    }

    public void refresh() {
        this.vBox.getChildren().clear();
        addEditHistory();
    }
}
