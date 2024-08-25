package com.L2.mvci_note;

import com.L2.dto.PartOrderDTO;
import com.L2.mvci_note.components.*;
import com.L2.widgetFx.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.util.Builder;
import javafx.scene.control.*;

import java.util.function.Consumer;

public class NoteView implements Builder<Region> {
    private final NoteModel noteModel;
    private final Consumer<NoteMessage> action;
    private final ServicePlan servicePlan;
    private final BasicInformation basicInformation;
    private final ServicePlanDetails servicePlanDetails;
    private final DateTimePicker dateTimePicker;
    private final SiteInformation siteInformation;
    private final WorkOrderBox workOrderBox;

    public NoteView(NoteModel noteModel, Consumer<NoteMessage> message) {
        this.noteModel = noteModel;
        this.action = message;
        this.basicInformation = new BasicInformation(this);
        this.servicePlan = new ServicePlan(this);
        this.servicePlanDetails = new ServicePlanDetails(this);
        this.dateTimePicker = new DateTimePicker(this);
        this.siteInformation = new SiteInformation(this);
        this.workOrderBox = new WorkOrderBox(this);
    }

    @Override
    public Region build() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(setMainVBox());
        setUpStatusBarCommunication();
        return scrollPane;
    }

    // writing to this StringProperty will automatically update the status bar
    private void setUpStatusBarCommunication() {
        noteModel.statusLabelProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                action.accept(NoteMessage.STATUS_BAR_CHANGE);
            }
        });
    }

    private Node setMainVBox() {
        VBox vBox = VBoxFx.of(true, 10, new Insets(10, 0, 20, 20));
        HBox hBox = new HBox();
        hBox.getChildren().addAll(basicInformation.build(), servicePlan.build(), setBox3Info());
        vBox.getChildren().addAll(hBox, setIssueBox(), workOrderBox.build());
        for(PartOrderDTO partOrderDTO: noteModel.getCurrentNote().getPartOrders()) {
            vBox.getChildren().add(new PartOrderBox(this, partOrderDTO));
        }
        vBox.getChildren().add(rowThreeBox());
        return vBox;
    }

    private Node setBox3Info() {
        VBox vBox = new VBox(20);
        vBox.setPadding(new Insets(0, 0, 0, 40));
        vBox.getChildren().addAll(dateTimePicker.build(), servicePlanDetails.build());
        return vBox;
    }

    private Node setIssueBox() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 5, 5, 5));
        vBox.getStyleClass().add("decorative-hbox");
        Label label = new Label("Issue");
        TextArea textAreaIssue = TextAreaFx.of(true, 200, 16, 5);
        textAreaIssue.setPrefWidth(900);
        textAreaIssue.setText(noteModel.getCurrentNote().issueProperty().get());
        ListenerFx.addFocusListener(textAreaIssue, "Issue field", noteModel.getCurrentNote().issueProperty(), noteModel.statusLabelProperty());
        vBox.getChildren().addAll(label, textAreaIssue);
        return vBox;
    }

    private Node rowThreeBox() {
        HBox hBox = new HBox(10);
        hBox.getChildren().add(siteInformation.build());
        return hBox;
    }

    // CLASS GETTERS
    public NoteModel getNoteModel() {
        return noteModel;
    }

    public ServicePlanDetails getServicePlanDetails() {
        return servicePlanDetails;
    }
}
