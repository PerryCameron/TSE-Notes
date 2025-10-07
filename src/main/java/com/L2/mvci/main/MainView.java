package com.L2.mvci.main;

import com.L2.mvci.main.components.TitleBar;
import com.L2.static_tools.ImageResources;
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
        // borderPane.setStyle("-fx-border-color: #878484; -fx-border-width: 1;"); // moved to css
        borderPane.getStyleClass().add("main-border-pane"); // Assign a style class
        return borderPane;
    } // I want to move the style for this to my dark.css and light.css files

    private Node setUpBottomPane() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_LEFT);
        Button prevNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.PREVIOUS_NOTE), ImageResources.DOWN, "Previous");
        Button nextNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.NEXT_NOTE), ImageResources.UP, "Next");
        nextNoteButton.disableProperty().bind(mainModel.nextButtonDisabledProperty());
        Button newNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.NEW_NOTE), ImageResources.NEW, "New Note");
        Button cloneButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.CLONE_NOTE), ImageResources.CLONE, "Clone Note");
        Button deleteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.DELETE_NOTE), ImageResources.DELETE, "Delete Note");
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
        action.accept(MainMessage.UPDATE_NOTE_TAB_NAME);
        action.accept(MainMessage.UPDATE_STATUSBAR_WITH_STRING);
    }

    protected void addNewTab(String name, Region region, boolean closeable, Image image) {
        if(!tabIsOpen(name)) {
            Tab newTab = new Tab(name, region);
            ImageView imageViewCopy = new ImageView(image);
            newTab.setGraphic(imageViewCopy);
            if (name.equals("Note")) mainModel.noteTabProperty().set(newTab);
            newTab.setClosable(closeable);
            mainModel.mainTabPaneProperty().get().getTabs().add(newTab);
            mainModel.mainTabPaneProperty().get().getSelectionModel().select(newTab);
        }
    }

    private boolean tabIsOpen(String name) {
        return mainModel.mainTabPaneProperty().get().getTabs()
                .stream()
                .filter(tab -> tab.getText().equals(name))
                .findFirst()
                .isPresent();
    }

    public Consumer<MainMessage> getAction() {
        return action;
    }
}