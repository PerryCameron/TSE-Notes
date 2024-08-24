package com.L2.mvci_note;

import com.L2.mvci_note.components.*;
import com.L2.widgetFx.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    private final PartTableView partTableView;
    private final SiteInformation siteInformation;

    public NoteView(NoteModel noteModel, Consumer<NoteMessage> message) {
        this.noteModel = noteModel;
        this.action = message;
        this.basicInformation = new BasicInformation(this);
        this.servicePlan = new ServicePlan(this);
        this.servicePlanDetails = new ServicePlanDetails(this);
        this.dateTimePicker = new DateTimePicker(this);
        this.partTableView = new PartTableView(this);
        this.siteInformation = new SiteInformation(this);
    }

    @Override
    public Region build() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(setCenter());
        setUpStatusBarCommunication();
        return scrollPane;
    }

    private void setUpStatusBarCommunication() {
        noteModel.statusLabelProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                action.accept(NoteMessage.STATUS_BAR_CHANGE);
            }
        });
    }

    private Node setCenter() {
        VBox vBox = VBoxFx.of(true, 10, new Insets(10, 20, 0, 20));
        HBox hBox = new HBox();
        hBox.getChildren().addAll(basicInformation.build(), servicePlan.build(), setBox3Info());
        vBox.getChildren().addAll(hBox, setIssueBox(), rowThreeBox());
        return vBox;
    }

    private Node rowThreeBox() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(siteInformation.build(), partTableView.build());
        return hBox;
    }

    private Node setBox3Info() {
        VBox vBox = new VBox(20);
        vBox.setPadding(new Insets(0, 0, 0, 40));
        vBox.getChildren().addAll(dateTimePicker.build(), servicePlanDetails.build());
        return vBox;
    }

    private Node setIssueBox() {
        VBox vBox = new VBox(4);
        Label lblIssue = new Label("Issue:");
        TextArea textAreaIssue = TextAreaFx.of(true, 200, 16, 5);
        textAreaIssue.setPrefWidth(980);
        textAreaIssue.setText(noteModel.getCurrentNote().issueProperty().get());
        ListenerFx.addFocusListener(textAreaIssue, "Issue field", noteModel.getCurrentNote().issueProperty(), noteModel.statusLabelProperty());
        vBox.getChildren().addAll(lblIssue, textAreaIssue);
        return vBox;
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }

    public ServicePlanDetails getServicePlanDetails() {
        return servicePlanDetails;
    }
}
