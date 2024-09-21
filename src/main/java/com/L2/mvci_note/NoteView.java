package com.L2.mvci_note;

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
    private final BasicInformation basicInformation;
    private final ServicePlanDetails servicePlanDetails;
    private final DateTimePicker dateTimePicker;
    private final ShippingInformation shippingInformation;
    private final WorkOrderBox workOrderBox;
    private final IssueBox issueBox;
    private final PartOrderHeader partOrderHeader;
    private final FinishBox finishBox;
    private final PartOrderBoxList partOrderBoxList;

    public NoteView(NoteModel noteModel, Consumer<NoteMessage> message) {
        this.noteModel = noteModel;
        this.action = message;
        this.basicInformation = new BasicInformation(this);
        this.servicePlanDetails = new ServicePlanDetails(this);
        this.dateTimePicker = new DateTimePicker(this);
        this.shippingInformation = new ShippingInformation(this);
        this.workOrderBox = new WorkOrderBox(this);
        this.issueBox = new IssueBox(this);
        this.partOrderHeader = new PartOrderHeader(this);
        this.partOrderBoxList = new PartOrderBoxList(this);
        this.finishBox = new FinishBox(this);
    }

    @Override
    public Region build() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(setMainVBox());
        setUpStatusBarCommunication();
        refreshBoundNoteListener();
        boundNoteListener();
        return scrollPane;
    }

    private void refreshBoundNoteListener() {
        noteModel.refreshBoundNoteProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue != true) {
               partOrderBoxList.refreshFields();
               dateTimePicker.refreshFields();
               basicInformation.refreshFields();
           }
        });
    }

    private void boundNoteListener() {
        noteModel.getBoundNote().idProperty().addListener((observable, oldValue, newValue) -> {
            // sends signal to noteListener to add part orders to currently selected NoteDTO
            action.accept(NoteMessage.REFRESH_PART_ORDERS);
            // sends signal to noteListInteractor to select current NoteDTO
            action.accept(NoteMessage.SELECT_BOUND_NOTE_IN_TABLE);
            // sends signal to mainInteractor to change note tab name
            action.accept(NoteMessage.UPDATE_NOTE_TAB_NAME);
        });
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
        VBox vBox = VBoxFx.of(true, 10, new Insets(10, 20, 20, 20));
        HBox hBox = new HBox();
        hBox.getChildren().addAll(basicInformation.build(), setBox3Info());
        vBox.getChildren().addAll(
                hBox,
                issueBox.build(),
                partOrderHeader.build(),
                partOrderBoxList.build(),
                rowThreeBox(),
                finishBox.build());
        return vBox;
    }

    public void flashGroupA() {
        basicInformation.flash();
        dateTimePicker.flash();
        issueBox.flash();
        partOrderBoxList.flash();
        shippingInformation.flash();
    }

    public void flashGroupB() {
        workOrderBox.flash();
        partOrderBoxList.flash();
    }

    private Node setBox3Info() {
        VBox vBox = new VBox(20);
        vBox.setPadding(new Insets(0, 0, 0, 40));
        vBox.getChildren().addAll(dateTimePicker.build(), servicePlanDetails.build());
        return vBox;
    }

    private Node rowThreeBox() {
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(shippingInformation.build(), workOrderBox.build());
        return hBox;
    }

    // CLASS GETTERS
    public NoteModel getNoteModel() {
        return noteModel;
    }

    public ServicePlanDetails getServicePlanDetails() {
        return servicePlanDetails;
    }

    public Consumer<NoteMessage> getAction() {
        return action;
    }

    public PartOrderBoxList getPartOrderBoxList() {
        return partOrderBoxList;
    }
}
