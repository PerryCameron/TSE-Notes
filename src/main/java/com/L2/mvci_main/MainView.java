package com.L2.mvci_main;

import com.L2.BaseApplication;
import com.L2.mvci_main.components.TitleBar;
import com.L2.static_tools.VersionUtil;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.DialogueFx;
import com.L2.widgetFx.MenuFx;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

public class MainView implements Builder<Region> {
    private final MainModel mainModel;
    Consumer<MainMessage> action;
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);

    public MainView(MainModel mainModel, Consumer<MainMessage> m) {
        this.mainModel = mainModel;
        action = m;
    }

    @Override
    public Region build() {
        VBox root = new VBox();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(setUpTopPane());
        borderPane.setCenter(setUpCenterPane());
        borderPane.setBottom(setUpBottomPane());
        root.getChildren().addAll(new TitleBar(this).build(), borderPane);
        root.setStyle("-fx-border-color: #878484; -fx-border-width: 1;");
        return root;
    }

    private Node setUpTopPane() {
        VBox topElements = new VBox();
        topElements.getChildren().add(setUpMenuBar());
        return topElements;
    }

    private Node setUpMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(createFileMenu(), createDebugMenu(), createHelpMenu());
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("File");
        MenuItem close = MenuFx.menuItemOf("Settings", x -> action.accept(MainMessage.OPEN_SETTINGS), null);
        menu.getItems().addAll(close);
        return menu;
    }

    private Menu createDebugMenu() {
        Menu menu = new Menu("Debug");
        MenuItem showDebugLog = MenuFx.menuItemOf("Show Log", x -> action.accept(MainMessage.SHOW_LOG), null);
        menu.getItems().add(showDebugLog);
        return menu;
    }

    private Menu createHelpMenu() {
        Menu menu = new Menu("Help");
        String message = "Version: " + VersionUtil.getVersion()
                + "\nBuilt: " + VersionUtil.getBuildTimestamp()
                + "\nBundled JDK: " + VersionUtil.getJavaVersion();
        MenuItem showAboutDialogue = MenuFx.menuItemOf("About", x -> {
            Alert alert = DialogueFx.aboutDialogue("TSE Notes", message, Alert.AlertType.INFORMATION);
            alert.showAndWait();
        }, null);
        menu.getItems().add(showAboutDialogue);
        return menu;
    }

    private Node setUpBottomPane() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_LEFT);
        Button prevNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.PREVIOUS_NOTE), "Previous", "/images/back-16.png");
        Button nextNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.NEXT_NOTE), "Next", "/images/forward-16.png");
        nextNoteButton.disableProperty().bind(mainModel.nextButtonDisabledProperty());
        Button newNoteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.NEW_NOTE), "New Note", "/images/create-16.png");
        Button cloneButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.CLONE_NOTE), "Clone Note", "/images/clone-16.png");
        Button deleteButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.DELETE_NOTE), "Delete Note", "/images/delete-16.png");
//        Button refreshButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.REFRESH_NOTE_TABLEVIEW), "Refresh", "/images/thumbs-16.png");
//        Button sortButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.SORT_NOTE_TABLEVIEW), "sort", "/images/thumbs-16.png");
//        Button createDataBaseButton = ButtonFx.utilityButton(() -> action.accept(MainMessage.CREATE_DATABASE), "Create Database", "/images/thumbs-16.png");
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
        mainModel.setMainTabPane(tabPane);


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
        if (name.equals("Note")) mainModel.setNoteTab(newTab);
        newTab.setClosable(closeable);
        mainModel.getMainTabPane().getTabs().add(newTab);
        mainModel.getMainTabPane().getSelectionModel().select(newTab);
    }

    public Consumer<MainMessage> getAction() {
        return action;
    }
}