package com.L2.mvci_note;

import com.L2.dto.PartOrderDTO;
import com.L2.mvci_note.components.*;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.util.Builder;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.function.Consumer;

public class NoteView implements Builder<Region> {
    private final NoteModel noteModel;
    private final Consumer<NoteMessage> action;
    private final BasicInformation basicInformation;
    private final ServicePlanDetails servicePlanDetails;
    private final DateTimePicker dateTimePicker;
    private final ShippingInformation shippingInformation;
    private final WorkOrderBox workOrderBox;

    public NoteView(NoteModel noteModel, Consumer<NoteMessage> message) {
        this.noteModel = noteModel;
        this.action = message;
        this.basicInformation = new BasicInformation(this);
        this.servicePlanDetails = new ServicePlanDetails(this);
        this.dateTimePicker = new DateTimePicker(this);
        this.shippingInformation = new ShippingInformation(this);
        this.workOrderBox = new WorkOrderBox(this);
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
        vBox.getChildren().addAll(hBox, setIssueBox(), workOrderBox.build(), partOrders(), rowThreeBox(), controls());
        return vBox;
    }

    private Node controls() {
        HBox hBox = new HBox();
        Button button = new Button("Customer Request");
        button.setOnAction(event -> {
            action.accept(NoteMessage.COPY_CUSTOMER_REQUEST);
        });
        hBox.getChildren().add(button);
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

    private Node setIssueBox() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 5, 5, 5));
        vBox.getStyleClass().add("decorative-hbox");
        TextArea textAreaIssue = TextAreaFx.of(true, 200, 16, 5);
        textAreaIssue.setPrefWidth(900);
        textAreaIssue.setText(noteModel.getCurrentNote().issueProperty().get());
        ListenerFx.addFocusListener(textAreaIssue, "Issue field", noteModel.getCurrentNote().issueProperty(), noteModel.statusLabelProperty());
        String[] boxInfo = {"Issue","Copy Issue"};
        vBox.getChildren().addAll(TitleBarFx.of(boxInfo, () -> {
            vBox.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-radius: 5px");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.setStyle("")); // Reset the style
            pause.play();
            action.accept(NoteMessage.COPY_ISSUE);
        }), textAreaIssue);
        return vBox;
    }

    private Node rowThreeBox() {
        HBox hBox = new HBox(10);
        hBox.getChildren().add(shippingInformation.build());
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
