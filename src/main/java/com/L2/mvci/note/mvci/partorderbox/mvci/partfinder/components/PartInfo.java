package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components;

import com.L2.dto.UpdatedByDTO;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderView;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.List;


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
            if(newValue) refresh();
        });
        return vBox;
    }

    public void addEditHistory() {
        List<UpdatedByDTO> dtoList = partView.getPartModel().getUpdatedByDTOs();
        if(!partView.getPartModel().getUpdatedByDTOs().isEmpty()) {
            System.out.println("list size is " + dtoList.size());
            for (UpdatedByDTO dto : dtoList) {
                String changes = dto.getChangeMade() == null ? "" : " Changes: " + dto.getChangeMade();
                vBox.getChildren().add(new Label(dto.getUpdatedBy() + " " + dto.getUpdatedDateTime() + changes));
            }
        } else {
            System.out.println("No updated by");
            vBox.getChildren().add(new Label("No updated by"));
        }
    }

    public void refresh() {
        this.vBox.getChildren().clear();
        System.out.println("Refreshing part info view");
        addEditHistory();
    }
}
