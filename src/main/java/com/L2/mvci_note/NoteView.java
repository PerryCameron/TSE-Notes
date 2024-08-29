package com.L2.mvci_note;

import com.L2.dto.PartOrderDTO;
import com.L2.mvci_note.components.*;
import com.L2.widgetFx.*;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.Objects;
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
        // for some reason this vBox is not filling the entire scrollPane that it is set in
        VBox vBox = VBoxFx.of(true, 10, new Insets(10, 20, 20, 20));
//        vBox.setStyle("-fx-background-color: #feffab;");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(setBasicInformation(), setBox3Info());
        vBox.getChildren().addAll(hBox, setIssueBox(), workOrderBox.build(), partOrders(), rowThreeBox());
        return vBox;
    }

    private Node setBasicInformation() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0, 5, 5, 5));
        hBox.getStyleClass().add("decorative-hbox");
        hBox.getChildren().addAll(basicInformation.build(), servicePlan.build());
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
        vBox.getChildren().addAll(clockBox(), servicePlanDetails.build());
        return vBox;
    }

    private Node clockBox() {
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.getStyleClass().add("decorative-hbox");
        Button copyButton = new Button();
        Image copyIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/copy-16.png")));
        ImageView imageViewCopy = new ImageView(copyIcon);
        copyButton.setGraphic(imageViewCopy);
        copyButton.getStyleClass().add("invisible-button");
        copyButton.setOnAction(e -> {
//            noteModel.getCurrentNote().setSelectedPartOrder(partOrderDTO);
//            noteView.getAction().accept(NoteMessage.COPY_PART_ORDER);
            // Apply a blue border to the VBox
            vBox.setStyle("-fx-border-color: blue; -fx-border-width: 2px; -fx-border-radius: 5px");
            // Use a PauseTransition to remove the border after 0.5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
            pause.setOnFinished(event -> vBox.setStyle("")); // Reset the style
            pause.play();
            action.accept(NoteMessage.COPY_NAME_DATE);
        });
        HBox hBox = new HBox();
        hBox.getChildren().add(copyButton);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(0, 5, 0, 0));
        vBox.getChildren().addAll(hBox ,dateTimePicker.build());
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

    public Consumer<NoteMessage> getAction() {
        return action;
    }
}
