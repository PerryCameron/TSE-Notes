package com.L2.mvci_note;

import com.L2.dto.PartOrderDTO;
import com.L2.mvci_note.components.*;
import com.L2.widgetFx.*;
import javafx.collections.ListChangeListener;
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
    }

    @Override
    public Region build() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
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
        VBox vBox = VBoxFx.of(true, 10, new Insets(10, 20, 20, 20));
        HBox hBox = new HBox();
        hBox.getChildren().addAll(basicInformation.build(), setBox3Info());
        vBox.getChildren().addAll(hBox, issueBox.build(), partOrderHeader.build(), partOrders(), rowThreeBox(), controls());
        return vBox;
    }

    private Node controls() {
        HBox hBox = new HBox();
        Button button = new Button("Customer Request");
        button.setOnAction(event -> {
            action.accept(NoteMessage.COPY_CUSTOMER_REQUEST);
        });
        Button button1 = new Button("Corrective Action");
        button1.setOnAction(event -> {
           action.accept(NoteMessage.COPY_CORRECTIVE_ACTION);
        });
        hBox.getChildren().addAll(button, button1);
        return hBox;
    }

    private Node partOrders() {
        VBox vBox = new VBox(10);
        for(PartOrderDTO partOrderDTO: noteModel.getCurrentNote().getPartOrders()) {
            vBox.getChildren().add(new PartOrderBox(partOrderDTO, this));
        }
        noteModel.getCurrentNote().getPartOrders().addListener((ListChangeListener<PartOrderDTO>) change -> {
            while (change.next()) {
                if(change.wasAdded()) {
                    vBox.getChildren().add(new PartOrderBox(noteModel.getCurrentNote().getPartOrders().getLast(),this));
                }
                action.accept(NoteMessage.REPORT_NUMBER_OF_PART_ORDERS);
            }
        });
        return vBox;
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
}
