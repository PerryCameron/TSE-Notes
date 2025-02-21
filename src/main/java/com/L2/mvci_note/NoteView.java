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
    private final Subject subject;
    private final BasicInformation basicInformation;
    private final ServicePlanDetails servicePlanDetails;
    private final DateTimePicker dateTimePicker;
    private final ShippingInformation shippingInformation;
    private final RelatedBox relatedBox;
    private final IssueBox issueBox;
    private final PartOrderHeader partOrderHeader;
    private final FinishBox finishBox;
    private final PartOrderBoxList partOrderBoxList;


    public NoteView(NoteModel noteModel, Consumer<NoteMessage> message) {
        this.noteModel = noteModel;
        this.action = message;
        this.subject = new Subject(this);
        this.basicInformation = new BasicInformation(this);
        this.servicePlanDetails = new ServicePlanDetails(this);
        this.dateTimePicker = new DateTimePicker(this);
        this.shippingInformation = new ShippingInformation(this);
        this.relatedBox = new RelatedBox(this);
        this.issueBox = new IssueBox(this);
        this.partOrderHeader = new PartOrderHeader(this);
        this.partOrderBoxList = new PartOrderBoxList(this);
        this.finishBox = new FinishBox(this);
    }

    @Override
    public Region build() {
        VBox vBox = VBoxFx.of(true, 10, new Insets(10, 5, 0, 0));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(setMainVBox());
        setUpStatusBarCommunication();
        refreshBoundNoteListener();
        boundNoteListener();
//        noteTabOpenListener();
        vBox.getChildren().add(scrollPane);
        return vBox;
    }

    private void refreshBoundNoteListener() {
        noteModel.refreshBoundNoteProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue != true) {
               partOrderBoxList.refreshFields();
               dateTimePicker.refreshFields();
               basicInformation.refreshFields();
               issueBox.refreshFields();
           }
        });
    }

    private void boundNoteListener() {
        noteModel.getBoundNote().idProperty().subscribe(() -> {
            // sends signal to noteListener to add part orders to currently selected NoteDTO
            action.accept(NoteMessage.REFRESH_PART_ORDERS);
            // sends signal to noteListInteractor to select current NoteDTO
            action.accept(NoteMessage.SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT);
            // sends signal to mainInteractor to change note tab name
            action.accept(NoteMessage.UPDATE_NOTE_TAB_NAME);
            // Enables / Disables next button
            action.accept(NoteMessage.CHECK_BUTTON_ENABLE);
        });
    }

    // writing to this StringProperty will automatically update the status bar
    private void setUpStatusBarCommunication() {
        noteModel.statusLabelProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                action.accept(NoteMessage.UPDATE_STATUSBAR);
            }
        });
    }

    private Node setMainVBox() {
        VBox vBox = VBoxFx.of(true, 10, new Insets(10, 20, 20, 20));
        vBox.getStyleClass().add("main-vbox");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(basicInformation.build(), setBox3Info());
        vBox.getChildren().addAll(
                subject.build(),
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
        relatedBox.flash();
        partOrderBoxList.flash();
        finishBox.flash();
    }

    public void flashGroupC() {
        basicInformation.flash();
        dateTimePicker.flash();
        issueBox.flash();
        partOrderBoxList.flash();
        shippingInformation.flash();
        relatedBox.flash();
        partOrderBoxList.flash();
        finishBox.flash();
    }

    private Node setBox3Info() {
        VBox vBox = new VBox(20);
        vBox.setPadding(new Insets(0, 0, 0, 40));
        vBox.getChildren().addAll(dateTimePicker.build(), servicePlanDetails.build());
        return vBox;
    }

    private Node rowThreeBox() {
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(shippingInformation.build(), relatedBox.build());
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
