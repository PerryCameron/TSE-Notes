package com.L2.mvci.main;

import com.L2.mvci.main.components.TitleBar;
import com.L2.widgetFx.ButtonFx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.Objects;
import java.util.function.Consumer;

public class MainView implements Builder<Region> {
    private final MainModel mainModel;
    Consumer<MainMessage> action;
//    private static final Logger logger = LoggerFactory.getLogger(MainView.class);

    public MainView(MainModel mainModel, Consumer<MainMessage> m) {
        this.mainModel = mainModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new TitleBar(this, borderPane).build()); // <- here is where I create the Menu, what if we used boarderPane instead of scene?
        borderPane.setCenter(setUpCenterPane());
        borderPane.setBottom(setUpBottomPane());
        borderPane.setStyle("-fx-border-color: #878484; -fx-border-width: 1;");
        return borderPane;
    }

    private Node setUpBottomPane() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_LEFT);
        Button prevNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.PREVIOUS_NOTE), "Previous", "/images/down-16.png");
        Button nextNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.NEXT_NOTE), "Next", "/images/up-16.png");
        nextNoteButton.disableProperty().bind(mainModel.nextButtonDisabledProperty());
        Button newNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.NEW_NOTE), "New Note", "/images/create-16.png");
        Button cloneButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.CLONE_NOTE), "Clone Note", "/images/clone-16.png");
        Button deleteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.DELETE_NOTE), "Delete Note", "/images/delete-16.png");
//        Button refreshButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.REFRESH_NOTE_TABLEVIEW), "Refresh", "/images/thumbs-16.png");
//        Button sortButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.SORT_NOTE_TABLEVIEW), "sort", "/images/thumbs-16.png");
        hBox.getChildren().addAll(statusLabel(), prevNoteButton, nextNoteButton, newNoteButton, cloneButton, deleteButton);
        hBox.getStyleClass().add("bottom-pane");
        return hBox;
    }

    private Node statusLabel() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(400);
        Label statusLabel = new Label();
        statusLabel.setPadding(new Insets(5.0f, 5.0f, 5.0f, 5.0f));
        statusLabel.textProperty().bind(mainModel.statusStringProperty());
        vBox.getChildren().add(statusLabel);
        return vBox;
    }

    private Node setUpCenterPane() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(4, 0, 0, 0));
        vBox.getStyleClass().add("center-pane");
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: white");
        mainModel.mainTabPaneProperty().set(tabPane);
        launchNormal();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                if (newTab.getText().equals("Manage Notes")) action.accept(MainMessage.SORT_NOTE_TABLEVIEW);
            }
        });
        vBox.getChildren().add(tabPane);
        return vBox;
    }

    private void launchNormal() {
        action.accept(MainMessage.OPEN_NOTE_TAB);
        action.accept(MainMessage.OPEN_NOTESLIST_TAB);
        action.accept(MainMessage.SELECT_NOTE_TAB);
        // this is a message for the tableView to select the correct row to match selected NoteDTO
        action.accept(MainMessage.SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT);
        // obviously to put the correct number on the tab
        action.accept(MainMessage.UPDATE_NOTE_TAB_NAME);
        action.accept(MainMessage.UPDATE_STATUSBAR_WITH_STRING);
    }

    protected void addNewTab(String name, Region region, boolean closeable, String image) {
        Tab newTab = new Tab(name, region);
        if (!image.isEmpty()) {
            Image copyIcon = new Image(Objects.requireNonNull(ButtonFx.class.getResourceAsStream(image)));
            ImageView imageViewCopy = new ImageView(copyIcon);
            newTab.setGraphic(imageViewCopy);
        }
        if (name.equals("Note")) mainModel.noteTabProperty().set(newTab);
        newTab.setClosable(closeable);
        mainModel.mainTabPaneProperty().get().getTabs().add(newTab);
        mainModel.mainTabPaneProperty().get().getSelectionModel().select(newTab);
    }

    public Consumer<MainMessage> getAction() {
        return action;
    }
}