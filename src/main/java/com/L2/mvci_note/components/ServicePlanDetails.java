package com.L2.mvci_note.components;

import atlantafx.base.theme.Styles;
import com.L2.mvci_note.NoteModel;
import com.L2.mvci_note.NoteView;
import com.L2.widgetFx.RegionFx;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;


public class ServicePlanDetails implements Builder<Region> {

    private final NoteView noteView;
    private final NoteModel noteModel;
    private VBox vBox;

    public ServicePlanDetails(NoteView noteView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
    }

    @Override
    public Region build() {
        this.vBox = new VBox();
//        updateDetails();
        return vBox;
    }

    public void updateDetails() {
        vBox.getChildren().clear();
        Label label = new Label(noteModel.getCurrentEntitlement().getName());
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ff0000;");
        Label label1 = new Label("Includes");
        label1.getStyleClass().add(Styles.TEXT_BOLD);
        String[] includes = noteModel.getCurrentEntitlement().getIncludes().split("\\R");
        String[] notIncludes = noteModel.getCurrentEntitlement().getNotIncludes().split("\\R");
        vBox.getChildren().addAll(label, label1);
        for (String include : includes) {
            vBox.getChildren().add(new Label(include));
        }
        Label label2 = new Label("Does not include:");
        label2.getStyleClass().add(Styles.TEXT_BOLD);
        vBox.getChildren().addAll(RegionFx.regionHeightOf(15), label2);
        for (String notInclude : notIncludes) {
            vBox.getChildren().add(new Label(notInclude));
        }
    }
    
}
