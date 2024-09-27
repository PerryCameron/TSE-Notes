package com.L2.mvci_main;

import com.L2.mvci_main.components.TitleBar;
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
        MenuItem openNotesList = MenuFx.menuItemOf("Open Notes List", x -> action.accept(MainMessage.OPEN_NOTESLIST_TAB), null);
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
        Button prevNoteButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.PREVIOUS_NOTE), "Previous", "/images/back-16.png");
        Button nextNoteButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.NEXT_NOTE), "Next", "/images/forward-16.png");
        Button newNoteButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.NEW_NOTE), "New Note", "/images/new-16.png");
        Button cloneButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.CLONE_NOTE), "Clone Note", "/images/clone-16.png");
        Button deleteButton = ButtonFx.utilityButton( () -> action.accept(MainMessage.DELETE_NOTE), "Delete Note", "/images/delete-16.png");
        hBox.getChildren().addAll(statusLabel(),prevNoteButton, nextNoteButton, newNoteButton, cloneButton, deleteButton);
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
        TabPane tabPane = new TabPane();
        mainModel.setMainTabPane(tabPane);
        // Open our initial tabs
        action.accept(MainMessage.OPEN_NOTE_TAB);
        action.accept(MainMessage.OPEN_NOTESLIST_TAB);
        action.accept(MainMessage.SELECT_NOTE_TAB);
        // this is a message for the tableView to select the correct row to match selected NoteDTO
        action.accept(MainMessage.SELECT_NOTE_IN_LIST_AND_SELECT_TABLEROW_WITH_IT);
        // obviously to put the correct number on the tab
        action.accept(MainMessage.UPDATE_NOTE_TAB_NAME);
        action.accept(MainMessage.UPDATE_STATUSBAR_WITH_STRING);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if(newTab != null) {
                if(newTab.getText().equals("Manage Notes")) action.accept(MainMessage.REFRESH_NOTE_TABLEVIEW);
            }
        });
        return tabPane;
    }

    protected void addNewTab(String name, Region region, boolean closeable) {
        Tab newTab = new Tab(name, region);
        if(name.equals("Note")) mainModel.setNoteTab(newTab);
        newTab.setClosable(closeable);
        mainModel.getMainTabPane().getTabs().add(newTab);
        mainModel.getMainTabPane().getSelectionModel().select(newTab);
    }

}