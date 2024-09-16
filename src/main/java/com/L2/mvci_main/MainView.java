package com.L2.mvci_main;

import com.L2.mvci_main.components.TitleBar;
import com.L2.mvci_note.NoteMessage;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.MenuFx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

import java.util.function.Consumer;

public class MainView implements Builder<Region> {
    private final MainModel mainModel;
    Consumer<MainMessage> action;

    public MainView(MainModel mainModel, Consumer<MainMessage> m) {
        this.mainModel = mainModel;
        action = m;
    }

    @Override
    public Region build() {
        VBox root = new VBox();
        BorderPane borderPane = new BorderPane();
//        borderPane.setPrefSize(1028, 830);
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
        ToolBar toolbar = new ToolBar();
        topElements.getChildren().add(toolbar);
        return topElements;
    }

    private Node setUpMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setPadding(new Insets(0, 0, 0, 0));
        menuBar.setMaxHeight(15);
        menuBar.getMenus().addAll(createFileMenu(), createEditMenu(), createDebugMenu());
        return menuBar;
    }

    private Menu createEditMenu() {
        Menu menu = new Menu("Edit");
        MenuItem undo = MenuFx.menuItemOf("Undo", x -> System.out.println("undo"), KeyCode.Z);
        MenuItem redo = MenuFx.menuItemOf("Redo", x -> System.out.println("Redo"), KeyCode.R);
        SeparatorMenuItem editSeparator = new SeparatorMenuItem();
        MenuItem cut = MenuFx.menuItemOf("Cut", x -> System.out.println("Cut"), KeyCode.X);
        MenuItem copy = MenuFx.menuItemOf("Copy", x -> System.out.println("Copy"), KeyCode.C);
        MenuItem paste = MenuFx.menuItemOf("Paste", x -> System.out.println("Paste"), KeyCode.V);
        menu.getItems().addAll(undo, redo, editSeparator, cut, copy, paste);
        return menu;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("File");
//        MenuItem openNewCase = MenuFx.menuItemOf("Open New Case", x -> action.accept(MainMessage.OPEN_NOTES), null);
        MenuItem openNotesList = MenuFx.menuItemOf("Open Notes List", x -> action.accept(MainMessage.OPEN_NOTES_LIST), null);
        MenuItem close = MenuFx.menuItemOf("Settings", x -> action.accept(MainMessage.OPEN_SETTINGS), null);
        menu.getItems().addAll(close, openNotesList);
        return menu;
    }

    private Menu createDebugMenu() {
        Menu menu = new Menu("Debug");
//        MenuItem findDebugLog = new MenuItem("Find Debug Log folder");
//        findDebugLog.setOnAction(e -> showDebugLogFolder());
        MenuItem showDebugLog = MenuFx.menuItemOf("Show Log", x -> action.accept(MainMessage.SHOW_LOG), null);
//        showDebugLog.setOnAction(event -> showDebugLog());
        menu.getItems().add(showDebugLog);
        return menu;
    }

    private Node setUpBottomPane() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_LEFT);
        hBox.setStyle("-fx-background-color: grey");
        Button prevNoteButton = ButtonFx.utilityButton( () -> {
            System.out.println("<- Previous Note Button clicked");
            action.accept(MainMessage.PREVIOUS_NOTE);
        }, "Previous", "/images/back-16.png");
        Button nextNoteButton = ButtonFx.utilityButton( () -> {
            System.out.println("-> Next Note Button clicked");
            action.accept(MainMessage.NEXT_NOTE);
        }, "Next", "/images/forward-16.png");
        Button setCompletedButton = ButtonFx.utilityButton( () -> {
            action.accept(MainMessage.SET_COMPLETE);
        }, "Set Completed", "/images/thumbs-16.png");

        Button newNoteButton = ButtonFx.utilityButton( () -> {
            action.accept(MainMessage.NEW_NOTE);
        }, "New Note", "/images/new-16.png");

        Button saveNoteButton = ButtonFx.utilityButton( () -> {
            action.accept(MainMessage.SAVE_NOTE);
        }, "Save", "/images/Save-16.png");

        Button testButton = ButtonFx.utilityButton( () -> {
            action.accept(MainMessage.TEST);
        }, "Test Dammit", "/images/apply-16.png");
        hBox.getChildren().addAll(statusLabel(),prevNoteButton, nextNoteButton, setCompletedButton, newNoteButton, saveNoteButton, testButton);
        hBox.getStyleClass().add("bottom-pane");
        return hBox;
    }

    private Node statusLabel() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(400);
        Label statusLabel = new Label();
        statusLabel.setPadding(new Insets(5.0f, 5.0f, 5.0f, 5.0f));
        statusLabel.textProperty().bind(mainModel.statusLabelProperty());
        mainModel.statusLabelProperty().set("(Not Connected) Ready.");
        vBox.getChildren().add(statusLabel);
        return vBox;
    }

    private Node setUpCenterPane() {
        TabPane tabPane = new TabPane();
        mainModel.setMainTabPane(tabPane);
        action.accept(MainMessage.OPEN_NOTES);
//        action.accept(MainMessage.OPEN_SETTINGS);
        return tabPane;
    }

    protected void addNewTab(String name, Region region, boolean closeable) {
        Tab newTab = new Tab(name, region);
        newTab.setClosable(closeable);
        mainModel.getMainTabPane().getTabs().add(newTab);
        mainModel.getMainTabPane().getSelectionModel().select(newTab);
    }
}