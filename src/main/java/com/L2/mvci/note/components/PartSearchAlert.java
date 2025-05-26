package com.L2.mvci.note.components;

import com.L2.BaseApplication;
import com.L2.dto.PartFx;
import com.L2.dto.ProductFamilyFx;
import com.L2.dto.global_spares.RangesFx;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.mvci.note.NoteMessage;
import com.L2.mvci.note.NoteModel;
import com.L2.mvci.note.NoteView;
import com.L2.widgetFx.DialogueFx;
import com.L2.widgetFx.SparesTableViewFx;
import com.L2.widgetFx.TableViewFx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PartSearchAlert implements Builder<Alert> {
    private static final Logger logger = LoggerFactory.getLogger(PartSearchAlert.class);
    private final TableView<PartFx> partsTableView;
    private final NoteModel noteModel;
    private final SimpleBooleanProperty searchedBefore;
    private final NoteView noteView;
    private Alert alert;
    private final double width = 800;
    private Label rangeNumberLabel;
    private Label messageLabel;
    private TableView<SparesDTO> sparesTableView;
    private HBox resultsLabelHbox;
    private TextField searchField;
    private HBox buttonBox;
    private DialogPane dialogPane;
    private VBox cancelHbox;
    private Button cancelButton;
    private HBox partContainerButtonBox;
    private VBox partContainer;
    private TreeView<String> treeView;
    private VBox content;
    private final BooleanProperty alertExtended = new SimpleBooleanProperty(false);
    private HBox moreInfoHbox;

    public PartSearchAlert(NoteView noteView, TableView<PartFx> partsTableView) {
        this.noteView = noteView;
        this.noteModel = noteView.getNoteModel();
        this.partsTableView = partsTableView;
        this.searchedBefore = new SimpleBooleanProperty(false);
    }

    @Override
    public Alert build() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        this.alert = alert;
        alert.setTitle("Search spares"); // Set custom title bar text
        // Since AlertType is set to NONE there is no close button which allows the x in the corner to
        // close the alert window. This listener fixes that.
        alert.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(event -> cleanAlertClose(noteModel, alert));
            }
        });
        DialogueFx.getTitleIcon(dialogPane());
        DialogueFx.tieAlertToStage(alert, width, 400);
        noteModel.numberInRangeProperty().addListener(rangeNumber -> rangeNumberLabel.setText("Spares in range: " + noteModel.numberInRangeProperty().get()));
        noteView.getAction().accept(NoteMessage.UPDATE_RANGE_COUNT);
        if (noteModel.selectedRangeProperty().get() != null)
            return alert;
        else return null;
    }

    private DialogPane dialogPane() {
        this.dialogPane = new DialogPane();
        alert.setDialogPane(dialogPane);
        dialogPane.getStylesheets().add("css/light.css");
        dialogPane.getStyleClass().add("search-dialogue");
        dialogPane.setPrefWidth(width);
        dialogPane.setMinWidth(width); // Ensure minimum width is 800
        dialogPane.setContent(contentBox());
        return dialogPane;
    }

    private Node partContainerButtonBox() {
        this.partContainerButtonBox = new HBox(5);
        partContainerButtonBox.setAlignment(Pos.CENTER_RIGHT);
        return partContainerButtonBox;
    }

    private Node partContainer() {
        this.partContainer = new VBox(10);
        // clears for new tableview
        partContainer.getChildren().clear();
        // make tableview with new list of parts
        this.sparesTableView = SparesTableViewFx.createTableView(noteModel);
        // set one time listener to notice when table row is first selected
        initialize();
        // add new tableview to dialogue
        return partContainer;
    }

    public void initialize() {
        ChangeListener<SparesDTO> firstSelectionListener = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends SparesDTO> obs, SparesDTO oldSelection, SparesDTO newSelection) {
                if (newSelection != null) {
                    // adds more button in the middle
                    partContainerButtonBox.getChildren().add(2, moreButton());
                    // Remove the listener after the first selection
                    sparesTableView.getSelectionModel().selectedItemProperty().removeListener(this);
                }
            }
        };
        sparesTableView.getSelectionModel().selectedItemProperty().addListener(firstSelectionListener);
    }

    private Node resultsLabelHbox() {
        this.resultsLabelHbox = new HBox();
        HBox.setHgrow(resultsLabelHbox, Priority.ALWAYS);
        resultsLabelHbox.setAlignment(Pos.CENTER_LEFT);
        resultsLabelHbox.getChildren().addAll(noteModel.resultsLabelProperty().get(), messageLabel);
        return resultsLabelHbox;
    }

    private Node buttonBox() {
        this.buttonBox = new HBox(10, rangeBox(noteModel, noteView), searchBox(), cancelBox());
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        return buttonBox;
    }

    private Node cancelBox() {
        this.cancelHbox = new VBox();
        this.cancelButton = new Button("Cancel");
        cancelHbox.setAlignment(Pos.CENTER_RIGHT);
        cancelHbox.getChildren().add(cancelButton);
        // Handle Cancel button
        cancelButton.setOnAction(e -> cleanAlertClose(noteModel, alert));
        return cancelHbox;
    }

    private Node contentBox() {
        this.content = new VBox(10);
        content.setPadding(new Insets(10, 10, 10, 10));
        content.setPrefWidth(width);
        this.messageLabel = new Label("Part Search");
        this.searchField = new TextField();
        searchField.setPromptText("Search Part Number or description...");
        content.getChildren().addAll(messageLabel, searchField, buttonBox(), partContainer(), partContainerButtonBox());
        return content;
    }

    private Control partOrderButton() {
        Button partOrderButton = new Button("Add to Part Order");
        partOrderButton.setOnAction(add -> {
            SparesDTO sparesDTO = sparesTableView.getSelectionModel().getSelectedItem();
            if (sparesDTO != null) {
                noteView.getAction().accept(NoteMessage.INSERT_PART);
                PartFx partDTO = noteModel.selectedPartProperty().get();
                partDTO.setPartNumber(sparesDTO.getSpareItem());
                partDTO.setPartDescription(sparesDTO.getSpareDescription());
                // no need to put in part into FX UI here as it is being done elsewhere
                noteView.getAction().accept(NoteMessage.UPDATE_PART);
                // Refresh the table view layout and focus
                TableViewFx.focusOnLastItem(partsTableView);
                // make sure we clear our label out so that we get no memory leaks, the label continues to exist but not the hbox
                resultsLabelHbox.getChildren().clear();
                cleanAlertClose(noteModel, alert);
            }
        });
        return partOrderButton;
    }

    private Node searchBox() {
        this.rangeNumberLabel = new Label("Spares");
        rangeNumberLabel.setPadding(new Insets(0, 200, 0, 0));
        HBox searchHbox = new HBox();
        HBox.setHgrow(searchHbox, Priority.ALWAYS); // Spacer grows to push buttons right
        searchHbox.setAlignment(Pos.CENTER_RIGHT);
        searchHbox.getChildren().addAll(rangeNumberLabel, searchButton());
        return searchHbox;
    }

    private Control searchButton() {
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            noteModel.searchWordProperty().set(searchField.getText().trim());
            if (!noteModel.searchWordProperty().get().isEmpty()) { // there are search terms
                if (partContainer.getChildren().isEmpty())
                    partContainer.getChildren().add(sparesTableView);
                noteView.getAction().accept(NoteMessage.SEARCH_PARTS);
                // clear everything in part container in case we already made a search
                if (!searchedBefore.get()) {
                    buttonBox.getChildren().remove(cancelHbox);
                    partContainerButtonBox.getChildren().addAll(resultsLabelHbox(),
                            partOrderButton(), cancelButton);
                }
                searchedBefore.set(true);
                dialogPane.requestLayout();
                alert.getDialogPane().getScene().getWindow().sizeToScene();
            }
        });
        // this allows us to search by hitting enter instead of having to click the search button
        searchField.setOnAction(event -> {  // since searchField is a class field and instantiated already this was easy, I would like it to also select the search button just for consistency
            searchButton.requestFocus(); // Visually select the button
            searchButton.fire();
        });
        return searchButton;
    }

    public Control moreButton() {
        Button moreButton = new Button("More");
        this.moreInfoHbox = new HBox();
        moreButton.setOnAction(e -> {
            alertExtended.set(true);
            setSelectedChangeListener();
            partContainerButtonBox.getChildren().remove(2);
            SparesDTO sparesDTO = sparesTableView.getSelectionModel().getSelectedItem();
            // Create or update TreeView
            createOrUpdateTreeView(sparesDTO);
        });
        return moreButton;
    }

    private void createOrUpdateTreeView(SparesDTO sparesDTO) {
        List<ProductFamilyFx> productFamilies = List.of();
        String jsonResponse = sparesDTO.getPim();
        try {
            productFamilies = noteModel.getObjectMapper().readValue(
                    jsonResponse,
                    new TypeReference<>() {
                    }
            );
        } catch (JsonProcessingException ex) {
            logger.error("Error deserializing JSON: {}", ex.getMessage());
        }
        if (moreInfoHbox.getChildren().isEmpty()) {
            // Initialize TreeView and add to HBox
            createTreeView(productFamilies, moreInfoHbox);
        } else {
            // Update existing TreeView
            TreeItem<String> rootItem = createTreeItemRoot(productFamilies);
            this.treeView.setRoot(rootItem);
        }
    }

    private void createTreeView(List<ProductFamilyFx> productFamilies, HBox moreInfoHbox) {
        this.treeView = createProductFamilyTreeView(productFamilies);
        this.treeView.setPrefSize(500, 200); // Optional: Set size
        alert.getDialogPane().setPrefSize(800, 600); // Set preferred size for DialogPane
        alert.getDialogPane().setMinSize(800, 600);  // Optional: Ensure minimum size
        moreInfoHbox.getChildren().add(this.treeView);
        this.content.getChildren().add(3, moreInfoHbox);
        alert.getDialogPane().getScene().getWindow().sizeToScene();
        repositionAlertForNewSize();
    }

    private void setSelectedChangeListener() {
        sparesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                createOrUpdateTreeView(newSelection);
            }
        });
    }

    private void repositionAlertForNewSize() {
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        if (BaseApplication.primaryStage != null) {
            double primaryX = BaseApplication.primaryStage.getX();
            double primaryY = BaseApplication.primaryStage.getY();
            double primaryWidth = BaseApplication.primaryStage.getWidth();
            double primaryHeight = BaseApplication.primaryStage.getHeight();
            alertStage.setX(primaryX + (primaryWidth / 2) - ((double) 800 / 2));
            alertStage.setY(primaryY + (primaryHeight / 2) - ((double) 600 / 2));
            System.out.println("Centered Alert at X: " + alertStage.getX() + ", Y: " + alertStage.getY());
        } else {
            System.out.println("Warning: primaryStage is null");
        }
    }

    private TreeView<String> createProductFamilyTreeView(List<ProductFamilyFx> productFamilies) {
        TreeItem<String> rootItem = createTreeItemRoot(productFamilies);
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

    private static Node rangeBox(NoteModel noteModel, NoteView noteView) {
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
        setSelectedRange("All", noteModel);
        rangeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Assuming noteModel.getRanges() returns a list of RangeDTO objects
            setSelectedRange(newValue, noteModel);
            noteView.getAction().accept(NoteMessage.UPDATE_RANGE_COUNT);
        });
        rangeComboBox.setItems(rangeItems);
        HBox.setHgrow(rangeComboBox, Priority.ALWAYS);
        hBox.getChildren().addAll(rangeLabel, rangeComboBox);
        return hBox;
    }

    private static void setSelectedRange(String newValue, NoteModel noteModel) {
        RangesFx selectedRange = noteModel.getRanges().stream()
                .filter(range -> range.getRange().equals(newValue))
                .findFirst()
                .orElse(null);
        if (selectedRange != null) {
            noteModel.selectedRangeProperty().set(selectedRange);
        } else {
            logger.error("No matching range found for: {}", newValue);
            if (!noteModel.getRanges().isEmpty()) {
                logger.warn("Defaulting to first range");
                noteModel.selectedRangeProperty().set(noteModel.getRanges().getFirst());
            } else {
                logger.error("Ranges list is empty, setting selectedRange to null");
                noteModel.selectedRangeProperty().set(null);
            }
        }
    }

    private static void cleanAlertClose(NoteModel noteModel, Alert alert) {
        try {
            noteModel.searchWordProperty().set("");
            noteModel.getSearchedParts().clear();
            noteModel.resultsLabelProperty().get().setText("");
            alert.setResult(ButtonType.CANCEL);
            alert.close(); // Use close() instead of hide()
        } catch (Exception e) {
            logger.error("Error closing alert: {}", e.getMessage());
            alert.hide();
        }
    }
}
