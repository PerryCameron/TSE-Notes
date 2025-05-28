package com.L2.mvci.parts;

import com.L2.BaseApplication;
import com.L2.dto.PartFx;
import com.L2.dto.ProductFamilyFx;
import com.L2.dto.global_spares.RangesFx;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.widgetFx.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PartView implements Builder<Alert> {
    private static final Logger logger = LoggerFactory.getLogger(PartView.class);

    private final Consumer<PartMessage> action;
    private final NoteView noteView;
    private final NoteModel noteModel;
    private final PartModel partModel;

    public PartView(NoteView noteView, PartModel partModel,  Consumer<PartMessage> message) {
        this.partModel = partModel;
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.action = message;
    }

    @Override
    public Alert build() {
        partModel.getAlert().setTitle("Search spares"); // Set custom title bar text
        // Since AlertType is set to NONE there is no close button which allows the x in the corner to
        // close the alert window. This listener fixes that.
        partModel.getAlert().showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                Stage stage = (Stage) partModel.getAlert().getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(event -> cleanAlertClose());
            }
        });
        partModel.getAlert().setDialogPane(createDialogPane());
        // here is the start of the UI
        partModel.getDialogPane().setContent(contentBox());
        DialogueFx.getTitleIcon(partModel.getDialogPane());
        DialogueFx.tieAlertToStage(partModel.getAlert(), partModel.getWidth(), 400);
        noteModel.numberInRangeProperty().addListener(rangeNumber -> partModel.getRangeNumberLabel()
                .setText("Spares in range: " + noteModel.numberInRangeProperty().get()));
        noteView.getAction().accept(NoteMessage.UPDATE_RANGE_COUNT);
        if (noteModel.selectedRangeProperty().get() != null)
            return partModel.getAlert();
        else return null;
    }

    private DialogPane createDialogPane() {
        partModel.getDialogPane().getStylesheets().add("css/light.css");
        partModel.getDialogPane().getStyleClass().add("decorative-hbox");
        partModel.getDialogPane().setPrefWidth(partModel.getWidth());
        partModel.getDialogPane().setMinWidth(partModel.getWidth()); // Ensure minimum width is 800
        return partModel.getDialogPane();
    }

    private Node partContainerButtonBox() {
        partModel.setPartContainerButtonBox(new HBox(5));
        partModel.getPartContainerButtonBox().setAlignment(Pos.CENTER_RIGHT);
        return partModel.getPartContainerButtonBox();
    }

    private Node partContainer() {
        partModel.setPartContainer(new VBox(10));
        // clears for new tableview
        partModel.getPartContainer().getChildren().clear();
        // make tableview with new list of parts
        partModel.setSparesTableView(SparesTableViewFx.createTableView(noteModel));
        // set one time listener to notice when table row is first selected
        initialize();
        // add new tableview to dialogue
        return partModel.getPartContainer();
    }

    private void initialize() {
        ChangeListener<SparesDTO> firstSelectionListener = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends SparesDTO> obs, SparesDTO oldSelection, SparesDTO newSelection) {
                if (newSelection != null) {
                    // adds more button in the middle
                    partModel.getPartContainerButtonBox().getChildren().add(1, partOrderButton());
                    partModel.getPartContainerButtonBox().getChildren().add(2, moreButton());
                    // Remove the listener after the first selection
                    partModel.getSparesTableView().getSelectionModel().selectedItemProperty().removeListener(this);
                }
            }
        };
        partModel.getSparesTableView().getSelectionModel().selectedItemProperty().addListener(firstSelectionListener);
    }

    private Node resultsLabelHbox() {
        partModel.setResultsLabelHbox(new HBox());
        HBox.setHgrow(partModel.getResultsLabelHbox(), Priority.ALWAYS);
        partModel.getResultsLabelHbox().setAlignment(Pos.CENTER_LEFT);
        partModel.getResultsLabelHbox().getChildren().addAll(noteModel.resultsLabelProperty().get(), partModel.getMessageLabel());
        return partModel.getResultsLabelHbox();
    }

    private Node buttonBox() {
        partModel.setButtonBox(new HBox(10, rangeBox(), searchBox(), cancelBox()));
        partModel.getButtonBox().setAlignment(Pos.CENTER_RIGHT);
        return partModel.getButtonBox();
    }

    private Node cancelBox() {
        partModel.setCancelHbox(new VBox());
        partModel.setCancelButton(new Button("Cancel"));
        partModel.getCancelHbox().setAlignment(Pos.CENTER_RIGHT);
        partModel.getCancelHbox().getChildren().add(partModel.getCancelButton());
        // Handle Cancel button
        partModel.getCancelButton().setOnAction(e -> cleanAlertClose());
        return partModel.getCancelHbox();
    }

    private Node contentBox() {
        partModel.setContent(new VBox(10));
        partModel.getContent().setPadding(new Insets(10, 10, 10, 10));
        partModel.getContent().setPrefWidth(partModel.getWidth());
        partModel.setMessageLabel(new Label("Part Search"));
        partModel.setSearchField(new TextField());
        partModel.getSearchField().setPromptText("Search Part Number or description...");
        partModel.getContent().getChildren().addAll(partModel.getMessageLabel(),
                partModel.getSearchField(), buttonBox(), partContainer(), partContainerButtonBox());
        return partModel.getContent();
    }

    private Control partOrderButton() {
        Button partOrderButton = new Button("Add to Part Order");
        partOrderButton.setOnAction(add -> {
            SparesDTO sparesDTO = partModel.getSparesTableView().getSelectionModel().getSelectedItem();
            if (sparesDTO != null) {
                noteView.getAction().accept(NoteMessage.INSERT_PART);
                PartFx partDTO = noteModel.selectedPartProperty().get();
                partDTO.setPartNumber(sparesDTO.getSpareItem());
                partDTO.setPartDescription(sparesDTO.getSpareDescription());
                // no need to put in part into FX UI here as it is being done elsewhere
                noteView.getAction().accept(NoteMessage.UPDATE_PART);
                // Refresh the table view layout and focus
                TableViewFx.focusOnLastItem(partModel.getPartsTableView());
                // make sure we clear our label out so that we get no memory leaks, the label continues to exist but not the hbox
                partModel.getResultsLabelHbox().getChildren().clear();
                cleanAlertClose();
            }
        });
        return partOrderButton;
    }

    private Node searchBox() {
        partModel.setRangeNumberLabel(new Label("Spares"));
        partModel.getRangeNumberLabel().setPadding(new Insets(0, 200, 0, 0));
        HBox searchHbox = new HBox();
        HBox.setHgrow(searchHbox, Priority.ALWAYS); // Spacer grows to push buttons right
        searchHbox.setAlignment(Pos.CENTER_RIGHT);
        searchHbox.getChildren().addAll(partModel.getRangeNumberLabel(), searchButton());
        return searchHbox;
    }

    private Control searchButton() {
        partModel.setSearchButton(new Button("Search"));
        partModel.getSearchButton().setOnAction(e -> {
            noteModel.searchWordProperty().set(partModel.getSearchField().getText().trim());
            if (!noteModel.searchWordProperty().get().isEmpty()) { // there are search terms
                if (partModel.getPartContainer().getChildren().isEmpty())
                    partModel.getPartContainer().getChildren().add(partModel.getSparesTableView());
                noteView.getAction().accept(NoteMessage.SEARCH_PARTS);
                // clear everything in part container in case we already made a search
                if (!partModel.searchedBeforeProperty().get()) {
                    partModel.getButtonBox().getChildren().remove(partModel.getCancelHbox());
                    partModel.getPartContainerButtonBox().getChildren().addAll(resultsLabelHbox(),
                            partModel.getCancelButton());
                }
                partModel.searchedBeforeProperty().set(true);
                partModel.getDialogPane().requestLayout();
                partModel.getAlert().getDialogPane().getScene().getWindow().sizeToScene();
            }
        });
        // this allows us the option to search by hitting enter instead of having to click the search button
        partModel.getSearchField().setOnAction(event -> {
            partModel.getSearchButton().requestFocus(); // Visually select the button
            partModel.getSearchButton().fire();
        });
        return partModel.getSearchButton();
    }

    private Control moreButton() {
        Button moreButton = new Button("More");
        partModel.setMoreInfoHbox(new HBox(5)); // moreInfoHbox
        moreButton.setOnAction(e -> {
            partModel.alertExtendedProperty().set(true);
            setSelectedChangeListener();
            partModel.getPartContainerButtonBox().getChildren().remove(2);
            createOrUpdateTreeView();
        });
        return moreButton;
    }

    private Node buttonStack(StackPane stackPane, Pane familyPane, Pane notePane, Pane keywordsPane, Pane infoPane) {
        VBox buttonStack = new VBox();
        buttonStack.setMinWidth(150);
        ToggleGroup toggleGroup = new ToggleGroup();

        ToggleButton familyButton = ButtonFx.toggleof("Product Families", 150, toggleGroup);
        ToggleButton noteButton = ButtonFx.toggleof("Note", 150, toggleGroup);
        ToggleButton keyWordsButton = ButtonFx.toggleof("Keywords", 150, toggleGroup);
        ToggleButton infoButton = ButtonFx.toggleof("Info", 150, toggleGroup);

        // Add all buttons to the VBox first
        buttonStack.getChildren().addAll(familyButton, noteButton, keyWordsButton, infoButton);

        // Set default content in the StackPane
        stackPane.getChildren().setAll(familyPane);

        // Set the default selected button AFTER adding to the scene graph
        familyButton.setSelected(true);

        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            stackPane.getChildren().clear();
            if (newToggle == null) {
                stackPane.getChildren().setAll(familyPane);
            } else if (newToggle == familyButton) {
                stackPane.getChildren().setAll(familyPane);
            } else if (newToggle == noteButton) {
                stackPane.getChildren().setAll(notePane);
            } else if (newToggle == keyWordsButton) {
                stackPane.getChildren().setAll(keywordsPane);
            } else if (newToggle == infoButton) {
                stackPane.getChildren().setAll(infoPane);
            }
        });

        return buttonStack;
    }


    private void createOrUpdateTreeView() {
        partModel.selectedSpareProperty().set(partModel.getSparesTableView().getSelectionModel().getSelectedItem());
        action.accept(PartMessage.JSON_MAP_PRODUCT_FAMILIES);
        if (partModel.getMoreInfoHbox().getChildren().isEmpty()) {
            // Initialize TreeView and add to HBox
            createTreeView();
        } else {
            // Update existing TreeView
            TreeItem<String> rootItem = createTreeItemRoot(partModel.getProductFamilies());
            partModel.getTreeView().setRoot(rootItem);
        }
    }

    private void createTreeView() {
        // Set the TreeView
        partModel.setTreeView(createProductFamilyTreeView());
        partModel.getTreeView().setPrefHeight(200); // Optional: Set size
        // Set up the StackPane
        partModel.setStackPane(new StackPane());
        // Create panes for each button
        Pane keywordPane = new VBox(HBoxFx.testBox("Note"));
        Pane infoPane = new VBox(HBoxFx.testBox("Info")); // Pane for infoButton
        // Set up the button stack and toggle group
        Node buttonStack = buttonStack(partModel.getStackPane(), familyPane(), notePane(), keywordPane, infoPane);
        // Add buttons and StackPane to the HBox
        partModel.getMoreInfoHbox().getChildren().addAll(buttonStack, partModel.getStackPane());
        partModel.getMoreInfoHbox().getStyleClass().add("inner-decorative-hbox");
        partModel.getMoreInfoHbox().setPadding(new Insets(10, 10, 10, 10));
        // Add HBox to content
        partModel.getContent().getChildren().add(3, partModel.getMoreInfoHbox());
        // Configure the Alert's DialogPane
        partModel.getAlert().getDialogPane().setPrefSize(800, 600);
        partModel.getAlert().getDialogPane().setMinSize(800, 600);
        partModel.getAlert().getDialogPane().getScene().getWindow().sizeToScene();
        repositionAlertForNewSize();
    }

    private Pane familyPane() {
        HBox hBox = new HBox();
        hBox.setPrefHeight(200);
        hBox.getChildren().add(partModel.getTreeView()); // Pane for familyButton
        return hBox;
    }

    private Pane notePane() {
        HBox hBox = new HBox(10);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setPrefHeight(200);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(vBox, Priority.ALWAYS);
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            partModel.selectedSpareProperty().get().setComments(newValue);
        });
        Button saveButton = ButtonFx.utilityButton("/images/save-16.png");
        Button modifyButton = ButtonFx.utilityButton("/images/modify-16.png");
        saveButton.setPrefWidth(100);
        modifyButton.setPrefWidth(100);
        saveButton.setText("Save");
        modifyButton.setText("Edit");
        // Create buttons
        saveButton.setOnAction(button -> {
            textArea.setEditable(false);
            saveButton.setVisible(false);
            saveButton.setManaged(false);
            modifyButton.setVisible(true);
            modifyButton.setManaged(true);
            action.accept(PartMessage.SAVE_PART_NOTE);
        });
        modifyButton.setOnAction(button -> {
            textArea.setEditable(true);
            modifyButton.setVisible(false);
            modifyButton.setManaged(false);
            saveButton.setVisible(true);
            saveButton.setManaged(true);
        });
        // Initially show only the modify button
        saveButton.setVisible(false);
        saveButton.setManaged(false);
        vBox.getChildren().addAll(modifyButton, saveButton);
        hBox.getChildren().addAll(textArea, vBox);
        return hBox;
    }

    private void setSelectedChangeListener() {
        partModel.getSparesTableView().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                createOrUpdateTreeView();  // I removed new selection
            }
        });
    }

    private void repositionAlertForNewSize() {
        Stage alertStage = (Stage) partModel.getAlert().getDialogPane().getScene().getWindow();
        if (BaseApplication.primaryStage != null) {
            double primaryX = BaseApplication.primaryStage.getX();
            double primaryY = BaseApplication.primaryStage.getY();
            double primaryWidth = BaseApplication.primaryStage.getWidth();
            double primaryHeight = BaseApplication.primaryStage.getHeight();
            alertStage.setX(primaryX + (primaryWidth / 2) - ((double) 800 / 2));
            alertStage.setY(primaryY + (primaryHeight / 2) - ((double) 600 / 2));
        } else {
            logger.error("Warning: primaryStage is null");
        }
    }

    private TreeView<String> createProductFamilyTreeView() {
        TreeItem<String> rootItem = createTreeItemRoot(partModel.getProductFamilies());
        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(true);
        return treeView;
    }

    private TreeItem<String> createTreeItemRoot(List<ProductFamilyFx> productFamilies) {
        TreeItem<String> rootItem = new TreeItem<>("Product Families");
        rootItem.setExpanded(true);
        for (ProductFamilyFx pf : productFamilies) {
            TreeItem<String> rangeItem = new TreeItem<>(pf.getRange());
            rangeItem.setExpanded(true);
            for (String productFamily : pf.getProductFamilies()) {
                rangeItem.getChildren().add(new TreeItem<>(productFamily));
            }
            rootItem.getChildren().add(rangeItem);
        }
        return rootItem;
    }

    private Node rangeBox() {
        ObservableList<String> rangeItems = FXCollections.observableArrayList(
                noteModel.getRanges().stream()
                        .map(RangesFx::getRange)
                        .collect(Collectors.toList())
        );
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        Label rangeLabel = new Label("Range");
        // Create ComboBox and set items
        ComboBox<String> rangeComboBox = new ComboBox<>();
        rangeComboBox.getSelectionModel().select("All");
        // sets the range to default so that it will search without looking
        System.out.println("Setting it here");
        setSelectedRange("All");
        rangeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // apply the range filter
            setSelectedRange(newValue);
            // if we have search terms in the box lets search them
            if(!partModel.getSearchField().textProperty().get().isEmpty()) partModel.getSearchButton().fire();
            // counts the number of spares with range filter on
            noteView.getAction().accept(NoteMessage.UPDATE_RANGE_COUNT);
        });
        rangeComboBox.setItems(rangeItems);
        HBox.setHgrow(rangeComboBox, Priority.ALWAYS);
        hBox.getChildren().addAll(rangeLabel, rangeComboBox);
        return hBox;
    }

    private void setSelectedRange(String newValue) {
        // newValue is string value returned from clicking on the ComboBox for Range
        logger.info("Selected Range: {}", newValue);
        partModel.comboBoxSelectedRangeProperty().set(newValue);
        action.accept(PartMessage.SET_SELECTED_RANGE);
    }

    private void cleanAlertClose() {
            noteModel.searchWordProperty().set("");
            noteModel.getSearchedParts().clear();
            noteModel.resultsLabelProperty().get().setText("");
            partModel.getAlert().setResult(ButtonType.CANCEL);
            partModel.getAlert().close(); // Use close() instead of hide()
            partModel.getAlert().hide();
    }
}
