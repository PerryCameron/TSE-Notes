package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder;

import com.L2.BaseApplication;
import com.L2.dto.PartFx;
import com.L2.dto.global_spares.RangesFx;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.components.*;
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

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PartFinderView implements Builder<Alert> {
    private static final Logger logger = LoggerFactory.getLogger(PartFinderView.class);

    private final Consumer<PartFinderMessage> action;
    private final NoteView noteView;
    private final NoteModel noteModel;
    private final PartFinderModel partFinderModel;

    public PartFinderView(NoteView noteView, PartFinderModel partFinderModel, Consumer<PartFinderMessage> message) {
        this.partFinderModel = partFinderModel;
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.action = message;
    }

    @Override
    public Alert build() {
        partFinderModel.getAlert().setTitle("Search spares"); // Set custom title bar text
        // Since AlertType is set to NONE there is no close button which allows the x in the corner to
        // close the alert window. This listener fixes that.
        partFinderModel.getAlert().showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                Stage stage = (Stage) partFinderModel.getAlert().getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(event -> cleanAlertClose());
            }
        });
        partFinderModel.getAlert().setDialogPane(createDialogPane());
        // here is the start of the UI for search
        partFinderModel.getDialogPane().setContent(contentBox());
        DialogueFx.getTitleIcon(partFinderModel.getDialogPane());
        DialogueFx.tieAlertToStage(partFinderModel.getAlert(), partFinderModel.getWidth(), 400);
        noteModel.numberInRangeProperty().addListener(rangeNumber -> partFinderModel.getRangeNumberLabel()
                .setText("Spares in range: " + noteModel.numberInRangeProperty().get()));
        noteView.getAction().accept(NoteMessage.UPDATE_RANGE_COUNT);
        if (noteModel.selectedRangeProperty().get() != null)
            return partFinderModel.getAlert();
        else return null;
    }

    private DialogPane createDialogPane() {
        //partFinderModel.getDialogPane().getStylesheets().add("css/light.css");
        partFinderModel.getDialogPane().getStylesheets().add("css/" + BaseApplication.theme + ".css");
        partFinderModel.getDialogPane().getStyleClass().add("decorative-hbox");
        partFinderModel.getDialogPane().setPrefWidth(partFinderModel.getWidth());
        partFinderModel.getDialogPane().setMinWidth(partFinderModel.getWidth()); // Ensure minimum width is 800
        return partFinderModel.getDialogPane();
    }

    private Node partContainerButtonBox() {
        partFinderModel.setPartContainerButtonBox(new HBox(5));
        partFinderModel.getPartContainerButtonBox().setAlignment(Pos.CENTER_RIGHT);
        return partFinderModel.getPartContainerButtonBox();
    }

    private Node partContainer() {
        partFinderModel.setPartContainer(new VBox(10));
        // clears for new tableview
        partFinderModel.getPartContainer().getChildren().clear();
        // make tableview with new list of parts
        partFinderModel.setSparesTableView(SparesTableViewFx.createTableView(noteModel));
        // set one time listener to notice when table row is first selected
        initialize();
        // add new tableview to dialogue
        return partFinderModel.getPartContainer();
    }

    private void initialize() {
        ChangeListener<SparesDTO> firstSelectionListener = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends SparesDTO> obs, SparesDTO oldSelection, SparesDTO newSelection) {
                if (newSelection != null) {
                    // adds more button in the middle
                    partFinderModel.getPartContainerButtonBox().getChildren().add(1, partOrderButton());
                    partFinderModel.getPartContainerButtonBox().getChildren().add(2, moreButton());
                    // Remove the listener after the first selection
                    partFinderModel.getSparesTableView().getSelectionModel().selectedItemProperty().removeListener(this);
                }
            }
        };
        partFinderModel.getSparesTableView().getSelectionModel().selectedItemProperty().addListener(firstSelectionListener);
    }

    private Node resultsLabelHbox() {
        partFinderModel.setResultsLabelHbox(new HBox());
        HBox.setHgrow(partFinderModel.getResultsLabelHbox(), Priority.ALWAYS);
        partFinderModel.getResultsLabelHbox().setAlignment(Pos.CENTER_LEFT);
        partFinderModel.getResultsLabelHbox().getChildren().addAll(noteModel.resultsLabelProperty().get(), partFinderModel.getMessageLabel());
        return partFinderModel.getResultsLabelHbox();
    }

    private Node buttonBox() {
        partFinderModel.setButtonBox(new HBox(10, rangeBox(), searchBox(), cancelBox()));
        partFinderModel.getButtonBox().setAlignment(Pos.CENTER_RIGHT);
        return partFinderModel.getButtonBox();
    }

    private Node cancelBox() {
        partFinderModel.setCancelHbox(new VBox());
        partFinderModel.setCancelButton(new Button("Close"));
        partFinderModel.getCancelHbox().setAlignment(Pos.CENTER_RIGHT);
        partFinderModel.getCancelHbox().getChildren().add(partFinderModel.getCancelButton());
        partFinderModel.getCancelButton().setOnAction(e -> cleanAlertClose());
        return partFinderModel.getCancelHbox();
    }

    private Node contentBox() {
        partFinderModel.setContent(new VBox(10));
        partFinderModel.getContent().setPadding(new Insets(10, 10, 10, 10));
        partFinderModel.getContent().setPrefWidth(partFinderModel.getWidth());
        partFinderModel.setMessageLabel(new Label("Part Search"));
        partFinderModel.setSearchField(new TextField());
        partFinderModel.getSearchField().setPromptText("Search Part Number or description...");
        partFinderModel.getContent().getChildren().addAll(partFinderModel.getMessageLabel(),
                partFinderModel.getSearchField(), buttonBox(), partContainer(), partContainerButtonBox());
        return partFinderModel.getContent();
    }

    private Control partOrderButton() {
        Button partOrderButton = new Button("Add to Part Order");
        partOrderButton.setOnAction(add -> {
            SparesDTO sparesDTO = partFinderModel.getSparesTableView().getSelectionModel().getSelectedItem();
            if (sparesDTO != null) {
                noteView.getAction().accept(NoteMessage.INSERT_PART);
                PartFx partDTO = noteModel.selectedPartProperty().get();
                partDTO.setPartNumber(sparesDTO.getSpareItem());
                partDTO.setPartDescription(sparesDTO.getSpareDescription());
                // no need to put in part into FX UI here as it is being done elsewhere
                noteView.getAction().accept(NoteMessage.UPDATE_PART);
                // Refresh the table view layout and focus
                TableViewFx.focusOnLastItem(partFinderModel.getPartsTableView());
                // make sure we clear our label out so that we get no memory leaks, the label continues to exist but not the hbox
                partFinderModel.getResultsLabelHbox().getChildren().clear();
                cleanAlertClose();
            }
        });
        return partOrderButton;
    }

    private Node searchBox() {
        partFinderModel.setRangeNumberLabel(new Label("Spares"));
        partFinderModel.getRangeNumberLabel().setPadding(new Insets(0, 200, 0, 0));
        HBox searchHbox = new HBox();
        HBox.setHgrow(searchHbox, Priority.ALWAYS); // Spacer grows to push buttons right
        searchHbox.setAlignment(Pos.CENTER_RIGHT);
//        Button testButton = new Button("Test");
//        testButton.setOnAction(event -> {
//            action.accept(PartFinderMessage.TEST_SOME_SHIT);
//        });
        searchHbox.getChildren().addAll(partFinderModel.getRangeNumberLabel(), searchButton());
        return searchHbox;
    }

    private Control searchButton() {
        partFinderModel.setSearchButton(new Button("Search"));
        partFinderModel.getSearchButton().setOnAction(e -> {
            noteModel.searchWordProperty().set(partFinderModel.getSearchField().getText().trim());
            if (!noteModel.searchWordProperty().get().isEmpty()) { // there are search terms
                if (partFinderModel.getPartContainer().getChildren().isEmpty())
                    partFinderModel.getPartContainer().getChildren().add(partFinderModel.getSparesTableView());
                noteView.getAction().accept(NoteMessage.SEARCH_PARTS);
                // clear everything in part container in case we already made a search
                if (!partFinderModel.searchedBeforeProperty().get()) {
                    partFinderModel.getButtonBox().getChildren().remove(partFinderModel.getCancelHbox());
                    partFinderModel.getPartContainerButtonBox().getChildren().addAll(resultsLabelHbox(),
                            partFinderModel.getCancelButton());
                }
                partFinderModel.searchedBeforeProperty().set(true);
                partFinderModel.getDialogPane().requestLayout();
                partFinderModel.getAlert().getDialogPane().getScene().getWindow().sizeToScene();
            }
        });
        // this allows us the option to search by hitting enter instead of having to click the search button
        partFinderModel.getSearchField().setOnAction(event -> {
            partFinderModel.getSearchButton().requestFocus(); // Visually select the button
            partFinderModel.getSearchButton().fire();
        });
        return partFinderModel.getSearchButton();
    }

    private Control moreButton() {
        Button moreButton = new Button("More");
        partFinderModel.setMoreInfoHbox(new HBox(5)); // moreInfoHbox
        moreButton.setOnAction(e -> {
            partFinderModel.alertExtendedProperty().set(true);
            setSelectedChangeListener();
            partFinderModel.getPartContainerButtonBox().getChildren().remove(2);
            buildExtraSection();
        });
        return moreButton;
    }

    private Node buttonStack(StackPane stackPane, Pane photoPane, Pane familyPane,  Pane notePane, Pane keywordsPane, Pane infoPane) {
        VBox buttonStack = new VBox(2);
        buttonStack.setMinWidth(150);
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton photoButton = ButtonFx.toggleof("Photo", 150, toggleGroup);
        ToggleButton familyButton = ButtonFx.toggleof("Product Families", 150, toggleGroup);
        ToggleButton noteButton = ButtonFx.toggleof("Note", 150, toggleGroup);
        ToggleButton keyWordsButton = ButtonFx.toggleof("Keywords", 150, toggleGroup);
        ToggleButton infoButton = ButtonFx.toggleof("Info", 150, toggleGroup);
        //  grab two buttons for use later
        partFinderModel.familyButtonProperty().set(familyButton);
        partFinderModel.imageButtonProperty().set(photoButton);
        // Add all buttons to the VBox first
        buttonStack.getChildren().addAll(photoButton, familyButton, noteButton, keyWordsButton, infoButton);
        // Set default content in the StackPane
        // Assume familyPane, notePane, keywordsPane, infoPane, photoPane are already created
        stackPane.getChildren().addAll(photoPane, familyPane, notePane, keywordsPane, infoPane);
        photoPane.setVisible(true);
        familyPane.setVisible(false);
        notePane.setVisible(false);
        keywordsPane.setVisible(false);
        infoPane.setVisible(false);

        // Set the default selected button AFTER adding to the scene graph
        photoButton.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                toggleGroup.selectToggle(oldToggle);  // Prevent deselecting by re-selecting the old toggle
                return;
            }
            // Hide all panes
            familyPane.setVisible(false);
            notePane.setVisible(false);
            keywordsPane.setVisible(false);
            infoPane.setVisible(false);
            photoPane.setVisible(false);

            // Show the selected pane
            if (newToggle == null || newToggle == familyButton) {
                familyPane.setVisible(true);
            } else if (newToggle == noteButton) {
                notePane.setVisible(true);
            } else if (newToggle == keyWordsButton) {
                keywordsPane.setVisible(true);
            } else if (newToggle == infoButton) {
                infoPane.setVisible(true);
            } else if (newToggle == photoButton) {
                photoPane.setVisible(true);
            }
        });
        return buttonStack;
    }

    private void buildExtraSection() {
        // set the selected spare
        partFinderModel.selectedSpareProperty().set(partFinderModel.getSparesTableView().getSelectionModel().getSelectedItem());
        // map the JSON in pim
        action.accept(PartFinderMessage.JSON_MAP_PRODUCT_FAMILIES);
        // map JSON updated_by
        action.accept(PartFinderMessage.GET_UPDATE_BY_INFORMATION);
        // Set up the StackPane
        partFinderModel.setStackPane(new StackPane());
        // Create panes for each button
        Node buttonStack = buttonStack(partFinderModel.getStackPane(),
                new PartPhoto(this).build(),
                new ProductFamily(this).build(),
                new PartNote(this).build(),
                new PartKeyWords(this).build(),
                new PartInfo(this).build());
        // Add buttons and StackPane to the HBox
        partFinderModel.getMoreInfoHbox().getChildren().addAll(buttonStack, partFinderModel.getStackPane());
        partFinderModel.getMoreInfoHbox().getStyleClass().add("inner-decorative-hbox");
        partFinderModel.getMoreInfoHbox().setPadding(new Insets(10, 10, 10, 10));
        // Add HBox to content
        partFinderModel.getContent().getChildren().add(3, partFinderModel.getMoreInfoHbox());
        // Configure the Alert's DialogPane
        partFinderModel.getAlert().getDialogPane().setPrefSize(800, 700);
        partFinderModel.getAlert().getDialogPane().setMinSize(800, 700);
        partFinderModel.getAlert().getDialogPane().getScene().getWindow().sizeToScene();
        repositionAlertForNewSize(800,700);
    }

    // whenever we change a selection this happens
    private void setSelectedChangeListener() {
        partFinderModel.getSparesTableView().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                logger.warn(("PartFinderView::setSelectedChangeListener"));
                // make sure inSparesTogSwitchProperty listener doesn't execute when we change a part
                //partFinderModel.getBlockTogSwitchListenerProperty().set(true);
                // updates our selected spare to match selected
                partFinderModel.selectedSpareProperty().set(newSelection);
                // updates treeView for product families and ranges
                updateTreeView();
                // updates the part note for new selection
                partFinderModel.partNoteProperty().get().setText(newSelection.getComments());
                // update the keywords for new selection
                partFinderModel.partKeyWordsProperty().get().setText(newSelection.getKeywords());
                // for the tree view component, if it is in edit mode this will set it back if we select a different part
                partFinderModel.getTreeView().editableProperty().set(false);
                // if there is no image this is needed to clear the old one out
                partFinderModel.getImageView().setImage(null);
                // loads the image if available
                action.accept(PartFinderMessage.LOAD_IMAGE);
                // refreshes the updated by field along with the date
                action.accept(PartFinderMessage.REFRESH_PART_INFO);
            }
        });
    }

    private void updateTreeView() {
        action.accept(PartFinderMessage.JSON_MAP_PRODUCT_FAMILIES);
        TreeItem<Object> rootItem = ProductFamily.createTreeItemRoot(partFinderModel.getProductFamilies());
        partFinderModel.getTreeView().setRoot(rootItem);
    }

    private void repositionAlertForNewSize(double width, double height) {
        Stage alertStage = (Stage) partFinderModel.getAlert().getDialogPane().getScene().getWindow();
        if (BaseApplication.primaryStage != null) {
            double primaryX = BaseApplication.primaryStage.getX();
            double primaryY = BaseApplication.primaryStage.getY();
            double primaryWidth = BaseApplication.primaryStage.getWidth();
            double primaryHeight = BaseApplication.primaryStage.getHeight();
            alertStage.setX(primaryX + (primaryWidth / 2) - (width / 2));
            alertStage.setY(primaryY + (primaryHeight / 2) - (height / 2));
        } else {
            logger.error("Warning: primaryStage is null");
        }
    }

    private Node rangeBox() {
        ObservableList<String> rangeItems = FXCollections.observableArrayList(
                noteModel.getRanges().stream()
                        .map(RangesFx::getRange)
                        .collect(Collectors.toList()));
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        Label rangeLabel = new Label("Range");
        // Create ComboBox and set items
        ComboBox<String> rangeComboBox = new ComboBox<>();
        rangeComboBox.getSelectionModel().select("All");
        // sets the range to default so that it will search without looking
        setSelectedRange("All");
        rangeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // apply the range filter
            setSelectedRange(newValue);
            // if we have search terms in the box lets search them
            if(!partFinderModel.getSearchField().textProperty().get().isEmpty()) partFinderModel.getSearchButton().fire();
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
        partFinderModel.comboBoxSelectedRangeProperty().set(newValue);
        action.accept(PartFinderMessage.SET_SELECTED_RANGE);
    }

    private void cleanAlertClose() {
            noteModel.searchWordProperty().set("");
            noteModel.getSearchedParts().clear();
            noteModel.resultsLabelProperty().get().setText("");
            partFinderModel.getAlert().setResult(ButtonType.CANCEL);
            partFinderModel.getAlert().close(); // Use close() instead of hide()
            partFinderModel.getAlert().hide();
    }

    public NoteView getNoteView() {
        return noteView;
    }

    public Consumer<PartFinderMessage> getAction() {
        return action;
    }

    public PartFinderModel getPartFinderModel() {
        return partFinderModel;
    }
}
