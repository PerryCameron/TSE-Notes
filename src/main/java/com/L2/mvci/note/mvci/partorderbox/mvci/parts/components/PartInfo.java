package com.L2.mvci.note.mvci.partorderbox.mvci.parts.components;

import com.L2.dto.UpdatedByDTO;
import com.L2.mvci.note.mvci.partorderbox.mvci.parts.PartView;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;



public class PartInfo implements Builder<Pane> {

    private final PartView partView;

    public PartInfo(PartView partView) {
        this.partView = partView;
    }

    @Override
    public Pane build() {
        VBox vBox = new VBox();
        java.util.List<UpdatedByDTO> dtoList = partView.getPartModel().getUpdatedByDTOs();
        if(!partView.getPartModel().getUpdatedByDTOs().isEmpty()) {
            System.out.println("not empty");
            for (UpdatedByDTO dto : dtoList) {
                vBox.getChildren().add(new Label(dto.getUpdatedBy() + " " + dto.getUpdatedDateTime()));
            }
        } else {
            System.out.println("empty");
        }
        return vBox;
    }
}
